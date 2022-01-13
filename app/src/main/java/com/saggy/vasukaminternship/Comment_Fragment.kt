package com.saggy.vasukaminternship


import android.widget.ImageButton
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.saggy.vasukaminternship.adapters.Comment_Horizontal_Adapter
import com.saggy.vasukaminternship.adapters.Comment_Vertical_Adapter
import com.saggy.vasukaminternship.adapters.Bottom_Sheet_Dialog_Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.*
import androidx.fragment.app.Fragment
import java.util.ArrayList

class Comment_Fragment : Fragment() {

    lateinit var currencyButton: ImageButton
    lateinit var priceHorRecycleView: RecyclerView
    var priceList: MutableList<String> = ArrayList()
    lateinit var commentVerRecycleView: RecyclerView
    var commentList: MutableList<String> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.video_comment_fragment, container, false)
        priceHorRecycleView = view.findViewById(R.id.price_recycle_view)
        commentVerRecycleView = view.findViewById(R.id.comment_recycle_view)
        currencyButton = view.findViewById(R.id.currencyButton)
        addValuesToPriceList()
        addValueToCommentList()
        val comment_horizontal_adapter = Comment_Horizontal_Adapter(requireContext(), priceList)
        priceHorRecycleView.setAdapter(comment_horizontal_adapter)
        priceHorRecycleView.setLayoutManager(
            LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        val comment_vertical_adapter = Comment_Vertical_Adapter(requireContext(), commentList)
        commentVerRecycleView.setAdapter(comment_vertical_adapter)
        commentVerRecycleView.setLayoutManager(LinearLayoutManager(context))
        currencyButton.setOnClickListener(View.OnClickListener { view1: View? ->
            val bottom_sheet_dialog_fragment = Bottom_Sheet_Dialog_Fragment()
            bottom_sheet_dialog_fragment.show(childFragmentManager, "TAG")
        })
        return view
    }

    private fun addValuesToPriceList() {
        priceList.add("$564")
        priceList.add("$10")
        priceList.add("$54")
        priceList.add("$68")
        priceList.add("$300")
        priceList.add("$432")
    }

    private fun addValueToCommentList() {
        commentList.add("Nice Video")
        commentList.add("amazing")
        commentList.add("superb")
        commentList.add("Nice Video")
        commentList.add("incredible")
        commentList.add("nice")
        commentList.add("good")
        commentList.add("appreciable")
        commentList.add("credulous")
    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(R.layout.award_activity)

//        AppCompatButton currencyButton = bottomSheetDialog.findViewById(R.id.currencyButton);
//        AppCompatButton awardButton = bottomSheetDialog.findViewById(R.id.awardButton);
//        FragmentManager fragmentManager = getChildFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.currencyFrameLayout,new Currency_Fragment());
//        fragmentTransaction.commit();

//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.frameLayoutVideo,fragment);
//            fragmentTransaction.commit();


//        LinearLayout copy = bottomSheetDialog.findViewById(R.id.copyLinearLayout);
//        TabLayout tb = bottomSheetDialog.findViewById(R.id.tab_layout);
//        ViewPager vp = bottomSheetDialog.findViewById(R.id.viewPager);
//
//        tb.addTab(tb.newTab().setText("Currency"));
//        tb.addTab(tb.newTab().setText("Awards"));
//
//        tb.setTabGravity(TabLayout.GRAVITY_FILL);
//
//        Tab_Layout_Adapter tab_layout_adapter = new Tab_Layout_Adapter(getContext(), tb.getTabCount(), getActivity().getFragmentManager() );
//        vp.setAdapter(tab_layout_adapter);
//
//        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tb));
//
//        tb.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                vp.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
        bottomSheetDialog.show()
    }
}