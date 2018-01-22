package com.legalimpurity.wardrobe.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.support.v4.content.FileProvider
import com.legalimpurity.wardrobe.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*








/**
 * Created by rajatkhanna on 22/01/18.
 */
class FileStorageUtil
{

    private val ATTACHMENT_NAME_PREFIX = "gallery_"

    private fun getUniqueFileNameForInternalStorage(originalFileName: String): String {
        val uniqueId = UUID.randomUUID().toString()
        return String.format("%s%s_%s", ATTACHMENT_NAME_PREFIX, uniqueId, originalFileName)
    }

    @Throws(NullPointerException::class, SecurityException::class)
    fun getFileOnActivityResult(context: Context, resultCode: Int, data: Intent): Uri? {
        if (resultCode == Activity.RESULT_OK) {
            val uri = data.data
            val fileName = getFileName(context, uri)
            fileName?.let { return saveFile(context, uri, fileName) }
        }
        return null
    }

    fun openImageChooserActivityFromActivity(activity: Activity, requestCode: Int) {
        val intentBrowseFiles = Intent(Intent.ACTION_GET_CONTENT)
        intentBrowseFiles.type = "image/*"
        intentBrowseFiles.addCategory(Intent.CATEGORY_OPENABLE)
        activity.startActivityForResult(intentBrowseFiles, requestCode)
    }

    @Throws(NullPointerException::class)
    private fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null
        if ("file" == uri.scheme) {
            // Process as a uri that points to a file
            fileName = uri.lastPathSegment

        } else if ("content" == uri.scheme) {
            // Changes for new storage access framework in KitKat
            // Process as a uri that points to a content item
            val proj = arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE)

            val cursor = context.contentResolver.query(uri, proj, null, null, null)
            cursor!!.moveToFirst()
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            fileName = cursor.getString(nameIndex)
            cursor.close()
        }
        return fileName
    }


    fun saveFile(context: Context, contentUri: Uri, originalFileName: String): Uri? {
        var outputStream: FileOutputStream? = null
        var inputStream: InputStream? = null
        val resolver = context.getContentResolver()
        var photoURI: Uri? = null

        try {
            inputStream = resolver.openInputStream(contentUri)

//            // Create a unique filename that does not already exist in internal storage
//            val storageDir = context.getExternalFilesDir(context.getString(R.string.storage_path))
//            val mainFileName = getUniqueFileNameForInternalStorage(originalFileName)
//            val fileName = storageDir.path+mainFileName

            // Create file in Internal App Directory
            val ff = createImageFile(context)
            outputStream = ff.outputStream() // creates file if not existing

            // Transfer bytes from uri inputstream to temporary file outputstream
            val bufferSize = 1024 * 4 // 4 KB
            val buffer = ByteArray(bufferSize)
            var len = inputStream!!.read(buffer)
            while (len != -1) {
                outputStream.write(buffer, 0, len)
                len = inputStream.read(buffer)
            }

//            val fileStreamPath = context.getFileStreamPath(mainFileName)
            photoURI = FileProvider.getUriForFile(context,
                    context.getString(R.string.provider_path_authority),
                    ff)
            return photoURI
            AppLogger.d(photoURI.toString())
        } catch (e: IOException) {
            AppLogger.d(e.localizedMessage)
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (e1: IOException) {
                    AppLogger.d(e1.localizedMessage)
                }

            }

            if (inputStream != null) {
                try {
                    inputStream!!.close()
                } catch (e2: IOException) {
                    AppLogger.d(e2.localizedMessage)
                }

            }
        }
        return photoURI
    }

    interface PictureFileCreated
    {
        fun pictureCreated(photoURI:String)
    }

    fun dispatchTakePictureIntent(activity: Activity, requestCode:Int, pictureFileCreated: PictureFileCreated)
    {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile(activity)
            } catch (ex: IOException) {
//                TODO()
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                var photoURI: Uri = FileProvider.getUriForFile(activity,
                        activity.getString(R.string.provider_path_authority),
                        photoFile)
                pictureFileCreated.pictureCreated(photoURI.toString())
//                mMainViewModel.pictureCreated(photoURI.toString())
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                activity.startActivityForResult(takePictureIntent, requestCode)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(context: Context): File {
        // Create an image file name
        val imageFileName = getUniqueFileNameForInternalStorage("fromCamera")
        val storageDir = context.getExternalFilesDir(context.getString(R.string.storage_path_without_slash))
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        AppLogger.d("-------------newFile:"+image.exists())
        AppLogger.d("-------------Path:"+image.path)

        // Save a file: path for use with ACTION_VIEW intents
        return image
    }


}