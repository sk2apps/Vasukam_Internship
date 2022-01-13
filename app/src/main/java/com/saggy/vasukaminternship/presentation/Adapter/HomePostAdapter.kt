package com.saggy.vasukaminternship.presentation.Adapter


import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.text.format.DateUtils.*
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.makeramen.roundedimageview.RoundedImageView
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.data.model.post.Post
import com.saggy.vasukaminternship.data.model.post.PostLike
import com.saggy.vasukaminternship.data.model.user.ProfileInfo
import com.saggy.vasukaminternship.presentation.activity.post.PostCommentActivity
import com.saggy.vasukaminternship.utils.displayToast
import com.saggy.vasukaminternship.utils.hide
import com.saggy.vasukaminternship.utils.invisible
import kr.co.prnd.readmore.ReadMoreTextView
import me.relex.circleindicator.CircleIndicator2
import android.os.CountDownTimer
import android.widget.*
import com.google.firebase.firestore.FieldValue.delete
import com.google.firebase.firestore.util.FileUtil.delete
import com.google.rpc.context.AttributeContext
import android.view.animation.Animation

import android.view.animation.Animation.AnimationListener

import android.graphics.Bitmap
import android.view.animation.AnimationUtils
import android.graphics.drawable.Drawable

import android.graphics.drawable.TransitionDrawable
import android.util.Log


private const val IMAGE_POST = 1
private const val TEXT_POST = 0

