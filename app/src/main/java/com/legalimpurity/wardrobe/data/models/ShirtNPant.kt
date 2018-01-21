package com.legalimpurity.wardrobe.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by rajatkhanna on 20/01/18.
 */
@Entity(tableName = "Shirts")
class ShirtNPant() : Parcelable
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var shirtnPantPath: String = ""

    // 0 = nothing
    // 1 = shirt
    // 2 = pant
    var shirtOrPant: Int = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        shirtnPantPath = parcel.readString()
        shirtOrPant = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(shirtnPantPath)
        parcel.writeInt(shirtOrPant)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShirtNPant> {
        override fun createFromParcel(parcel: Parcel): ShirtNPant {
            return ShirtNPant(parcel)
        }

        override fun newArray(size: Int): Array<ShirtNPant?> {
            return arrayOfNulls(size)
        }
    }
}