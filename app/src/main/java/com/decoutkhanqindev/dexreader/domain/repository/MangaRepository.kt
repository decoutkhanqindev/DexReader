package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.Manga

interface MangaRepository {
  suspend fun getLatestUpdateMangaList(): Result<List<Manga>>
  suspend fun getTrendingMangaList(): Result<List<Manga>>
  suspend fun getNewReleaseMangaList(): Result<List<Manga>>
  suspend fun getCompletedMangaList(): Result<List<Manga>>
  suspend fun getMangaDetails(mangaId: String): Result<Manga>

  suspend fun searchManga(
    query: String,
    offset: Int
  ): Result<List<Manga>>
}