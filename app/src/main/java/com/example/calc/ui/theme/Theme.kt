// File: app/src/main/java/com/example/calc/ui/theme/Theme.kt
package com.example.calc.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Using the color names directly from your Color.kt
private val AppDarkColorScheme = darkColorScheme(
    primary = LightPurpleOperatorButton,    // Main operator buttons
    secondary = DarkNumberButton,           // Number buttons
    tertiary = OrangeAccent,                // Equals button or other accents
    background = DarkPurpleBackground,      // Overall screen background
    surface = MediumPurpleBackground,       // Background of the calculator "card" or display area
    onPrimary = WhiteText,                  // Text on primary buttons
    onSecondary = WhiteText,                // Text on secondary buttons
    onTertiary = WhiteText,                 // Text on tertiary buttons
    onBackground = WhiteText,               // Text directly on the main background
    onSurface = WhiteText                   // Text on the surface (display text)
)

// Define a basic light theme if you ever want to support it
private val AppLightColorScheme = lightColorScheme(
    primary = LightPurpleOperatorButton, // Or choose different colors for light theme
    secondary = DarkNumberButton,
    tertiary = OrangeAccent,
    background = Color(0xFFF0F0F0), // Example light background
    surface = Color(0xFFFFFFFF),    // Example light surface
    onPrimary = WhiteText,
    onSecondary = WhiteText,
    onTertiary = WhiteText,
    onBackground = Color(0xFF1C1B1F), // Dark text on light background
    onSurface = Color(0xFF1C1B1F)     // Dark text on light surface
    // You'll need to define Color(0xFFF0F0F0) etc., in Color.kt if you use them here
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Forcing dark theme as per current design
    dynamicColor: Boolean = false, // Set to true to enable Material You on Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> AppDarkColorScheme
        else -> AppLightColorScheme // Placeholder if you ever want to switch
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.setDecorFitsSystemWindows(window, false) // Enable edge-to-edge

            val insetsController = WindowCompat.getInsetsController(window, view)
            // For dark theme, status bar icons should be light.
            // For light theme, status bar icons should be dark.
            insetsController.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Ensure Typography.kt is defined in this package
        content = content
    )
}
