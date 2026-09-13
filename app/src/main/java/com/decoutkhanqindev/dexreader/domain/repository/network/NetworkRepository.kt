package com.decoutkhanqindev.dexreader.domain.repository.network

import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
  fun observeIsAvailable(): Flow<Boolean>
}
