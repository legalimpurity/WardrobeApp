package com.legalimpurity.wardrobe.ui.mainui

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.widget.Toast
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.legalimpurity.wardrobe.BR
import com.legalimpurity.wardrobe.R
import com.legalimpurity.wardrobe.data.models.ShirtNPant
import com.legalimpurity.wardrobe.databinding.ActivityMainBinding
import com.legalimpurity.wardrobe.ui.base.BaseActivity
import com.legalimpurity.wardrobe.ui.mainui.shirtadapter.ShirtAdapter
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import java.io.File
import java.util.*
import javax.inject.Inject








/**
 * Created by rajatkhanna on 21/01/18.
 */
class MainActivity  : BaseActivity<ActivityMainBinding, MainViewModel>(), MainNavigator, HasSupportFragmentInjector
{
    val REQUEST_WRITE_ACCESS = 1
    val REQUEST_CAMERA_ACCESS = 3

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
        setupNotificationAt6am()
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

            if(shirtData.isEmpty())
                mMainViewModel.shirt_available.set(getString(R.string.no_shirts_added))
            else
                mMainViewModel.shirt_available.set("")
            }

            mActivityMainBinding?.shirtViewPager?.postDelayed({
                mActivityMainBinding?.shirtViewPager?.setCurrentItem(getViewModel().shirtBeingViewedPosition,false)
                mMainViewModel.checkBookmark()
            }, 100)
        })

        mMainViewModel.pantsLiveData.observe(this, Observer<List<ShirtNPant>> {
            pantData -> pantData?.let{
            mMainViewModel.addPantsToList(it)


            if(pantData.isEmpty())
                mMainViewModel.pant_available.set(getString(R.string.no_pants_added))
            else
                mMainViewModel.pant_available.set("")
            }
            mActivityMainBinding?.pantViewPager?.postDelayed({
                mActivityMainBinding?.pantViewPager?.setCurrentItem(getViewModel().pantBeingViewedPosition,false)
                mMainViewModel.checkBookmark()
            }, 100)
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
        if(throwable.message == "BookMarkBeforeAddingDataNotAllowed")
            Toast.makeText(this,getString(R.string.bookmark_error),Toast.LENGTH_LONG).show()
        else if(throwable.message == "NoShuffleWithoutData")
            Toast.makeText(this,getString(R.string.shuffle_error),Toast.LENGTH_LONG).show()
    }

    override fun openAdder() {
        var builder: AlertDialog.Builder? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            builder = AlertDialog.Builder(this)
        }
        builder.setTitle(getString(R.string.add_image))
        .setMessage(getString(R.string.how_image))
        .setPositiveButton(R.string.camera,   { _, _ ->
            dispatchGetPictureIntent()
        })
        .setNegativeButton(R.string.gallery,  { _, _ ->
            dispatchGetFromGalleryIntent()
        })
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show()
    }

    override fun updateAdapter(code: Int) {
        if(code == 1)
            mShirtAdapter.notifyDataSetChanged()
        else
            mPantAdapter.notifyDataSetChanged()
    }

    override fun setRandomCombo(shirt_code: Int, pant_code:Int)
    {
        mActivityMainBinding?.pantViewPager?.setCurrentItem(pant_code,true)
        mActivityMainBinding?.shirtViewPager?.setCurrentItem(shirt_code,true)
        mMainViewModel.checkBookmark()
    }

    override fun askWhichRandom() {
        var builder: AlertDialog.Builder? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            builder = AlertDialog.Builder(this)
        }
        builder.setTitle(getString(R.string.shuffle))
        .setMessage(getString(R.string.how_shuffle))
        .setPositiveButton(R.string.bookmarks,   { dialog, which ->
                mMainViewModel.getRandomFavourite()
         })
        .setNegativeButton(R.string.random,  { dialog, which ->
            mMainViewModel.getCompletelyRandom()
         })
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show()
    }


    // false = imagepicker
    // true = camera
    var whyWantWriteAccess:Boolean = false

    fun dispatchGetFromGalleryIntent()
    {
        whyWantWriteAccess = false
        val permissions2 = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions2, REQUEST_WRITE_ACCESS)
        } else {
            getImagePickerObservable(this)
        }
    }

    fun dispatchGetPictureIntent()
    {
        whyWantWriteAccess = true
        val permissions2 = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions2, REQUEST_WRITE_ACCESS)
        }
        else
        {
            // If writing permission is there, check camera permission.
            val permissions = arrayOf(Manifest.permission.CAMERA)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CAMERA_ACCESS)
            } else {
                openCameraFromActivity(this)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_ACCESS) {
            if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraFromActivity(this)
            }
        }

        if (requestCode == REQUEST_WRITE_ACCESS) {
            if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(whyWantWriteAccess)
                    openCameraFromActivity(this)
                else
                    getImagePickerObservable(this)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun openCameraFromActivity(activity: Activity) {
        ImagePicker.cameraOnly().start(activity)
    }

    fun getImagePickerObservable(activity: Activity)
    {
        ImagePicker.create(activity).single().start()
    }

    private fun imageAdded(images: List<Image>?) {
        if (images == null) return
        val urri = Uri.fromFile(File(images[images.size -1].path))
        mMainViewModel.pictureAdded(urri.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            val images = ImagePicker.getImages(data) as ArrayList<Image>
            imageAdded(images)
        }
    }

    // Notification Code

    private fun setupNotificationAt6am()
    {
        val alarmManager: AlarmManager? = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager?.let {
            val notificationIntent = Intent("android.media.action.DISPLAY_NOTIFICATION")
            notificationIntent.addCategory("android.intent.category.DEFAULT")

            val broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val cal = Calendar.getInstance()
            // If todays 6 am has already passed, it will notify gain tommorow.
            if(cal.get(Calendar.HOUR_OF_DAY) >= 6)
                cal.add(Calendar.DATE, 1)

            // Set time to 6am
            cal.set(Calendar.HOUR_OF_DAY, 6)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                it.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, broadcast)
            }
            else
                it.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, broadcast)
            it.setRepeating(AlarmManager.RTC, cal.timeInMillis, AlarmManager.INTERVAL_DAY, broadcast)
        }
    }

}