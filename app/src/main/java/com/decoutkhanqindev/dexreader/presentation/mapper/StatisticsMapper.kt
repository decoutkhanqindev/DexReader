package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.entity.user.MonthlyReadingStat
import com.decoutkhanqindev.dexreader.domain.entity.user.ReadingStats
import com.decoutkhanqindev.dexreader.domain.entity.user.YearlyReadingStat
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingChartPointModel
import java.text.SimpleDateFormat
import java.util.Locale

object StatisticsMapper {
  private val dateParseFormat by lazy {
    ThreadLocal.withInitial { SimpleDateFormat("yyyy-MM-dd", Locale.US) }
  }

  private val dayLabelFormat by lazy {
    ThreadLocal.withInitial { SimpleDateFormat("EEE", Locale.getDefault()) }
  }

  private val monthParseFormat by lazy {
    ThreadLocal.withInitial { SimpleDateFormat("yyyy-MM", Locale.US) }
  }

  private val monthLabelFormat by lazy {
    ThreadLocal.withInitial { SimpleDateFormat("MMM", Locale.getDefault()) }
  }

  fun ReadingStats.toWeeklyChartPoint(): ReadingChartPointModel {
    val label = dateParseFormat.get()!!.parse(date)?.let { dayLabelFormat.get()!!.format(it) }.orEmpty()
    return ReadingChartPointModel(
      id = date,
      label = label,
      minutes = (durationMillis / 60_000).toInt(),
    )
  }

  fun MonthlyReadingStat.toMonthlyChartPoint(): ReadingChartPointModel {
    val label = monthParseFormat.get()!!.parse(month)?.let { monthLabelFormat.get()!!.format(it) }.orEmpty()
    return ReadingChartPointModel(
      id = month,
      label = label,
      minutes = (durationMillis / 60_000).toInt(),
    )
  }

  fun YearlyReadingStat.toYearlyChartPoint(): ReadingChartPointModel =
    ReadingChartPointModel(
      id = year,
      label = year,
      minutes = (durationMillis / 60_000).toInt(),
    )
}
