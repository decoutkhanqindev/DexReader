package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.Manga

interface MangaRepository {
  suspend fun getLatestUpdateMangaList(): List<Manga>
  suspend fun getTrendingMangaList(): List<Manga>
  suspend fun getNewReleaseMangaList(): List<Manga>
  suspend fun getTopRatedMangaList(): List<Manga>
  suspend fun getMangaDetails(mangaId: String): Manga
  suspend fun searchManga(query: String, offset: Int): List<Manga>
}