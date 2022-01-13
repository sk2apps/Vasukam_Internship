package com.saggy.vasukaminternship

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import androidx.appcompat.widget.AppCompatSeekBar
import eu.okatrych.rightsheet.RightSheetBehavior
import com.saggy.vasukaminternship.fragments.Right_Slider_Landscape
import android.media.MediaPlayer.OnPreparedListener
import android.media.MediaPlayer
import android.widget.SeekBar.OnSeekBarChangeListener
import android.content.pm.ActivityInfo
import android.util.TypedValue
import eu.okatrych.rightsheet.RightSheetBehavior.RightSheetCallback
import android.net.Uri
import android.os.Handler
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import java.lang.IllegalStateException

class Video_Activity : AppCompatActivity() {
    lateinit var dropDownButton: ImageButton
    lateinit var chatButton: ImageButton
    lateinit var headingLayout: RelativeLayout
    lateinit var frameLayout: FrameLayout
    lateinit var frame_video: RelativeLayout
    lateinit var fullscreen: ImageButton
    lateinit var exit_fullscreen: ImageButton
    lateinit var backButton: ImageButton
    lateinit var pause: ImageButton
    lateinit var forward: ImageButton
    lateinit var backward: ImageButton
    lateinit var info: ImageButton
    lateinit var currency_landscape: ImageButton
    lateinit var messenger_landscape: ImageButton
    lateinit var chat_enable_landscape: ImageButton
    lateinit var videoView: VideoView
    lateinit var view: View
    lateinit var live_text: TextView
    lateinit var viewer_text: TextView
    lateinit var eye_icon: ImageButton
    lateinit var red_live: ImageView
    lateinit var L1: LinearLayout
    lateinit var L2: LinearLayout
    lateinit var descriptionLinear: LinearLayout
    lateinit var headingDescription: TextView
    lateinit var contentDescription: TextView
    var close = true
    var chat = 0
    lateinit var spinner: ProgressBar
    lateinit var seekBar: AppCompatSeekBar
    lateinit var rightSheetBehavior: RightSheetBehavior<*>
    var is_icon_showing = false
    var is_video_pause = false
    var url = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

