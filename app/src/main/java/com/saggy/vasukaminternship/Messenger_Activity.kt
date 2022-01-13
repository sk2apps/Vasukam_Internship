package com.saggy.vasukaminternship

import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.widget.EditText
import com.google.firebase.auth.FirebaseUser
import android.text.TextWatcher
import android.text.Editable
import com.saggy.vasukaminternship.models.Chat_Model
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.saggy.vasukaminternship.data.model.user.ProfileInfo
import com.saggy.vasukaminternship.adapters.Mesenger_Pager_Adapter
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import android.view.*
import com.saggy.vasukaminternship.models.Messages
import java.util.*
import kotlin.collections.ArrayList

class Messenger_Activity : AppCompatActivity() {
    lateinit var backButton: ImageButton
    lateinit var tb: TabLayout
    lateinit var vp: ViewPager
    lateinit var search_bar: EditText
    lateinit var firebaseUser: FirebaseUser
    lateinit var currentUserUid: String
    var userList: MutableList<ProfileInfo> = ArrayList()
    var resultList: MutableList<ProfileInfo> = ArrayList()
    var chat_user_list: MutableList<ProfileInfo> = ArrayList()
    var requestlist: MutableList<ProfileInfo> = ArrayList()
    var request_message_list: MutableList<Messages> = ArrayList()
    var temp_message_list: MutableList<Messages> = ArrayList()
    var temp = 0
    var chat_list = Stack<Chat_Model>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.messenger_activity)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        assert(firebaseUser != null)
        currentUserUid = firebaseUser.uid
        tb = findViewById(R.id.tab_layout)
        vp = findViewById(R.id.viewPager)
        backButton = findViewById(R.id.backButton)
        search_bar = findViewById(R.id.searchBar)
        tb.addTab(tb.newTab().setText("Chat"))
        tb.addTab(tb.newTab().setText("Request"))
        tb.tabGravity = TabLayout.GRAVITY_FILL

        // to get all the users
        FirebaseDatabase.getInstance().getReference("Users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (idSnapshot in dataSnapshot.children) {
                        val poi = idSnapshot.getValue(ProfileInfo::class.java)!!
                        if (poi.uid != currentUserUid) userList.add(poi)
                    }

                    //get chats of the current user
                    FirebaseDatabase.getInstance().reference.child("Chat").child(currentUserUid)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (idSnapshot in dataSnapshot.children) {
                                    val chat = idSnapshot.getValue(Chat_Model::class.java)
                                    if (chat != null) chat_list.push(chat)
                                }
                                for (chat_model in chat_list) {
                                    for (profileInfo in userList) {
                                        if (chat_model.uid == profileInfo!!.uid) {
                                            chat_user_list.add(profileInfo)
                                            break
                                        }
                                    }
                                }
                                FirebaseDatabase.getInstance().reference.child("Request").child(
                                    currentUserUid
                                ).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (dataSnapshot in snapshot.children) {
                                            val messages = dataSnapshot.getValue(
                                                Messages::class.java
                                            )
                                            if (messages != null) {
                                                temp_message_list.add(messages)
                                            }
                                        }
                                        var count = 0
                                        for (msg in temp_message_list) {
                                            if (msg!!.messageID == "new") {
                                                request_message_list.add(msg)
                                                count++
                                            }
                                        }
                                        for (msg in temp_message_list) {
                                            if (msg!!.messageID != "new") request_message_list.add(
                                                msg
                                            )
                                        }
                                        for (message in request_message_list) {
                                            for (profileInfo in userList) {
                                                if (profileInfo!!.uid == message!!.from) {
                                                    requestlist.add(profileInfo)
                                                    break
                                                }
                                            }
                                        }
                                        if (count != 0) Objects.requireNonNull(tb.getTabAt(1))!!.orCreateBadge.number =
                                            count
                                        val mesenger_pager_adapter = Mesenger_Pager_Adapter(
                                            this@Messenger_Activity,
                                            supportFragmentManager,
                                            tb.tabCount,
                                            0,
                                            chat_user_list,
                                            resultList,
                                            requestlist,
                                            request_message_list
                                        )
                                        vp.adapter = mesenger_pager_adapter
                                    }

                                    override fun onCancelled(error: DatabaseError) {}
                                })
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        vp.addOnPageChangeListener(TabLayoutOnPageChangeListener(tb))
        tb.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                vp.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        backButton.setOnClickListener(View.OnClickListener { view: View? -> onBackPressed() })
        search_bar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length > 2) {
                    resultList.clear()
                    for (profileInfo in userList) {
                        if (profileInfo!!.username.lowercase(Locale.getDefault())
                                .contains(charSequence.toString().lowercase(Locale.getDefault())) ||
                            profileInfo.name.lowercase(Locale.getDefault())
                                .contains(charSequence.toString().lowercase(Locale.getDefault()))
                        ) {
                            resultList.add(profileInfo)
                        }
                    }
                    if (resultList.size > 0) {
                        temp = 1
                        val mesenger_pager_adapter = Mesenger_Pager_Adapter(
                            this@Messenger_Activity,
                            supportFragmentManager,
                            tb.tabCount,
                            1,
                            chat_user_list,
                            resultList,
                            requestlist,
                            request_message_list
                        )
                        vp.adapter = mesenger_pager_adapter
                        Objects.requireNonNull(vp.adapter)!!.notifyDataSetChanged()
                    } else {
                        if (temp == 1) {
                            val mesenger_pager_adapter = Mesenger_Pager_Adapter(
                                this@Messenger_Activity,
                                supportFragmentManager,
                                tb.tabCount,
                                0,
                                chat_user_list,
                                resultList,
                                requestlist,
                                request_message_list
                            )
                            vp.adapter = mesenger_pager_adapter
                            Objects.requireNonNull(vp.adapter)!!.notifyDataSetChanged()
                            temp = 0
                        }
                    }
                } else {
                    if (temp == 1) {
                        val mesenger_pager_adapter = Mesenger_Pager_Adapter(
                            this@Messenger_Activity,
                            supportFragmentManager,
                            tb.tabCount,
                            0,
                            chat_user_list,
                            resultList,
                            requestlist,
                            request_message_list
                        )
                        vp.adapter = mesenger_pager_adapter
                        Objects.requireNonNull(vp.adapter)!!.notifyDataSetChanged()
                        temp = 0
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }
}