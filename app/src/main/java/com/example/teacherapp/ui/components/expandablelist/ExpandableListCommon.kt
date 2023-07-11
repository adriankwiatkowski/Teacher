package com.example.teacherapp.ui.components.expandablelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableLabel(
    label: String,
    toggleExpanded: () -> Unit,
    additionalIcon: @Composable (() -> Unit)?,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = toggleExpanded)
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .minimumInteractiveComponentSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Icon(
                icon,
                contentDescription = contentDescription,
            )
        }

        Text(text = label)

        Spacer(modifier = Modifier.weight(1f))

        if (additionalIcon != null) {
            additionalIcon()
        }
    }
}