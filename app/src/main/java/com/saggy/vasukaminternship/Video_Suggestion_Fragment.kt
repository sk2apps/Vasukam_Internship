package com.saggy.vasukaminternship


import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.saggy.vasukaminternship.adapters.Video_Verticle_Adapter
import android.view.*
import androidx.fragment.app.Fragment
import java.util.ArrayList

class Video_Suggestion_Fragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    var options: MutableList<String> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.video_list_fragment, container, false)
        recyclerView = view.findViewById(R.id.videoListRecycleView)
        addValuesToList()
        val video_verticle_adapter = Video_Verticle_Adapter(requireContext(), options)
        recyclerView.setAdapter(video_verticle_adapter)
        recyclerView.setLayoutManager(LinearLayoutManager(context))
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