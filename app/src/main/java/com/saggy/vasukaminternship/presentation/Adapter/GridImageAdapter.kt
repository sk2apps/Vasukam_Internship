package com.saggy.vasukaminternship.presentation.Adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import androidx.annotation.Nullable

import com.makeramen.roundedimageview.RoundedImageView
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.utils.show
import java.io.File


import android.graphics.BitmapFactory
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener


class GridImageAdapter(
     context: Context,
    private val layoutResource: Int,
    private val mAppend: String,
    private val imgURLs: ArrayList<String>
) : ArrayAdapter<String>(context, layoutResource, imgURLs) {

    private val mInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private class ViewHolder {
        lateinit var image: RoundedImageView
        lateinit var mProgressBar: ProgressBar
    }

    override fun getView(position: Int, @Nullable convertV: View?, parent: ViewGroup): View {

        /*
        Viewholder build pattern (Similar to recyclerview)
         */
        var convertView = convertV
        val holder: ViewHolder
        if (convertView == null) {
            convertView = mInflater.inflate(layoutResource, parent, false)
            holder = ViewHolder()
            holder.mProgressBar =
                convertView.findViewById<ProgressBar>(R.id.gridImageProgressbar)
            holder.image = convertView.findViewById<RoundedImageView>(R.id.gridImageView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val imgURL = getItem(position)

//        holder.mProgressBar.visibility = View.GONE
//        holder.image.show()
//
//
//        val imgFile = File(mAppend+imgURL)
//        if (imgFile.exists()) {
//            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//            holder.image.setImageBitmap(myBitmap)
//        }


        val imageLoader: ImageLoader = ImageLoader.getInstance()
        imageLoader.init(ImageLoaderConfiguration.createDefault(context))

        imageLoader.displayImage(mAppend + imgURL, holder.image, object : ImageLoadingListener {
            override fun onLoadingStarted(imageUri: String, view: View) {
                if (holder.mProgressBar != null) {
                    holder.mProgressBar.visibility = View.VISIBLE
                }
            }

            override fun onLoadingFailed(imageUri: String, view: View, failReason: FailReason) {
                if (holder.mProgressBar != null) {
                    holder.mProgressBar.visibility = View.GONE
                }
            }

            override fun onLoadingComplete(imageUri: String, view: View, loadedImage: Bitmap) {
                if (holder.mProgressBar != null) {
                    holder.mProgressBar.visibility = View.GONE
                }
            }

            override fun onLoadingCancelled(imageUri: String, view: View) {
                if (holder.mProgressBar != null) {
                    holder.mProgressBar.visibility = View.GONE
                }
            }
        })
        return convertView!!
    }

}