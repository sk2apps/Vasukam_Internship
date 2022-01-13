package com.saggy.vasukaminternship


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment


class Award_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.currency_tab_fragment, container, false)
        return view
    }
}