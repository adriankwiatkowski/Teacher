package com.example.teacher.core.auth

import android.content.Context
import androidx.fragment.app.FragmentActivity

interface Auth {

    fun canAuthenticate(context: Context): Boolean

    fun authenticate(activity: FragmentActivity, listener: AuthListener): Boolean
}