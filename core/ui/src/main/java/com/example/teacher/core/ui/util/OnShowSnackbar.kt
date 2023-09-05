package com.example.teacher.core.ui.util

import androidx.annotation.StringRes

fun interface OnShowSnackbar {
    fun onShowSnackbar(@StringRes message: Int)
}