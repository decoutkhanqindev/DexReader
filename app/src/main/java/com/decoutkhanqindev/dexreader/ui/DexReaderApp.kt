package com.decoutkhanqindev.dexreader.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.decoutkhanqindev.dexreader.ui.navigation.HomeDestination
import com.decoutkhanqindev.dexreader.ui.navigation.SearchDestination
import com.decoutkhanqindev.dexreader.ui.screens.home.HomeScreen
import com.decoutkhanqindev.dexreader.ui.screens.search.SearchScreen

@Composable
fun DexReaderApp() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = HomeDestination.route,
    modifier = Modifier.fillMaxSize()
  ) {
    composable(route = HomeDestination.route) {
      HomeScreen(
        onMenuClick = {},
        onSearchClick = { navController.navigate(SearchDestination.route) },
        onMangaClick = { },
        modifier = Modifier.fillMaxSize()
      )
    }
    composable(route = SearchDestination.route) {
      SearchScreen(
        onBackClick = { navController.navigateUp() },
        onMangaClick = { },
        modifier = Modifier.fillMaxSize()
      )
    }
  }
}