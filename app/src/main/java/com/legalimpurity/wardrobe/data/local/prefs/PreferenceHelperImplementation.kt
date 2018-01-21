package com.legalimpurity.wardrobe.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import com.legalimpurity.wardrobe.di.PreferenceInfo
import javax.inject.Inject

/**
 * Created by rajatkhanna on 20/01/18.
 */
class PreferenceHelperImplementation @Inject constructor(context: Context, @PreferenceInfo prefFileName:String) : PreferenceHelper {
    private var sharedPreferences: SharedPreferences? = null

    init {
        sharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
    }

    private val PREF_SHIRT_SELECTED_POS = "PREF_SHIRT_SELECTED_POS"
    private val PREF_PANT_SELECTED_POS = "PREF_PANT_SELECTED_POS"

    override fun getLastShirtSelected(): Int {
        var pp = 0
        sharedPreferences?.let {
            pp = it.getInt(PREF_SHIRT_SELECTED_POS,0)
            return pp
        }
        return pp
    }

    override fun setLastShirtSelected(pos: Int) {
        sharedPreferences?.edit()?.putInt(PREF_SHIRT_SELECTED_POS,pos)?.apply()
    }

    override fun getLastPantSelected(): Int {
        var pp = 0
        sharedPreferences?.let {
            pp = it.getInt(PREF_PANT_SELECTED_POS,0)
            return pp
        }
        return pp
    }

    override fun setLastPantSelected(pos: Int) {
        sharedPreferences?.edit()?.putInt(PREF_PANT_SELECTED_POS,pos)?.apply()
    }

}