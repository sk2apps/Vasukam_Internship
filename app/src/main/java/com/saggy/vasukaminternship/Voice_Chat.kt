package com.saggy.vasukaminternship


import com.sinch.android.rtc.SinchClient
import com.sinch.android.rtc.Sinch
import com.sinch.android.rtc.SinchError
import com.sinch.android.rtc.SinchClientListener
import com.sinch.android.rtc.ClientRegistration
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
import com.sinch.android.rtc.calling.CallListener
import android.media.AudioManager
import android.view.*
import com.sinch.android.rtc.calling.Call
import com.sinch.android.rtc.calling.CallClient
import com.sinch.android.rtc.calling.CallClientListener

class Voice_Chat : AppCompatActivity() {
    private lateinit var call: Call
    private lateinit var username: TextView
    private lateinit var sinchClient: SinchClient
    private lateinit var cancel_call: ImageButton
    private lateinit var callerId: String
    private lateinit var recipientId: String
    private lateinit var userRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_chat)
        cancel_call = findViewById(R.id.close)
        username = findViewById(R.id.video_username)
        userRef = FirebaseDatabase.getInstance().reference.child("user1")
        val intent = intent
        callerId = intent.getStringExtra("callerId").toString()
        recipientId = intent.getStringExtra("recipientId").toString()
        sinchClient = Sinch.getSinchClientBuilder()
            .context(this@Voice_Chat)
            .userId(recipientId)
            .applicationKey(APP_KEY)
            .environmentHost(ENVIRONMENT)
            .build()

        //sinchClient.setSupportCalling(true);
        sinchClient.addSinchClientListener(object : SinchClientListener {
            override fun onClientStarted(sinchClient: SinchClient) {}
            override fun onClientFailed(sinchClient: SinchClient, sinchError: SinchError) {}
            override fun onLogMessage(i: Int, s: String, s1: String) {}
            override fun onPushTokenRegistered() {}
            override fun onPushTokenRegistrationFailed(sinchError: SinchError) {}
            override fun onCredentialsRequired(clientRegistration: ClientRegistration) {
                //need to implement
            }

            override fun onUserRegistered() {}
            override fun onUserRegistrationFailed(sinchError: SinchError) {}
        })
        sinchClient.startListeningOnActiveConnection()
        sinchClient.start()
        sinchClient.callClient.addCallClientListener(SinchCallClientListener())
        cancel_call.setOnClickListener(View.OnClickListener { view: View? ->
            call.hangup()
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userRef.child(FirebaseAuth.getInstance().currentUser!!.uid).child("Ringing")
                        .removeValue()
                    userRef.child(FirebaseAuth.getInstance().currentUser!!.uid).child("calling")
                        .removeValue()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
            onBackPressed()
        })
        call.hangup()
    }

    private inner class SinchCallListener : CallListener {
        override fun onCallEnded(endedCall: Call) {
            val a = endedCall.details.error
            //  button.setText("Call");
            username.text = ""
            volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE
        }

        override fun onCallEstablished(establishedCall: Call) {
            username.text = "connected"
            volumeControlStream = AudioManager.STREAM_VOICE_CALL
        }

        override fun onCallProgressing(progressingCall: Call) {
            username.text = "ringing"
        }
    }

    private inner class SinchCallClientListener : CallClientListener {
        override fun onIncomingCall(callClient: CallClient, incomingCall: Call) {
            call = incomingCall
            Toast.makeText(this@Voice_Chat, "incoming call", Toast.LENGTH_SHORT).show()
            call.answer()
            call.addCallListener(SinchCallListener())
            // button.setText("Hang Up");
        }
    }

    companion object {
        private const val APP_KEY = "4bb38533-8728-4c77-a455-4acf2c729447"
        private const val APP_SECRET = "RZNvr3U6mk2o/dwLuCrcfQ=="
        private const val ENVIRONMENT = "clientapi.sinch.com"
    }
}