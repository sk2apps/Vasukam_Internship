package com.saggy.vasukaminternship


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import android.widget.ImageButton
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.widget.TextView
import android.widget.Toast
import android.view.*
import com.google.android.gms.tasks.Task
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class Video_Call : AppCompatActivity() {
    lateinit var profile_image: CircleImageView
    lateinit var username_text: TextView
    lateinit var cancel_call: ImageButton
    lateinit var make_call: ImageButton
    var callingid = ""
    var ringid = ""
    var receiverUserid: String? = ""
    var receiverUserimage = "null"
    var receiverUsername: String? = null
    var senderUserid = ""
    var senderUserimage = ""
    var checker = ""
    lateinit var userRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_call_activity)
        profile_image = findViewById(R.id.image)
        username_text = findViewById(R.id.video_username)
        cancel_call = findViewById(R.id.cancel_video)
        make_call = findViewById(R.id.make_call)
        receiverUserid = intent.extras!!.getString("uid")
        //        receiverUsername = getIntent().getExtras().getString("username");
        senderUserid = Objects.requireNonNull(FirebaseAuth.getInstance().currentUser)!!.uid

//        username_text.setText(receiverUsername);
        userRef = FirebaseDatabase.getInstance().reference.child("Users")
        userRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(senderUserid)
                        .hasChild("Ringing") && !snapshot.child(senderUserid).hasChild("calling")
                ) {
                    make_call.setVisibility(View.VISIBLE)
                }
                if (snapshot.child(receiverUserid!!).child("Ringing").hasChild("picked")) {
                    val intent = Intent(this@Video_Call, VideoChat::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        userRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(receiverUserid!!).exists()) {
                    //if(snapshot.child(receiverUserid).child("image").exists())  receiverUserimage=snapshot.child(receiverUserid).child("image").getValue().toString();
                    receiverUsername = Objects.requireNonNull(
                        snapshot.child(
                            receiverUserid!!
                        ).child("username").value
                    ).toString()
                    username_text.setText(receiverUsername)
                    //  Picasso.get().load(rec).placeholder(R.drawable.profile).into(messageViewHolder.receiverProfileImage);
                    //if(receiverUserimage!=null)  Picasso.get().load(receiverUserimage).placeholder(R.drawable.profile).into(profilcimage);
                }
                //                if(snapshot.child(senderUserid).exists()){
//                    if(snapshot.child(senderUserid).child("image").exists()) senderUserimage=snapshot.child(senderUserid).child("image").getValue().toString();
//                    senderrUsername=snapshot.child(senderUserid).child("phone").getValue().toString();
//                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        cancel_call.setOnClickListener(View.OnClickListener { view: View? ->
            checker = "clicked"
            Toast.makeText(application, "data$checker", Toast.LENGTH_SHORT).show()
            hello()
        })
        make_call.setOnClickListener(View.OnClickListener { view: View? ->
            val calling_pickup_map = HashMap<String, Any>()
            calling_pickup_map["picked"] = "picked"
            userRef!!.child(senderUserid).child("Ringing").updateChildren(calling_pickup_map)
                .addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        val intent = Intent(this@Video_Call, VideoChat::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
        })
    }

    override fun onStart() {
        super.onStart()
        userRef!!.child(receiverUserid!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!(checker == "clicked" || snapshot.hasChild("calling") || snapshot.hasChild("Ringing"))) {
                    //receiver not calling anyone and checker is not clicked
                    userRef!!.child(senderUserid)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (!(checker == "clicked" || snapshot.hasChild("calling") || snapshot.hasChild(
                                        "Ringing"
                                    ))
                                ) {
                                    // user also don't calling previously
                                    val callingInfo = HashMap<String, Any?>()
                                    callingInfo["calling"] = receiverUserid
                                    userRef!!.child(senderUserid).child("calling")
                                        .updateChildren(callingInfo)
                                        .addOnCompleteListener { task: Task<Void?> ->
                                            if (task.isSuccessful) {
                                                val ringingInfo = HashMap<String, Any>()
                                                ringingInfo["ringing"] = senderUserid
                                                userRef!!.child(receiverUserid!!).child("Ringing")
                                                    .updateChildren(ringingInfo)
                                            }
                                        }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })


        //when user come if other user calling this user
        userRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(senderUserid)
                        .hasChild("Ringing") && !snapshot.child(senderUserid).hasChild("calling")
                ) {
                    make_call!!.visibility = View.VISIBLE
                }
                if (snapshot.child(receiverUserid!!).child("Ringing").hasChild("picked")) {
                    val intent = Intent(this@Video_Call, VideoChat::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun hello() {
        userRef!!.child(senderUserid).child("calling")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists() && snapshot.hasChild("calling")) {
                        callingid =
                            Objects.requireNonNull(snapshot.child("calling").value).toString()
                        userRef!!.child(callingid).child("Ringing").removeValue()
                            .addOnCompleteListener { task: Task<Void?> ->
                                if (task.isSuccessful) {
                                    userRef!!.child(senderUserid).child("calling").removeValue()
                                        .addOnCompleteListener { task1: Task<Void?>? -> onBackPressed() }
                                }
                            }
                    } else onBackPressed()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        userRef!!.child(senderUserid).child("Ringing")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists() && snapshot.hasChild("ringing")) {
                        ringid = Objects.requireNonNull(snapshot.child("ringing").value).toString()
                        userRef!!.child(ringid)
                            .child("calling").removeValue()
                            .addOnCompleteListener { task: Task<Void?> ->
                                if (task.isSuccessful) {
                                    userRef!!.child(senderUserid).child("Ringing").removeValue()
                                        .addOnCompleteListener { task12: Task<Void?>? -> onBackPressed() }
                                }
                            }
                    } else onBackPressed()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}