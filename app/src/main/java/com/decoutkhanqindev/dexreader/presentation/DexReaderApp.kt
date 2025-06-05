package com.decoutkhanqindev.dexreader.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.decoutkhanqindev.dexreader.presentation.navigation.NavGraph

@Composable
fun DexReaderApp() {
  val navHostController = rememberNavController()

  NavGraph(
    navHostController = navHostController,
    modifier = Modifier.fillMaxSize()
  )
}