package com.university.ip.repository

import android.graphics.Bitmap
import android.graphics.Matrix
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Core.addWeighted
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.*
import java.lang.Math.abs

class Operators {
    fun increaseBrightness(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        src.convertTo(src, -1, 1.0, value.toDouble())
        var result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun increaseContrast(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        src.convertTo(src, -1, value.toDouble(), 1.0)
        var result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun rotate(bitmap: Bitmap, degrees: Float): Bitmap {
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            Matrix().apply { postRotate(degrees.toFloat()) },
            true
        )
    }

    fun gaussianBlur(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        var resultBitmap: Bitmap =
            Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        var resultMat = Mat()
        var newValue = value
        if (newValue % 2 == 0)
            newValue++
        Imgproc.GaussianBlur(src, resultMat, Size(newValue.toDouble(), newValue.toDouble()), 0.0)
        Utils.matToBitmap(resultMat, resultBitmap)
        return resultBitmap

    }

    fun toGrey(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        var resultBitmap: Bitmap =
            Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        var resultMat = Mat()
        Imgproc.cvtColor(src, resultMat, Imgproc.COLOR_RGB2GRAY)
        Utils.matToBitmap(resultMat, resultBitmap)
        return resultBitmap
    }

    fun adaptiveThreshold(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        var resultBitmap: Bitmap =
            Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        var greyMat = Mat()
        var resultMat = Mat()
        var newValue = value;
        if (!(newValue % 2 == 1 && newValue > 1))
            newValue++;
        Imgproc.cvtColor(src, greyMat, Imgproc.COLOR_RGB2GRAY)
        Imgproc.adaptiveThreshold(
            greyMat,
            resultMat,
            255.0,
            ADAPTIVE_THRESH_MEAN_C,
            THRESH_BINARY,
            newValue,
            12.0
        )
        Utils.matToBitmap(resultMat, resultBitmap)
        return resultBitmap
    }

    fun threshold(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        var resultBitmap: Bitmap =
            Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        var greyMat = Mat()
        var resultMat = Mat()
        var newValue = value * 5.1

        Imgproc.cvtColor(src, greyMat, Imgproc.COLOR_RGB2GRAY)
        Imgproc.threshold(greyMat, resultMat, newValue, 255.0, 0)
        Utils.matToBitmap(resultMat, resultBitmap)
        return resultBitmap
    }

    fun unsharpMask(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        var resultBitmap: Bitmap =
            Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        var resultMat = Mat()

        Imgproc.GaussianBlur(src, resultMat, Size(0.0, 0.0), 3.0)
        addWeighted(src, 1.5, resultMat, -0.5, 0.0, resultMat)
        Utils.matToBitmap(resultMat, resultBitmap)
        return resultBitmap

    }

    fun sobelFilter(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        var resultBitmap: Bitmap =
            Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        var greyMat = Mat()
        var resultMat = Mat()
        var newValue = value;
        if (newValue % 2 == 0)
            newValue++;
        Imgproc.cvtColor(src, greyMat, Imgproc.COLOR_RGB2GRAY)
        Imgproc.Sobel(greyMat, resultMat, CvType.CV_8U, 1, 1);
        Core.convertScaleAbs(resultMat, resultMat, newValue.toDouble(), 0.0);
        Imgproc.cvtColor(resultMat, src, Imgproc.COLOR_GRAY2BGRA, 4);
        Utils.matToBitmap(src, resultBitmap)
        return resultBitmap
    }

    fun cannyFilter(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        var resultBitmap: Bitmap =
            Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        var resultMat = Mat()
        var newValue = value;
        if (newValue % 2 == 0)
            newValue++;
        Imgproc.Canny(src, resultMat, newValue.toDouble(), 255.0)
        Utils.matToBitmap(resultMat, resultBitmap)
        return resultBitmap
    }

    fun medianBlur(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        var resultBitmap: Bitmap = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        var resultMat = Mat()
        var newValue = value;
        if (newValue % 2 == 0)
            newValue++;
        Imgproc.medianBlur(src, resultMat, newValue)
        Utils.matToBitmap(resultMat, resultBitmap)
        return resultBitmap
    }


}
