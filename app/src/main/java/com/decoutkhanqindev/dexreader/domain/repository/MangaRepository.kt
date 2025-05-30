package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.ChapterPages
import com.decoutkhanqindev.dexreader.domain.model.Manga

interface MangaRepository {
  suspend fun getLatestUpdateMangaList(): Result<List<Manga>>
  suspend fun getTrendingMangaList(): Result<List<Manga>>
  suspend fun getNewReleaseMangaList(): Result<List<Manga>>
  suspend fun getCompletedMangaList(): Result<List<Manga>>
  suspend fun getMangaDetails(mangaId: String): Result<Manga>
  suspend fun getChapterList(
    mangaId: String,
    offset: Int = 0,
    translatedLanguage: String = "en",
    volumeOrder: String = "desc",
    chapterOrder: String = "desc"
  ): Result<List<Chapter>>
  suspend fun getChapterPages(chapterId: String): Result<ChapterPages>
  suspend fun searchManga(
    query: String,
    offset: Int
  ): Result<List<Manga>>
}