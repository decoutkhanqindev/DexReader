package com.decoutkhanqindev.dexreader.data.repository

import com.decoutkhanqindev.dexreader.data.mapper.toDomain
import com.decoutkhanqindev.dexreader.data.network.mangadex_api.MangaDexApiService
import com.decoutkhanqindev.dexreader.di.UploadUrlQualifier
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.model.Tag
import com.decoutkhanqindev.dexreader.domain.repository.TagRepository
import com.decoutkhanqindev.dexreader.utils.runSuspendCatching
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
  private val mangaDexApiService: MangaDexApiService,
  @UploadUrlQualifier private val uploadUrl: String
) : TagRepository {
  override suspend fun getTagList(): Result<List<Tag>> =
    runSuspendCatching(Dispatchers.IO) {
      mangaDexApiService.getTagList().data.map { it.toDomain() }
    }

  override suspend fun getMangaListByTag(
    tagId: String,
    offset: Int
  ): Result<List<Manga>> =
    runSuspendCatching(Dispatchers.IO) {
      mangaDexApiService.getMangaListByTag(
        tagId = tagId,
        offset = offset
      ).data.map { it.toDomain(uploadUrl) }
    }
}