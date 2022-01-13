package com.saggy.vasukaminternship.presentation.activity.post

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.data.model.post.Post
import com.saggy.vasukaminternship.databinding.ActivityTextPostBinding
import com.saggy.vasukaminternship.presentation.activity.home.Home_Activity
import com.saggy.vasukaminternship.utils.PostHelperKit
import com.saggy.vasukaminternship.utils.displayToast

class TextPostActivity : AppCompatActivity() {

    private var _binding: ActivityTextPostBinding? = null
    val binding
        get() = _binding!!


    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_text_post)

        binding.nextBtn.setColorFilter(R.color.white)
        PostHelperKit.clearPostData()

        binding.nextBtn.setOnClickListener {

            when {
                binding.descPostEt.text.isEmpty() -> {
                    this.displayToast("Please Enter Post Description")
                }
                else -> {
                    PostHelperKit.post = Post()
                    PostHelperKit.post!!.title = binding.titlePostEt.text.toString()
                    PostHelperKit.post!!.desc = binding.descPostEt.text.toString()
                    PostHelperKit.post!!.postType = "TextPost"
                    PostHelperKit.post!!.post_id = "VasukamPost" + System.currentTimeMillis()
                    PostHelperKit.post!!.timestamp = System.currentTimeMillis().toString()

                    if (firebaseUser != null) {
                        PostHelperKit.post!!.CreatorUid = firebaseUser.uid

                    }

                    startActivity(Intent(this, TextPostReviewActivity::class.java))
                }
            }


        }


        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.imagePostCardView.setOnClickListener {
            startActivity(Intent(this, ImagePostActivity::class.java))
        }

    }

    override fun onBackPressed() {
        PostHelperKit.clearPostData()
        startActivity(Intent(this, Home_Activity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        finish()
    }

}