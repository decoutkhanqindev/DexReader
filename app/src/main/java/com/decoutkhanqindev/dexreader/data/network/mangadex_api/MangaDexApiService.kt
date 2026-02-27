package com.decoutkhanqindev.dexreader.data.network.mangadex_api

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.constant.MangaDexApiEndpoints
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.constant.MangaDexApiPaths
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.constant.MangaDexApiQueries
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.request.MangaContentRatingParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.request.MangaIncludesParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.request.MangaLanguageCodeParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.request.MangaSortOrderParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.request.MangaStatusParam
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.response.at_home.AtHomeServerResponse
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.response.chapter.ChapterDetailsResponse
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.response.chapter.ChapterListResponse
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.response.manga.MangaDetailsResponse
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.response.manga.MangaListResponse
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.response.tag.TagListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MangaDexApiService {
  @GET(MangaDexApiEndpoints.MANGA)
  suspend fun getLatestUpdateMangaList(
    @Query(MangaDexApiQueries.LIMIT) limit: Int = 20,
    @Query(MangaDexApiQueries.OFFSET) offset: Int = 0,
    @Query(MangaDexApiQueries.ORDER_UPDATED_AT)
    lastUpdated: String = MangaSortOrderParam.DESC.value,
    @Query(MangaDexApiQueries.STATUS) status: String = MangaStatusParam.ON_GOING.value,
    @Query(MangaDexApiQueries.INCLUDES)
    includes: List<String> =
      listOf(
        MangaIncludesParam.COVER_ART.value,
        MangaIncludesParam.AUTHOR.value,
        MangaIncludesParam.ARTIST.value
      ),
  ): MangaListResponse

  @GET(MangaDexApiEndpoints.MANGA)
  suspend fun getTrendingMangaList(
    @Query(MangaDexApiQueries.LIMIT) limit: Int = 20,
    @Query(MangaDexApiQueries.OFFSET) offset: Int = 0,
    @Query(MangaDexApiQueries.ORDER_FOLLOWED_COUNT)
    followedCount: String = MangaSortOrderParam.DESC.value,
    @Query(MangaDexApiQueries.INCLUDES)
    includes: List<String> =
      listOf(
        MangaIncludesParam.COVER_ART.value,
        MangaIncludesParam.AUTHOR.value,
        MangaIncludesParam.ARTIST.value
      ),
  ): MangaListResponse

  @GET(MangaDexApiEndpoints.MANGA)
  suspend fun getNewReleaseMangaList(
    @Query(MangaDexApiQueries.LIMIT) limit: Int = 20,
    @Query(MangaDexApiQueries.OFFSET) offset: Int = 0,
    @Query(MangaDexApiQueries.ORDER_CREATED_AT) createdAt: String = MangaSortOrderParam.DESC.value,
    @Query(MangaDexApiQueries.INCLUDES)
    includes: List<String> =
      listOf(
        MangaIncludesParam.COVER_ART.value,
        MangaIncludesParam.AUTHOR.value,
        MangaIncludesParam.ARTIST.value
      ),
  ): MangaListResponse

  @GET(MangaDexApiEndpoints.MANGA)
  suspend fun getTopRatedMangaList(
    @Query(MangaDexApiQueries.LIMIT) limit: Int = 20,
    @Query(MangaDexApiQueries.OFFSET) offset: Int = 0,
    @Query(MangaDexApiQueries.ORDER_RATING) rating: String = MangaSortOrderParam.DESC.value,
    @Query(MangaDexApiQueries.INCLUDES)
    includes: List<String> =
      listOf(
        MangaIncludesParam.COVER_ART.value,
        MangaIncludesParam.AUTHOR.value,
        MangaIncludesParam.ARTIST.value
      ),
  ): MangaListResponse

  @GET(MangaDexApiEndpoints.MANGA)
  suspend fun searchManga(
    @Query(MangaDexApiQueries.TITLE) query: String,
    @Query(MangaDexApiQueries.LIMIT) limit: Int = 20,
    @Query(MangaDexApiQueries.OFFSET) offset: Int = 0,
    @Query(MangaDexApiQueries.ORDER_RELEVANCE) order: String = MangaSortOrderParam.DESC.value,
    @Query(MangaDexApiQueries.INCLUDES)
    includes: List<String> = listOf(
      MangaIncludesParam.COVER_ART.value,
      MangaIncludesParam.AUTHOR.value,
      MangaIncludesParam.ARTIST.value
    ),
  ): MangaListResponse

  @GET(MangaDexApiEndpoints.MANGA_ID)
  suspend fun getMangaDetails(
    @Path(MangaDexApiPaths.ID) mangaId: String,
    @Query(MangaDexApiQueries.INCLUDES)
    includes: List<String> = listOf(
      MangaIncludesParam.COVER_ART.value,
      MangaIncludesParam.AUTHOR.value,
      MangaIncludesParam.ARTIST.value
    ),
  ): MangaDetailsResponse

  @GET(MangaDexApiEndpoints.MANGA_FEED)
  suspend fun getChapterList(
    @Path(MangaDexApiPaths.ID) mangaId: String,
    @Query(MangaDexApiQueries.LIMIT) limit: Int = 20,
    @Query(MangaDexApiQueries.OFFSET) offset: Int = 0,
    @Query(MangaDexApiQueries.TRANSLATED_LANGUAGE)
    translatedLanguages: String = MangaLanguageCodeParam.ENGLISH.value,
    @Query(MangaDexApiQueries.ORDER_VOLUME) volumeOrder: String = MangaSortOrderParam.DESC.value,
    @Query(MangaDexApiQueries.ORDER_CHAPTER) chapterOrder: String = MangaSortOrderParam.DESC.value,
    @Query(MangaDexApiQueries.INCLUDES)
    includes: List<String> = listOf(MangaIncludesParam.SCANLATION_GROUP.value),
  ): ChapterListResponse

  @GET(MangaDexApiEndpoints.CHAPTER_ID)
  suspend fun getChapterDetails(
    @Path(MangaDexApiPaths.ID) chapterId: String,
    @Query(MangaDexApiQueries.INCLUDES)
    includes: List<String> = listOf(
      MangaIncludesParam.MANGA.value,
      MangaIncludesParam.SCANLATION_GROUP.value
    ),
  ): ChapterDetailsResponse

  @GET(MangaDexApiEndpoints.AT_HOME_SERVER_ID)
  suspend fun getChapterPages(@Path(MangaDexApiPaths.ID) chapterId: String): AtHomeServerResponse

  @GET(MangaDexApiEndpoints.MANGA_TAG)
  suspend fun getTagList(): TagListResponse

  @GET(MangaDexApiEndpoints.MANGA)
  suspend fun getMangaListByTag(
    @Query(MangaDexApiQueries.LIMIT) limit: Int = 20,
    @Query(MangaDexApiQueries.OFFSET) offset: Int = 0,
    @Query(MangaDexApiQueries.INCLUDED_TAGS) tagId: String,
    // sorting
    @Query(MangaDexApiQueries.ORDER_UPDATED_AT) lastUpdated: String? = null, // latest update
    @Query(MangaDexApiQueries.ORDER_FOLLOWED_COUNT) followedCount: String? = null, // trending
    @Query(MangaDexApiQueries.ORDER_CREATED_AT) createdAt: String? = null, // new release
    @Query(MangaDexApiQueries.ORDER_RATING) rating: String? = null, // top rated
    // filtering
    @Query(MangaDexApiQueries.STATUS)
    status: List<String> = listOf(MangaStatusParam.ON_GOING.value), // ongoing, completed, hiatus, cancelled
    @Query(MangaDexApiQueries.CONTENT_RATING)
    contentRating: List<String> = listOf(MangaContentRatingParam.SAFE.value), // safe, suggestive, erotica
    @Query(MangaDexApiQueries.INCLUDES)
    includes: List<String> = listOf(
      MangaIncludesParam.COVER_ART.value,
      MangaIncludesParam.AUTHOR.value,
      MangaIncludesParam.ARTIST.value
    ),
  ): MangaListResponse
}
