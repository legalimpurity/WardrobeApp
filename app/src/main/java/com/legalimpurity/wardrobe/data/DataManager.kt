package com.legalimpurity.wardrobe.data

import com.legalimpurity.wardrobe.data.local.db.DatabaseHelper
import com.legalimpurity.wardrobe.data.local.prefs.PreferenceHelper

/**
 * Created by rajatkhanna on 20/01/18.
 */
interface DataManager : PreferenceHelper, DatabaseHelper
{
}