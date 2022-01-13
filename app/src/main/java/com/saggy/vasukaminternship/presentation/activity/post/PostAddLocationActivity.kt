package com.saggy.vasukaminternship.presentation.activity.post

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.databinding.ActivityPostAddLocationBinding


class PostAddLocationActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityPostAddLocationBinding
    val binding
        get() = _binding

    var postType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_post_add_location)

        postType = intent.getStringExtra("postType").toString()

        // Fetching API_KEY which we wrapped
        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        val value = ai.metaData["AIzaSyDfZWJysjwkKSajsSRPmssKCXyA1WGcgBo"]
        val apiKey = value.toString()

        // Initializing the Places API
        // with the help of our API_KEY
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        val autocompleteSupportFragment1 =
            supportFragmentManager.findFragmentById(R.id.placeSearch_addPostLoacationPage) as AutocompleteSupportFragment?

        autocompleteSupportFragment1!!.setPlaceFields(
            listOf(

                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.PHONE_NUMBER,
                Place.Field.LAT_LNG,
                Place.Field.OPENING_HOURS,
                Place.Field.RATING,
                Place.Field.USER_RATINGS_TOTAL

            )
        )

        // Display the fetched information after clicking on one of the options
        autocompleteSupportFragment1.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {


                // Information about the place
                val name = place.name
                val address = place.address
                val latlng = place.latLng
                val latitude = latlng?.latitude
                val longitude = latlng?.longitude

                val rating = place.rating
                val userRatings = place.userRatingsTotal

                Toast.makeText(applicationContext, name, Toast.LENGTH_SHORT).show()

            }

            override fun onError(status: Status) {
                Toast.makeText(applicationContext, "Some error occurred", Toast.LENGTH_SHORT).show()
            }
        })


        binding.doneBtn.setOnClickListener {

            if (postType == "TextPost") {
                startActivity(Intent(this, TextPostReviewActivity::class.java))
            } else if (postType == "ImagePost") {
                startActivity(Intent(this, ImagePostReviewActivity::class.java))
            }

        }

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

    }


}