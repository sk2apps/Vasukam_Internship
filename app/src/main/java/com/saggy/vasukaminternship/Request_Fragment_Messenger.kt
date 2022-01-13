package com.saggy.vasukaminternship

import android.content.Intent
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.saggy.vasukaminternship.data.model.user.ProfileInfo
import com.saggy.vasukaminternship.RecyclerTouchListener.ClickListener
import com.saggy.vasukaminternship.adapters.Chat_Contact_Adapter
import android.view.*
import androidx.fragment.app.Fragment
import com.saggy.vasukaminternship.models.Messages
import java.io.Serializable
import java.util.*

class Request_Fragment_Messenger : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    var currentUserId: String? = null
    private lateinit var chat_contact_adapter: Chat_Contact_Adapter
    lateinit var resultlist: List<ProfileInfo>
    lateinit var requestlist: List<ProfileInfo>
    lateinit var chat_user_list: List<ProfileInfo>
    lateinit var request_message_list: List<Messages>
    var temp = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        resultlist = (bundle!!.getSerializable("result") as List<ProfileInfo>?)!!
        requestlist = (bundle.getSerializable("request") as List<ProfileInfo>?)!!
        request_message_list = (bundle.getSerializable("message") as List<Messages>?)!!
        chat_user_list = (bundle.getSerializable("chat") as List<ProfileInfo>?)!!
        temp = bundle.getInt("val")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chat_messenger_activity, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        currentUserId = Objects.requireNonNull(firebaseAuth!!.currentUser)!!.uid
        recyclerView = view.findViewById(R.id.chat_messages_recycle_view)
        recyclerView.setLayoutManager(LinearLayoutManager(context))
        if (temp == 0) {
            // request
            chat_contact_adapter =
                Chat_Contact_Adapter(requireContext(), requestlist!!, object : ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        val pos = requestlist!![position]
                        //                // Toast.makeText(getApplicationContext(),  pos.getUid()+ " is selected!", Toast.LENGTH_SHORT).show();
                        if (currentUserId != pos.uid) {
                            FirebaseDatabase.getInstance().reference.child("Request").child(
                                currentUserId!!
                            )
                                .child(pos.uid!!).child("messageID").setValue("seen")
                            if (request_message_list!!.size == requestlist!!.size) {
                                val messages = request_message_list!![position]
                                val intent = Intent(context, Chat_Activity::class.java)
                                intent.putExtra("uid", pos.uid)
                                intent.putExtra("name", pos.name)
                                intent.putExtra("username", pos.username)
                                intent.putExtra("phoneNumber", pos.phoneNumber)
                                intent.putExtra("message", messages as Serializable)
                                intent.putExtra("val", 2)
                                startActivity(intent)
                                chat_contact_adapter!!.notifyDataSetChanged()
                            } else {
                                FirebaseDatabase.getInstance().reference.child("Request").child(
                                    currentUserId!!
                                ).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val messages = snapshot.child(
                                            requestlist!![position].uid!!
                                        ).getValue(
                                            Messages::class.java
                                        )
                                        val intent = Intent(context, Chat_Activity::class.java)
                                        intent.putExtra("uid", pos.uid)
                                        intent.putExtra("name", pos.name)
                                        intent.putExtra("username", pos.username)
                                        intent.putExtra("phoneNumber", pos.phoneNumber)
                                        intent.putExtra("message", messages as Serializable?)
                                        intent.putExtra("val", 2)
                                        startActivity(intent)
                                        chat_contact_adapter!!.notifyDataSetChanged()
                                    }

                                    override fun onCancelled(error: DatabaseError) {}
                                })
                            }
                        } else {
                            Toast.makeText(context, "your click your account", Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
            recyclerView.setAdapter(chat_contact_adapter)
        } else {
            // search
            chat_contact_adapter =
                Chat_Contact_Adapter(requireContext(), resultlist!!, object : ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        val pos = resultlist!![position]
                        var flag = 1
                        //check whether this profile is new to user or not
                        for (profileInfo in chat_user_list!!) {
                            if (profileInfo.uid == pos.uid) {
                                flag = 0
                                break
                            }
                        }
                        if (flag == 0) {
                            //chat
                            if (currentUserId != pos.uid) {
                                val intent = Intent(context, Chat_Activity::class.java)
                                intent.putExtra("uid", pos.uid)
                                intent.putExtra("name", pos.name)
                                intent.putExtra("username", pos.username)
                                intent.putExtra("phoneNumber", pos.phoneNumber)
                                intent.putExtra("val", 0)
                                startActivity(intent)
                            } else Toast.makeText(
                                context,
                                "your click your account",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            // new user
                            if (currentUserId != pos.uid) {
                                val intent = Intent(context, Chat_Activity::class.java)
                                intent.putExtra("uid", pos.uid)
                                intent.putExtra("name", pos.name)
                                intent.putExtra("username", pos.username)
                                intent.putExtra("phoneNumber", pos.phoneNumber)
                                intent.putExtra("val", 1)
                                startActivity(intent)
                            } else Toast.makeText(
                                context,
                                "your click your account",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
            recyclerView.setAdapter(chat_contact_adapter)
        }
        return view
    }
}