package com.saggy.vasukaminternship.utils


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.saggy.vasukaminternship.data.model.user.ProfileInfo


object FireBaseUtils {


    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
    val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun getUserDetails(userUid: String): ProfileInfo {

        var user = ProfileInfo()

        val db = FirebaseFirestore.getInstance()
        db.collection("Users")
            .document(userUid)
            .get()
            .addOnSuccessListener {
                val userT: ProfileInfo? = it.toObject(ProfileInfo::class.java)
                if (userT != null) {
                    user = userT
                }
            }

        return user
    }

}