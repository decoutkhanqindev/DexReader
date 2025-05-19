package com.decoutkhanqindev.dexreader.data.network

import com.decoutkhanqindev.dexreader.data.network.dto.AtHomeServerDto
import com.decoutkhanqindev.dexreader.data.network.response.ChapterListResponse
import com.decoutkhanqindev.dexreader.data.network.response.MangaDetailsResponse
import com.decoutkhanqindev.dexreader.data.network.response.MangaListResponse
import com.decoutkhanqindev.dexreader.data.network.response.TagListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MangaDexApiService {
  @GET("/manga")
  suspend fun getMangaList(
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int = 0,
    @Query("order[title]") title: String = "desc",
    @Query("includedTags[]") tags: List<String> = emptyList(),
    @Query("includes[]")
    includes: List<String> = listOf("cover_art", "author", "artist"),
  ): MangaListResponse

  @GET("manga/{id}")
  suspend fun getMangaDetails(
    @Path("id") mangaId: String,
    @Query("includes[]")
    includes: List<String> = listOf("cover_art", "author", "artist")
  ): MangaDetailsResponse

  @GET("manga/{id}/feed")
  suspend fun getChapterList(
    @Path("id") mangaId: String,
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int = 0,
    @Query("translatedLanguage[]")
    languages: List<String> = listOf("en", "vi"),
    @Query("order[volume]") volumeOrder: String = "desc",
    @Query("order[chapter]") chapterOrder: String = "desc",
    @Query("includes[]")
    includes: List<String> = listOf("scanlation_group")
  ): ChapterListResponse

  @GET("at-home/server/{id}")
  suspend fun getChapterPages(
    @Path("id") chapterId: String
  ): AtHomeServerDto

  @GET("/manga")
  suspend fun searchManga(
    @Query("title") query: String,
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int = 0,
    @Query("order[relevance]") order: String = "desc",
    @Query("includes[]")
    includes: List<String> = listOf("cover_art", "author", "artist"),
  ): MangaListResponse

  @GET("manga/tag")
  suspend fun getTags(): TagListResponse
}