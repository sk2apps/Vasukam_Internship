package com.saggy.vasukaminternship

import android.Manifest
import com.saggy.vasukaminternship.utils.SinchServer.StartFailedListener
import android.os.IBinder
import com.sinch.android.rtc.SinchError
import android.os.Bundle
import android.content.ComponentName
import android.os.Build

class Video_Calling_Setup : Base_Activity(), StartFailedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_calling_setup)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.READ_PHONE_STATE
                ), 100
            )
        }
        if (!sinchServiceInterface!!.isStarted) {
            sinchServiceInterface!!.startClient("userName") //need usernmae of current user
        } else {
            openPlaceCallActivity()
        }
    }

    private fun openPlaceCallActivity() {}

    //when connection establish with sinch
    override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
        sinchServiceInterface!!.setStartListener(this)
    }

    override fun onStartFailed(error: SinchError?) {}
    override fun onStarted() {
        //invoked when server started
        openPlaceCallActivity()
        //go to place activity
    }
}