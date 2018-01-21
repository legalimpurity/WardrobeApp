package com.legalimpurity.wardrobe.ui.mainui.shirtadapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

/**
 * Created by rajatkhanna on 21/01/18.
 */
class MainActivityPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm)
{
    private var tabCount:Int = 0

    override fun getItem(position: Int): Fragment
    {
        when(position)
        {
            0 -> return newEventsFragment()
            1-> return newIncubateesFragment()
            2-> return newContestsFragment()
        }
        return newEventsFragment()
    }

    override fun getCount(): Int = tabCount

    fun setTabCount(tabCount:Int)
    {
        this.tabCount = tabCount
    }

    override fun getPageTitle(position: Int): CharSequence {
        when(position)
        {
            0 ->  return mainActivity.getString(R.string.events)
            1 ->  return mainActivity.getString(R.string.articles)
            2 ->  return mainActivity.getString(R.string.contests)
        }
        return ""
    }
}