package com.decoutkhanqindev.dexreader.data.network.connectivity

import kotlinx.coroutines.flow.StateFlow

interface NetworkManager {
  val isAvailable: StateFlow<Boolean>
}
