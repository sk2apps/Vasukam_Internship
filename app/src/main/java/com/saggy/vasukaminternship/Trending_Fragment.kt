package com.saggy.vasukaminternship


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

class Trending_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.trending_tab_activity, container, false)
        return view
    }
}