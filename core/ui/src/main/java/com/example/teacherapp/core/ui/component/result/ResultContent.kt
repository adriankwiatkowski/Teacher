package com.example.teacherapp.core.ui.component.result

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.ui.theme.TeacherAppTheme

@Composable
fun <T> ResultContent(
    result: Result<T>,
    modifier: Modifier = Modifier,
    isDeleted: Boolean = false,
    deletedMessage: String = "Usunięto dane",
    loadingContent: @Composable (() -> Unit)? = null,
    errorContent: @Composable (() -> Unit)? = null,
    content: @Composable (T) -> Unit,
) {
    Box(modifier = modifier) {
        if (isDeleted) {
            DeletedScreen(label = deletedMessage)
            return
        }

        when (result) {
            Result.Loading -> if (loadingContent != null) loadingContent() else LoadingScreen()

            is Result.Error -> if (errorContent != null) {
                errorContent()
            } else {
                ErrorScreenWithDebugInfo(exception = result.exception)
            }

            is Result.Success -> content(result.data)
        }
    }
}

@Composable
private fun ErrorScreenWithDebugInfo(
    exception: Throwable?,
    modifier: Modifier = Modifier,
) {
    // TODO: Add debug info if in inspector mode.
//    val label: @Composable () -> Unit = if (BuildConfig.DEBUG) {
//        { Text(exception.toString()) }
//    } else {
//        {}
//    }
    val label: @Composable () -> Unit = {}

    ErrorScreen(modifier = modifier, label = label)
}

@Preview
@Composable
private fun ResultContentSuccessPreview() {
    TeacherAppTheme {
        Surface {
            ResultContent(result = Result.Success(Unit)) {
                Box(Modifier.fillMaxSize()) {
                    Text(text = "Success")
                }
            }
        }
    }
}

@Preview
@Composable
private fun ResultContentLoadingPreview() {
    TeacherAppTheme {
        Surface {
            ResultContent(result = Result.Loading) {}
        }
    }
}

@Preview
@Composable
private fun ResultContentErrorPreview() {
    TeacherAppTheme {
        Surface {
            ResultContent(result = Result.Error(NullPointerException())) {}
        }
    }
}