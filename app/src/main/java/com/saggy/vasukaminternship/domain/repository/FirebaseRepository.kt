package com.saggy.vasukaminternship.domain.repository


import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.saggy.vasukaminternship.data.model.user.ProfileInfo
import javax.inject.Inject


class FirebaseRepository
@Inject
constructor()
{


    private lateinit var firebaseUserMutableLiveData: MutableLiveData<ProfileInfo>
    private lateinit var firebaseCurrentUser: MutableLiveData<FirebaseUser>
    private lateinit var userLoggedMutableLiveData: MutableLiveData<Boolean>
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getFirebaseUserMutableLiveData(): MutableLiveData<ProfileInfo> {
        return firebaseUserMutableLiveData
    }

    fun getUserLoggedMutableLiveData(): MutableLiveData<Boolean> {
        return userLoggedMutableLiveData
    }

//    fun AuthenticationRepository() {
//        firebaseUserMutableLiveData = MutableLiveData()
//        userLoggedMutableLiveData = MutableLiveData()
//        if (auth.currentUser != null) {
//            firebaseCurrentUser.postValue(auth.currentUser)
//        }
//    }

    fun register(email: String?, pass: String?) {
        auth.createUserWithEmailAndPassword(email!!, pass!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseCurrentUser.postValue(auth.currentUser)
            }
 //           else {
//                Toast.makeText(application, task.exception!!.message, Toast.LENGTH_SHORT).show()
//           }
        }
    }

    fun login(email: String?, pass: String?) {
        auth.signInWithEmailAndPassword(email!!, pass!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseCurrentUser.postValue(auth.currentUser)
            }
//            else {
//                Toast.makeText(application, task.exception!!.message, Toast.LENGTH_SHORT).show()
//            }
        }
    }

    fun signOut() {
        auth.signOut()
        userLoggedMutableLiveData.postValue(true)
    }

}