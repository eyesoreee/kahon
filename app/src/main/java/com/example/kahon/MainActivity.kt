package com.example.kahon

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kahon.core.navigation.KahonNavHost
import com.example.kahon.core.ui.theme.KahonTheme
import com.example.kahon.core.util.AppTheme
import com.example.kahon.core.util.ThemePreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    @Inject
    lateinit var themePreferences: ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate intent: ${intent?.data}")
        enableEdgeToEdge()
        setContent {
            val appTheme by themePreferences.theme.collectAsState(initial = AppTheme.SYSTEM)
            KahonTheme(appTheme = appTheme) {
                navController = rememberNavController()
                KahonNavHost(navController = navController)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        if (::navController.isInitialized && intent.data?.scheme == "kahon") {
            navController.handleDeepLink(intent)
        }
    }
}