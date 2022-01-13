package com.saggy.vasukaminternship


import androidx.recyclerview.widget.RecyclerView

import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import android.view.GestureDetector.SimpleOnGestureListener
import android.content.Context
import android.view.*

class RecyclerTouchListener(
    context: Context?,
    recyclerView: RecyclerView,
    private val clickListener: ClickListener?
) : OnItemTouchListener {
    private val gestureDetector: GestureDetector
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildPosition(child))
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    interface ClickListener {
        fun onClick(view: View?, position: Int)
        fun onLongClick(view: View?, position: Int)
    }

    init {
        gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val child = recyclerView.findChildViewUnder(e.x, e.y)
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildPosition(child))
                }
            }
        })
    }
}