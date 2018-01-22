package com.legalimpurity.wardrobe.ui.mainui

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.widget.Toast
import com.legalimpurity.wardrobe.BR
import com.legalimpurity.wardrobe.R
import com.legalimpurity.wardrobe.data.models.ShirtNPant
import com.legalimpurity.wardrobe.databinding.ActivityMainBinding
import com.legalimpurity.wardrobe.ui.base.BaseActivity
import com.legalimpurity.wardrobe.ui.mainui.shirtadapter.ShirtAdapter
import com.legalimpurity.wardrobe.util.FileStorageUtil
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
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

    @Inject lateinit var fileStorageUtil : FileStorageUtil

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
    }

    override fun openAddder() {
        var builder: AlertDialog.Builder? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            builder = AlertDialog.Builder(this)
        }
        builder.setTitle(getString(R.string.add_image))
        .setMessage(getString(R.string.how_image))
        .setPositiveButton(R.string.camera,   { _, _ ->
            fileStorageUtil.dispatchTakePictureIntent(this,REQUEST_IMAGE_CAPTURE,object : FileStorageUtil.PictureFileCreated
            {
                override fun pictureCreated(strr:String){
                    mMainViewModel.pictureCreated(strr)
                }
            })
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

    override fun bookMarkCurrentCombo() {
        mActivityMainBinding?.fabFavourite?.setImageDrawable(resources.getDrawable(R.drawable.ic_favorite_black_24dp))
    }

    override fun setRandomCombo(shirt_code: Int, pant_code:Int)
    {
        mActivityMainBinding?.pantViewPager?.setCurrentItem(pant_code,true)
        mActivityMainBinding?.shirtViewPager?.setCurrentItem(shirt_code,true)
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


    fun dispatchGetFromGalleryIntent()
    {
        fileStorageUtil.openImageChooserActivityFromActivity(this,RESULT_LOAD_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
            mMainViewModel.pictureAdded()
        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            val urri = fileStorageUtil.getFileOnActivityResult(this,resultCode,data)
            mMainViewModel.pictureCreated(urri.toString())
            mMainViewModel.pictureAdded()
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
            cal.set(Calendar.HOUR_OF_DAY, 6)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                it.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, broadcast)
            }
            else
                it.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, broadcast)
        }
    }

}