package com.example.wordnotebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wordnotebook.ui.WordNotebookMainScreen
import com.example.wordnotebook.ui.WordNotebookViewModel

private val LightColors = lightColorScheme()

private val DarkColors = darkColorScheme(
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF2A2A2A),
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFFB0B0B0),
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF003258),
    outline = Color(0xFF444444)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: WordNotebookViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            MaterialTheme(
                colorScheme = if (uiState.isDarkMode) DarkColors else LightColors
            ) {
                WordNotebookMainScreen(viewModel = viewModel)
            }
        }
    }
}
