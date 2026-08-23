package com.decoutkhanqindev.dexreader.presentation.screens.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {
  fun vmLaunch(
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate,
    block: suspend CoroutineScope.() -> Unit,
  ): Job = viewModelScope.launch(context = coroutineContext, block = block)
}
