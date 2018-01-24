package com.legalimpurity.wardrobe.ui.mainui

import com.legalimpurity.wardrobe.ui.base.BaseNavigator

/**
 * Created by rajatkhanna on 21/01/18.
 */
interface MainNavigator : BaseNavigator
{
    fun openAdder()
    fun updateAdapter(code: Int)
    fun setRandomCombo(shirt_code: Int, pant_code:Int)
    fun askWhichRandom()
}