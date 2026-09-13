package com.decoutkhanqindev.dexreader.data.network.connectivity

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class NetworkManagerImpl @Inject constructor(
  private val app: Application,
) : NetworkManager {
  private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

  @OptIn(FlowPreview::class)
  override val isAvailable: StateFlow<Boolean> = callbackFlow {
    val connectivityManager = app.getSystemService(ConnectivityManager::class.java)
    val callback = object : ConnectivityManager.NetworkCallback() {
      override fun onCapabilitiesChanged(
        network: Network,
        networkCapabilities: NetworkCapabilities,
      ) {
        trySend(networkCapabilities.hasInternetAccess())
      }

      override fun onLost(network: Network) {
        trySend(false)
      }
    }

    trySend(connectivityManager.isInternetAvailable())
    connectivityManager.registerDefaultNetworkCallback(callback)
    awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
  }
    .debounce { isAvailable -> if (isAvailable) 0L else NETWORK_LOST_DEBOUNCE_MILLIS }
    .distinctUntilChanged()
    .catch { throwable ->
      if (throwable is CancellationException) throw throwable
      Timber.tag(this@NetworkManagerImpl::class.java.simpleName)
        .e("isAvailable have error: ${throwable.stackTraceToString()}")
      emit(true)
    }
    .stateIn(
      scope = scope,
      started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
      initialValue = true,
    )

  private fun ConnectivityManager.isInternetAvailable(): Boolean =
    activeNetwork?.let(::getNetworkCapabilities)?.hasInternetAccess() ?: false

  private fun NetworkCapabilities.hasInternetAccess(): Boolean =
    hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
        hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

  companion object {
    private const val NETWORK_LOST_DEBOUNCE_MILLIS = 500L
    private const val STOP_TIMEOUT_MILLIS = 5_000L
  }
}
