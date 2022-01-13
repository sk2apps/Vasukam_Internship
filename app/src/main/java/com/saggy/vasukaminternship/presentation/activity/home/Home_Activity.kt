package com.saggy.vasukaminternship.presentation.activity.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.databinding.ActivityHomeBinding
import com.saggy.vasukaminternship.databinding.AddBottonSheetBinding
import com.saggy.vasukaminternship.fragments.Live_Nav_Fragment
import com.saggy.vasukaminternship.presentation.activity.post.ImagePostActivity
import com.saggy.vasukaminternship.presentation.activity.post.TextPostActivity
import com.saggy.vasukaminternship.presentation.fragments.homeFragment.Home_Fragment
import com.saggy.vasukaminternship.utils.displayToast


class Home_Activity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    val binding: ActivityHomeBinding
        get() = _binding!!


    private lateinit var sheetBinding: AddBottonSheetBinding

    lateinit var bottomSheetDialog: BottomSheetDialog
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        sheetBinding = AddBottonSheetBinding.bind(
            LayoutInflater.from(this).inflate(R.layout.add_botton_sheet, null)
        )


        binding.navbar.setOnNavigationItemSelectedListener(navlistner)
        binding.navbar.itemIconTintList = null
        loadFragment(Home_Fragment())
        bottomSheetDialog = BottomSheetDialog(
            this, R.style.BottomSheetDialogTheme
        )

    }

    private val navlistner =
        BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> {
                    loadFragment(Home_Fragment())
                }
                R.id.live -> {
                    loadFragment(Live_Nav_Fragment())
                }
                R.id.add -> showBottomSheet()
                R.id.lightning -> {
                    loadFragment(Home_Fragment())
                }
                R.id.store -> {
                    loadFragment(Home_Fragment())
                }
            }

            true
        }

    fun loadFragment(fragment: Fragment?) {

        val fragmentManager = supportFragmentManager
        //create Fragment transaction
        val fragmentTransaction = fragmentManager.beginTransaction()
        //replace frame layout with new fragment
        fragmentTransaction.replace(binding.navHostFragment.id, fragment!!)
        //save changes
        fragmentTransaction.commit()
    }

    private fun showBottomSheet() {

        val bottomSheetView = LayoutInflater.from(applicationContext).inflate(
            R.layout.add_botton_sheet,
            findViewById<ConstraintLayout>(R.id.addBottomSheetCreatePost)
        )


        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

//        sheetBinding.createTextPost.setOnClickListener {
//            startActivity(Intent(this,TextPostActivity::class.java))
//        }

        bottomSheetDialog.findViewById<ImageView>(R.id.textPostIcon)!!.setOnClickListener {
            bottomSheetDialog.dismiss()
            startActivity(Intent(this, TextPostActivity::class.java))
        }
        bottomSheetDialog.findViewById<ImageView>(R.id.imagePostIcon)!!.setOnClickListener {
            bottomSheetDialog.dismiss()
            startActivity(Intent(this, ImagePostActivity::class.java))
        }

    }


    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        this.displayToast("Please click BACK again to exit")

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

}