package com.saggy.vasukaminternship.presentation.activity.post

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.databinding.ActivityPostOnBinding

class PostOnActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityPostOnBinding
    val binding
        get() = _binding

    var postType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_post_on)

        postType = intent.getStringExtra("postType").toString()

        binding.doneBtn.setOnClickListener {

            if (postType == "TextPost") {
                startActivity(Intent(this, TextPostReviewActivity::class.java))
            } else if (postType == "ImagePost") {
                startActivity(Intent(this, ImagePostReviewActivity::class.java))
            }

        }

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

    }
}