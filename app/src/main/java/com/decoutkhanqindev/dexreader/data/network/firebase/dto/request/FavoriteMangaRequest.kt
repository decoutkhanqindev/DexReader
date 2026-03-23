package com.decoutkhanqindev.dexreader.data.network.firebase.dto.request

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class FavoriteMangaRequest(
  @get:Exclude // Document ID — set via .document(id) by the caller.
  val id: String,

  @get:PropertyName("title")
  val title: String,

  @get:PropertyName("cover_url")
  val coverUrl: String,

  @get:PropertyName("author")
  val author: String,

  @get:PropertyName("status")
  val status: String,

  @ServerTimestamp
  val createdAt: Date? = null,
)
