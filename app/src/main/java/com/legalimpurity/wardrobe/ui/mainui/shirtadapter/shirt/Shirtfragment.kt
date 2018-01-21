package com.legalimpurity.wardrobe.ui.mainui.shirtadapter.shirt

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.legalimpurity.wardrobe.BR
import com.legalimpurity.wardrobe.R
import com.legalimpurity.wardrobe.data.models.ShirtNPant
import com.legalimpurity.wardrobe.databinding.FragmentShirtBinding
import com.legalimpurity.wardrobe.ui.base.BaseFragment
import com.squareup.picasso.Picasso
import javax.inject.Inject

/**
 * Created by rajatkhanna on 21/01/18.
 */
val ShirtNPant_OBJ = "ShirtNPant_OBJ"
fun newShirtFragment(shirtNPant: ShirtNPant) : Shirtfragment
{
    val bundle = Bundle()
    bundle.putParcelable(ShirtNPant_OBJ,shirtNPant)
    val eventsFragment = Shirtfragment()
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
        arguments.getParcelable<ShirtNPant>(ShirtNPant_OBJ)?.let {
            Picasso.with(this.context).load(Uri.parse(it.shirtnPantPath)).fit().centerCrop().into(mFragmentShirtBinding?.itemImageView)
        }
    }

    //Functions to be implemented by every Fragment
    override fun getViewModel() = mShirtViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.fragment_shirt
}