package com.decoutkhanqindev.dexreader.data.local

import androidx.room.RoomDatabase
import com.decoutkhanqindev.dexreader.data.local.dao.AppDao


abstract class AppDatabase: RoomDatabase() {
  abstract fun appDao(): AppDao
}