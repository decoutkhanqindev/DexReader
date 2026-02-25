package com.decoutkhanqindev.dexreader.data.network.mangadex_api

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.at_home.AtHomeServerResponse
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.chapter.ChapterDetailsResponse
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.chapter.ChapterListResponse
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.manga.MangaDetailsResponse
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.manga.MangaListResponse
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.response.tag.TagListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MangaDexApiService {
  @GET("/manga")
  suspend fun getLatestUpdateMangaList(
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int = 0,
    @Query("order[updatedAt]") lastUpdated: String = "desc",
    @Query("status[]") status: String = "ongoing",
    @Query("includes[]")
    includes: List<String> = listOf("cover_art", "author", "artist"),
  ): MangaListResponse

  @GET("/manga")
  suspend fun getTrendingMangaList(
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int = 0,
    @Query("order[followedCount]") followedCount: String = "desc",
    @Query("includes[]")
    includes: List<String> = listOf("cover_art", "author", "artist"),
  ): MangaListResponse

  @GET("/manga")
  suspend fun getNewReleaseMangaList(
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int = 0,
    @Query("order[createdAt]") createdAt: String = "desc",
    @Query("includes[]")
    includes: List<String> = listOf("cover_art", "author", "artist"),
  ): MangaListResponse

  @GET("/manga")
  suspend fun getTopRatedMangaList(
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int = 0,
    @Query("order[rating]=desc") rating: String = "desc",
    @Query("includes[]")
    includes: List<String> = listOf("cover_art", "author", "artist"),
  ): MangaListResponse

  @GET("/manga")
  suspend fun searchManga(
    @Query("title") query: String,
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int = 0,
    @Query("order[relevance]") order: String = "desc",
    @Query("includes[]")
    includes: List<String> = listOf("cover_art", "author", "artist"),
  ): MangaListResponse

  @GET("manga/{id}")
  suspend fun getMangaDetails(
    @Path("id") mangaId: String,
    @Query("includes[]")
    includes: List<String> = listOf("cover_art", "author", "artist"),
  ): MangaDetailsResponse

  @GET("manga/{id}/feed")
  suspend fun getChapterList(
    @Path("id") mangaId: String,
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int = 0,
    @Query("translatedLanguage[]")
    translatedLanguages: String = "en",
    @Query("order[volume]") volumeOrder: String = "desc",
    @Query("order[chapter]") chapterOrder: String = "desc",
    @Query("includes[]")
    includes: List<String> = listOf("scanlation_group"),
  ): ChapterListResponse

  @GET("/chapter/{id}")
  suspend fun getChapterDetails(
    @Path("id") chapterId: String,
    @Query("includes[]")
    includes: List<String> = listOf("manga", "scanlation_group"),
  ): ChapterDetailsResponse

  @GET("at-home/server/{id}")
  suspend fun getChapterPages(@Path("id") chapterId: String): AtHomeServerResponse

  @GET("manga/tag")
  suspend fun getTagList(): TagListResponse

  @GET("/manga")
  suspend fun getMangaListByTag(
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int = 0,
    @Query("includedTags[]") tagId: String,
    // sorting
    @Query("order[updatedAt]") lastUpdated: String? = null, // latest update
    @Query("order[followedCount]") followedCount: String? = null, // trending
    @Query("order[createdAt]") createdAt: String? = null, // new release
    @Query("order[rating]=desc") rating: String? = null, // top rated
    // filtering
    @Query("status[]") status: List<String> = listOf("ongoing"), // ongoing, completed, hiatus, cancelled
    @Query("contentRating[]") contentRating: List<String> = listOf("safe"), // safe, suggestive, erotica
    @Query("includes[]")
    includes: List<String> = listOf("cover_art", "author", "artist"),
  ): MangaListResponse
}