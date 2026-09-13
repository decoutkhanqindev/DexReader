package com.decoutkhanqindev.dexreader.data.local.datastore

import kotlinx.coroutines.flow.StateFlow

interface DataStoreManager {
  val isDark: StateFlow<Boolean?>
  val selectedLangCode: StateFlow<String?>
  val isFirstOpen: StateFlow<Boolean?>
  fun saveIsDark(value: Boolean)
  fun saveSelectedLangCode(value: String)
  fun saveIsFirstOpen(value: Boolean)
}
