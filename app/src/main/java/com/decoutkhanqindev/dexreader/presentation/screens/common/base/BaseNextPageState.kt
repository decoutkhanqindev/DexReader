package com.decoutkhanqindev.dexreader.presentation.screens.common.base

enum class BaseNextPageState {
  IDLE,
  LOADING,
  NO_MORE_ITEMS,
  ERROR;

  companion object {
    fun fromPageSize(resultSize: Int, pageSize: Int): BaseNextPageState =
      if (resultSize >= pageSize) IDLE else NO_MORE_ITEMS
  }
}