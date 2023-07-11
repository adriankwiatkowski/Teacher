package com.example.teacherapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// TODO: Add global scope to run mutable queries on database.

@HiltAndroidApp
class TeacherApp : Application()