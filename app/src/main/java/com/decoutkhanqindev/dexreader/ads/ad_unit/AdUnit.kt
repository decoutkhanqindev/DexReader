package com.decoutkhanqindev.dexreader.ads.ad_unit

import android.content.Context
import com.decoutkhanqindev.dexreader.data.network.connectivity.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

abstract class AdUnit(
  val floors: List<Pair<String, String>>,
  private val networkManager: NetworkManager,
) {
  protected val tag: String get() = javaClass.simpleName

  protected val scope = CoroutineScope(Dispatchers.Main)

  private var floorIndex = 0
  private var loadGeneration = 0

  protected val currentId: String get() = floors[floorIndex].first
  protected val currentName: String get() = floors[floorIndex].second

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
    floorIndex = 0
  }

  private fun tryFallback(): Boolean {
    if (floorIndex + 1 >= floors.size) return false
    floorIndex++
    return true
  }

  private fun nextGeneration(): Int {
    loadGeneration++
    return loadGeneration
  }

  protected fun isCurrentGeneration(generation: Int): Boolean = generation == loadGeneration

  protected fun onLoadFailed(context: Context, generation: Int) {
    if (!isCurrentGeneration(generation)) return
    val failedName = currentName
    if (tryFallback()) {
      Timber.tag(tag).d("$failedName - Falling back to $currentName")
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
