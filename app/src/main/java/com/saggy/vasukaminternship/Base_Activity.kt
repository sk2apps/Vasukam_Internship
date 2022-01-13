package com.saggy.vasukaminternship


import com.saggy.vasukaminternship.utils.SinchServer.SinchServiceInterface
import com.saggy.vasukaminternship.utils.SinchServer
import android.content.Intent
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.ServiceConnection
import android.content.ComponentName

open class Base_Activity : AppCompatActivity(), ServiceConnection {
    protected var sinchServiceInterface: SinchServiceInterface? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applicationContext.bindService(
            Intent(this, SinchServer::class.java), this,
            BIND_AUTO_CREATE
        )
    }

    override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
        if (SinchServer::class.java.name == componentName.className) {
            sinchServiceInterface = iBinder as SinchServiceInterface
            onServiceConnected()
        }
    }

    override fun onServiceDisconnected(componentName: ComponentName) {
        if (SinchServer::class.java.name == componentName.className) {
            sinchServiceInterface = null
            onServiceDisconnected()
        }
    }

    protected fun onServiceConnected() {
        // for subclasses
    }

    protected fun onServiceDisconnected() {
        // for subclasses
    }
}