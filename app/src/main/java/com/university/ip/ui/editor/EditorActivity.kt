package com.university.ip.ui.editor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.rotationMatrix
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.university.ip.R
import com.university.ip.repository.Operators
import com.university.ip.ui.main.MainActivity
import com.university.ip.util.files.FileSaver.Companion.IMAGE_MIME_TYPE
import com.university.ip.util.files.FileSaverLegacy
import org.opencv.android.OpenCVLoader

class EditorActivity : AppCompatActivity(), EditorContract.View, View.OnClickListener,
    FiltersAdapter.ItemClickListener, SeekBar.OnSeekBarChangeListener {

    override fun appContext(): Context = applicationContext
    private val TAG = "EditorActivity"

    private lateinit var backButton: ImageView
    private lateinit var saveButton: TextView
    private lateinit var rotateRightButton:ImageView
    private lateinit var rotateLeftButton:ImageView
    private lateinit var flip:ImageView


    private lateinit var imageView: ImageView
    private lateinit var filterList: RecyclerView
    private lateinit var seekBar: SeekBar

    private lateinit var adapter: FiltersAdapter

    private lateinit var fileSaver: FileSaverLegacy
    private lateinit var bitmap: Bitmap
    private lateinit var modifiedBitmap: Bitmap
    private lateinit var selectedFilter: String
    private lateinit var presenter: EditorPresenter

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        //list initialization
        val layoutManager = LinearLayoutManager(appContext(), LinearLayoutManager.HORIZONTAL, false)
        filterList = findViewById(R.id.filters_list)
        filterList.layoutManager = layoutManager
        adapter = FiltersAdapter(appContext(), this)
        adapter.setMediaList(FILTERS_ARRAY);
        filterList.adapter = adapter

        seekBar = findViewById(R.id.seek_bar_editor)

        backButton = findViewById(R.id.back_editor)
        backButton.setOnClickListener(this)

        rotateRightButton=findViewById(R.id.rotate_right_editor)
        rotateRightButton.setOnClickListener(this)
        rotateLeftButton=findViewById(R.id.rotate_left_editor)
        rotateLeftButton.setOnClickListener(this)
        flip=findViewById(R.id.flip)
        flip.setOnClickListener(this)


        imageView = findViewById(R.id.image_edited)

        fileSaver = FileSaverLegacy(appContext())
        saveButton = findViewById(R.id.save_button)
        saveButton.setOnClickListener(this)
        presenter = EditorPresenter()
        presenter.bindView(this)
        //image load
        loadImage()
        openCvInit()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
    }

    private fun openCvInit() {
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCV not loaded");
        } else {
            Log.e(TAG, "OpenCV loaded");
        }
    }

    private fun loadImage() {
        val data = intent.getBundleExtra(INTENT_EXTRAS)
        val requestCode = intent.getIntExtra(REQUEST_CODE, 2)
        val resultCode = intent.getIntExtra(RESULT_CODE, Activity.RESULT_CANCELED)

        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == Activity.RESULT_OK && intent != null) {
                    var selectedImage = data.get("data") as Bitmap
                    bitmap = selectedImage
                    imageView.setImageBitmap(selectedImage)
                }
                1 -> if (resultCode == Activity.RESULT_OK && intent != null) {
                    val selectedImage = intent.data!!
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(
                        selectedImage,
                        filePathColumn, null, null, null
                    )
                    if (cursor != null) {
                        cursor.moveToFirst()

                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val picturePath = cursor.getString(columnIndex)
                        bitmap = BitmapFactory.decodeFile(picturePath)
                        modifiedBitmap=bitmap
                        imageView.setImageBitmap(bitmap)
                        cursor.close()
                    }

                }
            }
        }
    }

    companion object {
        const val INTENT_EXTRAS: String = "INTENT_EXTRAS"
        const val REQUEST_CODE: String = "REQUEST_CODE"
        const val RESULT_CODE: String = "RESULT_CODE"
        val FILTERS_ARRAY: List<String> = listOf("Brightness", "Contrast", "Gaussian Blur","ToGrey","Threshold","Adaptive Threshold","Sobel","Canny","Unsharp Mask","Median Blur")
        val FILTERS_SLIDER_ARRAY: List<String> = listOf("Brightness", "Contrast","Gaussian Blur","ToGrey","Threshold","Adaptive Threshold","Sobel","Canny","Unsharp Mask","Median Blur")

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_editor -> {
                finish()
            }
            R.id.save_button -> {
                var uri = fileSaver.getFileUri(IMAGE_MIME_TYPE) ?: return
                appContext().contentResolver.openOutputStream(uri)?.use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                }
                startActivity(Intent(appContext(), MainActivity::class.java))
            }
            R.id.rotate_right_editor -> {
             bitmap=presenter.rotation(bitmap,-90f)

            }
            R.id.rotate_left_editor -> {
                bitmap=presenter.rotation(bitmap,90f)

            }
            R.id.flip -> {
                bitmap=presenter.rotation(bitmap,180f)

            }
        }
    }

    override fun onItemClick(filter: String) {
        selectedFilter = filter
        if (FILTERS_SLIDER_ARRAY.indexOf(selectedFilter) >= 0) {
            seekBar.visibility = View.VISIBLE
            seekBar.setOnSeekBarChangeListener(this)
        } else {
            seekBar.visibility = View.GONE
        }
        println(FILTERS_SLIDER_ARRAY.indexOf(selectedFilter))
        bitmap=modifiedBitmap

    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        println(progress)
        println(selectedFilter)
        when (FILTERS_SLIDER_ARRAY.indexOf(selectedFilter)) {


            0 -> {
               modifiedBitmap= presenter.brightness(bitmap, progress)

        //ask ?
            }

            1 -> {

                modifiedBitmap=presenter.contrast(bitmap, progress)


            }

            2->{
                modifiedBitmap= presenter.gaussianBluring(bitmap,progress)


            }

            3->{
                modifiedBitmap=presenter.greyFilter(bitmap,progress)

            }

            4->{
                modifiedBitmap=presenter.threshold(bitmap,progress)

            }

            5->{
                modifiedBitmap= presenter.applyAdaptiveThreshold(bitmap,progress)

            }

            6->{
                modifiedBitmap=presenter.applySobel(bitmap,progress)

            }

            7->{
                modifiedBitmap=presenter.applyCanny(bitmap,progress)

            }

            8->{
                modifiedBitmap=presenter.applyUnsharpMask(bitmap,progress)

            }

            9->{
                modifiedBitmap=presenter.applyMedianBlur(bitmap,progress)

            }


            else -> return
        }

    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {}

    override fun setBitmap(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }
}