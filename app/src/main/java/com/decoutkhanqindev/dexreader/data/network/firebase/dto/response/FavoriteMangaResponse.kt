package com.decoutkhanqindev.dexreader.data.network.firebase.dto.response

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class FavoriteMangaResponse(
  @get:Exclude @set:Exclude // Populated from document.id via .copy(), not from document data.
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
  var createdAt: Date? = null,
)
