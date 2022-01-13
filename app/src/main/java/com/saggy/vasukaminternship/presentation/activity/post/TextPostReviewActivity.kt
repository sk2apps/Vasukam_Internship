package com.saggy.vasukaminternship.presentation.activity.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.data.model.post.Post
import com.saggy.vasukaminternship.databinding.ActivityTextPostReviewBinding
import com.saggy.vasukaminternship.presentation.activity.home.Home_Activity
import com.saggy.vasukaminternship.utils.PostHelperKit
import com.saggy.vasukaminternship.utils.displayToast

class TextPostReviewActivity : AppCompatActivity() {

    private var _binding: ActivityTextPostReviewBinding? = null
    val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_text_post_review)


        binding.titlePost.text = PostHelperKit.post!!.title
        binding.DescPost.text = PostHelperKit.post!!.desc
        PostHelperKit.post!!.on_market = binding.marketPlace.isChecked

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.doneBtn.setOnClickListener {
            uploadPost()
        }

        binding.postOnBtn.setOnClickListener {
            val intent = Intent(this, PostOnActivity::class.java)
            intent.putExtra("postType", "TextPost")
            startActivity(intent)
        }

        binding.addLocation.setOnClickListener {
            val intent = Intent(this, PostAddLocationActivity::class.java)
            intent.putExtra("postType", "TextPost")
            startActivity(intent)
        }

        binding.marketPlace.setOnCheckedChangeListener { button, isChecked ->
            PostHelperKit.post!!.on_market = isChecked
        }

        binding.advanceSettings.setOnClickListener {
            val intent = Intent(this, PostAdvanceSettingsActivity::class.java)
            intent.putExtra("postType", "TextPost")
            startActivity(intent)
        }

    }


    fun uploadPost() {
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("Posts")


        ref.add(PostHelperKit.post!!).addOnSuccessListener {

            this.displayToast("Post Uploaded Successfully")
            PostHelperKit.clearPostData()
            startActivity(Intent(this, Home_Activity::class.java))
            finish()

        }.addOnFailureListener {

            this.displayToast(it.message)

        }

    }

}