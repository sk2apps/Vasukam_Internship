package com.saggy.vasukaminternship.presentation.Adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.data.model.post.Comment
import com.saggy.vasukaminternship.data.model.user.ProfileInfo
import com.saggy.vasukaminternship.databinding.SingleRepliesRowBinding

class PostCommentReplyAdapter :
    RecyclerView.Adapter<PostCommentReplyAdapter.PostCommentViwHolder>() {

    val comment: ArrayList<Comment> = arrayListOf()

    fun submitData(comments: List<Comment>) {
        comment.clear()
        comment.addAll(comments)
    }

    class PostCommentViwHolder(val binding: SingleRepliesRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            if (comment == null){
                return
            }
            binding.CommentSingleCommentReplayRow.text = comment.msg
            val ago = DateUtils.getRelativeTimeSpanString(
                comment.timestamp.toLong(),
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_NUMERIC_DATE
            )
            binding.timeAgoSingleCommentReplayRow.text = ago

            val db = FirebaseFirestore.getInstance()
            db.collection("Users")
                .document(comment.userUid)
                .get()
                .addOnSuccessListener {
                    val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                    if (user != null) {
                        binding.userNameSingleCommentReplayRow.text = user.name

                        Glide.with(itemView).load(user.profile_pic).error(R.drawable.person_user)
                            .into(binding.userPhotoSingleCommentReplayRow)

                    }
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCommentViwHolder {
        val view =
            SingleRepliesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostCommentViwHolder(view)
    }

    override fun onBindViewHolder(holder: PostCommentViwHolder, position: Int) {
        holder.bind(comment[position])
    }

    override fun getItemCount(): Int {
        return comment.size
    }


}