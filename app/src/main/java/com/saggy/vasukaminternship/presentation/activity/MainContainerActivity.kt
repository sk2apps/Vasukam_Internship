package com.saggy.vasukaminternship.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.databinding.ActivityMainContainerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainContainerActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainContainerBinding
    private val binding
        get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this,R.layout.activity_main_container)

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
    }

}