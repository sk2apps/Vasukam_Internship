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
import com.saggy.vasukaminternship.databinding.ActivityPostCommentReplyBinding
import com.saggy.vasukaminternship.presentation.Adapter.PostCommentReplyAdapter
import com.saggy.vasukaminternship.utils.displayToast
import com.saggy.vasukaminternship.utils.invisible
import com.saggy.vasukaminternship.utils.show

class PostCommentReplyActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityPostCommentReplyBinding
    val binding
        get() = _binding

    var CommentId = ""
    var postId = ""

    val firebaseAuth = FirebaseAuth.getInstance()
    val user = firebaseAuth.currentUser
    val db = FirebaseFirestore.getInstance()

    private lateinit var postCommentReplyAdapter: PostCommentReplyAdapter
    private lateinit var replies: ArrayList<Comment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_post_comment_reply)

        CommentId = intent.getStringExtra("CommentId").toString()
        postId = intent.getStringExtra("PostId").toString()

        replies = arrayListOf()
        postCommentReplyAdapter = PostCommentReplyAdapter()

        binding.repliesRecView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postCommentReplyAdapter
            setHasFixedSize(false)
        }
        getComment()
        readCommentFireStoreData()
        checkUser()

        binding.replyBtn.setOnClickListener {

            if (user != null) {

                if (binding.reply.text.isNotEmpty()) {
                    updateReply()
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


        val ref =
            db.collection("PostActions").document(postId).collection("Comments").document(CommentId)
                .collection("Replies")

        ref.orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val document: QuerySnapshot = it.result
                    document.forEach { data ->
                        val comment: Comment = data.toObject(Comment::class.java)
                        replies.add(comment)
                    }
                    postCommentReplyAdapter.submitData(replies)
                    postCommentReplyAdapter.notifyDataSetChanged()
                    binding.ProgressBar.invisible()
                    binding.repliesRecView.show()
                }
            }
            .addOnFailureListener {
                this.displayToast(it.message)
            }
    }

    private fun getComment() {
        val db = FirebaseFirestore.getInstance()
        val ref =
            db.collection("PostActions").document(postId).collection("Comments").document(CommentId)

        ref.get().addOnSuccessListener {
            val comment: Comment? = it.toObject(Comment::class.java)
            if (comment != null) {
                binding.Comment.text = comment.msg
                loadCommentInfo(comment.userUid)
            }
        }
    }

    private fun updateReply() {
        val id = "CMNTRePly" + System.currentTimeMillis().toString()
        val comment = Comment(
            "",
            id,
            user!!.uid,
            binding.reply.text.toString(),
            System.currentTimeMillis().toString()
        )

        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("PostActions").document(postId).collection("Comments").document(CommentId).collection("Replies").document(id)

        ref.set(comment).addOnSuccessListener {
            binding.reply.setText("")
            replies.clear()
            readCommentFireStoreData()
        }.addOnFailureListener {

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

    private fun loadCommentInfo(userUId: String) {

        db.collection("Users")
            .document(userUId)
            .get()
            .addOnSuccessListener {
                val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                if (user != null) {
                    binding.userName.text = user.name
                    Glide.with(this).load(user.profile_pic).override(200, 200)
                        .error(R.drawable.person_user)
                        .into(binding.userPhoto)

                }
            }
            .addOnFailureListener {
                this.displayToast(it.message)
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

}