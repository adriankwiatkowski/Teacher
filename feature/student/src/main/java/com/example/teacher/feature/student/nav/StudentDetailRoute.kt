package com.example.teacher.feature.student.nav

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.teacher.core.model.data.Student
import com.example.teacher.feature.student.R
import com.example.teacher.feature.student.StudentDetailScreen

@Composable
internal fun StudentDetailRoute(
    snackbarHostState: SnackbarHostState,
    student: Student,
) {
    val context = LocalContext.current

    StudentDetailScreen(
        snackbarHostState = snackbarHostState,
        student = student,
        onEmailClick = {
            student.email.ifNotEmpty { email -> startEmailIntent(context, email) }
        },
        onPhoneClick = {
            student.phone.ifNotEmpty { phone -> startPhoneCallIntent(context, phone) }
        },
    )
}

private fun startEmailIntent(context: Context, email: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
    }
    startIntent(context, intent, context.getString(R.string.student_send_email))
}

private fun startPhoneCallIntent(context: Context, phone: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phone")
    }
    startIntent(context, intent, context.getString(R.string.student_phone_call))
}

private fun startIntent(context: Context, intent: Intent, title: String) {
    context.startActivity(Intent.createChooser(intent, title))
}

private fun String?.ifNotEmpty(block: (String) -> Unit) {
    if (!this.isNullOrEmpty()) {
        block(this)
    }
}