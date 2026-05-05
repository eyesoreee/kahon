package com.example.kahon

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kahon.core.navigation.KahonNavHost
import com.example.kahon.core.ui.theme.KahonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate intent: ${intent?.data}")
        enableEdgeToEdge()
        setContent {
            KahonTheme {
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