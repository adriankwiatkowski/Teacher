package com.example.teacher.core.auth

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import javax.inject.Inject

internal class AuthImpl @Inject constructor() : Auth {

    override fun authenticate(activity: FragmentActivity, listener: AuthListener): Boolean {
        if (!canAuthenticate(context = activity, launchBiometricSettings = true)) {
            return false
        }

        val promptInfo =
            setBiometricPromptInfo(title = activity.getString(R.string.identity_confirmation))
        val biometricPrompt = initBiometricPrompt(activity, listener)
        biometricPrompt.authenticate(promptInfo)

        return true
    }

    override fun canAuthenticate(context: Context): Boolean =
        canAuthenticate(context = context, launchBiometricSettings = false)

    private fun setBiometricPromptInfo(title: String): BiometricPrompt.PromptInfo {
        val builder = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setAllowedAuthenticators(authenticators)

        return builder.build()
    }

    private fun initBiometricPrompt(
        activity: FragmentActivity,
        listener: AuthListener
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.w(TAG, "$errorCode $errString")
                listener.onError()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.w(TAG, "Authentication failed for an unknown reason")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.w(TAG, "$result")
                listener.onSuccess()
            }
        }

        return BiometricPrompt(activity, executor, callback)
    }

    private fun canAuthenticate(context: Context, launchBiometricSettings: Boolean): Boolean {
        val biometricManager = BiometricManager.from(context)
        val result = biometricManager.canAuthenticate(authenticators)

        if (result == BiometricManager.BIOMETRIC_SUCCESS) {
            return true
        }

        if (result == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
            // Prompts the user to create credentials that your app accepts.
            if (launchBiometricSettings) {
                lunchBiometricSettings(context)
            }
        }

        return false
    }

    private fun lunchBiometricSettings(context: Context) {
        val enrollIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, authenticators)
            }
        } else {
            Intent(Settings.ACTION_SECURITY_SETTINGS)
        }
        ActivityCompat.startActivity(context, enrollIntent, null)
    }
}

private const val TAG = "Auth"

private const val authenticators: Int = BiometricManager.Authenticators.BIOMETRIC_WEAK or
        BiometricManager.Authenticators.DEVICE_CREDENTIAL