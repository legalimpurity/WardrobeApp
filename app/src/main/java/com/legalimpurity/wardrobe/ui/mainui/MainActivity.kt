package com.legalimpurity.wardrobe.ui.mainui

import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
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

    @Inject lateinit var mMainViewModel: MainViewModel

    @Inject lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject lateinit var mShirtAdapter : ShirtAdapter

    private var mActivityMainBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainBinding = getViewDataBinding()
        mMainViewModel.setNavigator(this)
        mMainViewModel.fetchShirtsListMix()
        mMainViewModel.pant_available.set(getString(R.string.loading_pants))
        mMainViewModel.shirt_available.set(getString(R.string.loading_shirts))
        setShirtAdapter()
        subscribeToLiveData()
    }

    // Initialize and set adapter
    private fun setShirtAdapter()
    {
        mActivityMainBinding?.shirtViewPager?.adapter = mShirtAdapter

        // Get
//        mActivityMainBinding?.shirtViewPager?.setCurrentItem(getViewModel().getAppColor(),true)

        mActivityMainBinding?.shirtViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
            }
        })
    }

    private fun subscribeToLiveData()
    {
        mMainViewModel.shirtsLiveData.observe(this, Observer<List<ShirtNPant>> { timeTableData -> timeTableData?.let{ mMainViewModel.addShirtsToList(it) } })
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE)
        {
            mMainViewModel.pictureAdded()
        }
    }




}