package com.saggy.vasukaminternship.adapters

import android.content.Context
import com.saggy.vasukaminternship.data.model.user.ProfileInfo
import androidx.fragment.app.FragmentStatePagerAdapter
import com.saggy.vasukaminternship.Chat_fragments_Messenger
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.saggy.vasukaminternship.Request_Fragment_Messenger
import com.saggy.vasukaminternship.models.Messages
import java.io.Serializable

class Mesenger_Pager_Adapter(
    var context: Context, fm: FragmentManager, var totalTabs: Int, var `val`: Int,
    var chat_user_list: List<ProfileInfo>, var resultList: List<ProfileInfo>,
    var request_list: List<ProfileInfo>, var request_message_list: List<Messages>
) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                val chat_fragments_messenger = Chat_fragments_Messenger()
                val bundle = Bundle()
                bundle.putSerializable("result", resultList as Serializable)
                bundle.putSerializable("chat", chat_user_list as Serializable)
                bundle.putInt("val", `val`)
                chat_fragments_messenger.arguments = bundle
                chat_fragments_messenger
            }
            1 -> {
                val request_fragment_messenger = Request_Fragment_Messenger()
                val bundle1 = Bundle()
                bundle1.putSerializable("result", resultList as Serializable)
                bundle1.putSerializable("request", request_list as Serializable)
                bundle1.putSerializable("message", request_message_list as Serializable)
                bundle1.putSerializable("chat", chat_user_list as Serializable)
                bundle1.putInt("val", `val`)
                request_fragment_messenger.arguments = bundle1
                request_fragment_messenger
            }
            else -> {
                val chat_fragments_messenger = Chat_fragments_Messenger()
                val bundle = Bundle()
                bundle.putSerializable("result", resultList as Serializable)
                bundle.putSerializable("chat", chat_user_list as Serializable)
                bundle.putInt("val", `val`)
                chat_fragments_messenger.arguments = bundle
                chat_fragments_messenger
            }
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}