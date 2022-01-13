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
import android.app.Activity
import android.view.*
import androidx.fragment.app.Fragment
import java.util.*

class Chat_fragments_Messenger : Fragment() {

    lateinit var activity: Messenger_Activity
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    var currentUserId: String? = null
    private lateinit var chat_contact_adapter: Chat_Contact_Adapter
    lateinit var resultlist: List<ProfileInfo>
    lateinit var chat_user_list: List<ProfileInfo>
    var temp = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        resultlist = (bundle!!.getSerializable("result") as List<ProfileInfo>?)!!
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

        // 0 for chat
        // 1 for search for request user
        if (temp == 0) {
            //chat
            chat_contact_adapter =
                Chat_Contact_Adapter(requireContext(), chat_user_list!!, object : ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        val pos = chat_user_list!![position]
                        //                // Toast.makeText(getApplicationContext(),  pos.getUid()+ " is selected!", Toast.LENGTH_SHORT).show();
                        if (currentUserId != pos.uid) {
                            val intent = Intent(context, Chat_Activity::class.java)
                            intent.putExtra("uid", pos.uid)
                            intent.putExtra("name", pos.name)
                            intent.putExtra("username", pos.username)
                            intent.putExtra("phoneNumber", pos.phoneNumber)
                            intent.putExtra("val", 0)
                            startActivity(intent)
                            chat_contact_adapter!!.notifyDataSetChanged()
                        } else {
                            Toast.makeText(context, "your click your account", Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
            recyclerView.setAdapter(chat_contact_adapter)
        } else {
            //search
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
                                chat_contact_adapter!!.notifyDataSetChanged()
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
                                chat_contact_adapter!!.notifyDataSetChanged()
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


        //video call
        val userRef = FirebaseDatabase.getInstance().reference.child("Users")
        userRef.child(currentUserId!!).child("Ringing")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild("ringing")) {
                        val valuer = snapshot.child("ringing").value.toString()
                        userRef.child(snapshot.child("ringing").value.toString()).child("calling")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.hasChild("calling")) {
                                        val intent = Intent(activity, Video_Call::class.java)
                                        intent.putExtra("uid", valuer)
                                        // intent.putExtra("name",  messageReceiverName);
                                        startActivity(intent)
                                    } else {
                                        userRef.child(currentUserId!!).child("Ringing")
                                            .removeValue()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })


        //voice call
        val userRef1 = FirebaseDatabase.getInstance().reference.child("user1")
        userRef1.child(currentUserId!!).child("Ringing")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild("ringing")) {
                        val valuer = snapshot.child("ringing").value.toString()
                        userRef1.child(snapshot.child("ringing").value.toString()).child("calling")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.hasChild("calling")) {
                                        val intent =
                                            Intent(activity, Voice_Call::class.java) //voice call
                                        intent.putExtra("uid", valuer)
                                        // intent.putExtra("name",  messageReceiverName);
                                        startActivity(intent)
                                    } else {
                                        userRef1.child(currentUserId!!).child("Ringing")
                                            .removeValue()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return view
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activity = activity as Messenger_Activity
    }
}