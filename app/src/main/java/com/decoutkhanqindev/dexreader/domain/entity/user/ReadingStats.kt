package com.decoutkhanqindev.dexreader.domain.entity.user

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class ReadingStats(
  val date: String, // format: yyyy-MM-dd
  val durationMillis: Long,
) {
  companion object {
    private const val WEEKLY_DAY_COUNT = 7

    fun getCurrentDate(): String {
      val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
      return sdf.format(Date())
    }

    fun generateId(userId: String, date: String): String = "${userId}_${date}"

    fun getLastDates(days: Int = WEEKLY_DAY_COUNT): List<String> {
      val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
      val calendar = Calendar.getInstance()
      return (days - 1 downTo 0).map { offset ->
        val day = calendar.clone() as Calendar
        day.add(Calendar.DAY_OF_YEAR, -offset)
        sdf.format(day.time)
      }
    }

    fun buildWeeklyBreakdown(statsList: List<ReadingStats>): List<ReadingStats> {
      val statsByDate = statsList.associateBy { it.date }
      return getLastDates().map { date ->
        statsByDate[date] ?: ReadingStats(date = date, durationMillis = 0L)
      }
    }

    fun buildMonthlyBreakdown(statsList: List<ReadingStats>): List<MonthlyReadingStat> {
      if (statsList.isEmpty()) {
        return listOf(MonthlyReadingStat(month = getCurrentMonth(), durationMillis = 0L))
      }
      return statsList
        .groupBy { it.date.substring(0, 7) }
        .map { (month, entries) ->
          MonthlyReadingStat(month = month, durationMillis = entries.sumOf { it.durationMillis })
        }
        .sortedBy { it.month }
    }

    private fun getCurrentMonth(): String {
      val sdf = SimpleDateFormat("yyyy-MM", Locale.US)
      return sdf.format(Date())
    }

    fun buildYearlyBreakdown(statsList: List<ReadingStats>): List<YearlyReadingStat> {
      if (statsList.isEmpty()) {
        return listOf(YearlyReadingStat(year = getCurrentYear(), durationMillis = 0L))
      }
      return statsList
        .groupBy { it.date.substring(0, 4) }
        .map { (year, entries) ->
          YearlyReadingStat(year = year, durationMillis = entries.sumOf { it.durationMillis })
        }
        .sortedBy { it.year }
    }

    private fun getCurrentYear(): String {
      val sdf = SimpleDateFormat("yyyy", Locale.US)
      return sdf.format(Date())
    }
  }
}
