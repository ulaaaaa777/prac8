package com.example.kotlinlab11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.kotlinlab11.task2.NetworkScreen
import com.example.kotlinlab11.task3.CameraScreen
import com.example.kotlinlab11.task4.OverlayScreen
import com.example.kotlinlab11.task5.LocationScreen
import com.example.kotlinlab11.task6.NotificationScreen
import com.example.kotlinlab11.ui.theme.KotlinLab11Theme

enum class LabTab(val title: String) {
    NETWORK("Network (T2)"),
    CAMERA("Camera (T3)"),
    OVERLAY("Overlay (T4)"),
    LOCATION("Location (T5)"),
    NOTIFICATIONS("Notify (T6)")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinLab11Theme {
                MainAppScreen()
            }
        }
    }
}

@Composable
fun MainAppScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = LabTab.entries

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        label = { Text(text = tab.title) },
                        icon = { Text(text = "${index + 2}") }
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        when (tabs[selectedTab]) {
            LabTab.NETWORK -> NetworkScreen(modifier = modifier)
            LabTab.CAMERA -> CameraScreen(modifier = modifier)
            LabTab.OVERLAY -> OverlayScreen(modifier = modifier)
            LabTab.LOCATION -> LocationScreen(modifier = modifier)
            LabTab.NOTIFICATIONS -> NotificationScreen(modifier = modifier)
        }
    }
}
