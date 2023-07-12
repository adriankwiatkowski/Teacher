package com.example.teacherapp.ui.components.student

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing

@Composable
fun StudentItem(
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
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

        Column {
            Text(text = "$name $surname")

            if (email != null) {
                Text(text = "Email: $email")
            }

            if (phone != null) {
                Text(text = "Telefon: $phone")
            }
        }
    }
}

@Preview
@Composable
private fun StudentItemPreview() {
    TeacherAppTheme {
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