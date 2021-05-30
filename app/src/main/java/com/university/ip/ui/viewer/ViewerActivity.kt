package com.university.ip.ui.viewer

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.github.chrisbanes.photoview.PhotoView
import com.university.ip.R
import com.university.ip.model.Photo
import com.university.ip.ui.editor.EditorPresenter
import com.university.ip.ui.main.MainActivity
import com.university.ip.util.files.FileSaver
import com.university.ip.util.files.FileSaverLegacy


class ViewerActivity : AppCompatActivity(), ViewerContract.View, View.OnClickListener {


    override fun appContext(): Context = applicationContext

    private lateinit var presenter: ViewerPresenter
    private lateinit var imageView: PhotoView
    private lateinit var backButton: ImageView
    private lateinit var photo: Photo
    private lateinit var bitmap: Bitmap
    private lateinit var rotateRightButton:ImageView
    private lateinit var rotateLeftButton:ImageView
    private lateinit var flip:ImageView
    private lateinit var fileSaver: FileSaverLegacy



    /** Called when the activity is first created. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)

        fileSaver = FileSaverLegacy(appContext())
        imageView = findViewById(R.id.image_viewer)
        backButton = findViewById(R.id.back_viewer)
        backButton.setOnClickListener(this)
        rotateRightButton=findViewById(R.id.rotate_right_viewer)
        rotateRightButton.setOnClickListener(this)
        rotateLeftButton=findViewById(R.id.rotate_left_viewer)
        rotateLeftButton.setOnClickListener(this)
        flip=findViewById(R.id.flip_viewer)
        flip.setOnClickListener(this)
        presenter = ViewerPresenter()
        presenter.bindView(this)

        photo = Photo(
            name = intent.getStringExtra(PHOTO_NAME)!!,
            path = intent.getStringExtra(PHOTO_PATH)!!
        )
        bitmap = BitmapFactory.decodeFile(photo.path)
        imageView.setImageBitmap(bitmap)


    }


    override fun onClick(v: View?) {
//        startActivity(Intent(appContext(), MainActivity::class.java))

        when (v?.id) {
            R.id.rotate_right_viewer -> {
                bitmap=presenter.rotation(bitmap,-90f)

            }
            R.id.rotate_left_viewer -> {
                bitmap=presenter.rotation(bitmap,90f)

            }
            R.id.flip_viewer -> {
                bitmap=presenter.rotation(bitmap,180f)

            }
            R.id.back_viewer -> {
                finish()
            }


        }
    }
    override fun setBitmap(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }

    companion object {
        const val PHOTO_PATH: String = "PHOTO_PATH"
        const val PHOTO_NAME: String = "PHOTO_NAME"
    }
}