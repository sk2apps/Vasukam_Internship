package com.saggy.vasukaminternship.adapters

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import com.saggy.vasukaminternship.R
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import android.view.ViewGroup.MarginLayoutParams
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class Bottom_Sheet_Dialog_Fragment : BottomSheetDialogFragment() {
    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val view = LayoutInflater.from(context).inflate(R.layout.award_activity, null)
        val tb: TabLayout = view.findViewById(R.id.tab_layout)
        val vp: ViewPager = view.findViewById(R.id.popupViewPager)
        tb.addTab(tb.newTab().setText("Currency"))
        tb.addTab(tb.newTab().setText("Awards"))
        //using below java code we can add margin between the tabs of tab layout
//        for(int i=0; i < tb.getTabCount(); i++) {
//            View tab = ((ViewGroup) tb.getChildAt(0)).getChildAt(i);
//            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
//            p.setMargins(0, 0, 50, 0);
//            tab.requestLayout();
//        }

        //using below java code in main activity we can add drawable + text in tabs of the tab layout
        tb.getTabAt(0)!!.setIcon(R.drawable.money_figma_1)
        tb.getTabAt(1)!!.setIcon(R.drawable.trophy_black_figma)

//        for (i in 0 until tabCount) {
//            val tabView: View = (images_videos_tab_layout.getChildAt(0) as ViewGroup).getChildAt(i)
//            tabView.requestLayout()
//            ViewCompat.setBackground(tabView,setImageButtonStateNew(requireContext()));
//            ViewCompat.setPaddingRelative(tabView, tabView.paddingStart, tabView.paddingTop, tabView.paddingEnd, tabView.paddingBottom);
//        }
//        fun setImageButtonStateNew(mContext: Context): StateListDrawable {
//            val states = StateListDrawable()
//            states.addState(intArrayOf(android.R.attr.state_selected), ContextCompat.getDrawable(mContext, R.drawable.tab_bg_normal_blue))
//            states.addState(intArrayOf(-android.R.attr.state_selected), ContextCompat.getDrawable(mContext, R.drawable.tab_bg_normal))
//            return states
//        }
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
        tb.tabGravity = TabLayout.GRAVITY_FILL
        val tab_layout_adapter = Tab_Layout_Adapter(requireContext(), tb.tabCount, childFragmentManager)
        vp.adapter = tab_layout_adapter
        vp.addOnPageChangeListener(TabLayoutOnPageChangeListener(tb))
        tb.addOnTabSelectedListener(object : OnTabSelectedListener {
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
        dialog.setContentView(view)
    }
}