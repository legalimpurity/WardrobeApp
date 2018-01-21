package com.legalimpurity.wardrobe.util

import android.databinding.BindingAdapter
import android.support.v4.view.ViewPager
import com.legalimpurity.wardrobe.data.models.ShirtNPant
import com.legalimpurity.wardrobe.ui.mainui.shirtadapter.ShirtAdapter
import java.util.*

/**
 * Created by rajatkhanna on 21/01/18.
 */
@BindingAdapter("adapter")
fun wardrobeAdapterBinding(viewPager: ViewPager,
                           courses: ArrayList<ShirtNPant>) {
    if(viewPager.adapter is ShirtAdapter) {
        val adapter = viewPager.adapter as ShirtAdapter
        adapter.setData(courses)
    }
}
