
package com.university.ip.ui.editor

import android.graphics.Bitmap
import com.university.ip.repository.Operators
import com.university.ip.ui.base.BasePresenter
import org.opencv.core.Mat

class EditorPresenter : BasePresenter<EditorContract.View>(), EditorContract.Presenter {

    private val operators = Operators()

    override fun brightness(bitmap: Bitmap, value: Int):Bitmap {
        val result = operators.increaseBrightness(bitmap, value)
        getView()?.setBitmap(result)
        return result
    }

    override fun contrast(bitmap: Bitmap, value: Int):Bitmap{
        val result = operators.increaseContrast(bitmap, value)
        getView()?.setBitmap(result)
        return result

    }

    override fun rotation(bitmap: Bitmap, value: Float):Bitmap {
        val result=operators.rotate(bitmap,value)
        getView()?.setBitmap(result)
        return result
    }

    override fun gaussianBluring(bitmap: Bitmap, value: Int):Bitmap {
        val result=operators.gaussianBlur(bitmap,value)
        getView()?.setBitmap(result)
        return result
    }

    override fun greyFilter(bitmap: Bitmap, value: Int):Bitmap {
        val result=operators.toGrey(bitmap,value)
        getView()?.setBitmap(result)
        return result
    }

    override fun threshold(bitmap: Bitmap, value: Int):Bitmap {
        val result=operators.threshold(bitmap,value)
        getView()?.setBitmap(result)
        return result
    }

    override fun applyAdaptiveThreshold(bitmap: Bitmap, value: Int):Bitmap {
        val result=operators.adaptiveThreshold(bitmap,value)
        getView()?.setBitmap(result)
        return result
    }

    override fun applySobel(bitmap: Bitmap, value: Int):Bitmap {
        val result=operators.sobelFilter(bitmap,value)
        getView()?.setBitmap(result)
        return result
    }

    override fun applyCanny(bitmap: Bitmap, value: Int):Bitmap {
        val result=operators.cannyFilter(bitmap,value)
        getView()?.setBitmap(result)
        return result
    }

    override fun applyUnsharpMask(bitmap: Bitmap, value: Int):Bitmap {
        val result=operators.unsharpMask(bitmap,value)
        getView()?.setBitmap(result)
        return result
    }

    override fun applyMedianBlur(bitmap: Bitmap, value: Int):Bitmap {
        val result=operators.medianBlur(bitmap,value)
        getView()?.setBitmap(result)
        return result
    }






}