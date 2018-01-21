package com.legalimpurity.wardrobe.ui.mainui.shirtadapter.shirt

import android.os.Bundle
import android.view.View
import com.legalimpurity.wardrobe.BR
import com.legalimpurity.wardrobe.R
import com.legalimpurity.wardrobe.databinding.FragmentShirtBinding
import com.legalimpurity.wardrobe.ui.base.BaseFragment
import javax.inject.Inject

/**
 * Created by rajatkhanna on 21/01/18.
 */
fun newShirtFragment() : Shirtfragment
{
    var bundle = Bundle()
    var eventsFragment = Shirtfragment()
    eventsFragment.arguments = bundle
    return eventsFragment
}

class Shirtfragment : BaseFragment<FragmentShirtBinding, ShirtViewModel>()
{

    @Inject lateinit var mShirtViewModel: ShirtViewModel

    private var mFragmentShirtBinding: FragmentShirtBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentShirtBinding = getViewDataBinding()
    }

    //Functions to be implemented by every Fragment
    override fun getViewModel() = mShirtViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_shirt
}