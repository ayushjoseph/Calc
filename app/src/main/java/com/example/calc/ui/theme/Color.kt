// File: app/src/main/java/com/example/calc/ui/theme/Color.kt
package com.example.calc.ui.theme

import androidx.compose.ui.graphics.Color

// Calculator-specific colors that are actively used by your theme
val DarkPurpleBackground = Color(0xFF2D263B)
val MediumPurpleBackground = Color(0xFF443D5C) // Used for surfaces or gradients
val LightPurpleOperatorButton = Color(0xFF9370DB) // For C, operators
val DarkNumberButton = Color(0xFF4B425F)        // For numbers, decimal
val WhiteText = Color.White                      // For text on buttons and display
val OrangeAccent = Color(0xFFFFA500)             // For the '=' button or other accents

// These are likely unused if your theme is customized as above.
// If you confirm they are not in DarkColorScheme or LightColorScheme, you can remove them.
// val Purple80 = Color(0xFFD0BCFF)
// val PurpleGrey80 = Color(0xFFCCC2DC)
// val Pink80 = Color(0xFFEFB8C8)

// val Purple40 = Color(0xFF6650a4)
// val PurpleGrey40 = Color(0xFF625b71)
// val Pink40 = Color(0xFF7D5260)
