package com.saggy.vasukaminternship.presentation.fragments.homeFragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.saggy.vasukaminternship.*
import com.saggy.vasukaminternship.databinding.HomeFragmentBinding
import com.saggy.vasukaminternship.presentation.activity.Filter.FilterActivity
import com.saggy.vasukaminternship.presentation.activity.profile.ProfileActivity


class Home_Fragment : Fragment() {

    private lateinit var _binding : HomeFragmentBinding
    val binding
    get() = _binding

    lateinit var liveButton: AppCompatButton
    lateinit var trendingButton: AppCompatButton
    lateinit var exploreButton: AppCompatButton
    lateinit var filterButton: ImageButton
    lateinit var headText: TextView
    lateinit var view1: View
    lateinit var view3: View
    lateinit var v1: AppCompatButton
    lateinit var v2: AppCompatButton
    lateinit var v3: AppCompatButton
    lateinit var v4: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = HomeFragmentBinding.inflate(inflater,container,false)

        liveButton = binding.liveButton
        exploreButton = binding.exploreButton
        trendingButton = binding.trendingButton
        filterButton = binding.filterButton

        headText = binding.headText
        view1 = binding.view1
        view3 = binding.view3
        v4 = binding.filterButton
        v3 = binding.btnView3
        v2 = binding.btnView2
        v1 = binding.btnView1

        loadFragment2(HomeFeedFragment())
        headText.text = "Feed"
        view1.background = resources.getDrawable(R.drawable.view1_feed)
        view3.background = resources.getDrawable(R.drawable.view3_feed)
        liveButton.setBackgroundDrawable(resources.getDrawable(R.drawable.tab_icon_figma2))
        trendingButton.setBackgroundColor(resources.getColor(R.color.transparent))
        exploreButton.setBackgroundColor(resources.getColor(R.color.transparent))
        exploreButton.setTextColor(resources.getColor(R.color.grey_tab))
        trendingButton.setTextColor(resources.getColor(R.color.grey_tab))
        liveButton.setTextColor(resources.getColor(R.color.white))
        liveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        trendingButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        exploreButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)

        binding.messenger.setOnClickListener {
            val intent = Intent(context, Messenger_Activity::class.java)
            startActivity(intent)
        }


        v1.setOnClickListener(View.OnClickListener { view: View? ->
            loadFragment2(HomeFeedFragment())
            headText.text = "Feed"
            view1.background = resources.getDrawable(R.drawable.view1_feed)
            view3.background = resources.getDrawable(R.drawable.view3_feed)
            liveButton.setBackgroundDrawable(resources.getDrawable(R.drawable.tab_icon_figma2))
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
            view1.background = resources.getDrawable(R.drawable.view1_trending2)
            view3.background = resources.getDrawable(R.drawable.view3_trending2)
            liveButton.setBackgroundColor(resources.getColor(R.color.transparent))
            exploreButton.setBackgroundColor(resources.getColor(R.color.transparent))
            trendingButton.setBackgroundDrawable(resources.getDrawable(R.drawable.tab_icon_figma2))
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
            view1.background = resources.getDrawable(R.drawable.view1_explore2)
            view3.background = resources.getDrawable(R.drawable.view3_explore2)
            liveButton.setBackgroundColor(resources.getColor(R.color.transparent))
            trendingButton.setBackgroundColor(resources.getColor(R.color.transparent))
            exploreButton.setBackgroundDrawable(resources.getDrawable(R.drawable.tab_explore_figma2))
            exploreButton.setTextColor(resources.getColor(R.color.white))
            trendingButton.setTextColor(resources.getColor(R.color.grey_tab))
            liveButton.setTextColor(resources.getColor(R.color.grey_tab))
            liveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            trendingButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            exploreButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        })
        v4.setOnClickListener(View.OnClickListener { view: View? ->
            startActivity(Intent(context,FilterActivity::class.java))
        })

        binding.person.setOnClickListener {
            startActivity(Intent(context,ProfileActivity::class.java))
        }

        return binding.root
    }


    private fun loadFragment2(fragment: Fragment?) {
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment!!)
        fragmentTransaction.commit()
    }
}