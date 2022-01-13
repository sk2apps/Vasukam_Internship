package com.saggy.vasukaminternship.presentation.activity.post

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.data.model.post.Comment
import com.saggy.vasukaminternship.data.model.user.ProfileInfo
import com.saggy.vasukaminternship.databinding.ActivityPostCommentBinding
import com.saggy.vasukaminternship.presentation.Adapter.PostCommentAdapter
import com.saggy.vasukaminternship.utils.displayToast
import com.saggy.vasukaminternship.utils.invisible
import com.saggy.vasukaminternship.utils.show

class PostCommentActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityPostCommentBinding
    val binding
        get() = _binding

    var postId = ""

    val firebaseAuth = FirebaseAuth.getInstance()
    val user = firebaseAuth.currentUser


    private lateinit var postCommentAdapter: PostCommentAdapter
    private lateinit var comments: ArrayList<Comment>

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_post_comment)

        binding.ProgressBar.show()
        binding.commentsRecView.invisible()

        postId = intent.getStringExtra("PostId").toString()

        comments = arrayListOf()

        postCommentAdapter = PostCommentAdapter()
        binding.commentsRecView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postCommentAdapter
            setHasFixedSize(false)
        }

        readCommentFireStoreData()

        checkUser()

        binding.commentBtn.setOnClickListener {

            if (user != null) {
                if (binding.comment.text.isNotEmpty()) {
                    updateComment()
                }

            } else {
                this.displayToast("User Not Found")
            }
        }

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun readCommentFireStoreData() {
        db.collection("PostActions").document(postId).collection("Comments")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val document: QuerySnapshot = it.result
                    document.forEach { data ->
                        val comment: Comment = data.toObject(Comment::class.java)
                        comments.add(comment)
                    }
                    postCommentAdapter.submitData(comments)
                    postCommentAdapter.notifyDataSetChanged()
                    binding.ProgressBar.invisible()
                    binding.commentsRecView.show()
                }
            }
            .addOnFailureListener {
                this.displayToast(it.message)
            }
    }

    private fun checkUser() {

        if (user == null) {
            this.displayToast("User is Null")
        } else {
            loadMyInfo(user)
        }

    }


    private fun loadMyInfo(user: FirebaseUser) {

        db.collection("Users")
            .document(user.uid)
            .get()
            .addOnSuccessListener {
                val user1: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                if (user1 != null) {
                    Glide.with(this).load(user1.profile_pic).override(200, 200)
                        .error(R.drawable.person_user)
                        .into(binding.profileImage)
                }
            }
            .addOnFailureListener {
                this.displayToast(it.message)
            }
    }

    private fun updateComment() {
        val id = "CMNT" + System.currentTimeMillis().toString()
        val comment = Comment(
            id,
            postId,
            user!!.uid,
            binding.comment.text.toString(),
            System.currentTimeMillis().toString()
        )

        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("PostActions").document(postId).collection("Comments").document(id)

        ref.set(comment).addOnSuccessListener {
            binding.comment.setText("")
            comments.clear()
            readCommentFireStoreData()
        }.addOnFailureListener {

            this.displayToast(it.message)

        }

    }
}