package com.saggy.vasukaminternship.presentation.activity.profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.saggy.vasukaminternship.R
import com.saggy.vasukaminternship.data.model.user.ProfileInfo
import com.saggy.vasukaminternship.databinding.ActivityProfileEditBinding
import com.saggy.vasukaminternship.utils.displayToast
import java.io.IOException
import java.util.*

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityProfileEditBinding
    val binding
        get() = _binding

    private lateinit var firebaseAuth: FirebaseAuth

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    val db = FirebaseFirestore.getInstance()
    var currentUser: FirebaseUser? = null

    private var previousImageUrl: String = ""
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_edit)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        currentUser = firebaseAuth.currentUser!!

        if (currentUser != null) {
            loadMyInfo(currentUser!!)
        }

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.saveBtm.setOnClickListener {
            if (currentUser != null) {
                updateInfo()
            }
        }

        binding.updateProfileImage.setOnClickListener {
            if (currentUser != null) {
                launchGallery()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                binding.userProfilePic.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (filePath != null) {
                uploadImage()
            }
        }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun uploadImage() {
        if (filePath != null) {

            progressDialog = ProgressDialog(this)
            with(progressDialog) {
                setMessage("Uploading Post")
                setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            }
            progressDialog.isIndeterminate = true
            progressDialog.progress = 0
            progressDialog.show()

            val ref = storageReference?.child("UserImages/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

            uploadTask?.addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                    progressDialog.progress = 100
                    addUploadRecordToDb(imageUrl)
                    deletePreviousImage()
                }

            }?.addOnProgressListener {
                val progress = (100 * it.bytesTransferred) / it.totalByteCount
                progressDialog.progress = progress.toInt()
            }?.addOnFailureListener {
                this.displayToast(it.message)
            }
        }
    }

    private fun deletePreviousImage() {

        try {
            val ref: StorageReference = firebaseStore!!.getReferenceFromUrl(previousImageUrl)
            ref.delete().addOnSuccessListener {

            }.addOnFailureListener {

            }
        } catch (e: Exception) {

        }

    }

    private fun addUploadRecordToDb(uri: String) {

        db.collection("Users")
            .document(currentUser!!.uid)
            .update(
                "profile_pic", uri
            )
            .addOnSuccessListener {
                progressDialog.dismiss()
                this.displayToast("Profile Photo Updated!")

            }
            .addOnFailureListener {
                this.displayToast(it.message)
            }
    }

    private fun loadMyInfo(u: FirebaseUser) {

        db.collection("Users")
            .document(u.uid)
            .get()
            .addOnSuccessListener {
                val user: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                if (user != null) {
                    Glide.with(this).load(user.profile_pic).error(R.drawable.person_user)
                        .into(binding.userProfilePic)
                    binding.name.setText(user.name)
                    binding.username.setText(user.username)
                    binding.bio.setText(user.description)
                    previousImageUrl = user.profile_pic
                }
            }
            .addOnFailureListener {
                this.displayToast(it.message)
            }
    }


    private fun updateInfo() {

        val data = hashMapOf(
            "name" to binding.name.text.toString(),
            "username" to binding.username.text.toString(),
            "description" to binding.bio.text.toString()
        )

        db.collection("Users")
            .document(currentUser!!.uid)
            .update(
                data as Map<String, Any>
            )
            .addOnSuccessListener {
                this.displayToast("User Info Updated!")
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            .addOnFailureListener {
                this.displayToast(it.message)
                startActivity(Intent(this, ProfileActivity::class.java))
            }
    }
}