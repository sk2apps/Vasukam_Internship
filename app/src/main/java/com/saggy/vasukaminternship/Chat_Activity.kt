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
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseUser
import com.saggy.vasukaminternship.adapters.Message_Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.TextWatcher
import android.annotation.SuppressLint
import android.text.Editable
import com.saggy.vasukaminternship.models.Chat_Model
import com.google.firebase.database.ChildEventListener
import android.view.*
import com.google.android.gms.tasks.Task
import com.saggy.vasukaminternship.models.Messages
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class Chat_Activity : AppCompatActivity() {
    lateinit var backButton: ImageButton
    lateinit var message_send_button: ImageButton
    lateinit var video_call: ImageButton
    lateinit var voice_call: ImageButton
    lateinit var photo_send: ImageButton
    lateinit var camera: ImageButton
    lateinit var addButton: ImageButton
    lateinit var usernameText: TextView
    lateinit var userMessagesList: RecyclerView
    lateinit var messageBox: EditText
    lateinit var profile_imageR: CircleImageView
    lateinit var bottom_layout: RelativeLayout
    lateinit var sender_cardView: CardView
    lateinit var receiver_cardView: CardView
    lateinit var sent_text: TextView
    lateinit var received_text: TextView
    lateinit var request_dialog: CardView
    lateinit var accept_request: AppCompatButton
    lateinit var cancel_dialog: AppCompatButton
    private lateinit var RootRef: DatabaseReference
    lateinit var firebaseUser: FirebaseUser
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var currentUserUid: String
    lateinit var usernameR: String
    lateinit var nameR: String
    lateinit var uidR: String
    lateinit var phoneNumberR: String
    var number = 0
    lateinit var sender_key: String
    lateinit var receiver_key: String
    var messagesList: MutableList<Messages> = ArrayList()
    lateinit var message_adapter: Message_Adapter
    lateinit var request_message: Messages
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
        currentUserUid = firebaseUser!!.uid
        RootRef = FirebaseDatabase.getInstance().reference
        usernameText = findViewById(R.id.username)
        backButton = findViewById(R.id.backButton)
        userMessagesList = findViewById(R.id.chat_list_messages)
        messageBox = findViewById(R.id.messageBox)
        message_send_button = findViewById(R.id.mic_chat_button)
        video_call = findViewById(R.id.video_call)
        voice_call = findViewById(R.id.mic_button)
        photo_send = findViewById(R.id.image_button)
        camera = findViewById(R.id.camera_button)
        addButton = findViewById(R.id.add_button)
        profile_imageR = findViewById(R.id.profile_picture)
        bottom_layout = findViewById(R.id.bottom_layout)
        sender_cardView = findViewById(R.id.message_cardS)
        received_text = findViewById(R.id.receive_message)
        sent_text = findViewById(R.id.send_message)
        receiver_cardView = findViewById(R.id.message_cardR)
        cancel_dialog = findViewById(R.id.cancel_dialog)
        accept_request = findViewById(R.id.accept_request)
        request_dialog = findViewById(R.id.request_dialog)
        receiver_cardView.setVisibility(View.GONE)
        sender_cardView.setVisibility(View.GONE)
        request_dialog.setVisibility(View.GONE)
        number = intent.extras!!.getInt("val")
        usernameR = intent.extras!!.getString("username").toString()
        nameR = intent.extras!!.getString("name").toString()
        uidR = intent.extras!!.getString("uid").toString()
        phoneNumberR = intent.extras!!.getString("phoneNumber").toString()
        usernameText.setText(usernameR)
        message_adapter = Message_Adapter(messagesList)
        userMessagesList.setLayoutManager(LinearLayoutManager(this))
        userMessagesList.setAdapter(message_adapter)
        if (number == 0 || messagesList.size > 0) {
            FirebaseDatabase.getInstance().reference.child("Messages").child(currentUserUid!!)
                .child(uidR!!).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child("keycode").exists()) sender_key =
                            snapshot.child("keycode").getValue(
                                String::class.java
                            )!! else number = 1
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            FirebaseDatabase.getInstance().reference.child("Messages").child(uidR!!)
                .child(currentUserUid!!)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child("keycode").exists()) receiver_key =
                            snapshot.child("keycode").getValue(
                                String::class.java
                            )!! else number = 1
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        } else if (number == 2) {
            //request
            bottom_layout.setVisibility(View.GONE)
            request_message = (intent.getSerializableExtra("message") as Messages?)!!
            request_dialog.setVisibility(View.VISIBLE)
            receiver_cardView.setVisibility(View.VISIBLE)
            received_text.setText(request_message!!.message)
        } else {
            //search
            FirebaseDatabase.getInstance().reference.child("Request").child(uidR!!)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(currentUserUid!!).exists()) {
                            bottom_layout.setVisibility(View.GONE)
                            sender_cardView.setVisibility(View.VISIBLE)
                            sent_text.setText(
                                snapshot.child(currentUserUid!!).child("message").getValue(
                                    String::class.java
                                )
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }
        backButton.setOnClickListener(View.OnClickListener { view: View? -> onBackPressed() })
        video_call.setOnClickListener(View.OnClickListener { view: View? ->
            if (number == 0) {
                val intent = Intent(this@Chat_Activity, Video_Call::class.java)
                intent.putExtra("uid", uidR)
                //                intent.putExtra("username", usernameR);
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "you can't video call $usernameR", Toast.LENGTH_SHORT).show()
            }
        })
        voice_call.setOnClickListener(View.OnClickListener { view: View? ->
            if (number == 0) {
                val intent = Intent(this@Chat_Activity, Voice_Call::class.java)
                intent.putExtra("uid", uidR)
                //                intent.putExtra("username", usernameR);
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "you can't voice call $usernameR", Toast.LENGTH_SHORT).show()
            }
        })
        messageBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length > 0) {
                    message_send_button.setImageDrawable(resources.getDrawable(R.drawable.messenger_right_figma))
                } else {
                    message_send_button.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_mic_none_24))
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        message_send_button.setOnClickListener(View.OnClickListener { view: View? ->
            if (messageBox.getText().toString().length > 0) {
                //want to send message
                if (number == 0) {
                    // already in his chat
                    val message = messageBox.getText().toString()
                    //                    String messageSenderRef = "Messages/" + currentUserUid + "/" + uidR + "/messages" ;
//                    String messageReceiverRef = "Messages/" + uidR + "/" + currentUserUid + "/messages" ;
                    val userMessageKeyRef = RootRef!!.child("Messages")
                        .child(currentUserUid!!).child(uidR!!).child("messages").push()
                    val messagePushID = userMessageKeyRef.key
                    val calendar = Calendar.getInstance()
                    val currentDate = SimpleDateFormat("MM dd, yyyy")
                    val saveCurrentDate = currentDate.format(calendar.time)
                    val currentTime = SimpleDateFormat("hh:mm a")
                    val saveCurrentTime = currentTime.format(calendar.time)
                    val messages = messagePushID?.let {
                        Messages(
                            currentUserUid, message, "text", uidR, it,
                            saveCurrentTime, saveCurrentDate
                        )
                    }
                    assert(messagePushID != null)
                    FirebaseDatabase.getInstance().reference.child("Messages")
                        .child(currentUserUid!!)
                        .child(uidR!!).child("messages").child(messagePushID!!).setValue(messages)
                    FirebaseDatabase.getInstance().reference.child("Messages").child(uidR!!)
                        .child(currentUserUid!!).child("messages").child(messagePushID)
                        .setValue(messages)
                    FirebaseDatabase.getInstance().reference.child("Chat").child(currentUserUid!!)
                        .child(
                            sender_key!!
                        ).removeValue()
                    FirebaseDatabase.getInstance().reference.child("Chat").child(uidR!!).child(
                        receiver_key!!
                    ).removeValue()
                    val sref1 = RootRef!!.child("Chat").child(currentUserUid!!).push()
                    val rref2 = RootRef!!.child("Chat").child(uidR!!).push()
                    val new_sender_key = sref1.key
                    val new_receiver_key = rref2.key
                    val senderChat = new_sender_key?.let { Chat_Model(it, uidR) }
                    val receiverChat = new_receiver_key?.let { Chat_Model(it, currentUserUid) }
                    assert(new_sender_key != null)
                    FirebaseDatabase.getInstance().reference.child("Chat").child(currentUserUid!!)
                        .child(new_sender_key!!).setValue(senderChat)
                    assert(new_receiver_key != null)
                    FirebaseDatabase.getInstance().reference.child("Chat").child(uidR!!)
                        .child(new_receiver_key!!).setValue(receiverChat)
                    FirebaseDatabase.getInstance().reference.child("Messages")
                        .child(currentUserUid!!)
                        .child(uidR!!).child("keycode").setValue(new_sender_key)
                    FirebaseDatabase.getInstance().reference.child("Messages").child(uidR!!)
                        .child(currentUserUid!!).child("keycode").setValue(new_receiver_key)
                    sender_key = new_sender_key
                    receiver_key = new_receiver_key
                    messageBox.setText("")
                } else {
                    // want to give request
                    FirebaseDatabase.getInstance().reference.child("Request").child(uidR!!)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.child(currentUserUid!!).exists()) {
                                    Toast.makeText(
                                        this@Chat_Activity,
                                        "you already sent the request to $usernameR",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val message = messageBox.getText().toString()
                                    val calendar = Calendar.getInstance()
                                    val currentDate = SimpleDateFormat("MM dd, yyyy")
                                    val saveCurrentDate = currentDate.format(calendar.time)
                                    val currentTime = SimpleDateFormat("hh:mm a")
                                    val saveCurrentTime = currentTime.format(calendar.time)
                                    val messages = Messages(
                                        currentUserUid, message, "text", uidR, "new",
                                        saveCurrentTime, saveCurrentDate
                                    )
                                    FirebaseDatabase.getInstance().reference.child("Request")
                                        .child(uidR!!).child(
                                        currentUserUid!!
                                    ).setValue(messages)
                                    Toast.makeText(
                                        this@Chat_Activity,
                                        "Request sent successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    sender_cardView.setVisibility(View.VISIBLE)
                                    sent_text.setText(message)
                                    bottom_layout.setVisibility(View.GONE)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                }
            } else {
                //want to send audio
            }
        })
        cancel_dialog.setOnClickListener(View.OnClickListener { view: View? ->
            if (number == 2) {
                request_dialog.setVisibility(View.GONE)
            }
        })
        accept_request.setOnClickListener(View.OnClickListener { view: View? ->
            if (number == 2) {
                if (!request_message!!.message!!.isEmpty()) {
                    val userMessageKeyRef = RootRef!!.child("Messages")
                        .child(currentUserUid!!).child(uidR!!).child("messages").push()
                    val messagePushId = userMessageKeyRef.key
                    val messages = messagePushId?.let {
                        Messages(
                            request_message!!.from,
                            request_message!!.message,
                            "text",
                            request_message!!.to,
                            it,
                            request_message!!.time,
                            request_message!!.date
                        )
                    }
                    assert(messagePushId != null)
                    FirebaseDatabase.getInstance().reference.child("Messages")
                        .child(currentUserUid!!)
                        .child(uidR!!).child("messages").child(messagePushId!!).setValue(messages)
                    FirebaseDatabase.getInstance().reference.child("Messages").child(uidR!!)
                        .child(currentUserUid!!).child("messages").child(messagePushId)
                        .setValue(messages)
                    val sref1 = RootRef!!.child("Chat").child(currentUserUid!!).push()
                    val rref2 = RootRef!!.child("Chat").child(uidR!!).push()
                    val new_sender_key = sref1.key
                    val new_receiver_key = rref2.key
                    val senderChat = new_sender_key?.let { Chat_Model(it, uidR) }
                    val receiverChat = new_receiver_key?.let { Chat_Model(it, currentUserUid) }
                    assert(new_sender_key != null)
                    FirebaseDatabase.getInstance().reference.child("Chat").child(currentUserUid!!)
                        .child(new_sender_key!!).setValue(senderChat)
                    assert(new_receiver_key != null)
                    FirebaseDatabase.getInstance().reference.child("Chat").child(uidR!!)
                        .child(new_receiver_key!!).setValue(receiverChat)
                    FirebaseDatabase.getInstance().reference.child("Messages")
                        .child(currentUserUid!!)
                        .child(uidR!!).child("keycode").setValue(new_sender_key)
                    FirebaseDatabase.getInstance().reference.child("Messages").child(uidR!!)
                        .child(currentUserUid!!).child("keycode").setValue(new_receiver_key)
                    FirebaseDatabase.getInstance().reference.child("Request").child(uidR!!).child(
                        currentUserUid!!
                    ).removeValue()
                    FirebaseDatabase.getInstance().reference.child("Request")
                        .child(currentUserUid!!).child(
                        uidR!!
                    ).removeValue()
                    Toast.makeText(this, "request accepted successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Chat_Activity, Messenger_Activity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })
        /*sendfilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[]=new CharSequence[]
                        {
                                "Images",
                                "PDF Files",
                                "Ms Word Files"
                        };
                AlertDialog.Builder builder=new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("Select the file");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){
                            checker="image";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/ *");
                            startActivityForResult(intent.createChooser(intent,"Select Image"),438);
                        }
                        if(i==1){
                            checker="pdf";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent.createChooser(intent,"Select Pdf file"),438);
                        }
                        if(i==2){
                            checker="docx";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/msword");
                            startActivityForResult(intent.createChooser(intent,"Select MS Word file"),438);
                        }
                    }
                });
                builder.show();

            }
        });*/
    }

    override fun onStart() {
        super.onStart()
        val userRef = FirebaseDatabase.getInstance().reference.child("Users")
        userRef.child(currentUserUid!!).child("Ringing")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild("ringing")) {
                        val valuer = snapshot.child("ringing").value.toString()
                        userRef.child(snapshot.child("ringing").value.toString()).child("calling")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.hasChild("calling")) {
                                        val intent =
                                            Intent(this@Chat_Activity, Video_Call::class.java)
                                        intent.putExtra("uid", valuer)
                                        // intent.putExtra("name",  messageReceiverName);
                                        startActivity(intent)
                                    } else {
                                        userRef.child(currentUserUid!!).child("Ringing")
                                            .removeValue()
                                            .addOnCompleteListener { task: Task<Void?>? -> }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        val userRef1 = FirebaseDatabase.getInstance().reference.child("user1")
        userRef1.child(currentUserUid).child("Ringing")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild("ringing")) {
                        val valuer = snapshot.child("ringing").value.toString()
                        userRef1.child(snapshot.child("ringing").value.toString()).child("calling")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.hasChild("calling")) {
                                        val intent =
                                            Intent(this@Chat_Activity, Voice_Call::class.java)
                                        intent.putExtra("uid", valuer)
                                        // intent.putExtra("name",  messageReceiverName);
                                        startActivity(intent)
                                    } else {
                                        userRef1.child(currentUserUid!!).child("Ringing")
                                            .removeValue()
                                            .addOnCompleteListener { task: Task<Void?>? -> }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        RootRef!!.child("Messages").child(currentUserUid!!).child(uidR!!).child("messages")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val messages = dataSnapshot.getValue(
                        Messages::class.java
                    )
                    if (messages != null) {
                        messagesList.add(messages)
                    }
                    message_adapter!!.notifyDataSetChanged()
                    userMessagesList!!.smoothScrollToPosition(
                        Objects.requireNonNull(
                            userMessagesList.adapter
                        ).itemCount
                    )
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }
}