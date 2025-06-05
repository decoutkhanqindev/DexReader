package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.model.Tag

interface TagRepository {
  suspend fun getTagList(): Result<List<Tag>>
  suspend fun getMangaListByTag(
    tagId: String,
    offset: Int = 0
  ): Result<List<Manga>>
}