package com.example.cinemahub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cinemahub.navigation.RootAppNavigation
import com.example.cinemahub.ui.theme.CinemaHubTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManagerSingleton.init(this.applicationContext)
        setContent {
            CinemaHubTheme {
                RootAppNavigation()
            }
        }
    }
}
