package com.example.teacher.feature.schoolclass.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.feature.schoolclass.R

@Composable
internal fun StudentItem(
    name: String,
    surname: String,
    email: String?,
    phone: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        leadingContent = {
            val icon = TeacherIcons.person()
            Icon(
                imageVector = icon.icon,
                contentDescription = null,
            )
        },
        headlineContent = { Text("$name $surname") },
        supportingContent = if (email != null || phone != null) {
            {
                Column {
                    if (email != null) {
                        Text(text = stringResource(R.string.school_class_email, email))
                    }

                    if (phone != null) {
                        Text(text = stringResource(R.string.school_class_phone, phone))
                    }
                }
            }
        } else {
            null
        }
    )
}

@Preview
@Composable
private fun StudentItemPreview() {
    TeacherTheme {
        Surface {
            StudentItem(
                name = "Jan",
                surname = "Kowalski",
                email = "jan.kowalski@domain.com",
                phone = "123456789",
                onClick = {},
            )
        }
    }
}