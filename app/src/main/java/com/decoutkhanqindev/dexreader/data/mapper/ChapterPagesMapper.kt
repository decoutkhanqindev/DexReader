package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.local.database.entity.ChapterCacheEntity
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.constant.MangaDexApiEndpoints
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.at_home.AtHomeServerResponse
import com.decoutkhanqindev.dexreader.domain.model.ChapterPages

object ChapterPagesMapper {

  fun AtHomeServerResponse.toChapterPages(chapterId: String): ChapterPages {
    val hash = chapter?.hash
    val data = chapter?.data

    // {baseUrl}/data/{dataHash}/{pageHash}
    val pages = data?.map { pageHash ->
      "$baseUrl/${MangaDexApiEndpoints.DATA_PATH}/$hash/$pageHash"
    } ?: emptyList()

    return ChapterPages(
      chapterId = chapterId,
      baseUrl = baseUrl ?: ChapterPages.DEFAULT_BASE_URL,
      dataHash = hash ?: ChapterPages.DEFAULT_HASH,
      pages = pages,
      totalPages = pages.size
    )
  }

  fun ChapterCacheEntity.toChapterPages(): ChapterPages {
    // {baseUrl}/data/{dataHash}/{pageHash}
    val pages = pageHashes.map { hash ->
      "$baseUrl/${MangaDexApiEndpoints.DATA_PATH}/$dataHash/$hash"
    }

    return ChapterPages(
      chapterId = chapterId,
      baseUrl = baseUrl,
      dataHash = dataHash,
      pages = pages,
      totalPages = pages.size
    )
  }

  fun ChapterPages.toChapterCacheEntity(mangaId: String): ChapterCacheEntity {
    val pageHashes = pages.map { url ->
      url.substringAfterLast("/")
    }

    return ChapterCacheEntity(
      chapterId = chapterId,
      mangaId = mangaId,
      baseUrl = baseUrl,
      dataHash = dataHash,
      pageHashes = pageHashes,
      totalPages = pages.size,
      cachedAt = System.currentTimeMillis()
    )
  }
}
