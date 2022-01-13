package com.saggy.vasukaminternship.presentation.activity.post


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.makeramen.roundedimageview.RoundedImageView
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.createDefault
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.data.model.post.Post
import com.saggy.vasukaminternship.databinding.ActivityImagePostBinding
import com.saggy.vasukaminternship.presentation.Adapter.GridImageAdapter
import com.saggy.vasukaminternship.presentation.activity.home.Home_Activity
import com.saggy.vasukaminternship.utils.*
import com.saggy.vasukaminternship.utils.FileSearch.getAllImages
import com.saggy.vasukaminternship.utils.FileSearch.getDirectoryPaths
import com.saggy.vasukaminternship.utils.FileSearch.getFilePaths
import com.saggy.vasukaminternship.utils.PostHelperKit.selectedImageURI
import java.io.File


class ImagePostActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityImagePostBinding
    val binding
        get() = _binding

    private val CAMERA_REQUEST_CODE = 5
    private val GALLERY_REQUEST_CODE = 6
    private val MY_PERMISSIONS_REQUEST_CAMERA = 0
    private val MY_PERMISSIONS_REQUEST_READ_WRITE = 1

    private val TAG = "GalleryFragment"

    //vars
    private lateinit var directories: ArrayList<String>
    private val mAppend = "file:/"
    private var mSelectedImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_image_post)

        binding.progressBar.invisible()
        directories = arrayListOf()

        PostHelperKit.clearPostData()
        PostHelperKit.clearSelectedImage()
        PostHelperKit.post = Post()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                MY_PERMISSIONS_REQUEST_READ_WRITE
            )
        } else {
            init()
        }

        binding.textPostCardView.setOnClickListener {
            startActivity(Intent(this, TextPostActivity::class.java))
        }

        binding.camera.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA
                )
            } else {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            }

        }
        binding.gallery.setOnClickListener {
            openGalleryForImage()
        }

        binding.nextBtn.setOnClickListener {

            PostHelperKit.selectedImage = mAppend + mSelectedImage
            val intent = Intent(this, ImagePostReviewActivity::class.java)
            startActivity(intent)
        }

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            with(binding) {
                galleryImageView.setImageURI(data?.data)

            } // handle chosen image
            selectedImageURI = data?.data

            mSelectedImage = FileSearch.getRealPathFromURI(this, data?.data!!)

        } else if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {

            with(binding) {

                galleryImageView.setImageBitmap(data?.extras?.get("data") as Bitmap)
            }

            selectedImageURI = data?.data
            mSelectedImage = FileSearch.getRealPathFromURI(this, data?.data!!)

        }

    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun init() {

        directories.add("AllImages")
        directories.addAll(getDirectoryPaths(FilePaths.CAMERA))
        directories.addAll(getDirectoryPaths(FilePaths.PICTURES))

        val wpFolder = File(FilePaths.WHATSAPP)

        when {
            wpFolder.exists() -> {
                directories.add(FilePaths.WHATSAPP)
            }
        }

        val directoryNames: ArrayList<String> = ArrayList()
        for (i in directories.indices) {
            if (i != 0) {
                val index = directories[i].lastIndexOf("/")
                val string = directories[i].substring(index)
                directoryNames.add(string.drop(1))
            } else {
                directoryNames.add("All Images")
            }
        }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, directoryNames
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDirectory.adapter = adapter
        binding.spinnerDirectory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    setupGridView(directories[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

    }


    private fun setupGridView(selectedDirectory: String) {

        var imgURLs = ArrayList<String>()

        when (selectedDirectory) {
            "AllImages" -> {
                imgURLs = getAllImages(this)
            }
            else -> {
                imgURLs = getFilePaths(selectedDirectory)
            }
        }

        //set the grid column width
//        val gridWidth = resources.displayMetrics.widthPixels
//        val imageWidth = gridWidth / NUM_GRID_COLUMNS
//        binding.galaryImageRecView.columnWidth = imageWidth

        //use the grid adapter to adapter the images to gridview
        val adapter = GridImageAdapter(this, R.layout.layout_grid_imageview, mAppend, imgURLs)
        binding.galaryImageRecView.adapter = adapter

        if (imgURLs.isNotEmpty()) {
            try {
                setImage(imgURLs[0], binding.galleryImageView, mAppend)
                mSelectedImage = imgURLs[0]
                selectedImageURI = Uri.fromFile(File(mSelectedImage))

            } catch (e: ArrayIndexOutOfBoundsException) {
                Log.e(TAG, "setupGridView: ArrayIndexOutOfBoundsException: " + e.message)
            }
        }

        binding.galaryImageRecView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                Log.d(TAG, "onItemClick: selected an image: " + imgURLs[position])
                setImage(imgURLs[position], binding.galleryImageView, mAppend)
                mSelectedImage = imgURLs[position]
                selectedImageURI = Uri.fromFile(File(mSelectedImage))
            }
    }

    private fun setImage(imgURL: String, image: RoundedImageView, append: String) {


        val imageLoader: ImageLoader = ImageLoader.getInstance()
        imageLoader.init(createDefault(this))
//        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MyActivity.this));

        imageLoader.displayImage(append + imgURL, image, object : ImageLoadingListener {
            override fun onLoadingStarted(imageUri: String, view: View) {
                binding.progressBar.show()
            }

            override fun onLoadingFailed(imageUri: String, view: View, failReason: FailReason) {
                binding.progressBar.invisible()
            }

            override fun onLoadingComplete(imageUri: String, view: View, loadedImage: Bitmap) {
                binding.progressBar.invisible()
            }

            override fun onLoadingCancelled(imageUri: String, view: View) {
                binding.progressBar.invisible()
            }
        })


    }

    override fun onBackPressed() {
        PostHelperKit.clearPostData()
        PostHelperKit.clearSelectedImage()
        startActivity(Intent(this, Home_Activity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }

}