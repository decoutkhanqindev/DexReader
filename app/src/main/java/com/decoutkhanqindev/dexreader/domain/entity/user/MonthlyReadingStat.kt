package com.decoutkhanqindev.dexreader.domain.entity.user

data class MonthlyReadingStat(
  val month: String, // format: yyyy-MM
  val durationMillis: Long,
)
