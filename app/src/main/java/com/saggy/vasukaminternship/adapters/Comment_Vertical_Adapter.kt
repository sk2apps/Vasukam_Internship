package com.saggy.vasukaminternship.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.saggy.vasukaminternship.R
import android.widget.TextView
import android.view.View


class Comment_Vertical_Adapter(var context: Context, var commentList: List<String>) :
    RecyclerView.Adapter<Comment_Vertical_Adapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view =
            inflater.inflate(R.layout.listitem_video_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val textView = holder.textView
        textView.text = commentList[position]
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView

        init {
            textView = itemView.findViewById(R.id.commentText)
        }
    }
}