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

}