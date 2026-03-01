package com.decoutkhanqindev.dexreader.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.decoutkhanqindev.dexreader.presentation.navigation.NavGraph

@Composable
fun DexReaderApp(
  viewModel: UserViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
) {
  val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsStateWithLifecycle()
  val currentUser by viewModel.userProfile.collectAsStateWithLifecycle()

  NavGraph(
    isUserLoggedIn = isUserLoggedIn,
    currentUser = currentUser,
    modifier = modifier
  )
}