    //    ImageButton currencyButton;
    //    RecyclerView priceHorRecycleView;
    //    List<String> priceList = new ArrayList<>();
    //
    //    RecyclerView commentVerRecycleView;
    //    List<String> commentList = new ArrayList<>();
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_activity)
        chatButton = findViewById(R.id.chatButton)
        headingLayout = findViewById(R.id.secondLayout)
        L1 = findViewById(R.id.thirdLayout)
        L2 = findViewById(R.id.fourthLayout)
        descriptionLinear = findViewById(R.id.descriptionLayout)
        headingDescription = findViewById(R.id.headingDescription)
        contentDescription = findViewById(R.id.contentDescription)
        frameLayout = findViewById(R.id.frameLayoutVideo)
        frame_video = findViewById(R.id.frame_layout)
        exit_fullscreen = findViewById(R.id.fullscreen_exit)
        fullscreen = findViewById(R.id.fullscreen)
        view = findViewById(R.id.v1)
        dropDownButton = findViewById(R.id.dropDownButton)
        spinner = findViewById(R.id.progress_bar)
        videoView = findViewById(R.id.video_view)
        pause = findViewById(R.id.pause)
        forward = findViewById(R.id.forward)
        backButton = findViewById(R.id.backbtn)
        backward = findViewById(R.id.rewind)
        info = findViewById(R.id.settingbtn)
        currency_landscape = findViewById(R.id.currency_landscape)
        seekBar = findViewById(R.id.seekbar)
        eye_icon = findViewById(R.id.eye_image)
        red_live = findViewById(R.id.red_image)
        live_text = findViewById(R.id.live_text)
        viewer_text = findViewById(R.id.total_live)
        messenger_landscape = findViewById(R.id.messenger)
        chat_enable_landscape = findViewById(R.id.chat_enable)
        supportFragmentManager.beginTransaction()
            .replace(R.id.right_sheet, Right_Slider_Landscape()).commit()
        val sheet = findViewById<View>(R.id.right_sheet)
        rightSheetBehavior = RightSheetBehavior.from(sheet)
        loadFragment(Video_Suggestion_Fragment())
        videoView.setVideoURI(Uri.parse(url))
        videoView.setOnPreparedListener(OnPreparedListener { mediaPlayer: MediaPlayer? ->
            videoView.start()
            seekBar.setMax(videoView.getDuration())
            MyThread().start()
            spinner.setVisibility(View.GONE)
            DisplayIconButton(true)
        })
        currency_landscape.setOnClickListener(View.OnClickListener { view1: View? ->
            rightSheetBehavior.setPeekWidth(
                120
            )
        })
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val progress = seekBar.progress
                videoView.seekTo(progress)
            }
        })
        chatButton.setOnClickListener(View.OnClickListener { view1: View? ->
            chat = if (chat == 0) {
                chatButton.setImageDrawable(resources.getDrawable(R.drawable.chat_icon_figma))
                loadFragment(Comment_Fragment())
                1
            } else {
                chatButton.setImageDrawable(resources.getDrawable(R.drawable.chat_icon_outline_figma))
                loadFragment(Video_Suggestion_Fragment())
                0
            }
        })
        frame_video.setOnClickListener(View.OnClickListener { view1: View? -> DisplayIconButton(!is_icon_showing) })
        pause.setOnClickListener(View.OnClickListener { view1: View? ->
            is_video_pause = if (is_video_pause) {
                videoView.start()
                pause.setImageDrawable(resources.getDrawable(R.drawable.pause_figma))
                false
            } else {
                videoView.pause()
                pause.setImageDrawable(resources.getDrawable(R.drawable.play_figma))
                true
            }
        })
        forward.setOnClickListener(View.OnClickListener { view1: View? -> videoView.seekTo(videoView.getCurrentPosition() + 5 * 1000) })
        backward.setOnClickListener(View.OnClickListener { view1: View? ->
            videoView.seekTo(
                videoView.getCurrentPosition() - 5 * 1000
            )
        })
        backButton.setOnClickListener(View.OnClickListener { view1: View? -> onBackPressed() })
        fullscreen.setOnClickListener(View.OnClickListener { view1: View? ->
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            frame_video.setLayoutParams(
                RelativeLayout.LayoutParams(
                    WindowManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
            )
            DisplayIconButton(true)
        })
        exit_fullscreen.setOnClickListener(View.OnClickListener { view: View? ->
            rightSheetBehavior.setPeekWidth(0)
            rightSheetBehavior.setState(RightSheetBehavior.STATE_COLLAPSED)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val height = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                200f,
                resources.displayMetrics
            )
                .toInt()
            frame_video.setLayoutParams(
                RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        height
                    )
                )
            )
            DisplayIconButton(true)
        })

        //new Handler().postDelayed(() -> headingLayout.setVisibility(View.GONE), 4000);
        dropDownButton.setOnClickListener(View.OnClickListener { view1: View? ->
            if (close) {
                close = false
                dropDownButton.setRotation(180f)
                descriptionLinear.setVisibility(View.VISIBLE)
                L1.setVisibility(View.GONE)
                L2.setVisibility(View.GONE)
                view.setVisibility(View.GONE)
                frameLayout.setVisibility(View.GONE)
                contentDescription.setText(
                    """
    5 October 2021
    
    
    Everyday brings new opportunities so in esports. Here with the best teams will fight for their survivals in the tournament.
    
    
    Give a follow for more such awesome and a thrilling tournaments.
    
    
    Also follow:
    
    Discord:(Link)
    Instagram:(Link)
    Twitter:(Link)
    """.trimIndent()
                )
            } else {
                close = true
                dropDownButton.setRotation(0f)
                descriptionLinear.setVisibility(View.GONE)
                L1.setVisibility(View.VISIBLE)
                L2.setVisibility(View.VISIBLE)
                view.setVisibility(View.VISIBLE)
                frameLayout.setVisibility(View.VISIBLE)
            }
        })
        rightSheetBehavior.setRightSheetCallback(object : RightSheetCallback() {
            override fun onStateChanged(rightSheet: View, newState: Int) {
                if (newState == RightSheetBehavior.STATE_DRAGGING) {
                    if (rightSheetBehavior.getPeekWidth() == 0) {
                        rightSheetBehavior.setState(RightSheetBehavior.STATE_COLLAPSED)
                    }
                }
                //                switch (newState){
//                    case RightSheetBehavior.STATE_DRAGGING:
//                        if(open == 0)
//                            rightSheetBehavior.setState(RightSheetBehavior.STATE_COLLAPSED);
//                        break;
//                    case RightSheetBehavior.STATE_COLLAPSED:
//                        open = 0;
//                        break;
//                    case RightSheetBehavior.STATE_EXPANDED:
//                        break;
//                    case RightSheetBehavior.STATE_HALF_EXPANDED:
//                        break;
//                    case RightSheetBehavior.STATE_SETTLING:
//                        break;
//                    case RightSheetBehavior.STATE_HIDDEN:
//                        open = 0;
//                        break;
//                }
            }

            override fun onSlide(rightSheet: View, slideOffset: Float) {}
        })
    }

    private fun DisplayIconButton(b: Boolean) {
        if (b) {
            // we have to show icons
            pause!!.visibility = View.VISIBLE
            forward!!.visibility = View.VISIBLE
            backward!!.visibility = View.VISIBLE
            backButton!!.visibility = View.VISIBLE
            info!!.visibility = View.VISIBLE
            val orientation = resources.configuration.orientation
            seekBar!!.visibility = View.VISIBLE
            red_live!!.visibility = View.VISIBLE
            live_text!!.visibility = View.VISIBLE
            viewer_text!!.visibility = View.VISIBLE
            eye_icon!!.visibility = View.VISIBLE
            if (orientation == 2) {
                //landscape
                currency_landscape!!.visibility = View.VISIBLE
                messenger_landscape!!.visibility = View.VISIBLE
                chat_enable_landscape!!.visibility = View.VISIBLE
                exit_fullscreen!!.visibility = View.VISIBLE
                fullscreen!!.visibility = View.GONE
            } else if (orientation == 1) {
                // portrait
                exit_fullscreen!!.visibility = View.GONE
                fullscreen!!.visibility = View.VISIBLE
            }
            Handler().postDelayed({ DisplayIconButton(false) }, 3500)
        } else {
            //we have to remove icons
            if (!is_video_pause) {
                pause!!.visibility = View.GONE
                forward!!.visibility = View.GONE
                backward!!.visibility = View.GONE
                backButton!!.visibility = View.GONE
                info!!.visibility = View.GONE
                exit_fullscreen!!.visibility = View.GONE
                fullscreen!!.visibility = View.GONE
                currency_landscape!!.visibility = View.GONE
                chat_enable_landscape!!.visibility = View.GONE
                messenger_landscape!!.visibility = View.GONE
                seekBar!!.visibility = View.GONE
                red_live!!.visibility = View.GONE
                live_text!!.visibility = View.GONE
                viewer_text!!.visibility = View.GONE
                eye_icon!!.visibility = View.GONE
            }
        }
    }

    fun loadFragment(fragment: Fragment?) {
        //create a fragment manager
        val fragmentManager = supportFragmentManager
        //create Fragment transaction
        val fragmentTransaction = fragmentManager.beginTransaction()
        //replace framelayout with new fragment
        fragmentTransaction.replace(R.id.frameLayoutVideo, fragment!!)
        //save changes
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        val orientation = resources.configuration.orientation
        if (orientation == 2) {
            //landscape
            rightSheetBehavior!!.peekWidth = 0
            rightSheetBehavior!!.state = RightSheetBehavior.STATE_COLLAPSED
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            val height = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                200f,
                resources.displayMetrics
            )
                .toInt()
            frame_video!!.layoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    height
                )
            )
            DisplayIconButton(true)
        } else if (orientation == 1) {
            // portrait
            super.onBackPressed()
        }
    }

    //start a method of thread
    internal inner class MyThread : Thread() {
        override fun run() {
            super.run()
            val runtime = videoView!!.duration
            var currentPosition = 0
            var adv = 0
            while ((if (runtime - currentPosition.also { adv = it } < 500) adv else 500.also {
                    adv = it
                }) > 2) {
                try {
                    currentPosition = videoView!!.currentPosition
                    if (seekBar != null) {
                        seekBar!!.progress = currentPosition
                    }
                    sleep(adv.toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } catch (e: IllegalStateException) {
                    seekBar!!.progress = runtime
                    break
                }
            }
            //            while (seekBar.getProgress() <= seekBar.getMax()) {
//                seekBar.setProgress(videoView.getCurrentPosition());
//            }
        }
    }
}