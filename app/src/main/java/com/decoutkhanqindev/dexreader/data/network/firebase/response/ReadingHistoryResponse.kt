package com.decoutkhanqindev.dexreader.data.network.firebase.response

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ReadingHistoryResponse(
  @get:PropertyName("reading_history_id")
  @set:PropertyName("reading_history_id")
  var id: String = "",

  @get:PropertyName("manga_id")
  @set:PropertyName("manga_id")
  var mangaId: String = "",

  @get:PropertyName("manga_title")
  @set:PropertyName("manga_title")
  var mangaTitle: String = "",

  @get:PropertyName("manga_cover_url")
  @set:PropertyName("manga_cover_url")
  var mangaCoverUrl: String = "",

  @get:PropertyName("chapter_id")
  @set:PropertyName("chapter_id")
  var chapterId: String = "",

  @get:PropertyName("chapter_title")
  @set:PropertyName("chapter_title")
  var chapterTitle: String = "",

  @get:PropertyName("chapter_number")
  @set:PropertyName("chapter_number")
  var chapterNumber: String = "",

  @get:PropertyName("chapter_volume")
  @set:PropertyName("chapter_volume")
  var chapterVolume: String = "",

  @get:PropertyName("last_read_page")
  @set:PropertyName("last_read_page")
  var lastReadPage: Int = 0,

  @get:PropertyName("total_chapter_pages")
  @set:PropertyName("total_chapter_pages")
  var totalChapterPages: Int = 0,

  @ServerTimestamp
  var createdAt: Date? = null,
)
