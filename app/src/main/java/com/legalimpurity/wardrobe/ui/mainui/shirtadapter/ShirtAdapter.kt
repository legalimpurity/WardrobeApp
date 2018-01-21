package com.legalimpurity.wardrobe.ui.mainui.shirtadapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.legalimpurity.wardrobe.data.models.ShirtNPant
import com.legalimpurity.wardrobe.ui.mainui.shirtadapter.shirt.newShirtFragment

/**
 * Created by rajatkhanna on 21/01/18.
 */
class ShirtAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    var shirtsList: MutableList<ShirtNPant> = ArrayList()


    override fun getItem(position: Int): Fragment = newShirtFragment(shirtsList[position])

    override fun getCount(): Int = shirtsList.size

    override fun getPageTitle(position: Int) = shirtsList[position].id.toString()

    fun setData(repoList: List<ShirtNPant>?) {

        repoList?.let {
            shirtsList.clear()
            shirtsList.addAll(repoList)
            notifyDataSetChanged()
        }
    }

}