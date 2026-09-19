package com.decoutkhanqindev.dexreader.ads.ad_unit

import android.content.Context
import com.decoutkhanqindev.dexreader.data.network.connectivity.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

abstract class AdUnit(
  val id: Pair<String, String>,
  val name: Pair<String, String>,
  private val networkManager: NetworkManager,
) {
  protected val tag: String get() = javaClass.simpleName

  protected val job = SupervisorJob()
  protected val scope = CoroutineScope(Dispatchers.Main + job)

  private var usingFallback = false
  private var loadGeneration = 0

  protected val currentId: String get() = if (usingFallback) id.second else id.first
  protected val currentName: String get() = if (usingFallback) name.second else name.first

  protected val _state = MutableStateFlow(AdUnitState.NONE)
  val state: StateFlow<AdUnitState> = _state.asStateFlow()

  fun load(context: Context) {
    if (!networkManager.isAvailable.value) {
      Timber.tag(tag).d("$currentName - No network, not loading")
      _state.value = AdUnitState.FAILED
      return
    }
    resetWaterfall()
    requestLoad(context, nextGeneration())
  }

  private fun resetWaterfall() {
    usingFallback = false
  }

  private fun tryFallback(): Boolean {
    if (usingFallback) return false
    usingFallback = true
    return true
  }

  private fun nextGeneration(): Int {
    loadGeneration++
    return loadGeneration
  }

  protected fun isCurrentGeneration(generation: Int): Boolean = generation == loadGeneration

  protected fun onLoadFailed(context: Context, generation: Int) {
    if (!isCurrentGeneration(generation)) return
    if (tryFallback()) {
      Timber.tag(tag).d("$currentName - Falling back to ${name.second}")
      requestLoad(context, generation)
    } else {
      Timber.tag(tag).d("$currentName - No fallback left, giving up")
      _state.value = AdUnitState.FAILED
    }
  }

  protected abstract fun requestLoad(context: Context, generation: Int)
  abstract fun destroy()

  companion object {
    const val LOAD_TIMEOUT = 20_000L
  }
}
