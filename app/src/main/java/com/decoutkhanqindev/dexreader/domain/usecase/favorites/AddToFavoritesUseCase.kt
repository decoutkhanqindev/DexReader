package com.decoutkhanqindev.dexreader.domain.usecase.favorites

import com.decoutkhanqindev.dexreader.domain.model.FavoriteManga
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.domain.repository.FavoritesRepository
import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import javax.inject.Inject

class AddToFavoritesUseCase @Inject constructor(
  private val repository: FavoritesRepository,
) {
  suspend operator fun invoke(userId: String, manga: Manga): Result<Unit> =
    runSuspendResultCatching {
      val favoriteManga = FavoriteManga(
        id = manga.id,
        title = manga.title,
        coverUrl = manga.coverUrl,
        author = manga.author,
        status = manga.status,
        addedAt = null
      )
      repository.addToFavorites(userId, favoriteManga)
    }
}