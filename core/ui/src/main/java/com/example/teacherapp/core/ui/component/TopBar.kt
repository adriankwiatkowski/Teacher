package com.example.teacherapp.core.ui.component

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.core.ui.model.ActionItem
import com.example.teacherapp.core.ui.theme.TeacherAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherTopBar(
    title: String,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    menuItems: List<ActionItem> = emptyList(),
) {
//    AnimatedVisibility(
//        modifier = modifier,
//        visible = visible,
//        enter = fadeIn(),
//        exit = fadeOut(),
//    ) {
//    }
    if (!visible) {
        return
    }

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(title)
        },
        navigationIcon = {
            if (showNavigationIcon) {
                IconButton(onClick = onNavigationIconClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        },
        actions = {
            for (item in menuItems) {
                IconButton(onClick = item.onClick) {
                    Icon(
                        imageVector = item.imageVector,
                        contentDescription = item.contentDescription,
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TeacherTopBarPreview() {
    TeacherAppTheme {
        Surface {
            TeacherTopBar(
                title = "Title",
                showNavigationIcon = true,
                onNavigationIconClick = {},
                scrollBehavior = TeacherTopBarDefaults.default(),
            )
        }
    }
}

object TeacherTopBarDefaults {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun default(
        state: TopAppBarState = rememberTopAppBarState(),
        canScroll: () -> Boolean = { true }
    ): TopAppBarScrollBehavior = pinnedScrollBehavior(
        state = state,
        canScroll = canScroll,
    )

    @ExperimentalMaterial3Api
    @Composable
    fun pinnedScrollBehavior(
        state: TopAppBarState = rememberTopAppBarState(),
        canScroll: () -> Boolean = { true }
    ): TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        state = state,
        canScroll = canScroll,
    )

    @ExperimentalMaterial3Api
    @Composable
    fun enterAlwaysScrollBehavior(
        state: TopAppBarState = rememberTopAppBarState(),
        canScroll: () -> Boolean = { true },
        snapAnimationSpec: AnimationSpec<Float>? = spring(stiffness = Spring.StiffnessMediumLow),
        flingAnimationSpec: DecayAnimationSpec<Float>? = rememberSplineBasedDecay()
    ): TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = state,
        snapAnimationSpec = snapAnimationSpec,
        flingAnimationSpec = flingAnimationSpec,
        canScroll = canScroll
    )

    @ExperimentalMaterial3Api
    @Composable
    fun exitUntilCollapsedScrollBehavior(
        state: TopAppBarState = rememberTopAppBarState(),
        canScroll: () -> Boolean = { true },
        snapAnimationSpec: AnimationSpec<Float>? = spring(stiffness = Spring.StiffnessMediumLow),
        flingAnimationSpec: DecayAnimationSpec<Float>? = rememberSplineBasedDecay()
    ): TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = state,
        snapAnimationSpec = snapAnimationSpec,
        flingAnimationSpec = flingAnimationSpec,
        canScroll = canScroll
    )
}