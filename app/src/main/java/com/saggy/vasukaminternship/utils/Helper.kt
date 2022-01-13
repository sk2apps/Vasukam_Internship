package com.saggy.vasukaminternship.utils

import android.text.TextUtils
import android.util.Patterns

object Helper {

    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}