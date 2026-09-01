package com.decoutkhanqindev.dexreader.presentation.screens.categories


import com.decoutkhanqindev.dexreader.domain.entity.value.category.CategoryType
import com.decoutkhanqindev.dexreader.domain.entity.value.criteria.MangaSortCriteria
import com.decoutkhanqindev.dexreader.domain.usecase.category.GetCategoryListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.manga.GetMangaListUseCase
import com.decoutkhanqindev.dexreader.domain.usecase.settings.ObserveContentLanguageUseCase
import com.decoutkhanqindev.dexreader.presentation.mapper.CategoryMapper.toCategoryModel
import com.decoutkhanqindev.dexreader.presentation.mapper.ErrorMapper.toFeatureError
import com.decoutkhanqindev.dexreader.presentation.model.value.category.CategoryTypeValue
import com.decoutkhanqindev.dexreader.presentation.screens.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
  private val getCategoryListUseCase: GetCategoryListUseCase,
  private val getMangaListUseCase: GetMangaListUseCase,
  private val observeContentLanguageUseCase: ObserveContentLanguageUseCase,
) : BaseViewModel() {
  private val _categoryListUiState =
    MutableStateFlow<CategoryListUiState>(CategoryListUiState.Loading)
  val categoryListUiState: StateFlow<CategoryListUiState> = _categoryListUiState.asStateFlow()

  private val _categoryCoverStates =
    MutableStateFlow<PersistentMap<String, CategoryCoverUiState>>(persistentMapOf())
  val categoryCoverStates: StateFlow<ImmutableMap<String, CategoryCoverUiState>> =
    _categoryCoverStates.asStateFlow()

  init {
    fetchTagList()
    observeContentLanguageChange()
  }

  private fun observeContentLanguageChange() {
    vmLaunch {
      observeContentLanguageUseCase()
        .drop(1)
        .collect { result -> result.onSuccess { fetchTagList() } }
    }
  }

  private fun fetchTagList() {
    vmLaunch {
      _categoryListUiState.value = CategoryListUiState.Loading

      getCategoryListUseCase()
        .onSuccess { grouped ->
          val categoryMap =
            CategoryTypeValue.entries
              .filter { it != CategoryTypeValue.UNKNOWN }
              .associateWith { type ->
                (grouped[CategoryType.valueOf(type.name)]
                  ?: persistentListOf()).map { it.toCategoryModel() }.toPersistentList()
              }
              .toImmutableMap()

          _categoryListUiState.value = CategoryListUiState.Success(categoryMap = categoryMap)
        }
        .onFailure { throwable ->
          _categoryListUiState.value = CategoryListUiState.Error(throwable.toFeatureError())
          Timber.tag(this::class.java.simpleName)
            .e("fetchTagList have error: ${throwable.stackTraceToString()}")
        }
    }
  }

  fun loadCategoryCover(categoryId: String) {
    val current = _categoryCoverStates.value[categoryId]
    if (current is CategoryCoverUiState.Loading || current is CategoryCoverUiState.Success) return

    vmLaunch {
      _categoryCoverStates.update { it.put(categoryId, CategoryCoverUiState.Loading) }

      getMangaListUseCase(
        categoryId = categoryId,
        limit = 1,
        sortCriteria = MangaSortCriteria.TRENDING,
        includeStats = false,
      )
        .onSuccess { mangaList ->
          _categoryCoverStates.update {
            it.put(
              categoryId,
              CategoryCoverUiState.Success(mangaList.firstOrNull()?.coverUrl.orEmpty())
            )
          }
        }
        .onFailure { throwable ->
          _categoryCoverStates.update {
            it.put(categoryId, CategoryCoverUiState.Error(throwable.toFeatureError()))
          }
          Timber.tag(this::class.java.simpleName)
            .e("loadCategoryCover($categoryId) have error: ${throwable.stackTraceToString()}")
        }
    }
  }

  fun refresh() {
    fetchTagList()
  }

  fun retry() {
    if (_categoryListUiState.value is CategoryListUiState.Error) fetchTagList()
  }
}
