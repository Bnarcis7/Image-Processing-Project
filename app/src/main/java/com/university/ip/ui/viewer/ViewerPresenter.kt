package com.university.ip.ui.viewer

import android.graphics.Bitmap
import com.university.ip.repository.Operators
import com.university.ip.ui.base.BasePresenter

class ViewerPresenter : BasePresenter<ViewerContract.View>(), ViewerContract.Presenter{

    private val operators = Operators()

    override fun rotation(bitmap: Bitmap, value: Float):Bitmap {
        val result=operators.rotate(bitmap,value)
        getView()?.setBitmap(result)
        return result
    }
}