package com.example.teacherapp

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.auth.Auth
import com.example.teacherapp.core.auth.AuthListener
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.settings.SettingsRepository
import com.example.teacherapp.core.model.data.SettingsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val auth: Auth,
    repository: SettingsRepository,
) : ViewModel(), AuthListener {

    val settings: StateFlow<Result<SettingsData>> = repository.settings
        .stateIn(initialValue = Result.Loading)

    private val _authState = MutableStateFlow(AuthState())
    val authState = _authState.asStateFlow()

    fun authenticate(activity: FragmentActivity) {
        _authState.update { authState -> authState.copy(isAuthenticated = false) }
        val canAuthenticate = auth.authenticate(activity = activity, listener = this)
        _authState.update { authState -> authState.copy(isDeviceSecured = canAuthenticate) }
    }

    override fun onSuccess() {
        _authState.update { authState -> authState.copy(isAuthenticated = true) }
    }

    override fun onError() {
        _authState.update { authState -> authState.copy(isAuthenticated = false) }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )
}

data class AuthState(
    val isAuthenticated: Boolean = false,
    val isDeviceSecured: Boolean = false,
)