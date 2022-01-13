package com.saggy.vasukaminternship.presentation.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.data.model.onBoarding.OnBoardingData

class OnBoardingAdapter (private val context: Context, private val data: List<OnBoardingData>):     PagerAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.single_onboarding_layout, null)
        val imageView: ImageView = view.findViewById(R.id.image_Onboarding)

        imageView.run {
            setImageResource(data[position].image)
        }

        container.addView(view)
        return view
    }

}