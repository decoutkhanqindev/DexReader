package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.local.database.entity.ChapterCacheEntity
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.at_home.AtHomeServerResponse
import com.decoutkhanqindev.dexreader.domain.model.ChapterPages

fun AtHomeServerResponse.toDomain(chapterId: String): ChapterPages {
  val hash = chapter?.hash
  val data = chapter?.data

  // {baseUrl}/data/{chapterDataHash}/{pageHash}
  val pageUrls = data?.map { pageHash ->
    "$baseUrl/data/$hash/$pageHash"
  } ?: emptyList()

  return ChapterPages(
    chapterId = chapterId,
    baseUrl = baseUrl ?: "",
    chapterDataHash = hash ?: "",
    pageUrls = pageUrls,
    totalPages = pageUrls.size
  )
}

fun ChapterCacheEntity.toDomain(): ChapterPages {
  // {baseUrl}/data/{chapterDataHash}/{pageHash}
  val pageUrls = pageHashes.map { hash ->
    "$baseUrl/data/$chapterDataHash/$hash"
  }

  return ChapterPages(
    chapterId = chapterId,
    baseUrl = baseUrl,
    chapterDataHash = chapterDataHash,
    pageUrls = pageUrls,
    totalPages = pageUrls.size
  )
}

fun ChapterPages.toEntity(mangaId: String): ChapterCacheEntity {
  val pageHashes = pageUrls.map { url ->
    url.substringAfterLast("/")
  }

  return ChapterCacheEntity(
    chapterId = chapterId,
    mangaId = mangaId,
    baseUrl = baseUrl,
    chapterDataHash = chapterDataHash,
    pageHashes = pageHashes,
    totalPages = pageUrls.size,
    cachedAt = System.currentTimeMillis()
  )
}