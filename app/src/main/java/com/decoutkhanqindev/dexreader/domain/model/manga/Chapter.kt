package com.decoutkhanqindev.dexreader.domain.model.manga

data class Chapter(
  val id: String,
  val mangaId: String,
  val title: String,
  val number: String,
  val volume: String,
  val publishedAt: String,
  val language: MangaLanguage,
) {
  data class NavPosition(
    val foundAtIndex: Int,
    val previousChapterId: String?,
    val nextChapterId: String?,
    val canNavigatePrevious: Boolean,
    val canNavigateNext: Boolean,
  )

  companion object {
    const val DEFAULT_MANGA_ID = "0"
    const val DEFAULT_TITLE = "Untitled"
    const val DEFAULT_CHAPTER_NUMBER = "0"
    const val DEFAULT_VOLUME = "0"
    val DEFAULT_LANGUAGE = MangaLanguage.ENGLISH

    fun isPrefetchNextPage(currentIndex: Int, listSize: Int): Boolean =
      listSize > 0 && currentIndex >= (listSize - 1 - PREFETCH_THRESHOLD)

    fun determineNavPosition(
      currentChapterId: String,
      chapterList: List<Chapter>,
      hasNextPage: Boolean,
    ): NavPosition {
      val index = chapterList.indexOfFirst { it.id == currentChapterId }

      if (index == -1) {
        return NavPosition(
          foundAtIndex = -1,
          previousChapterId = null,
          nextChapterId = null,
          canNavigatePrevious = false,
          canNavigateNext = hasNextPage,
        )
      }

      val isFirst = index == 0
      val isLast = index == chapterList.lastIndex

      return NavPosition(
        foundAtIndex = index,
        previousChapterId = if (!isFirst) chapterList[index - 1].id else null,
        nextChapterId = if (!isLast) chapterList[index + 1].id else null,
        canNavigatePrevious = !isFirst,
        canNavigateNext = !isLast || hasNextPage,
      )
    }

    private const val PREFETCH_THRESHOLD = 5
  }
}
