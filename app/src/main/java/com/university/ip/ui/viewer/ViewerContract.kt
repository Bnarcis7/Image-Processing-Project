package com.university.ip.ui.viewer

import android.graphics.Bitmap
import com.university.ip.ui.base.BaseContract

interface ViewerContract{
    interface View : BaseContract.View{
        //view functions for each change of activity
        fun setBitmap(bitmap: Bitmap)
    }

    interface Presenter{
        //functions that are going to use our library
        fun rotation(photo: Bitmap,value: Float):Bitmap
    }
}