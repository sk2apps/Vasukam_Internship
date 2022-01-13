package com.saggy.vasukaminternship.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.saggy.vasukaminternship.R
import android.view.View

class Video_Verticle_Adapter(var context: Context, var videoLink: List<String>) :
    RecyclerView.Adapter<Video_Verticle_Adapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.listitem_video, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return videoLink.size
    }

    inner class ViewHolder  //        TextView textView;
        (itemView: View) : RecyclerView.ViewHolder(itemView)
}