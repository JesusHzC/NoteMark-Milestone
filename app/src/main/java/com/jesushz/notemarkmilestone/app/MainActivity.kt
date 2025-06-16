package com.jesushz.notemarkmilestone.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.jesushz.notemarkmilestone.app.navigation.NavigationRoot
import com.jesushz.notemarkmilestone.core.designsystem.theme.NoteMarkMilestoneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = android.graphics.Color.TRANSPARENT
            ),
        )
        setContent {
            NoteMarkMilestoneTheme {
                val navController = rememberNavController()
                NavigationRoot(
                    navController = navController
                )
            }
        }
    }
}
