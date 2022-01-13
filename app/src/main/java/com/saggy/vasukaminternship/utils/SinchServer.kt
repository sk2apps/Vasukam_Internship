package com.saggy.vasukaminternship.utils


import com.sinch.android.rtc.SinchClient
import com.sinch.android.rtc.Sinch
import android.content.Intent
import android.os.IBinder
import com.sinch.android.rtc.video.VideoController
import com.sinch.android.rtc.AudioController
import com.sinch.android.rtc.SinchError
import com.sinch.android.rtc.SinchClientListener
import com.sinch.android.rtc.ClientRegistration
import com.saggy.vasukaminternship.Video_Call
import android.app.Service
import android.os.Binder
import android.util.Log
import com.sinch.android.rtc.calling.Call
import com.sinch.android.rtc.calling.CallClient
import com.sinch.android.rtc.calling.CallClientListener

class SinchServer : Service() {
    private val mSinchServiceInterface = SinchServiceInterface()
    private lateinit var mSinchClient: SinchClient
    var userName: String? = null
        private set
    private var mListener: StartFailedListener? = null
    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        if (mSinchClient.isStarted) {
            mSinchClient.terminateGracefully()
        }
        super.onDestroy()
    }

    private fun start(userName: String) {
        if (mSinchClient == null) {
            this.userName = userName
            mSinchClient =
                Sinch.getSinchClientBuilder().context(applicationContext).userId(userName)
                    .applicationKey(APP_KEY)
                    .environmentHost(ENVIRONMENT).build()

            //mSinchClient.setSupportCalling(true);
            mSinchClient.startListeningOnActiveConnection()
            mSinchClient.addSinchClientListener(MySinchClientListener())
            mSinchClient.getCallClient().addCallClientListener(SinchCallClientListener())
            mSinchClient.start()
        }
    }

    private fun stop() {
        mSinchClient.terminateGracefully()
    }

    private val isStarted: Boolean
        private get() = mSinchClient != null && mSinchClient!!.isStarted

    override fun onBind(intent: Intent): IBinder? {
        return mSinchServiceInterface
    }

    inner class SinchServiceInterface : Binder() {
        fun callUserVideo(userId: String?): Call {
            return mSinchClient!!.callClient.callUserVideo(userId)
        }

        val isStarted: Boolean
            get() = this@SinchServer.isStarted

        fun startClient(userName: String) {
            start(userName)
        }

        fun stopClient() {
            stop()
        }

        fun setStartListener(listener: StartFailedListener?) {
            mListener = listener
        }

        fun getCall(callId: String?): Call {
            return mSinchClient!!.callClient.getCall(callId)
        }

        val videoController: VideoController?
            get() = if (!isStarted) {
                null
            } else mSinchClient!!.videoController
        val audioController: AudioController?
            get() = if (!isStarted) {
                null
            } else mSinchClient!!.audioController
    }

    interface StartFailedListener {
        fun onStartFailed(error: SinchError?)
        fun onStarted()
    }

    private inner class MySinchClientListener : SinchClientListener {
        override fun onClientFailed(client: SinchClient, error: SinchError) {
            if (mListener != null) {
                mListener!!.onStartFailed(error)
            }
            mSinchClient.terminateGracefully()

        }

        override fun onClientStarted(client: SinchClient) {
            Log.d(TAG, "SinchClient started")
            if (mListener != null) {
                mListener!!.onStarted()
            }
        }

        override fun onLogMessage(level: Int, area: String, message: String) {
            when (level) {
                Log.DEBUG -> Log.d(area, message)
                Log.ERROR -> Log.e(area, message)
                Log.INFO -> Log.i(area, message)
                Log.VERBOSE -> Log.v(area, message)
                Log.WARN -> Log.w(area, message)
            }
        }

        override fun onPushTokenRegistered() {}
        override fun onPushTokenRegistrationFailed(sinchError: SinchError) {}
        override fun onCredentialsRequired(clientRegistration: ClientRegistration) {}
        override fun onUserRegistered() {}
        override fun onUserRegistrationFailed(sinchError: SinchError) {}
    }

    private inner class SinchCallClientListener : CallClientListener {
        override fun onIncomingCall(callClient: CallClient, call: Call) {
            Log.d(TAG, "Incoming call")
            val intent = Intent(this@SinchServer, Video_Call::class.java) //Incomingcallactivity
            intent.putExtra(CALL_ID, call.callId)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            this@SinchServer.startActivity(intent)
        }
    }

    companion object {
        private const val APP_KEY = "your app key here"
        private const val APP_SECRET = "your app secret here"
        private const val ENVIRONMENT = "clientapi.sinch.com"
        const val CALL_ID = "CALL_ID"
        val TAG = SinchServer::class.java.simpleName
    }
}