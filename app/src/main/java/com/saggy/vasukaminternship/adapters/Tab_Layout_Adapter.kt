package com.saggy.vasukaminternship.adapters

import android.content.Context
import androidx.fragment.app.FragmentPagerAdapter
import com.saggy.vasukaminternship.Currency_Fragment
import com.saggy.vasukaminternship.Award_Fragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


class Tab_Layout_Adapter(var context: Context, var totalTabs: Int, fm: FragmentManager?) :
    FragmentPagerAdapter(
        fm!!
    ) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> Currency_Fragment()
            1 -> Award_Fragment()
            else -> Currency_Fragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}