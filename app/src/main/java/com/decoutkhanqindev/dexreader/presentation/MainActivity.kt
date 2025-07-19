package com.decoutkhanqindev.dexreader.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      DexReaderTheme {
        DexReaderApp(
          modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
        )
      }
    }
  }
}