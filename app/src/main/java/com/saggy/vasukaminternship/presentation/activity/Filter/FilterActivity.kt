package com.saggy.vasukaminternship.presentation.activity.Filter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import com.saggy.vasukaminternship.R

class FilterActivity : AppCompatActivity() {

    lateinit var backButtonFilter: AppCompatImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        backButtonFilter = findViewById<AppCompatImageView>(R.id.backButtonFilter)

        backButtonFilter.setOnClickListener{
            onBackPressed()
        }
    }
}