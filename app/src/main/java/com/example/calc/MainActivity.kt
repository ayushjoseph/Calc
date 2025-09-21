// File: app/src/main/java/com/example/calc/MainActivity.kt
package com.example.calc // Corrected package

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge // Good for modern UI
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
// Remove Brush import if you switch to solid theme colors
// import androidx.compose.ui.graphics.Brush
// import androidx.compose.ui.graphics.Color // Remove if all colors come from theme
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
// Remove sp import if all font sizes come from theme typography
// import androidx.compose.ui.unit.sp
import com.example.calc.ui.theme.MyApplicationTheme // Corrected import
import java.util.Locale
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Call this before setContent for proper edge-to-edge
        setContent {
            MyApplicationTheme(darkTheme = true) { // Explicitly set darkTheme, or let it use system
                CalculatorScreen()
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var currentInput by remember { mutableStateOf("") } // Renamed for clarity
    var resultDisplay by remember { mutableStateOf("0") } // Renamed for clarity
    var currentOperator by remember { mutableStateOf<String?>(null) }
    var previousInput by remember { mutableStateOf<String?>(null) } // Renamed for clarity

    fun performCalculation() {
        val num1 = previousInput?.toFloatOrNull()
        val num2 = currentInput.toFloatOrNull()

        if (num1 != null && num2 != null && currentOperator != null) {
            val calcResult = when (currentOperator) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "x" -> num1 * num2
                "÷" -> if (num2 != 0f) num1 / num2 else Float.NaN // Handle division by zero
                else -> num2 // Should not happen
            }
            resultDisplay = if (calcResult.isNaN()) {
                "Error"
            } else {
                if (calcResult % 1 == 0f) {
                    calcResult.toInt().toString()
                } else {
                    // Use Locale.US for consistent decimal formatting (period as separator)
                    String.format(Locale.US, "%.2f", calcResult)
                }
            }
            currentInput = resultDisplay // For chaining calculations, result becomes new input
            previousInput = null
            currentOperator = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 380.dp) // Slightly adjusted max width
                .padding(16.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp), // Inner padding
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Display Texts (currentInput and resultDisplay)
            Text(
                text = currentInput.ifEmpty { previousInput ?: "0" } + (currentOperator ?: ""),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                textAlign = TextAlign.End,
                maxLines = 2
            )

            Text(
                text = "= $resultDisplay",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis // Handle long results
            )

            CalculatorButtons(
                // ... (your existing lambdas) ...
                onNumberClick = { num ->
                    if (resultDisplay != "0" && previousInput == null && currentOperator == null) { // After an equals, start new number
                        currentInput = num
                        resultDisplay = "0" // Clear previous result from display
                    } else {
                        currentInput += num
                    }
                },
                onOperatorClick = { op ->
                    if (currentInput.isNotEmpty()) {
                        if (previousInput != null && currentOperator != null) {
                            performCalculation() // Calculate previous operation first
                            previousInput = resultDisplay // Use the result of the previous calculation
                        } else {
                            previousInput = currentInput
                        }
                        currentOperator = op
                        currentInput = "" // Clear current input for the next number
                    } else if (previousInput != null) { // Allow changing operator if currentInput is empty but we have a previousInput
                        currentOperator = op
                        // resultDisplay remains the same (previousInput)
                    }
                },
                onEqualsClick = {
                    performCalculation()
                    // currentInput = resultDisplay // Keep resultDisplay as the source of truth for display
                    currentOperator = null
                    previousInput = null // Reset for new calculation start
                },
                onClearClick = {
                    currentInput = ""
                    resultDisplay = "0"
                    currentOperator = null
                    previousInput = null
                },
                onBackspaceClick = {
                    if (currentInput.isNotEmpty()) {
                        currentInput = currentInput.dropLast(1)
                    } else if (currentOperator != null) { // If current input is empty, clear operator
                        currentOperator = null
                        currentInput = previousInput ?: "" // Restore previous input
                        previousInput = null
                    } else if (previousInput != null) { // If no operator, clear previous input
                        currentInput = previousInput?.dropLast(1) ?: ""
                        if(currentInput.isEmpty()) previousInput = null
                    }
                },
                onDecimalClick = {
                    if ("." !in currentInput && currentInput.isNotEmpty()) {
                        currentInput += "."
                    } else if (currentInput.isEmpty()) {
                        currentInput = "0."
                    }
                }
            )
        }
    }
}

