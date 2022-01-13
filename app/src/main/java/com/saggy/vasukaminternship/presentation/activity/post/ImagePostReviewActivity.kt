package com.saggy.vasukaminternship.presentation.activity.post

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.databinding.ActivityImagePostReviewBinding
import com.saggy.vasukaminternship.presentation.activity.home.Home_Activity
import com.saggy.vasukaminternship.utils.PostHelperKit
import com.saggy.vasukaminternship.utils.displayToast
import com.saggy.vasukaminternship.utils.invisible
import com.saggy.vasukaminternship.utils.show


class ImagePostReviewActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityImagePostReviewBinding
    val binding
        get() = _binding

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_image_post_review)


        PostHelperKit.post!!.on_market = binding.marketPlace.isChecked

        if (PostHelperKit.selectedImage != null) {
            setImage()
        }

        binding.postOnBtn.setOnClickListener {
            val intent = Intent(this, PostOnActivity::class.java)
            intent.putExtra("postType", "ImagePost")
            startActivity(intent)
        }


        binding.addLocation.setOnClickListener {
            val intent = Intent(this, PostAddLocationActivity::class.java)
            intent.putExtra("postType", "ImagePost")
            startActivity(intent)
        }

        binding.marketPlace.setOnCheckedChangeListener { button, isChecked ->
            PostHelperKit.post!!.on_market = isChecked
        }

        binding.advanceSettings.setOnClickListener {
            val intent = Intent(this, PostAdvanceSettingsActivity::class.java)
            intent.putExtra("postType", "ImagePost")
            startActivity(intent)
        }

        binding.doneBtn.setOnClickListener {

            when {
                binding.titlePostEt.text.isEmpty() -> {
                    this.displayToast("Please Enter Post title")
                }
                binding.descPostEt.text.isEmpty() -> {
                    this.displayToast("Please Enter Post Description")
                }
                else -> {

                    PostHelperKit.post!!.title = binding.titlePostEt.text.toString()
                    PostHelperKit.post!!.desc = binding.descPostEt.text.toString()
                    PostHelperKit.post!!.postType = "ImagePost"
                    PostHelperKit.post!!.post_id = "VasukamPost" + System.currentTimeMillis()
                    PostHelperKit.post!!.timestamp = System.currentTimeMillis().toString()

                    if (firebaseUser != null) {
                        PostHelperKit.post!!.CreatorUid = firebaseUser.uid
                    }

                    uploadImage()
                }
            }


        }
    }

    fun uploadImage() {
        val fileName = "VasukamPost" + System.currentTimeMillis() + ".jpg"

        var list: List<String> = arrayListOf()
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading Post")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.isIndeterminate = true
        progressDialog.progress = 0
        progressDialog.show()

        val refStorage = FirebaseStorage.getInstance().reference.child("Posts/$fileName")
        refStorage.putFile(PostHelperKit.selectedImageURI!!)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    list = listOf(imageUrl)
                    PostHelperKit.post?.imageUrl = list
                    progressDialog.progress = 100
                    uploadPost()
                }
            }
            .addOnProgressListener {
                val progress = (100 * it.bytesTransferred) / it.totalByteCount
                progressDialog.progress = progress.toInt()
            }
            .addOnFailureListener { e ->
                this.displayToast(e.message)
            }
    }


    fun uploadPost() {
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("Posts")


        ref.add(PostHelperKit.post!!).addOnSuccessListener {

            this.displayToast("Post Uploaded Successfully")
            PostHelperKit.clearPostData()
            PostHelperKit.clearSelectedImage()
            progressDialog.dismiss()
            startActivity(Intent(this, Home_Activity::class.java))
            finish()

        }.addOnFailureListener {

//            this.displayToast(it.message)

        }

    }

    private fun setImage() {
        val imageLoader: ImageLoader = ImageLoader.getInstance()
        imageLoader.init(ImageLoaderConfiguration.createDefault(this))

        imageLoader.displayImage(
            PostHelperKit.selectedImage,
            binding.galleryImageView,
            object : ImageLoadingListener {
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
}
