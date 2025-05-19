package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.local.database.entity.ChapterCacheEntity
import com.decoutkhanqindev.dexreader.data.network.dto.AtHomeServerDto
import com.decoutkhanqindev.dexreader.domain.model.ChapterPages

fun AtHomeServerDto.toDomain(chapterId: String): ChapterPages {
  val hash = chapter.hash
  val data = chapter.data
  val pageUrls = data.map { url ->
    "$baseUrl/data/$hash/$url"
  }

  return ChapterPages(
    chapterId = chapterId,
    baseUrl = baseUrl,
    pageUrls = pageUrls,
    totalPages = pageUrls.size
  )
}

fun ChapterCacheEntity.toDomain(): ChapterPages {
  val pageUrls = pageHashes.map { hash ->
    "$baseUrl/data/$chapterId/$hash"
  }

  return ChapterPages(
    chapterId = chapterId,
    baseUrl = baseUrl,
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
    pageHashes = pageHashes,
    totalPages = pageUrls.size,
    cachedAt = System.currentTimeMillis()
  )
}