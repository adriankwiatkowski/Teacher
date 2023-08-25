package com.example.teacherapp.core.auth

import androidx.fragment.app.FragmentActivity

interface Auth {
    fun authenticate(activity: FragmentActivity, listener: AuthListener): Boolean
}