package com.saggy.vasukaminternship.utils

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}