package com.saggy.vasukaminternship.presentation.Adapter

import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.makeramen.roundedimageview.RoundedImageView
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.data.model.post.Comment
import com.saggy.vasukaminternship.data.model.user.ProfileInfo
import com.saggy.vasukaminternship.presentation.activity.post.PostCommentReplyActivity
import com.saggy.vasukaminternship.utils.hide
import com.saggy.vasukaminternship.utils.show
import kr.co.prnd.readmore.ReadMoreTextView

class PostCommentAdapter :
    RecyclerView.Adapter<PostCommentAdapter.PostCommentViwHolder>() {

    val comment : ArrayList<Comment> = arrayListOf()

//    private val differCallback = object : DiffUtil.ItemCallback<Comment>() {
//        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
//            return oldItem.commentId == newItem.commentId
//        }
//
//        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
//            return oldItem == newItem
//        }
//    }
//
//    val differ = AsyncListDiffer(this, differCallback)

    fun submitData( comments : List<Comment>){
        comment.clear()
        comment.addAll(comments)
    }

    class PostCommentViwHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(comment: Comment) {
            val userImage = itemView.findViewById<RoundedImageView>(R.id.userPhoto_singleCommentRow)
            val userName = itemView.findViewById<TextView>(R.id.userName_singleCommentRow)
            val totalReplies_single_Comment_row =
                itemView.findViewById<TextView>(R.id.totalReplies_single_Comment_row)
            val reply_singleCommentRow =
                itemView.findViewById<TextView>(R.id.reply_singleCommentRow)
            val timeAgo_singleCommentRw =
                itemView.findViewById<TextView>(R.id.timeAgo_singleCommentRw)
            val commentText = itemView.findViewById<ReadMoreTextView>(R.id.Comment_singleCommentRow)

            commentText.text = comment.msg
            totalReplies_single_Comment_row.hide()

            val ago = DateUtils.getRelativeTimeSpanString(
                comment.timestamp.toLong(),
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_NUMERIC_DATE
            )
            timeAgo_singleCommentRw.text = ago

            val db = FirebaseFirestore.getInstance()
            db.collection("Users")
                .document(comment.userUid)
                .get()
                .addOnSuccessListener {
                    val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                    if (user != null) {
                        userName.text = user.name

                        Glide.with(itemView).load(user.profile_pic).error(R.drawable.person_user)
                            .into(userImage)

                    }
                }

            try {
                val totalRepliesRef =
                    db.collection("PostActions").document(comment.postId).collection("Comments")
                        .document(comment.commentId)
                        .collection("Replies")

                totalRepliesRef.get().addOnSuccessListener {
                    val doc = it.documents
                    if (doc.size > 0) {
                        totalReplies_single_Comment_row.show()
                        totalReplies_single_Comment_row.text = "${doc.size}  Replies"
                    }
                }
            } catch (e: Exception) {

            }



            reply_singleCommentRow.setOnClickListener {
                val intent = Intent(itemView.context, PostCommentReplyActivity::class.java)
                intent.putExtra("CommentId", comment.commentId)
                intent.putExtra("PostId", comment.postId)
                itemView.context.startActivity(intent)
            }
            totalReplies_single_Comment_row.setOnClickListener {
                val intent = Intent(itemView.context, PostCommentReplyActivity::class.java)
                intent.putExtra("CommentId", comment.commentId)
                intent.putExtra("PostId", comment.postId)
                itemView.context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCommentViwHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_comment_row, parent, false)
        return PostCommentViwHolder(view)
    }

    override fun onBindViewHolder(holder: PostCommentViwHolder, position: Int) {
        holder.bind(comment[position])
    }

    override fun getItemCount(): Int {
        return comment.size
    }


}