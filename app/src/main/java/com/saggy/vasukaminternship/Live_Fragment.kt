package com.saggy.vasukaminternship


import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.widget.NestedScrollView
import android.widget.VideoView
import com.saggy.vasukaminternship.adapters.Item_Horizontal_Adapter
import com.saggy.vasukaminternship.adapters.Video_Horizontal_Adapter
import com.saggy.vasukaminternship.adapters.Video_Verticle_Adapter
import android.view.*
import androidx.fragment.app.Fragment
import java.util.ArrayList

class Live_Fragment : Fragment() {

    lateinit var nestedScrollView: NestedScrollView
    lateinit var horVideoRecycleView: RecyclerView

    //    List<String> videolinks = new ArrayList<>();
    var options: MutableList<String> = ArrayList()
    lateinit var itemRecycleView: RecyclerView
    lateinit var verVideoRecycleView: RecyclerView
    lateinit var videoTop: VideoView
    lateinit var rl: RelativeLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.live_tab_activity, container, false)
        horVideoRecycleView = view.findViewById(R.id.hor_video_recycle_view)
        verVideoRecycleView = view.findViewById(R.id.ver_video_recycle_view)
        rl = view.findViewById(R.id.rl)
        nestedScrollView = view.findViewById(R.id.nested_scroll_view)
        videoTop = view.findViewById(R.id.videotop)
        itemRecycleView = view.findViewById(R.id.optionRecycleView)
        addValuesToList()
        val item_horizontal_adapter = Item_Horizontal_Adapter(requireContext(), options)
        itemRecycleView.adapter = item_horizontal_adapter
        itemRecycleView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val video_horizontal_adapter = Video_Horizontal_Adapter(requireContext(), options)
        horVideoRecycleView.adapter = video_horizontal_adapter
        horVideoRecycleView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val video_verticle_adapter = Video_Verticle_Adapter(requireContext(), options)
        verVideoRecycleView.adapter = video_verticle_adapter
        verVideoRecycleView.layoutManager = LinearLayoutManager(context)
        rl.setOnClickListener(View.OnClickListener { view1: View? ->
            val intent = Intent(context, Video_Activity::class.java)
            startActivity(intent)
        })
        return view
    }

    private fun addValuesToList() {
        options.add("Item 1")
        options.add("Item 2")
        options.add("Item 3")
        options.add("Item 4")
        options.add("Item 5")
        options.add("Item 6")
        options.add("Item 7")
    }
}