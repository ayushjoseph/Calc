// File: app/src/main/java/com/example/calc/ui/theme/Type.kt
package com.example.calc.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Define typography using WhiteText from your Color.kt for the dark theme
val Typography = Typography(
    // Display for the current number/equation
    headlineSmall = TextStyle( // Was 24.sp in your code
        fontFamily = FontFamily.Default, // Or a custom font
        fontWeight = FontWeight.Light,
        fontSize = 30.sp, // Slightly larger for better readability
        color = WhiteText // This will be overridden by MaterialTheme.colorScheme.onSurface
    ),
    // Display for the result
    displayMedium = TextStyle( // Was 48.sp in your code
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 52.sp, // Slightly larger
        color = WhiteText // This will be overridden by MaterialTheme.colorScheme.onSurface
    ),
    // For button text
    titleLarge = TextStyle( // Was 24.sp for button text
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp, // Slightly larger button text
        color = WhiteText // This will be overridden by onPrimary, onSecondary etc.
    )
    /* Other default text styles to override if needed:
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    */
)
