package com.example.teacher.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.provider.TeacherIcons
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
            Text(
                text = stringResource(R.string.sensitive_data),
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

            Text(
                text = stringResource(R.string.sensitive_data_explanation),
                style = MaterialTheme.typography.bodyLarge,
            )

            if (!isDeviceSecure) {
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                Text(
                    text = stringResource(R.string.device_not_secure),
                    style = MaterialTheme.typography.bodyLarge,
                )

                val icon = TeacherIcons.warning()
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                Icon(imageVector = icon.icon, contentDescription = stringResource(icon.text))
            }

            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
            TeacherButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.unlock_app),
                onClick = authenticate,
            )
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