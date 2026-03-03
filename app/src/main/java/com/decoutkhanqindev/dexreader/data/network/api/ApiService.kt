package com.decoutkhanqindev.dexreader.data.network.api

import com.decoutkhanqindev.dexreader.data.network.api.constant.ApiEndpoints
import com.decoutkhanqindev.dexreader.data.network.api.constant.ApiPaths
import com.decoutkhanqindev.dexreader.data.network.api.constant.ApiQueries
import com.decoutkhanqindev.dexreader.data.network.api.param.MangaContentRatingParam
import com.decoutkhanqindev.dexreader.data.network.api.param.MangaIncludesParam
import com.decoutkhanqindev.dexreader.data.network.api.param.MangaLanguageCodeParam
import com.decoutkhanqindev.dexreader.data.network.api.param.MangaSortOrderParam
import com.decoutkhanqindev.dexreader.data.network.api.param.MangaStatusParam
import com.decoutkhanqindev.dexreader.data.network.api.response.at_home.AtHomeServerResponse
import com.decoutkhanqindev.dexreader.data.network.api.response.chapter.ChapterDetailsResponse
import com.decoutkhanqindev.dexreader.data.network.api.response.chapter.ChapterListResponse
import com.decoutkhanqindev.dexreader.data.network.api.response.manga.MangaDetailsResponse
import com.decoutkhanqindev.dexreader.data.network.api.response.manga.MangaListResponse
import com.decoutkhanqindev.dexreader.data.network.api.response.tag.TagListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
  @GET(ApiEndpoints.MANGA)
  suspend fun getLatestUpdateMangaList(
    @Query(ApiQueries.LIMIT) limit: Int = 20,
    @Query(ApiQueries.OFFSET) offset: Int = 0,
    @Query(ApiQueries.ORDER_UPDATED_AT)
    lastUpdated: String = MangaSortOrderParam.DESC.value,
    @Query(ApiQueries.STATUS) status: String = MangaStatusParam.ON_GOING.value,
    @Query(ApiQueries.INCLUDES)
    includes: List<String> = listOf(
      MangaIncludesParam.COVER_ART.value,
      MangaIncludesParam.AUTHOR.value,
      MangaIncludesParam.ARTIST.value
    ),
  ): MangaListResponse

  @GET(ApiEndpoints.MANGA)
  suspend fun getTrendingMangaList(
    @Query(ApiQueries.LIMIT) limit: Int = 20,
    @Query(ApiQueries.OFFSET) offset: Int = 0,
    @Query(ApiQueries.ORDER_FOLLOWED_COUNT)
    followedCount: String = MangaSortOrderParam.DESC.value,
    @Query(ApiQueries.INCLUDES)
    includes: List<String> = listOf(
      MangaIncludesParam.COVER_ART.value,
      MangaIncludesParam.AUTHOR.value,
      MangaIncludesParam.ARTIST.value
    ),
  ): MangaListResponse

  @GET(ApiEndpoints.MANGA)
  suspend fun getNewReleaseMangaList(
    @Query(ApiQueries.LIMIT) limit: Int = 20,
    @Query(ApiQueries.OFFSET) offset: Int = 0,
    @Query(ApiQueries.ORDER_CREATED_AT) createdAt: String = MangaSortOrderParam.DESC.value,
    @Query(ApiQueries.INCLUDES)
    includes: List<String> = listOf(
      MangaIncludesParam.COVER_ART.value,
      MangaIncludesParam.AUTHOR.value,
      MangaIncludesParam.ARTIST.value
    ),
  ): MangaListResponse

  @GET(ApiEndpoints.MANGA)
  suspend fun getTopRatedMangaList(
    @Query(ApiQueries.LIMIT) limit: Int = 20,
    @Query(ApiQueries.OFFSET) offset: Int = 0,
    @Query(ApiQueries.ORDER_RATING) rating: String = MangaSortOrderParam.DESC.value,
    @Query(ApiQueries.INCLUDES)
    includes: List<String> = listOf(
      MangaIncludesParam.COVER_ART.value,
      MangaIncludesParam.AUTHOR.value,
      MangaIncludesParam.ARTIST.value
    ),
  ): MangaListResponse

  @GET(ApiEndpoints.MANGA)
  suspend fun searchManga(
    @Query(ApiQueries.TITLE) query: String,
    @Query(ApiQueries.LIMIT) limit: Int = 20,
    @Query(ApiQueries.OFFSET) offset: Int = 0,
    @Query(ApiQueries.ORDER_RELEVANCE) order: String = MangaSortOrderParam.DESC.value,
    @Query(ApiQueries.INCLUDES)
    includes: List<String> = listOf(
      MangaIncludesParam.COVER_ART.value,
      MangaIncludesParam.AUTHOR.value,
      MangaIncludesParam.ARTIST.value
    ),
  ): MangaListResponse

  @GET(ApiEndpoints.MANGA_ID)
  suspend fun getMangaDetails(
    @Path(ApiPaths.ID) mangaId: String,
    @Query(ApiQueries.INCLUDES)
    includes: List<String> = listOf(
      MangaIncludesParam.COVER_ART.value,
      MangaIncludesParam.AUTHOR.value,
      MangaIncludesParam.ARTIST.value
    ),
  ): MangaDetailsResponse

  @GET(ApiEndpoints.MANGA_FEED)
  suspend fun getChapterList(
    @Path(ApiPaths.ID) mangaId: String,
    @Query(ApiQueries.LIMIT) limit: Int = 20,
    @Query(ApiQueries.OFFSET) offset: Int = 0,
    @Query(ApiQueries.TRANSLATED_LANGUAGE)
    translatedLanguages: String = MangaLanguageCodeParam.ENGLISH.value,
    @Query(ApiQueries.ORDER_VOLUME) volumeOrder: String = MangaSortOrderParam.DESC.value,
    @Query(ApiQueries.ORDER_CHAPTER) chapterOrder: String = MangaSortOrderParam.DESC.value,
    @Query(ApiQueries.INCLUDES)
    includes: List<String> = listOf(MangaIncludesParam.SCANLATION_GROUP.value),
  ): ChapterListResponse

  @GET(ApiEndpoints.CHAPTER_ID)
  suspend fun getChapterDetails(
    @Path(ApiPaths.ID) chapterId: String,
    @Query(ApiQueries.INCLUDES)
    includes: List<String> = listOf(
      MangaIncludesParam.MANGA.value,
      MangaIncludesParam.SCANLATION_GROUP.value
    ),
  ): ChapterDetailsResponse

  @GET(ApiEndpoints.AT_HOME_SERVER_ID)
  suspend fun getChapterPages(@Path(ApiPaths.ID) chapterId: String): AtHomeServerResponse

  @GET(ApiEndpoints.MANGA_TAG)
  suspend fun getTagList(): TagListResponse

  @GET(ApiEndpoints.MANGA)
  suspend fun getMangaListByTag(
    @Query(ApiQueries.LIMIT) limit: Int = 20,
    @Query(ApiQueries.OFFSET) offset: Int = 0,
    @Query(ApiQueries.INCLUDED_TAGS) tagId: String,
    // sorting
    @Query(ApiQueries.ORDER_UPDATED_AT) lastUpdated: String? = null, // latest update
    @Query(ApiQueries.ORDER_FOLLOWED_COUNT) followedCount: String? = null, // trending
    @Query(ApiQueries.ORDER_CREATED_AT) createdAt: String? = null, // new release
    @Query(ApiQueries.ORDER_RATING) rating: String? = null, // top rated
    // filtering
    @Query(ApiQueries.STATUS)
    status: List<String> = listOf(MangaStatusParam.ON_GOING.value), // ongoing, completed, hiatus, cancelled
    @Query(ApiQueries.CONTENT_RATING)
    contentRating: List<String> = listOf(MangaContentRatingParam.SAFE.value), // safe, suggestive, erotica
    @Query(ApiQueries.INCLUDES)
    includes: List<String> = listOf(
      MangaIncludesParam.COVER_ART.value,
      MangaIncludesParam.AUTHOR.value,
      MangaIncludesParam.ARTIST.value
    ),
  ): MangaListResponse
}
