package com.saggy.vasukaminternship.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.saggy.vasukaminternship.R
import androidx.appcompat.widget.AppCompatButton
import android.widget.ImageButton
import android.widget.TextView
import android.annotation.SuppressLint
import com.saggy.vasukaminternship.Live_Fragment
import android.content.Intent
import com.saggy.vasukaminternship.Messenger_Activity
import android.util.TypedValue
import android.view.View
import com.saggy.vasukaminternship.Trending_Fragment
import com.saggy.vasukaminternship.Explore_Fragment
import androidx.fragment.app.Fragment

class Live_Nav_Fragment : Fragment() {

    lateinit var liveButton: AppCompatButton
    lateinit var trendingButton: AppCompatButton
    lateinit var exploreButton: AppCompatButton
    lateinit var filterButton: ImageButton
    lateinit var messenger: ImageButton
    lateinit var headText: TextView
    lateinit var view1: View
    lateinit var view3: View
    lateinit var v1: AppCompatButton
    lateinit var v2: AppCompatButton
    lateinit var v3: AppCompatButton

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.live_navbar_fragment, container, false)
        loadFragment2(Live_Fragment())
        liveButton = view.findViewById(R.id.liveButton)
        exploreButton = view.findViewById(R.id.exploreButton)
        trendingButton = view.findViewById(R.id.trendingButton)
        filterButton = view.findViewById(R.id.filterButton)
        messenger = view.findViewById(R.id.messenger)
        headText = view.findViewById(R.id.headText)
        view1 = view.findViewById(R.id.view1)
        view3 = view.findViewById(R.id.view3)
        v3 = view.findViewById(R.id.btn_view3)
        v2 = view.findViewById(R.id.btn_view2)
        v1 = view.findViewById(R.id.btn_view1)

        messenger.setOnClickListener{ view ->
            val intent = Intent(context, Messenger_Activity::class.java)
            startActivity(intent)
        }

        v1.setOnClickListener(View.OnClickListener { view: View? ->
            loadFragment2(Live_Fragment())
            headText.text = "Live"
            view1.background = resources.getDrawable(R.drawable.view1_live)
            view3.background = resources.getDrawable(R.drawable.view3_live)
            liveButton.setBackgroundDrawable(resources.getDrawable(R.drawable.tab_icon_figma))
            trendingButton.setBackgroundColor(resources.getColor(R.color.transparent))
            exploreButton.setBackgroundColor(resources.getColor(R.color.transparent))
            exploreButton.setTextColor(resources.getColor(R.color.grey_tab))
            trendingButton.setTextColor(resources.getColor(R.color.grey_tab))
            liveButton.setTextColor(resources.getColor(R.color.white))
            liveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            trendingButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            exploreButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        })
        v2.setOnClickListener(View.OnClickListener { view: View? ->
            loadFragment2(Trending_Fragment())
            headText.text = "Trending"
            view1.background = resources.getDrawable(R.drawable.view1_trending)
            view3.background = resources.getDrawable(R.drawable.view3_trending)
            liveButton.setBackgroundColor(resources.getColor(R.color.transparent))
            exploreButton.setBackgroundColor(resources.getColor(R.color.transparent))
            trendingButton.setBackgroundDrawable(resources.getDrawable(R.drawable.tab_icon_figma))
            exploreButton.setTextColor(resources.getColor(R.color.grey_tab))
            trendingButton.setTextColor(resources.getColor(R.color.white))
            liveButton.setTextColor(resources.getColor(R.color.grey_tab))
            liveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            trendingButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            exploreButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        })
        v3.setOnClickListener(View.OnClickListener { view: View? ->
            loadFragment2(Explore_Fragment())
            headText.text = "Explore"
            view1.background = resources.getDrawable(R.drawable.view1_explore)
            view3.background = resources.getDrawable(R.drawable.view3_explore)
            liveButton.setBackgroundColor(resources.getColor(R.color.transparent))
            trendingButton.setBackgroundColor(resources.getColor(R.color.transparent))
            exploreButton.setBackgroundDrawable(resources.getDrawable(R.drawable.tab_explore_figma))
            exploreButton.setTextColor(resources.getColor(R.color.white))
            trendingButton.setTextColor(resources.getColor(R.color.grey_tab))
            liveButton.setTextColor(resources.getColor(R.color.grey_tab))
            liveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            trendingButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            exploreButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        })
        return view
    }

    private fun loadFragment2(fragment: Fragment?) {
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment!!)
        fragmentTransaction.commit()
    }
}