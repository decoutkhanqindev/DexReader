package com.decoutkhanqindev.dexreader.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle

object Lifecycle {
  @Composable
  inline fun LaunchedEffectRepeatOnLifecycle(
    state: Lifecycle.State,
    crossinline block: suspend () -> Unit,
  ) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
      lifecycleOwner.repeatOnLifecycle(state) { block() }
    }
  }
}