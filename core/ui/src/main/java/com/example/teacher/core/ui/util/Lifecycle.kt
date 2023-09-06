package com.example.teacher.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

// https://stackoverflow.com/a/66807899
@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun OnResume(onResume: () -> Unit) = OnLifecycleEvent(
    onEvent = { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            onResume()
        }
    }
)

@Composable
fun OnPause(onPause: () -> Unit) = OnLifecycleEvent(
    onEvent = { _, event ->
        if (event == Lifecycle.Event.ON_PAUSE) {
            onPause()
        }
    }
)