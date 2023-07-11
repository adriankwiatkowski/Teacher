package com.example.teacherapp.ui.components.student

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teacherapp.ui.theme.TeacherAppTheme

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

        Spacer(modifier = Modifier.width(8.dp))

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