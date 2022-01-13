package com.saggy.vasukaminternship.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.saggy.vasukaminternship.R
import android.widget.TextView
import android.view.View

class Item_Horizontal_Adapter(var context: Context, var optionList: List<String>) :
    RecyclerView.Adapter<Item_Horizontal_Adapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.listitem_options, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val textView = holder.textView
        textView.text = optionList[position]
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView

        init {
            textView = itemView.findViewById(R.id.option_text)
        }
    }
}