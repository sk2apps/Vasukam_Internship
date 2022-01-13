package com.saggy.vasukaminternship.presentation.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saggy.vasukaminternship.R


class PostImagesAdapter(var images: List<String>) :
    RecyclerView.Adapter<PostImagesAdapter.ImagePostViwHolder>() {


    class ImagePostViwHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(post: String) {
            val image = itemView.findViewById<ImageView>(R.id.imageView_singleImagePostImage)
            Glide.with(image).load(post).into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePostViwHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_image_post_images, parent, false)
        return ImagePostViwHolder(view)
    }

    override fun onBindViewHolder(holder: ImagePostViwHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }


}