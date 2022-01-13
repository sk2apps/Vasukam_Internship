package com.saggy.vasukaminternship.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.saggy.vasukaminternship.R
import android.widget.TextView
import android.view.View


class Comment_Horizontal_Adapter(var context: Context, var priceList: List<String>) :
    RecyclerView.Adapter<Comment_Horizontal_Adapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view =
            inflater.inflate(R.layout.listitem_videotext_ovals, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val textView = holder.textView
        textView.text = priceList[position]
    }

    override fun getItemCount(): Int {
        return priceList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView

        init {
            textView = itemView.findViewById(R.id.priceText)
        }
    }
}