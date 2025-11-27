package com.example.stushare.feature_contribution 

import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.core.os.LocaleListCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// Sửa các import bên dưới: thay com.stushare -> com.example.stushare
import com.example.stushare.feature_contribution.data.AppPreferences
import com.example.stushare.feature_contribution.navigation.AppNavigationGraph
import com.example.stushare.feature_contribution.navigation.Screen
import com.example.stushare.feature_contribution.ui.components.BottomNavBar
import com.example.stushare.feature_contribution.ui.theme.StuShareTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    // Interface cho Fragment gọi lại (để sửa lỗi onFabClicked ở UploadFragment)
    interface FabClickListener {
        fun onFabClicked()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = AppPreferences(applicationContext)
        val initialLanguage = runBlocking { prefs.languageCode.first() }
        applyLanguage(initialLanguage)

        setContent {
            val isDarkTheme by prefs.isDarkTheme.collectAsState(initial = false)
            val fontScale by prefs.fontScale.collectAsState(initial = 1.0f)
            val languageCode by prefs.languageCode.collectAsState(initial = initialLanguage)

            LaunchedEffect(languageCode) {
                applyLanguage(languageCode)
            }

            val currentDensity = LocalDensity.current
            val customDensity = Density(currentDensity.density, fontScale = fontScale)

            CompositionLocalProvider(LocalDensity provides customDensity) {
                StuShareTheme(darkTheme = isDarkTheme) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val showBottomBar = currentRoute in listOf(
                        Screen.Home.route, Screen.Search.route, Screen.Noti.route, Screen.Profile.route
                    )

                    Scaffold(
                        bottomBar = {
                            if (showBottomBar) BottomNavBar(navController = navController)
                        }
                    ) { innerPadding ->
                        AppNavigationGraph(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    private fun applyLanguage(languageCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = getSystemService(LocaleManager::class.java)
            val currentAppLocales = localeManager.applicationLocales
            if (currentAppLocales.isEmpty || currentAppLocales.get(0).language != languageCode) {
                localeManager.applicationLocales = LocaleList.forLanguageTags(languageCode)
            }
        } else {
            val appLocale = AppCompatDelegate.getApplicationLocales()
            if (appLocale.isEmpty || appLocale.get(0)?.language != languageCode) {
                val localeList = LocaleListCompat.forLanguageTags(languageCode)
                AppCompatDelegate.setApplicationLocales(localeList)
            }
        }
    }
}