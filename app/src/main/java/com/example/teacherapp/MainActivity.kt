package com.example.teacherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teacherapp.data.models.FabAction
import com.example.teacherapp.ui.screens.MainScreen
import com.example.teacherapp.ui.screens.MainScreenViewModel
import com.example.teacherapp.ui.theme.TeacherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeacherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val viewModel: MainScreenViewModel = hiltViewModel()

                    MainScreen(
                        title = viewModel.title,
                        setTitle = { title -> viewModel.title = title },
                        menuItems = viewModel.actionMenuItems,
                        addActionMenuItems = viewModel::addActionMenuItems,
                        removeActionMenuItems = viewModel::removeActionMenuItems,
                        fabAction = viewModel.fabAction,
                        addFabAction = viewModel::addFabAction,
                        removeFabAction = viewModel::removeFabAction,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    TeacherAppTheme {
        MainScreen(
            title = "",
            setTitle = {},
            menuItems = listOf(),
            addActionMenuItems = {},
            removeActionMenuItems = {},
            fabAction = FabAction(
                onClick = {},
                imageVector = Icons.Default.AddCircle,
                contentDescription = null,
            ),
            addFabAction = {},
            removeFabAction = {},
        )
    }
}