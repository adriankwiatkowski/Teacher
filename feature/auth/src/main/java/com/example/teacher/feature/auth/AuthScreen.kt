package com.example.teacher.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing

@Composable
internal fun AuthScreen(
    authenticate: () -> Unit,
    isDeviceSecure: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "Dane wraźliwe", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

            Text(
                text = "Przechowywanie danych wraźliwych wymaga dodatkowego zabezpieczenia. Do korzystania z aplikacji musisz zalogować się na urządzeniu",
                style = MaterialTheme.typography.bodyLarge,
            )

            if (!isDeviceSecure) {
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                Text(
                    text = "Nie można korzystać z aplikacji, ponieważ urządzenie nie jest zabezpieczone. Urządzenie musi posiadać PIN, albo inną formę zabezpieczenia",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
            TeacherButton(modifier = Modifier.fillMaxWidth(), onClick = authenticate) {
                Text(text = "Odblokuj aplikację")
            }
        }
    }
}

@Preview
@Composable
private fun AuthScreenNotSecurePreview() {
    TeacherTheme {
        Surface {
            AuthScreen(
                authenticate = {},
                isDeviceSecure = false,
            )
        }
    }
}

@Preview
@Composable
private fun AuthScreenPreview() {
    TeacherTheme {
        Surface {
            AuthScreen(
                authenticate = {},
                isDeviceSecure = true,
            )
        }
    }
}