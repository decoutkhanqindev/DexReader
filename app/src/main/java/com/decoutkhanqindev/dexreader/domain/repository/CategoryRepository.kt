package com.decoutkhanqindev.dexreader.domain.repository

import com.decoutkhanqindev.dexreader.domain.model.Category
import com.decoutkhanqindev.dexreader.domain.model.Manga

interface CategoryRepository {
  suspend fun getTagList(): Result<List<Category>>
  suspend fun getMangaListByTag(
    tagId: String,
    offset: Int = 0
  ): Result<List<Manga>>
}