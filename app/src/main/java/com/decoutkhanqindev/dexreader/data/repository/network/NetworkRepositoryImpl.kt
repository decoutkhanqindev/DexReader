package com.decoutkhanqindev.dexreader.data.repository.network

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.decoutkhanqindev.dexreader.domain.repository.network.NetworkRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
  private val app: Application,
) : NetworkRepository {

  @OptIn(FlowPreview::class)
  override fun observeIsAvailable(): Flow<Boolean> = callbackFlow {
    val connectivityManager = app.getSystemService(ConnectivityManager::class.java)
    val callback = object : ConnectivityManager.NetworkCallback() {
      override fun onCapabilitiesChanged(
        network: Network,
        networkCapabilities: NetworkCapabilities,
      ) {
        trySend(networkCapabilities.hasInternetAccess())
      }

      override fun onLost(network: Network) {
        trySend(connectivityManager.isInternetAvailable())
      }
    }

    trySend(connectivityManager.isInternetAvailable())
    connectivityManager.registerDefaultNetworkCallback(callback)
    awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
  }
    .debounce { isAvailable -> if (isAvailable) 0L else NETWORK_LOST_DEBOUNCE_MILLIS }
    .distinctUntilChanged()

  private fun ConnectivityManager.isInternetAvailable(): Boolean =
    activeNetwork?.let(::getNetworkCapabilities)?.hasInternetAccess() ?: false

  private fun NetworkCapabilities.hasInternetAccess(): Boolean =
    hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
        hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

  companion object {
    private const val NETWORK_LOST_DEBOUNCE_MILLIS = 500L
  }
}
