package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.local.database.entity.ChapterCacheEntity
import com.decoutkhanqindev.dexreader.data.network.api.response.at_home.AtHomeServerResponse
import com.decoutkhanqindev.dexreader.domain.entity.manga.ChapterPages
object ChapterPagesMapper {

  private const val DATA_URL_SEGMENT = "data"

  fun AtHomeServerResponse.toChapterPages(chapterId: String, mangaId: String): ChapterPages? {
    val resolvedBaseUrl = baseUrl ?: return null
    val hash = chapter?.hash ?: return null
    val data = chapter.data

    // {baseUrl}/data/{dataHash}/{pageHash}
    val pages = data?.map { pageHash ->
      "$resolvedBaseUrl/$DATA_URL_SEGMENT/$hash/$pageHash"
    } ?: emptyList()

    return ChapterPages(
      chapterId = chapterId,
      mangaId = mangaId,
      baseUrl = resolvedBaseUrl,
      dataHash = hash,
      pages = pages,
    )
  }

  fun ChapterCacheEntity.toChapterPages(): ChapterPages {
    // {baseUrl}/data/{dataHash}/{pageHash}
    val pages = pageHashes.map { hash ->
      "$baseUrl/$DATA_URL_SEGMENT/$dataHash/$hash"
    }

    return ChapterPages(
      chapterId = chapterId,
      mangaId = mangaId,
      baseUrl = baseUrl,
      dataHash = dataHash,
      pages = pages,
    )
  }

  fun ChapterPages.toChapterCacheEntity(): ChapterCacheEntity {
    val pageHashes = pages.map { url ->
      url.substringAfterLast("/")
    }

    return ChapterCacheEntity(
      chapterId = chapterId,
      mangaId = mangaId,
      baseUrl = baseUrl,
      dataHash = dataHash,
      pageHashes = pageHashes,
      totalPages = pageHashes.size,
      cachedAt = System.currentTimeMillis()
    )
  }
}
