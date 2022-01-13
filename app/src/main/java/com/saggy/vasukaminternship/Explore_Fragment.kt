package com.saggy.vasukaminternship


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

class Explore_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.explore_tab_activity, container, false)
        return view
    }
}