@Composable
fun CalculatorButtons(
    onNumberClick: (String) -> Unit,
    onOperatorClick: (String) -> Unit,
    onEqualsClick: () -> Unit,
    onClearClick: () -> Unit,
    onBackspaceClick: () -> Unit,
    onDecimalClick: () -> Unit
) {
    val buttonSpacing = 8.dp
    val baseButtonAspectRatio = 1.2f // Define the aspect ratio value here
    val baseButtonModifier = Modifier.aspectRatio(baseButtonAspectRatio)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(buttonSpacing)
    ) {
        // ... (Row 1, Row 2, Row 3 are the same) ...
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            CalcButton(text = "C", modifier = baseButtonModifier.weight(1f), onClick = onClearClick, backgroundColor = MaterialTheme.colorScheme.primaryContainer, textColor = MaterialTheme.colorScheme.onPrimaryContainer)
            CalcButton(text = "←", modifier = baseButtonModifier.weight(1f), onClick = onBackspaceClick, backgroundColor = MaterialTheme.colorScheme.primaryContainer, textColor = MaterialTheme.colorScheme.onPrimaryContainer)
            CalcButton(text = "÷", modifier = baseButtonModifier.weight(1f), onClick = { onOperatorClick("÷") }, backgroundColor = MaterialTheme.colorScheme.primary, textColor = MaterialTheme.colorScheme.onPrimary)
            CalcButton(text = "x", modifier = baseButtonModifier.weight(1f), onClick = { onOperatorClick("x") }, backgroundColor = MaterialTheme.colorScheme.primary, textColor = MaterialTheme.colorScheme.onPrimary)
        }
        // Row 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            CalcButton(text = "7", modifier = baseButtonModifier.weight(1f), onClick = { onNumberClick("7") })
            CalcButton(text = "8", modifier = baseButtonModifier.weight(1f), onClick = { onNumberClick("8") })
            CalcButton(text = "9", modifier = baseButtonModifier.weight(1f), onClick = { onNumberClick("9") })
            CalcButton(text = "-", modifier = baseButtonModifier.weight(1f), onClick = { onOperatorClick("-") }, backgroundColor = MaterialTheme.colorScheme.primary, textColor = MaterialTheme.colorScheme.onPrimary)
        }
        // Row 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            CalcButton(text = "4", modifier = baseButtonModifier.weight(1f), onClick = { onNumberClick("4") })
            CalcButton(text = "5", modifier = baseButtonModifier.weight(1f), onClick = { onNumberClick("5") })
            CalcButton(text = "6", modifier = baseButtonModifier.weight(1f), onClick = { onNumberClick("6") })
            CalcButton(text = "+", modifier = baseButtonModifier.weight(1f), onClick = { onOperatorClick("+") }, backgroundColor = MaterialTheme.colorScheme.primary, textColor = MaterialTheme.colorScheme.onPrimary)
        }


        // Row 4 (1, 2, 3, =)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
        ) {
            Column(modifier = Modifier.weight(3f), verticalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalcButton(text = "1", modifier = baseButtonModifier.weight(1f), onClick = { onNumberClick("1") })
                    CalcButton(text = "2", modifier = baseButtonModifier.weight(1f), onClick = { onNumberClick("2") })
                    CalcButton(text = "3", modifier = baseButtonModifier.weight(1f), onClick = { onNumberClick("3") })
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(buttonSpacing)) {
                    CalcButton(text = "0", modifier = baseButtonModifier.weight(2.08f), onClick = { onNumberClick("0") })
                    CalcButton(text = ".", modifier = baseButtonModifier.weight(1f), onClick = onDecimalClick)
                }
            }
            CalcButton(
                text = "=",
                modifier = Modifier
                    .weight(1f) // Takes 1 part of the main row's width
                    // Directly use the known baseButtonAspectRatio
                    .aspectRatio(baseButtonAspectRatio / 2.05f) // Make it roughly double height
                    .fillMaxHeight(), // Crucial for taller button in a Row
                onClick = onEqualsClick,
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                textColor = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}

// REMOVE THE extractAspectRatioOrDefault function
// fun Modifier.extractAspectRatioOrDefault(default: Float = 1f): Float { ... }



@Composable
fun CalcButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    backgroundColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.secondary,
    textColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSecondary
) {
    Button(
        onClick = onClick,
        modifier = modifier, // Removed .fillMaxHeight() from here
        shape = RoundedCornerShape(16.dp), // Slightly smaller corner radius for a tighter look
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        contentPadding = PaddingValues(0.dp) // Use this to control inner padding if needed
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis // Ensure text fits
        )
    }
}



@Preview(showBackground = true, device = "spec:width=360dp,height=640dp,dpi=480")
@Composable
fun CalculatorPreview() {
    MyApplicationTheme(darkTheme = true) {
        CalculatorScreen()
    }
}
