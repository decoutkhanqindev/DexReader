package com.decoutkhanqindev.dexreader.data.network.mangadex_api.constant

object MangaDexApiQueries {
  const val LIMIT = "limit"
  const val OFFSET = "offset"
  const val TITLE = "title"
  const val INCLUDED_TAGS = "includedTags[]"
  const val STATUS = "status[]"
  const val CONTENT_RATING = "contentRating[]"
  const val TRANSLATED_LANGUAGE = "translatedLanguage[]"
  const val INCLUDES = "includes[]"

  // Order
  const val ORDER_UPDATED_AT = "order[updatedAt]"
  const val ORDER_FOLLOWED_COUNT = "order[followedCount]"
  const val ORDER_CREATED_AT = "order[createdAt]"
  const val ORDER_RATING = "order[rating]=desc"
  const val ORDER_RELEVANCE = "order[relevance]"
  const val ORDER_VOLUME = "order[volume]"
  const val ORDER_CHAPTER = "order[chapter]"
}
