package com.example.kreedaperana.utils

import android.util.Patterns

object Validator {
    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isStrongPassword(password: String): Boolean {
        return password.length >= 6
    }
}
