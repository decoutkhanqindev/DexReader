package com.decoutkhanqindev.dexreader.presentation.screens.common.locals

import androidx.compose.runtime.staticCompositionLocalOf
import com.decoutkhanqindev.dexreader.data.local.datastore.DataStoreManager
import com.decoutkhanqindev.dexreader.data.local.locale.LanguageManager
import com.decoutkhanqindev.dexreader.data.network.connectivity.NetworkManager

val LocalDataStoreManager = staticCompositionLocalOf<DataStoreManager> {
  error("No DataStoreManager provided")
}

val LocalNetworkManager = staticCompositionLocalOf<NetworkManager> {
  error("No NetworkManager provided")
}

val LocalLanguageManager = staticCompositionLocalOf<LanguageManager> {
  error("No LanguageManager provided")
}
