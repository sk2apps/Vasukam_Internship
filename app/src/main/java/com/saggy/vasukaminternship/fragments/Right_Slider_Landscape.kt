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
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.core.view.ViewCompat
import android.view.ViewGroup.MarginLayoutParams
import androidx.fragment.app.Fragment
import eu.okatrych.rightsheet.RightSheetBehavior
import com.saggy.vasukaminternship.adapters.Tab_Layout_Adapter
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class Right_Slider_Landscape : Fragment() {


    lateinit var close: ImageButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.right_sheet_landscape, container, false)
        close = view.findViewById(R.id.close_btn)
        val tb: TabLayout = view.findViewById(R.id.tab_layout)
        val vp: ViewPager = view.findViewById(R.id.popupViewPager)
        tb.addTab(tb.newTab().setText("Currency"))
        tb.addTab(tb.newTab().setText("Awards"))
        tb.getTabAt(0)!!.setIcon(R.drawable.money_figma_1)
        tb.getTabAt(1)!!.setIcon(R.drawable.trophy_black_figma)
        for (j in 0 until tb.tabCount) {
            val tab = (tb.getChildAt(0) as ViewGroup).getChildAt(j)
            tab.requestLayout()
            if (j == 0) {
                ViewCompat.setBackground(tab, resources.getDrawable(R.drawable.white_rounded))
                val p = tab.layoutParams as MarginLayoutParams
                p.setMargins(0, 0, 20, 0)
            } else {
                ViewCompat.setBackground(
                    tab,
                    resources.getDrawable(R.drawable.yellow_rounded_stroke)
                )
                val p = tab.layoutParams as MarginLayoutParams
                p.setMargins(20, 0, 0, 0)
            }
        }
        close.setOnClickListener(View.OnClickListener { view1: View? ->
            val rightSheetBehavior: RightSheetBehavior<*> = RightSheetBehavior.from(
                requireActivity().findViewById(R.id.right_sheet)
            )
            rightSheetBehavior.peekWidth = 0
            rightSheetBehavior.setState(RightSheetBehavior.STATE_COLLAPSED)
        })
        tb.tabGravity = TabLayout.GRAVITY_FILL
        val tab_layout_adapter = Tab_Layout_Adapter(requireContext(), tb.tabCount, childFragmentManager)
        vp.adapter = tab_layout_adapter
        vp.addOnPageChangeListener(TabLayoutOnPageChangeListener(tb))
        tb.addOnTabSelectedListener(object : OnTabSelectedListener {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onTabSelected(tab: TabLayout.Tab) {
                vp.currentItem = tab.position
                for (j in 0 until tb.tabCount) {
                    val tabl = (tb.getChildAt(0) as ViewGroup).getChildAt(j)
                    tabl.requestLayout()
                    if (j == tab.position) {
                        ViewCompat.setBackground(
                            tabl,
                            resources.getDrawable(R.drawable.white_rounded)
                        )
                    } else {
                        ViewCompat.setBackground(
                            tabl,
                            resources.getDrawable(R.drawable.yellow_rounded_stroke)
                        )
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        return view
    }
}