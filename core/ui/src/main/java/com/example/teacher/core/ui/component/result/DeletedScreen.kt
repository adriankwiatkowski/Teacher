package com.example.teacher.core.ui.component.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.R
import com.example.teacher.core.ui.theme.TeacherTheme

@Composable
fun DeletedScreen(
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.data_deleted),
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = label, style = MaterialTheme.typography.displayMedium)
    }
}

@Preview
@Composable
private fun DeletedScreenPreview() {
    TeacherTheme {
        Surface {
            DeletedScreen()
        }
    }
}