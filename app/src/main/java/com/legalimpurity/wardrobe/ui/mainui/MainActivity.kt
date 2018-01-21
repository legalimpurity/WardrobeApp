package com.legalimpurity.wardrobe.ui.mainui

import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v4.view.ViewPager
import com.legalimpurity.wardrobe.BR
import com.legalimpurity.wardrobe.R
import com.legalimpurity.wardrobe.data.models.ShirtNPant
import com.legalimpurity.wardrobe.databinding.ActivityMainBinding
import com.legalimpurity.wardrobe.ui.base.BaseActivity
import com.legalimpurity.wardrobe.ui.mainui.shirtadapter.ShirtAdapter
import com.legalimpurity.wardrobe.util.AppLogger
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject








/**
 * Created by rajatkhanna on 21/01/18.
 */
class MainActivity  : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator, HasSupportFragmentInjector
{

    val REQUEST_IMAGE_CAPTURE = 1
    val RESULT_LOAD_IMAGE = 2

    @Inject lateinit var mMainViewModel: MainViewModel

    @Inject lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject lateinit var mShirtAdapter : ShirtAdapter
    @Inject lateinit var mPantAdapter : ShirtAdapter

    private var mActivityMainBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainBinding = getViewDataBinding()
        mMainViewModel.setNavigator(this)
        mMainViewModel.pant_available.set(getString(R.string.loading_pants))
        mMainViewModel.shirt_available.set(getString(R.string.loading_shirts))
        setShirtAdapter()
        setPantAdapter()
        subscribeToLiveData()
    }

    // Initialize and set adapter
    private fun setShirtAdapter()
    {
        mActivityMainBinding?.shirtViewPager?.adapter = mShirtAdapter
        mActivityMainBinding?.shirtViewPager?.offscreenPageLimit = 3
        mActivityMainBinding?.shirtViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mMainViewModel.setShirtPosBeingSeen(position)
            }
        })
    }

    // Initialize and set adapter
    private fun setPantAdapter()
    {
        mActivityMainBinding?.pantViewPager?.adapter = mPantAdapter
        mActivityMainBinding?.pantViewPager?.offscreenPageLimit = 3
        mActivityMainBinding?.pantViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mMainViewModel.setPantPosBeingSeen(position)
            }
        })
    }

    private fun subscribeToLiveData()
    {
        mMainViewModel.shirtsLiveData.observe(this, Observer<List<ShirtNPant>> {
            shirtData -> shirtData?.let{
            mMainViewModel.addShirtsToList(it)

            getViewModel().shirtBeingViewedPosition.value?.let {
                mActivityMainBinding?.shirtViewPager?.setCurrentItem(it,true)
            }

            if(shirtData.isEmpty())
                mMainViewModel.shirt_available.set(getString(R.string.no_shirts_added))
            else
                mMainViewModel.shirt_available.set("")
            }

            mActivityMainBinding?.shirtViewPager?.postDelayed(Runnable {
                getViewModel().shirtBeingViewedPosition.value?.let {
                    mActivityMainBinding?.shirtViewPager?.setCurrentItem(it,true)
                }
            }, 1000)

        })

        mMainViewModel.pantsLiveData.observe(this, Observer<List<ShirtNPant>> {
            pantData -> pantData?.let{
            mMainViewModel.addPantsToList(it)


            if(pantData.isEmpty())
                mMainViewModel.pant_available.set(getString(R.string.no_pants_added))
            else
                mMainViewModel.pant_available.set("")
            }
            mActivityMainBinding?.pantViewPager?.postDelayed(Runnable {
                getViewModel().pantBeingViewedPosition.value?.let {
                    mActivityMainBinding?.pantViewPager?.setCurrentItem(it,true)
                }
            }, 1000)
        })
    }

    // Functions to be implemented by every Activity
    override fun getViewModel() = mMainViewModel

    override fun getBindingVariable() = BR.viewModel

    override fun getLayoutId() = R.layout.activity_main

    // Fragment support

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    // Navigator Functions
    override fun apiError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun openAddder() {
        dispatchTakePictureIntent()
//        dispatchGetFromGalleryIntent()
    }

    override fun updateAdapter(code: Int) {
        if(code == 1)
            mShirtAdapter.notifyDataSetChanged()
        else
            mPantAdapter.notifyDataSetChanged()
    }

    // Image Capturing Functions

    var mCurrentPhotoPath: String? = null

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(getString(R.string.storage_path_without_slash))
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
        AppLogger.d("-------------newFile:"+image.exists())
        AppLogger.d("-------------Path:"+image.path)
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    @Throws(IOException::class)
    private fun copyFile(sourceStream: FileInputStream, destFile: File) {
            val inStream = sourceStream as FileInputStream
            val outStream = FileOutputStream(destFile)
            val inChannel = inStream.getChannel()
            val outChannel = outStream.getChannel()
            inChannel.transferTo(0, inChannel.size(), outChannel)
            inStream.close()
            outStream.close()
    }

    fun dispatchTakePictureIntent()
    {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                TODO()
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                var photoURI: Uri = FileProvider.getUriForFile(this,
                getString(R.string.provider_path_authority),
                photoFile)
                mMainViewModel.pictureCreated(photoURI.toString())
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    fun dispatchGetFromGalleryIntent()
    {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE)
        {
            mMainViewModel.pictureAdded()
        }
        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            var newFile = createImageFile()
            if (newFile.exists())
            {
                try {
//                    getPath(this,requestCode)
//                    copyFile(contentResolver.openInputStream(data.getData()), newFile)
                    mMainViewModel.pictureAdded()
                } catch (e: IOException) {
                    AppLogger.d(e.localizedMessage)
                }
            }
        }
    }

}