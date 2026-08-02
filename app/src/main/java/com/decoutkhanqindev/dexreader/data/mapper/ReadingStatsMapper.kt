package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.firebase.dto.response.ReadingStatsResponse
import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingStats

object ReadingStatsMapper {

  fun ReadingStatsResponse.toReadingStats() =
    ReadingStats(date = date, durationMillis = durationMillis)
}
