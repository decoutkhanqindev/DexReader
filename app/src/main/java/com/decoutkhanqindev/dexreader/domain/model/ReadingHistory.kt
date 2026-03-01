package com.decoutkhanqindev.dexreader.domain.model

data class ReadingHistory(
  val id: String,
  val mangaId: String,
  val mangaTitle: String,
  val mangaCoverUrl: String,
  val chapterId: String,
  val chapterTitle: String,
  val chapterNumber: String,
  val chapterVolume: String,
  val lastReadPage: Int,
  val pageCount: Int,
  val lastReadAt: String?,
) {
  companion object {
    fun generateId(mangaId: String, chapterId: String): String = "${mangaId}_${chapterId}"

    fun findContinueTarget(historyList: List<ReadingHistory>): ReadingHistory? =
      historyList.firstOrNull { it.lastReadPage < it.pageCount - 1 }
        ?: historyList.firstOrNull()

    fun findInitialPage(
      chapterId: String,
      navChapterId: String,
      navPage: Int,
      historyList: List<ReadingHistory>,
    ): Int {
      if (chapterId == navChapterId && navPage > 0) return navPage
      return historyList.find { it.chapterId == chapterId }?.lastReadPage ?: 1
    }
  }
}