class HomePostAdapter(var postListItem: List<Post>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // View Holder


    class ImagePostViwHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var adapterImages: PostImagesAdapter
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        val db = FirebaseFirestore.getInstance()


        @SuppressLint("ClickableViewAccessibility")
        fun bind(post: Post) {

            var isLiked: Int = 0
            var isFireFigmLike: Boolean = false
            var otherLikeCount: Int = 0
            var firstUserLikeImage: ProfileInfo? = null
            val title = itemView.findViewById<TextView>(R.id.titlePost)
            val likes_others_imagePost =
                itemView.findViewById<TextView>(R.id.likes_others_imagePost)
            val desc = itemView.findViewById<ReadMoreTextView>(R.id.DescPost)

            val postImageRecView_single_ImagePost =
                itemView.findViewById<RecyclerView>(R.id.postImageRecView_single_ImagePost)

            val date_location =
                itemView.findViewById<TextView>(R.id.date_time_location_ImagePostSingle)
            val commentBtn_ImagePost = itemView.findViewById<ImageView>(R.id.commentBtn_ImagePost)
            val fireBtn = itemView.findViewById<ImageView>(R.id.fireBtn_ImagePost)
            val ownerNmae = itemView.findViewById<TextView>(R.id.ownerName_ImagePost)
            val ownerImage_ImagePost =
                itemView.findViewById<RoundedImageView>(R.id.ownerImage_ImagePost)
            val firstLiker_SingleImagePost =
                itemView.findViewById<RoundedImageView>(R.id.firstLiker_SingleImagePost)
            val userImage_sigleImagePost =
                itemView.findViewById<RoundedImageView>(R.id.userImage_sigleImagePost)
            val userName = itemView.findViewById<TextView>(R.id.userName)
            val indicator_imagePost =
                itemView.findViewById<CircleIndicator2>(R.id.indicator_imagePost)
            val linearLayout_firefigma_like =
                itemView.findViewById<LinearLayout>(R.id.linearLayout_like_singleImagePost)
            val fireBtn_unlike =
                itemView.findViewById<ImageView>(R.id.fireBtn_unlike_singleImagePost)
            val fireBtn_orange =
                itemView.findViewById<ImageView>(R.id.fireBtn_orange_singleImagePost)
            val fireBtn_yellow =
                itemView.findViewById<ImageView>(R.id.fireBtn_yellow_singleImagePost)
            val fireBtn_red = itemView.findViewById<ImageView>(R.id.fireBtn_red_singleImagePost)
            val fireBtn_blue = itemView.findViewById<ImageView>(R.id.fireBtn_blue_singleImagePost)

            db.collection("Users")
                .document(post.CreatorUid)
                .get()
                .addOnSuccessListener {
                    val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                    if (user != null) {
                        userName.text = user.name
                        ownerNmae.text = "Owned by " + user.name
                        Glide.with(itemView).load(user.profile_pic).override(200, 200)
                            .error(R.drawable.person_user)
                            .into(ownerImage_ImagePost)
                        Glide.with(itemView).load(user.profile_pic).override(200, 200)
                            .error(R.drawable.person_user)
                            .into(userImage_sigleImagePost)
                    }
                }


            if (post.imageUrl != null) {
                adapterImages = PostImagesAdapter(post.imageUrl!!)

                postImageRecView_single_ImagePost.apply {
                    this.adapter = adapterImages
                    setHasFixedSize(false)
                }

                val pagerSnapHelper = PagerSnapHelper()
                pagerSnapHelper.attachToRecyclerView(postImageRecView_single_ImagePost)
                indicator_imagePost.attachToRecyclerView(
                    postImageRecView_single_ImagePost,
                    pagerSnapHelper
                )

                if (post.imageUrl!!.size == 1) {
                    indicator_imagePost.invisible()
                }

            } else {
                val adapter = PostImagesAdapter(listOf())
                postImageRecView_single_ImagePost.apply {
                    this.adapter = adapter
                    setHasFixedSize(false)
                }
                postImageRecView_single_ImagePost.hide()
                indicator_imagePost.invisible()
            }

            val ago = getRelativeTimeSpanString(
                post.timestamp.toLong(),
                System.currentTimeMillis(),
                MINUTE_IN_MILLIS,
                FORMAT_NUMERIC_DATE
            )
            if (post.location != null) {
                date_location.text = "${post.location!!.city}, $ago"
            } else {
                date_location.text = "Unkhown, $ago"
            }

            title.text = post.title
            desc.text = post.desc

            if (currentUser != null) {

                val PostLikeRef =
                    db.collection("PostActions").document(post.post_id).collection("Likes")
                        .document(currentUser.uid)
                PostLikeRef.get().addOnSuccessListener {
                    if (it.exists()) {
                        val colorLike: PostLike? = it.toObject(PostLike::class.java)
                        if (colorLike?.color!!.equals("Orange")) {
                            fireBtn.setImageDrawable(
                                ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.fire_orange
                                )
                            )
                        } else if (colorLike?.color!!.equals("Yellow")) {
                            fireBtn.setImageDrawable(
                                ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.fire_yellow
                                )
                            )
                        } else if (colorLike?.color!!.equals("Red")) {
                            fireBtn.setImageDrawable(
                                ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.fire_red
                                )
                            )
                        } else if (colorLike?.color!!.equals("Blue")) {
                            fireBtn.setImageDrawable(
                                ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.fire_blue
                                )
                            )
                        } else {
                            fireBtn.setImageDrawable(
                                ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.fire_blue
                                )
                            )
                        }
                        isLiked = 1
                    }
                }.addOnFailureListener {
                    itemView.context.displayToast(it.message)
                    isLiked = 0


                }

            }

            val PostAllLikesRef =
                db.collection("PostActions").document(post.post_id).collection("Likes")

            PostAllLikesRef.get().addOnSuccessListener {
                val snapshot = it.documents

                likes_others_imagePost.text = "+ ${snapshot.size ?: 0} Others"
                otherLikeCount = snapshot.size

                var firstUser = ""

                try {
                    firstUser = snapshot[0]["userUid"].toString()
                    db.collection("Users")
                        .document(firstUser)
                        .get()
                        .addOnSuccessListener {
                            firstUserLikeImage = it.toObject(ProfileInfo::class.java)
                            if (firstUserLikeImage != null) {
                                Glide.with(itemView).load(firstUserLikeImage!!.profile_pic)
                                    .override(50, 50)
                                    .error(R.drawable.person_user)
                                    .into(firstLiker_SingleImagePost)
                            }
                        }
                } catch (e: Exception) {

                }

            }.addOnFailureListener {
                itemView.context.displayToast(it.message)
            }

            commentBtn_ImagePost.setOnClickListener {
                val intent = Intent(itemView.context, PostCommentActivity::class.java)
                intent.putExtra("PostId", post.post_id)
                itemView.context.startActivity(intent)
            }

            fireBtn_unlike.setOnClickListener {
                /*fireBtn.setColorFilter(
                     ContextCompat.getColor(itemView.context, R.color.black),
                     android.graphics.PorterDuff.Mode.SRC_IN
                 )*/
                fireBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.fire_figma
                    )
                )
                linearLayout_firefigma_like.visibility = View.GONE
                isFireFigmLike = false
                deleteLike(post.post_id, currentUser!!.uid)
                if (isLiked == 1) {
                    otherLikeCount = otherLikeCount - 1
                    likes_others_imagePost.text = "+ ${otherLikeCount ?: 0} Others"
                    Glide.with(itemView).load(R.drawable.person_user).override(50, 50)
                        .error(R.drawable.person_user)
                        .into(firstLiker_SingleImagePost)
                    isLiked = 0
                }
            }
            fireBtn_orange.setOnClickListener {

                fireBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.fire_orange
                    )
                )
                linearLayout_firefigma_like.visibility = View.GONE
                isFireFigmLike = false
                updateLike(post.post_id, currentUser!!.uid, "Orange")
                if (isLiked == 0) {
                    otherLikeCount = otherLikeCount + 1
                    likes_others_imagePost.text = "+ ${otherLikeCount ?: 0} Others"
                    db.collection("Users").document(currentUser!!.uid)
                        .get().addOnSuccessListener {
                            val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                            Glide.with(itemView).load(user!!.profile_pic).override(50, 50)
                                .error(R.drawable.person_user)
                                .into(firstLiker_SingleImagePost)
                        }
                    isLiked = 1
                }
            }
            fireBtn_yellow.setOnClickListener {

                fireBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.fire_yellow
                    )
                )
                linearLayout_firefigma_like.visibility = View.GONE
                isFireFigmLike = false
                updateLike(post.post_id, currentUser!!.uid, "Yellow")
                if (isLiked == 0) {
                    otherLikeCount = otherLikeCount + 1
                    likes_others_imagePost.text = "+ ${otherLikeCount ?: 0} Others"
                    db.collection("Users").document(currentUser.uid)
                        .get().addOnSuccessListener {
                            val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                            Glide.with(itemView).load(user!!.profile_pic).override(50, 50)
                                .error(R.drawable.person_user)
                                .into(firstLiker_SingleImagePost)
                        }
                    isLiked = 1
                }
            }
            fireBtn_red.setOnClickListener {

                fireBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.fire_red
                    )
                )
                linearLayout_firefigma_like.visibility = View.GONE
                isFireFigmLike = false
                updateLike(post.post_id, currentUser!!.uid, "Red")
                if (isLiked == 0) {
                    otherLikeCount = otherLikeCount + 1
                    likes_others_imagePost.text = "+ ${otherLikeCount ?: 0} Others"
                    db.collection("Users").document(currentUser.uid)
                        .get().addOnSuccessListener {
                            val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                            Glide.with(itemView).load(user!!.profile_pic).override(50, 50)
                                .error(R.drawable.person_user)
                                .into(firstLiker_SingleImagePost)
                        }
                    isLiked = 1
                }
            }
            fireBtn_blue.setOnClickListener {

                fireBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.fire_blue
                    )
                )
                linearLayout_firefigma_like.visibility = View.GONE
                isFireFigmLike = false
                updateLike(post.post_id, currentUser!!.uid, "Blue")
                if (isLiked == 0) {
                    otherLikeCount = otherLikeCount + 1
                    likes_others_imagePost.text = "+ ${otherLikeCount ?: 0} Others"
                    db.collection("Users").document(currentUser.uid)
                        .get().addOnSuccessListener {
                            val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                            Glide.with(itemView).load(user!!.profile_pic).override(50, 50)
                                .error(R.drawable.person_user)
                                .into(firstLiker_SingleImagePost)
                        }
                    isLiked = 1
                }
            }


            var progressValue: Int = 0
            var timer = object : CountDownTimer(1000, 1) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.e("ProgressValue: ", progressValue.toString())
                    if (progressValue <= 1000) {
                        progressValue = progressValue + 1
                        //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000)
                    }
                }

                override fun onFinish() {
                    linearLayout_firefigma_like.visibility = View.VISIBLE
                    isFireFigmLike = true
                }
            }

            fireBtn.setOnTouchListener { _, motionEvent ->

                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        //when user touch down
                        timer.start()
                        /*if(progressValue > 700){
                            linearLayout_firefigma_like.visibility = View.VISIBLE
                            isFireFigmLike = true
                        }*/


                    }
                    MotionEvent.ACTION_UP -> {
                        //when user touch release

                        if (progressValue <= 500) {
                            if (!isFireFigmLike) {
                                if (isLiked == 0) {
                                    fireBtn.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            itemView.context,
                                            R.drawable.fire_orange
                                        )
                                    )
                                    linearLayout_firefigma_like.visibility = View.GONE
                                    isFireFigmLike = false
                                    updateLike(post.post_id, currentUser!!.uid, "Orange")
                                    if (isLiked == 0) {
                                        otherLikeCount = otherLikeCount + 1
                                        likes_others_imagePost.text =
                                            "+ ${otherLikeCount ?: 0} Others"
                                        db.collection("Users").document(currentUser!!.uid)
                                            .get().addOnSuccessListener {
                                                val user: ProfileInfo? =
                                                    it.toObject(ProfileInfo::class.java)
                                                Glide.with(itemView).load(user!!.profile_pic)
                                                    .override(50, 50)
                                                    .error(R.drawable.person_user)
                                                    .into(firstLiker_SingleImagePost)
                                            }
                                        isLiked = 1
                                    }
                                } else {
                                    fireBtn.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            itemView.context,
                                            R.drawable.fire_figma
                                        )
                                    )
                                    linearLayout_firefigma_like.visibility = View.GONE
                                    isFireFigmLike = false
                                    deleteLike(post.post_id, currentUser!!.uid)
                                    if (isLiked == 1) {
                                        otherLikeCount = otherLikeCount - 1
                                        likes_others_imagePost.text =
                                            "+ ${otherLikeCount ?: 0} Others"
                                        Glide.with(itemView).load(R.drawable.person_user)
                                            .override(50, 50)
                                            .error(R.drawable.person_user)
                                            .into(firstLiker_SingleImagePost)
                                        isLiked = 0
                                    }
                                }
                            } else {
                                linearLayout_firefigma_like.visibility = View.GONE
                                isFireFigmLike = false
                            }
                        }

                        timer.cancel()
                        progressValue = 0
                    }
                }
                true
            }
        }

        private fun deleteLike(postId: String, creatorUid: String) {
            val db = FirebaseFirestore.getInstance()
            val ref = db.collection("PostActions").document(postId).collection("Likes")
                .document(creatorUid)


            ref.delete().addOnSuccessListener {

            }.addOnFailureListener {

                itemView.context.displayToast(it.message)

            }
        }

        private fun updateLike(postId: String, creatorUid: String, color: String) {
            val db = FirebaseFirestore.getInstance()
            val ref = db.collection("PostActions").document(postId).collection("Likes")
                .document(creatorUid)

            val like = PostLike(
                "Like" + System.currentTimeMillis().toString(),
                color,
                creatorUid,
                System.currentTimeMillis().toString()
            )

            ref.set(like).addOnSuccessListener {

            }.addOnFailureListener {

                itemView.context.displayToast(it.message)

            }

        }

    }

    class TextPostViwHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser

        @SuppressLint("ClickableViewAccessibility")
        fun bind(post: Post) {

            var isLiked: Int = 0
            var isFireFigmLike: Boolean = false
            var otherLikeCount: Int = 0
            val title = itemView.findViewById<TextView>(R.id.titlePost)
            val likeOthers_textPostSingle =
                itemView.findViewById<TextView>(R.id.likeOthers_textPostSingle)
            val desc = itemView.findViewById<ReadMoreTextView>(R.id.DescPost)
            val commentBtn_SingleTextPost =
                itemView.findViewById<ImageView>(R.id.commentBtn_SingleTextPost)
            val fireBtn = itemView.findViewById<ImageView>(R.id.fireBtn_singleTextPost)
            val userName_textPost = itemView.findViewById<TextView>(R.id.userName_textPost)
            val userImage_singleTextPost =
                itemView.findViewById<RoundedImageView>(R.id.userImage_singleTextPost)
            val ownerImage_SingleTextPost =
                itemView.findViewById<RoundedImageView>(R.id.ownerImage_SingleTextPost)
            val ownerName_TextPost =
                itemView.findViewById<TextView>(R.id.ownerName_TextPost)
            val date_location =
                itemView.findViewById<TextView>(R.id.date_time_location_TextPostSingle)
            val firstLiker_SingleTextPost =
                itemView.findViewById<RoundedImageView>(R.id.firstLiker_SingleTextPost)
            val linearLayout_firefigma_like =
                itemView.findViewById<LinearLayout>(R.id.linearLayout_like_singleTextPost)
            val fireBtn_unlike =
                itemView.findViewById<ImageView>(R.id.fireBtn_unlike_singleTextPost)
            val fireBtn_orange =
                itemView.findViewById<ImageView>(R.id.fireBtn_orange_singleTextPost)
            val fireBtn_yellow =
                itemView.findViewById<ImageView>(R.id.fireBtn_yellow_singleTextPost)
            val fireBtn_red = itemView.findViewById<ImageView>(R.id.fireBtn_red_singleTextPost)
            val fireBtn_blue = itemView.findViewById<ImageView>(R.id.fireBtn_blue_singleTextPost)

            val db = FirebaseFirestore.getInstance()
            db.collection("Users")
                .document(post.CreatorUid)
                .get()
                .addOnSuccessListener {
                    val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                    if (user != null) {
                        userName_textPost.text = user.name
                        ownerName_TextPost.text = "Owned by " + user.name
                        Glide.with(itemView).load(user.profile_pic).override(200, 200)
                            .error(R.drawable.person_user)
                            .into(ownerImage_SingleTextPost)
                        Glide.with(itemView).load(user.profile_pic).override(200, 200)
                            .error(R.drawable.person_user)
                            .into(userImage_singleTextPost)
                    }
                }


            val ago = getRelativeTimeSpanString(
                post.timestamp.toLong(),
                System.currentTimeMillis(),
                MINUTE_IN_MILLIS,
                FORMAT_NUMERIC_DATE
            )

            if (post.location != null) {
                date_location.text = "${post.location!!.city}, $ago"
            } else {
                date_location.text = "Unkhown, $ago"
            }

            title.text = post.title
            desc.text = post.desc

            if (user != null) {
                val PostLikeRef =
                    db.collection("PostActions").document(post.post_id).collection("Likes")
                        .document(user.uid)
                PostLikeRef.get().addOnSuccessListener {
                    if (it.exists()) {
                        val colorLike: PostLike? = it.toObject(PostLike::class.java)
                        colorLike?.color
                        if (colorLike?.color!!.equals("Orange")) {
                            fireBtn.setImageDrawable(
                                ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.fire_orange
                                )
                            )
                        } else if (colorLike?.color!!.equals("Yellow")) {
                            fireBtn.setImageDrawable(
                                ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.fire_yellow
                                )
                            )
                        } else if (colorLike?.color!!.equals("Red")) {
                            fireBtn.setImageDrawable(
                                ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.fire_red
                                )
                            )
                        } else if (colorLike?.color!!.equals("Blue")) {
                            fireBtn.setImageDrawable(
                                ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.fire_blue
                                )
                            )
                        } else {
                            fireBtn.setImageDrawable(
                                ContextCompat.getDrawable(
                                    itemView.context,
                                    R.drawable.fire_blue
                                )
                            )
                        }
                        isLiked = 1
                    }

                }.addOnFailureListener {
                    itemView.context.displayToast(it.message)
                    isLiked = 0
                }

            }

            val PostAllLikesRef =
                db.collection("PostActions").document(post.post_id).collection("Likes")

            PostAllLikesRef.get().addOnSuccessListener {
                val snapshot = it.documents

                likeOthers_textPostSingle.text = "+ ${snapshot.size ?: 0} Others"
                otherLikeCount = snapshot.size

//                var firstUser = ""
//
//                try {
//                    firstUser = snapshot[0]["userUid"].toString()
//                    db.collection("Users")
//                        .document(firstUser)
//                        .get()
//                        .addOnSuccessListener {
//                            val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
//                            if (user != null) {
//                                Glide.with(itemView).load(user.profile_pic).override(50, 50)
//                                    .error(R.drawable.person_user)
//                                    .into(firstLiker_SingleImagePost)
//                            }
//                        }
//                } catch (e: Exception) {
//
//                }

            }.addOnFailureListener {
                itemView.context.displayToast(it.message)
            }


            commentBtn_SingleTextPost.setOnClickListener {
                val intent = Intent(itemView.context, PostCommentActivity::class.java)
                intent.putExtra("PostId", post.post_id)
                itemView.context.startActivity(intent)
            }

            fireBtn_unlike.setOnClickListener {
                /*fireBtn.setColorFilter(
                    ContextCompat.getColor(itemView.context, R.color.black),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )*/
                fireBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.fire_figma
                    )
                )
                linearLayout_firefigma_like.visibility = View.GONE
                isFireFigmLike = false
                deleteLike(post.post_id, user!!.uid)
                if (isLiked == 1) {
                    otherLikeCount = otherLikeCount - 1
                    likeOthers_textPostSingle.text = "+ ${otherLikeCount ?: 0} Others"
                    Glide.with(itemView).load(R.drawable.person_user).override(50, 50)
                        .error(R.drawable.person_user)
                        .into(firstLiker_SingleTextPost)
                    isLiked = 0
                }
            }
            fireBtn_orange.setOnClickListener {

                fireBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.fire_orange
                    )
                )
                linearLayout_firefigma_like.visibility = View.GONE
                isFireFigmLike = false
                updateLike(post.post_id, user!!.uid, "Orange")
                if (isLiked == 0) {
                    otherLikeCount = otherLikeCount + 1
                    likeOthers_textPostSingle.text = "+ ${otherLikeCount ?: 0} Others"
                    db.collection("Users").document(user.uid)
                        .get().addOnSuccessListener {
                            val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                            Glide.with(itemView).load(user!!.profile_pic).override(50, 50)
                                .error(R.drawable.person_user)
                                .into(firstLiker_SingleTextPost)
                        }
                    isLiked = 1
                }
            }
            fireBtn_yellow.setOnClickListener {

                fireBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.fire_yellow
                    )
                )
                linearLayout_firefigma_like.visibility = View.GONE
                isFireFigmLike = false
                updateLike(post.post_id, user!!.uid, "Yellow")
                if (isLiked == 0) {
                    otherLikeCount = otherLikeCount + 1
                    likeOthers_textPostSingle.text = "+ ${otherLikeCount ?: 0} Others"
                    db.collection("Users").document(user.uid)
                        .get().addOnSuccessListener {
                            val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                            Glide.with(itemView).load(user!!.profile_pic).override(50, 50)
                                .error(R.drawable.person_user)
                                .into(firstLiker_SingleTextPost)
                        }
                    isLiked = 1
                }
            }
            fireBtn_red.setOnClickListener {

                fireBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.fire_red
                    )
                )
                linearLayout_firefigma_like.visibility = View.GONE
                isFireFigmLike = false
                updateLike(post.post_id, user!!.uid, "Red")
                if (isLiked == 0) {
                    otherLikeCount = otherLikeCount + 1
                    likeOthers_textPostSingle.text = "+ ${otherLikeCount ?: 0} Others"
                    db.collection("Users").document(user.uid)
                        .get().addOnSuccessListener {
                            val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                            Glide.with(itemView).load(user!!.profile_pic).override(50, 50)
                                .error(R.drawable.person_user)
                                .into(firstLiker_SingleTextPost)
                        }
                    isLiked = 1
                }
            }
            fireBtn_blue.setOnClickListener {

                fireBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.fire_blue
                    )
                )
                linearLayout_firefigma_like.visibility = View.GONE
                isFireFigmLike = false
                updateLike(post.post_id, user!!.uid, "Blue")
                if (isLiked == 0) {
                    otherLikeCount = otherLikeCount + 1
                    likeOthers_textPostSingle.text = "+ ${otherLikeCount ?: 0} Others"
                    db.collection("Users").document(user.uid)
                        .get().addOnSuccessListener {
                            val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                            Glide.with(itemView).load(user!!.profile_pic).override(50, 50)
                                .error(R.drawable.person_user)
                                .into(firstLiker_SingleTextPost)
                        }
                    isLiked = 1
                }
            }

            var progressValue: Int = 0
            var timer = object : CountDownTimer(1000, 1) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.e("ProgressValue: ", progressValue.toString())
                    if (progressValue <= 1000) {
                        progressValue = progressValue + 1
                        //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000)
                    }
                }

                override fun onFinish() {
                    linearLayout_firefigma_like.visibility = View.VISIBLE
                    isFireFigmLike = true
                }
            }

            fireBtn.setOnTouchListener { _, motionEvent ->

                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        //when user touch down
                        timer.start()
                        /*if(progressValue > 700){
                            linearLayout_firefigma_like.visibility = View.VISIBLE
                            isFireFigmLike = true
                        }*/


                    }
                    MotionEvent.ACTION_UP -> {
                        //when user touch release

                        if (progressValue <= 500) {
                            if (!isFireFigmLike) {
                                if (isLiked == 0) {
                                    fireBtn.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            itemView.context,
                                            R.drawable.fire_orange
                                        )
                                    )
                                    linearLayout_firefigma_like.visibility = View.GONE
                                    isFireFigmLike = false
                                    updateLike(post.post_id, user!!.uid, "Orange")
                                    if (isLiked == 0) {
                                        otherLikeCount = otherLikeCount + 1
                                        likeOthers_textPostSingle.text =
                                            "+ ${otherLikeCount ?: 0} Others"
                                        db.collection("Users").document(user!!.uid)
                                            .get().addOnSuccessListener {
                                                val user: ProfileInfo? =
                                                    it.toObject(ProfileInfo::class.java)
                                                Glide.with(itemView).load(user!!.profile_pic)
                                                    .override(50, 50)
                                                    .error(R.drawable.person_user)
                                                    .into(firstLiker_SingleTextPost)
                                            }
                                        isLiked = 1
                                    }
                                } else {
                                    fireBtn.setImageDrawable(
                                        ContextCompat.getDrawable(
                                            itemView.context,
                                            R.drawable.fire_figma
                                        )
                                    )
                                    linearLayout_firefigma_like.visibility = View.GONE
                                    isFireFigmLike = false
                                    deleteLike(post.post_id, user!!.uid)
                                    if (isLiked == 1) {
                                        otherLikeCount = otherLikeCount - 1
                                        likeOthers_textPostSingle.text =
                                            "+ ${otherLikeCount ?: 0} Others"
                                        Glide.with(itemView).load(R.drawable.person_user)
                                            .override(50, 50)
                                            .error(R.drawable.person_user)
                                            .into(firstLiker_SingleTextPost)
                                        isLiked = 0
                                    }
                                }
                            } else {
                                linearLayout_firefigma_like.visibility = View.GONE
                                isFireFigmLike = false
                            }
                        }

                        timer.cancel()
                        progressValue = 0
                    }
                }
                true
            }

        }

        private fun deleteLike(postId: String, creatorUid: String) {
            val db = FirebaseFirestore.getInstance()
            val ref = db.collection("PostActions").document(postId).collection("Likes")
                .document(creatorUid)


            ref.delete().addOnSuccessListener {

            }.addOnFailureListener {

                itemView.context.displayToast(it.message)

            }
        }

        private fun updateLike(postId: String, creatorUid: String, color: String) {
            val db = FirebaseFirestore.getInstance()
            val ref = db.collection("PostActions").document(postId).collection("Likes")
                .document(creatorUid)

            val like = PostLike(
                "Like" + System.currentTimeMillis().toString(),
                color,
                creatorUid,
                System.currentTimeMillis().toString()
            )

            ref.set(like).addOnSuccessListener {

            }.addOnFailureListener {

                itemView.context.displayToast(it.message)

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (postListItem[position].postType == "ImagePost") {
            IMAGE_POST
        } else if (postListItem[position].postType == "TextPost") {
            TEXT_POST
        } else {
            TEXT_POST
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TEXT_POST) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.single_text_post, parent, false)
            return TextPostViwHolder(view)
        } else if (viewType == IMAGE_POST) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.single_image_post, parent, false)
            return ImagePostViwHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.single_text_post, parent, false)
            return TextPostViwHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == IMAGE_POST) {
            (holder as ImagePostViwHolder).bind(postListItem[position])
        } else {
            (holder as TextPostViwHolder).bind(postListItem[position])
        }
    }

    override fun getItemCount(): Int = postListItem.size
}