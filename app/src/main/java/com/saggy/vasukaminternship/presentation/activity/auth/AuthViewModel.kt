package com.saggy.vasukaminternship.presentation.activity.auth

import androidx.lifecycle.ViewModel
import com.saggy.vasukaminternship.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


import androidx.lifecycle.MutableLiveData

import com.google.firebase.auth.FirebaseUser
import com.saggy.vasukaminternship.data.model.user.ProfileInfo


@HiltViewModel
class AuthViewModel
@Inject
constructor(private val repository: FirebaseRepository)
    :ViewModel()
{


    private lateinit var userData: MutableLiveData<ProfileInfo>
    private lateinit var loggedStatus: MutableLiveData<Boolean>

    fun getUserData(): MutableLiveData<ProfileInfo> {
        return userData
    }

    fun getLoggedStatus(): MutableLiveData<Boolean> {
        return loggedStatus
    }

    fun AuthViewModel() {
        userData = repository.getFirebaseUserMutableLiveData()
        loggedStatus = repository.getUserLoggedMutableLiveData()
    }

    fun register(email: String?, pass: String?) {
        repository.register(email, pass)
    }

    fun signIn(email: String?, pass: String?) {
        repository.login(email, pass)
    }

    fun signOut() {
        repository.signOut()
    }
}