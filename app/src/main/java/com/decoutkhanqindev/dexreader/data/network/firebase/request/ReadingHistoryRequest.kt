package com.decoutkhanqindev.dexreader.data.network.firebase.request

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ReadingHistoryRequest(
  @get:PropertyName("reading_history_id")
  val id: String,

  @get:PropertyName("manga_id")
  val mangaId: String,

  @get:PropertyName("manga_title")
  val mangaTitle: String,

  @get:PropertyName("manga_cover_url")
  val mangaCoverUrl: String,

  @get:PropertyName("chapter_id")
  val chapterId: String,

  @get:PropertyName("chapter_title")
  val chapterTitle: String,

  @get:PropertyName("chapter_number")
  val chapterNumber: String,

  @get:PropertyName("chapter_volume")
  val chapterVolume: String,

  @get:PropertyName("last_read_page")
  val lastReadPage: Int,

  @get:PropertyName("total_chapter_pages")
  val totalChapterPages: Int,

  @ServerTimestamp
  val createdAt: Date? = null,
)
