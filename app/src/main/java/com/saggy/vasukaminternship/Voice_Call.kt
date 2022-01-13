package com.saggy.vasukaminternship

import android.Manifest
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
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.view.*
import com.google.android.gms.tasks.Task
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class Voice_Call : AppCompatActivity() {
    lateinit var profile_image: CircleImageView
    lateinit var username_text: TextView
    lateinit var cancel_call: ImageButton
    lateinit var make_call: ImageButton
    private  var callingid = ""
    private  var ringid = ""
    private  var receiverUserid = ""
    private  var receiverUserimage = "null"
    private  var receiverUsername = ""
    private  var senderUserid = ""
    private  var senderUserimage = ""
    private  var senderrUsername = ""
    private  var checker = ""
    private lateinit var userRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_call)
        profile_image = findViewById(R.id.image)
        username_text = findViewById(R.id.video_username)
        cancel_call = findViewById(R.id.cancel_video)
        make_call = findViewById(R.id.make_call)
        receiverUserid = intent.extras!!["uid"].toString()
        userRef = FirebaseDatabase.getInstance().reference.child("user1")
        senderUserid = Objects.requireNonNull(FirebaseAuth.getInstance().currentUser)!!.uid
        cancel_call.setOnClickListener(View.OnClickListener { view: View? ->
            checker = "clicked"
            Toast.makeText(application, "data$checker", Toast.LENGTH_SHORT).show()
            hello()
        })
        make_call.setOnClickListener(View.OnClickListener { view: View? ->
            val callingpickupmap = HashMap<String, Any>()
            callingpickupmap["picked"] = "picked"
            userRef!!.child(senderUserid).child("Ringing").updateChildren(callingpickupmap)
                .addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        val intent = Intent(this@Voice_Call, Voice_Chat::class.java)
                        intent.putExtra("callerId", senderUserid)
                        intent.putExtra("recipientId", receiverUserid)
                        startActivity(intent)
                        finish()
                    }
                }
        })
        userRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(senderUserid)
                        .hasChild("Ringing") && !snapshot.child(senderUserid).hasChild("calling")
                ) {
                    make_call.setVisibility(View.VISIBLE)
                }
                if (snapshot.child(receiverUserid).child("Ringing").hasChild("picked")) {
                    ///   mediaPlayer.stop();
                    val intent = Intent(this@Voice_Call, Voice_Chat::class.java)
                    intent.putExtra("callerId", senderUserid)
                    intent.putExtra("recipientId", receiverUserid)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(receiverUserid).exists()) {
                    //if(snapshot.child(receiverUserid).child("image").exists())  receiverUserimage=snapshot.child(receiverUserid).child("image").getValue().toString();
                    receiverUsername = Objects.requireNonNull(
                        snapshot.child(receiverUserid).child("username").value
                    ).toString()
                    username_text.setText(receiverUsername)
                    //  Picasso.get().load(rec).placeholder(R.drawable.profile).into(messageViewHolder.receiverProfileImage);
                    //if(receiverUserimage!=null)  Picasso.get().load(receiverUserimage).placeholder(R.drawable.profile).into(profilcimage);
                }
                if (snapshot.child(senderUserid).exists()) {
                    //if(snapshot.child(senderUserid).child("image").exists()) senderUserimage=snapshot.child(senderUserid).child("image").getValue().toString();
                    senderrUsername =
                        Objects.requireNonNull(snapshot.child(senderUserid).child("username").value)
                            .toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onStart() {
        if (ContextCompat.checkSelfPermission(
                this@Voice_Call,
                Manifest.permission.RECORD_AUDIO
            ) !=
            PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this@Voice_Call,
                Manifest.permission.READ_PHONE_STATE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@Voice_Call,
                arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE),
                1
            )
        }
        super.onStart()
        userRef!!.child(receiverUserid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!(checker == "clicked" || snapshot.hasChild("calling") || snapshot.hasChild("Ringing"))) {
                    userRef!!.child(senderUserid)
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (!(checker == "clicked" || snapshot.hasChild("calling") || snapshot.hasChild(
                                        "Ringing"
                                    ))
                                ) {
                                    val callingInfo = HashMap<String, Any>()
                                    callingInfo["calling"] = receiverUserid
                                    userRef!!.child(senderUserid).child("calling")
                                        .updateChildren(callingInfo)
                                        .addOnCompleteListener { task: Task<Void?> ->
                                            if (task.isSuccessful) {
                                                val ringingInfo = HashMap<String, Any>()
                                                ringingInfo["ringing"] = senderUserid
                                                userRef!!.child(receiverUserid).child("Ringing")
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
        userRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(senderUserid)
                        .hasChild("Ringing") && !snapshot.child(senderUserid).hasChild("calling")
                ) {
                    make_call!!.visibility = View.VISIBLE
                }
                if (snapshot.child(receiverUserid).child("Ringing").hasChild("picked")) {
                    val intent = Intent(this@Voice_Call, Voice_Chat::class.java)
                    intent.putExtra("callerId", senderUserid)
                    intent.putExtra("recipientId", receiverUserid)
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
                        callingid = snapshot.child("calling").value.toString()
                        userRef!!.child(callingid)
                            .child("Ringing").removeValue()
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
                        ringid = snapshot.child("ringing").value.toString()
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