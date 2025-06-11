package com.decoutkhanqindev.dexreader.data.network.firebase.dto

import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class FavoriteMangaDto(
  @get:PropertyName("manga_id")
  @set:PropertyName("manga_id")
  var id: String = "",

  @get:PropertyName("title")
  @set:PropertyName("title")
  var title: String = "",

  @get:PropertyName("cover_url")
  @set:PropertyName("cover_url")
  var coverUrl: String = "",

  @get:PropertyName("author")
  @set:PropertyName("author")
  var author: String = "",

  @get:PropertyName("status")
  @set:PropertyName("status")
  var status: String = "",

  @ServerTimestamp
  @get:PropertyName("added_at")
  @set:PropertyName("added_at")
  var addedAt: Date? = null
)
