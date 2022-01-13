package com.saggy.vasukaminternship

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.opentok.android.Session.SessionListener
import com.opentok.android.PublisherKit.PublisherListener
import android.widget.FrameLayout
import com.google.firebase.database.DatabaseReference
import android.widget.ImageButton
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.AfterPermissionGranted
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.*
import com.google.android.gms.tasks.Task
import com.opentok.android.*
import java.util.*

class VideoChat : AppCompatActivity(), SessionListener, PublisherListener {
    private lateinit var mSubscriber: Subscriber
    private lateinit var mPublisher: Publisher
    lateinit var userid: String
    private lateinit var mSession: Session
    private lateinit var mPublisherViewContainer: FrameLayout
    private lateinit var mSubscriberViewContainer: FrameLayout
    private lateinit var userRef: DatabaseReference
    private lateinit var close: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_chat)
        userRef = FirebaseDatabase.getInstance().reference.child("Users")
        userid = Objects.requireNonNull(FirebaseAuth.getInstance().currentUser)!!.uid
        close = findViewById(R.id.close)
        requestPermissions()
        close.setOnClickListener(View.OnClickListener { view: View? ->
            userRef!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(userid!!).hasChild("Ringing")) {
                        userRef!!.child(userid!!).child("Ringing")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists() && snapshot.hasChild("picked")) {
                                        val ringid = snapshot.child("picked").value.toString()
                                        userRef!!.child(ringid)
                                            .child("calling").removeValue()
                                            .addOnCompleteListener { task: Task<Void?> ->
                                                if (task.isSuccessful) {
                                                    userRef!!.child(userid!!).child("Ringing")
                                                        .removeValue()
                                                }
                                            }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
                        userRef!!.child(userid!!).child("Ringing").removeValue()
                        mPublisher!!.destroy()
                        mSubscriber!!.destroy()
                        onBackPressed()
                    }
                    if (snapshot.child(userid!!).hasChild("calling")) {
                        userRef!!.child(userid!!).child("calling")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists() && snapshot.hasChild("calling")) {
                                        val callingid = snapshot.child("calling").value.toString()
                                        userRef!!.child(callingid)
                                            .child("Ringing").removeValue()
                                            .addOnCompleteListener { task: Task<Void?> ->
                                                if (task.isSuccessful) {
                                                    userRef!!.child(userid!!).child("calling")
                                                        .removeValue()
                                                }
                                            }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
                        userRef!!.child(userid!!).child("calling").removeValue()
                        if (mPublisher != null) mPublisher!!.destroy()
                        if (mSubscriber != null) mSubscriber!!.destroy()
                        onBackPressed()
                    } else {
                        if (mPublisher != null) mPublisher!!.destroy()
                        if (mSubscriber != null) mSubscriber!!.destroy()
                        onBackPressed()
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private fun requestPermissions() {
        val perms = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            // initialize view objects from your layout
            mPublisherViewContainer = findViewById<View>(R.id.publisher_container) as FrameLayout
            mSubscriberViewContainer = findViewById<View>(R.id.subscriber_container) as FrameLayout

            // initialize and connect to the session
            mSession = Session.Builder(this, API_KEY, SESSION_ID).build()
            mSession.setSessionListener(this)
            mSession.connect(TOKEN)
        } else {
            EasyPermissions.requestPermissions(
                this,
                "This app needs access to your camera and mic to make video calls",
                RC_VIDEO_APP_PERM,
                *perms
            )
        }
    }

    //publisher listener's method
    override fun onStreamCreated(publisherKit: PublisherKit, stream: Stream) {
        Log.i(LOG_TAG, "Publisher onStreamCreated")
    }

    override fun onStreamDestroyed(publisherKit: PublisherKit, stream: Stream) {
        Log.i(LOG_TAG, "Publisher onStreamDestroyed")
    }

    override fun onError(publisherKit: PublisherKit, opentokError: OpentokError) {
        Log.e(LOG_TAG, "Publisher error: " + opentokError.message)
    }

    //Session Listener's method
    override fun onConnected(session: Session) {
        Log.i(LOG_TAG, "Session Connected")
        mPublisher = Publisher.Builder(this).build()
        mPublisher.setPublisherListener(this)
        mPublisherViewContainer!!.addView(mPublisher.getView())
        if (mPublisher.getView() is GLSurfaceView) {
            (mPublisher.getView() as GLSurfaceView).setZOrderOnTop(true)
        }
        mSession!!.publish(mPublisher)
    }

    override fun onDisconnected(session: Session) {
        Log.i(LOG_TAG, "Session Disconnected")
    }

    override fun onStreamReceived(session: Session, stream: Stream) {
        Log.i(LOG_TAG, "Stream Received")
    }

    override fun onStreamDropped(session: Session, stream: Stream) {
        Log.i(LOG_TAG, "Stream Dropped")
        mSubscriberViewContainer!!.removeAllViews()
    }

    override fun onError(session: Session, opentokError: OpentokError) {
        Log.e(LOG_TAG, "Session error: " + opentokError.message)
    }

    companion object {
        private const val API_KEY = "47351701"
        private const val SESSION_ID =
            "1_MX40NzM1MTcwMX5-MTYzMzU1NDAwNjk4MH4zWlNjZ3VHRjZLOXpZalg1MldVNGUvWGJ-fg"
        private const val TOKEN =
            "T1==cGFydG5lcl9pZD00NzM1MTcwMSZzaWc9Y2QzYjI3YTA0OTFhNGY0N2FlYjMzZmIzMjQ1OWFiZDk" +
                    "xNDIxOTFiODpzZXNzaW9uX2lkPTFfTVg0ME56TTFNVGN3TVg1LU1UWXpNelUxTkRBd05qazRNSDR6V2xOalozVkhSalpMT1hwWmFsZz" +
                    "FNbGRWTkdVdldHSi1mZyZjcmVhdGVfdGltZT0xNjMzNTU0MDg2Jm5vbmNlPTAuMjg3NDUyNzE5NTM3MTkwNTQmcm9sZT1wdWJsaXNoZXIm" +
                    "ZXhwaXJlX3RpbWU9MTYzNjE0NjA4NSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ=="

        //validity 30 days
        private const val RC_VIDEO_APP_PERM = 124
        private val LOG_TAG = VideoChat::class.java.simpleName
    }
}