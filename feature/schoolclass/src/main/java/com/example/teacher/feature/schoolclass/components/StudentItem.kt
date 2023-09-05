package com.example.teacher.feature.schoolclass.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.schoolclass.R

@Composable
internal fun StudentItem(
    name: String,
    surname: String,
    email: String?,
    phone: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val icon = TeacherIcons.person()
        Icon(
            imageVector = icon.icon,
            contentDescription = stringResource(icon.text),
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

        Column {
            Text(text = "$name $surname")

            if (email != null) {
                Text(text = stringResource(R.string.school_class_email, email))
            }

            if (phone != null) {
                Text(text = stringResource(R.string.school_class_phone, phone))
            }
        }
    }
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
            )
        }
    }
}