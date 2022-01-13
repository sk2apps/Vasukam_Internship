package com.saggy.vasukaminternship.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.saggy.vasukaminternship.R
import android.view.View


class Video_Horizontal_Adapter(var context: Context, var videoLink: List<String>) :
    RecyclerView.Adapter<Video_Horizontal_Adapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view =
            inflater.inflate(R.layout.listitem_livevideos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        TextView textView = holder.textView;
//        textView.setText(videoLink.get(position));
    }

    override fun getItemCount(): Int {
        return videoLink.size
    }

    inner class ViewHolder  //        TextView textView;
        (itemView: View) : RecyclerView.ViewHolder(itemView)
}