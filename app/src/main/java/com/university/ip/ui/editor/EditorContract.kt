package com.university.ip.ui.editor

import android.graphics.Bitmap
import com.university.ip.ui.base.BaseContract

 interface EditorContract {

    interface View : BaseContract.View {
        //view functions for each change of activity
        fun setBitmap(bitmap: Bitmap)
    }

    interface Presenter {
        //functions that are going to use our library

        fun brightness(bitmap: Bitmap, value: Int):Bitmap

        fun contrast(bitmap: Bitmap, value: Int):Bitmap

        fun rotation(bitmap: Bitmap,value: Float):Bitmap

        fun gaussianBluring(bitmap: Bitmap,value: Int):Bitmap

        fun greyFilter(bitmap: Bitmap,value:Int):Bitmap

        fun threshold(bitmap: Bitmap, value:Int):Bitmap

        fun applyAdaptiveThreshold(bitmap: Bitmap, value:Int):Bitmap

        fun applySobel(bitmap: Bitmap,value: Int):Bitmap

        fun applyCanny(bitmap: Bitmap,value: Int):Bitmap

        fun applyUnsharpMask(bitmap: Bitmap,value: Int):Bitmap

        fun applyMedianBlur(bitmap: Bitmap,value: Int):Bitmap


    }
}