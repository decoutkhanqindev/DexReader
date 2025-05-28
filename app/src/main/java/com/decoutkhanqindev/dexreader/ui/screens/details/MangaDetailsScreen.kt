package com.decoutkhanqindev.dexreader.ui.screens.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.ui.components.bar.MangaDetailsTopBar
import com.decoutkhanqindev.dexreader.ui.components.content.AllItemLoadedText
import com.decoutkhanqindev.dexreader.ui.components.content.LoadMoreText
import com.decoutkhanqindev.dexreader.ui.components.content.MoveToTopButton
import com.decoutkhanqindev.dexreader.ui.components.states.ErrorScreen
import com.decoutkhanqindev.dexreader.ui.components.states.ListLoadingScreen
import com.decoutkhanqindev.dexreader.ui.components.states.LoadPageErrorScreen
import com.decoutkhanqindev.dexreader.ui.components.states.LoadingScreen
import com.decoutkhanqindev.dexreader.ui.components.states.NextPageLoadingScreen
import com.decoutkhanqindev.dexreader.utils.toFullLanguageName
import com.decoutkhanqindev.dexreader.utils.toLanguageCode
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun MangaDetailsScreen(
  onNavigateBack: () -> Unit,
  onSelectedTag: (String) -> Unit,
  onSelectedChapter: (String) -> Unit,
  viewModel: MangaDetailsViewModel = hiltViewModel(),
  modifier: Modifier = Modifier
) {
  val infoUiState by viewModel.infoUiState.collectAsStateWithLifecycle()
  val chaptersUiState by viewModel.chaptersUiState.collectAsStateWithLifecycle()
  val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()

  Scaffold(
    topBar = {
      MangaDetailsTopBar(
        title = stringResource(R.string.manga_details),
        onNavigateBack = onNavigateBack,
        modifier = Modifier.fillMaxWidth()
      )
    },
    content = { innerPadding ->
      MangaDetailsContent(
        infoUiState = infoUiState,
        chaptersUiState = chaptersUiState,
        selectedLanguage = selectedLanguage.toFullLanguageName(),
        onSelectedLanguage = { viewModel.setChapterLanguage(it.toLanguageCode()) },
        onLoadNextChapterListPage = { viewModel.loadChapterListNextPage() },
        onRetryLoadNextChapterListPage = { viewModel.retryLoadNextChapterListPage() },
        onSelectedTag = onSelectedTag,
        onSelectedChapter = onSelectedChapter,
        onRetry = { viewModel.retry() },
        modifier = modifier
          .padding(innerPadding)
          .fillMaxSize()
      )
    }
  )
}

@Composable
private fun MangaDetailsContent(
  infoUiState: MangaInfoUiState,
  chaptersUiState: MangaChaptersUiState,
  selectedLanguage: String,
  onSelectedLanguage: (String) -> Unit,
  onLoadNextChapterListPage: () -> Unit,
  onRetryLoadNextChapterListPage: () -> Unit,
  onSelectedTag: (String) -> Unit,
  onSelectedChapter: (String) -> Unit,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  val lazyListState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  Box(modifier = modifier) {
    when (infoUiState) {
      MangaInfoUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())

      MangaInfoUiState.Error -> ErrorScreen(
        message = stringResource(R.string.oops_something_went_wrong_please_try_again),
        onRetry = onRetry,
        modifier = Modifier.fillMaxSize()
      )

      is MangaInfoUiState.Success -> {
        val manga = infoUiState.manga

        // Background cover art
        AsyncImage(
          model = ImageRequest.Builder(LocalContext.current)
            .data(manga.coverUrl)
            .crossfade(true)
            .size(1080)
            .build(),
          contentDescription = manga.title,
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )

        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(
                  MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                  MaterialTheme.colorScheme.surface.copy(alpha = 1.5f),
                )
              )
            )
        ) {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState
          ) {
            // Manga Info
            item {
              MangaInfoHeader(
                manga = manga,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(4.dp)
                  .padding(bottom = 16.dp)
              )
              MangaSummary(
                manga = manga,
                onSelectedTag = onSelectedTag,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(bottom = 16.dp)
              )
            }
            // Manga Chapters List
            item {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 4.dp)
                  .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Text(
                  text = stringResource(R.string.chapters),
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.ExtraBold,
                  modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
                )
                LanguageDropdownMenu(
                  languages = manga.availableTranslatedLanguages,
                  selectedLanguage = selectedLanguage,
                  onSelectedLanguage = onSelectedLanguage,
                  modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
                )
              }
            }

            when (chaptersUiState) {
              MangaChaptersUiState.FirstPageLoading -> item {
                ListLoadingScreen(modifier = Modifier.fillMaxSize())
              }

              MangaChaptersUiState.FirstPageError -> item {
                LoadPageErrorScreen(
                  message = stringResource(R.string.something_went_wrong_while_loading_chapters_please_try_again),
                  onRetry = onRetry,
                  modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp)
                )
              }

              is MangaChaptersUiState.Content -> {
                val chapterList = chaptersUiState.items
                val nextPageState = chaptersUiState.nextPageState

                if (chapterList.isEmpty()) {
                  item {
                    Text(
                      text = stringResource(R.string.no_chapters_available),
                      style = MaterialTheme.typography.titleMedium,
                      fontWeight = FontWeight.Bold,
                      fontStyle = FontStyle.Italic,
                      textAlign = TextAlign.Center,
                      modifier = Modifier.fillMaxWidth(),
                    )
                  }
                } else {
                  items(chapterList, key = { it.id }) { chapter ->
                    ChapterItem(
                      chapter = chapter,
                      onSelectedChapter = onSelectedChapter,
                      modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .padding(horizontal = 4.dp)
                    )
                  }

                  // Load more chapters
                  when (nextPageState) {
                    MangaChaptersNextPageState.LOADING -> item {
                      NextPageLoadingScreen(
                        modifier = Modifier
                          .fillMaxWidth()
                          .padding(bottom = 12.dp)
                      )
                    }

                    MangaChaptersNextPageState.ERROR -> item {
                      LoadPageErrorScreen(
                        message = stringResource(R.string.can_t_load_next_chapter_page_please_try_again),
                        onRetry = onRetryLoadNextChapterListPage,
                        modifier = Modifier
                          .fillMaxWidth()
                          .padding(top = 8.dp)
                      )
                    }

                    MangaChaptersNextPageState.IDLE -> item {
                      LoadMoreText(
                        onLoadMore = onLoadNextChapterListPage,
                        modifier = Modifier
                          .fillMaxWidth()
                          .padding(horizontal = 8.dp)
                          .padding(bottom = 12.dp)
                      )

//                    LaunchedEffect(lazyListState) {
//                      snapshotFlow {
//                        val layoutInfo = lazyListState.layoutInfo
//                        val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
//                        val totalItems = layoutInfo.totalItemsCount
//                        // Emit the last visible item index and total items count
//                        lastVisibleItemIndex to totalItems
//                      }
//                        .collect { (lastVisibleItem, totalItems) ->
//                          val VISIBLE_THRESHOLD = 2
//                          if (lastVisibleItem + VISIBLE_THRESHOLD >= totalItems) {
//                            onLoadNextChapterPage()
//                          }
//                        }
//                    }
                    }

                    MangaChaptersNextPageState.NO_MORE_ITEMS -> item {
                      AllItemLoadedText(
                        title = stringResource(R.string.all_chapters_loaded),
                        modifier = Modifier
                          .fillMaxWidth()
                          .padding(horizontal = 8.dp)
                          .padding(bottom = 12.dp)
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    val isVisibleFloatingButton = chaptersUiState is MangaChaptersUiState.Content &&
        chaptersUiState.items.size > 20 &&
        lazyListState.firstVisibleItemIndex > 0
    AnimatedVisibility(
      visible = isVisibleFloatingButton,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
    ) {
      MoveToTopButton(
        onClick = {
          coroutineScope.launch {
            lazyListState.animateScrollToItem(0)
          }
        },
        modifier = Modifier.size(56.dp)
      )
    }
  }
}

@Composable
private fun MangaInfoHeader(
  manga: Manga,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    MangaCoverArt(
      manga = manga,
      modifier = Modifier
        .weight(0.4f)
        .fillMaxWidth()
        .height(222.dp)
    )
    MangaInfo(
      manga = manga,
      modifier = Modifier.weight(0.6f)
    )
  }
}

@Composable
private fun MangaSummary(
  manga: Manga,
  onSelectedTag: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var isExpanded by rememberSaveable { mutableStateOf(false) }

  Column(modifier = modifier) {
    Text(
      text = stringResource(R.string.summary),
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.ExtraBold,
      modifier = Modifier
        .padding(horizontal = 4.dp)
        .padding(bottom = 12.dp)
    )
    Text(
      text = manga.description,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.Bold,
      maxLines = if (isExpanded) Int.MAX_VALUE else 3,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier
        .padding(horizontal = 4.dp)
        .clickable { isExpanded = !isExpanded }
        .padding(bottom = 8.dp))
    TagList(
      manga = manga,
      onSelectedTag = onSelectedTag,
      modifier = Modifier.fillMaxWidth()
    )
  }
}

@Composable
private fun MangaCoverArt(
  manga: Manga,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = MaterialTheme.shapes.large,
    elevation = CardDefaults.cardElevation(8.dp),
  ) {
    AsyncImage(
      model = ImageRequest.Builder(LocalContext.current)
        .data(manga.coverUrl)
        .crossfade(true)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .diskCachePolicy(CachePolicy.ENABLED)
        .build(),
      contentDescription = manga.title,
      error = painterResource(R.drawable.placeholder),
      placeholder = painterResource(R.drawable.placeholder),
      contentScale = ContentScale.FillBounds,
      modifier = Modifier.fillMaxSize()
    )
  }
}

@Composable
private fun MangaInfo(
  manga: Manga,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.Start,
  ) {
    Text(
      text = manga.title,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.ExtraBold,
      overflow = TextOverflow.Ellipsis,
      modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(
      text = stringResource(R.string.author, manga.author),
      fontWeight = FontWeight.Bold,
      fontStyle = FontStyle.Italic,
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(
      text = stringResource(R.string.artist, manga.artist),
      fontWeight = FontWeight.Bold,
      fontStyle = FontStyle.Italic,
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(
      text = stringResource(R.string.year, manga.year),
      fontWeight = FontWeight.Bold,
      fontStyle = FontStyle.Italic,
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(
      text = stringResource(R.string.status, manga.status.capitalize(Locale.US)),
      fontWeight = FontWeight.Bold,
      fontStyle = FontStyle.Italic,
      style = MaterialTheme.typography.bodyMedium,
      modifier = Modifier.padding(bottom = 4.dp)
    )
  }
}

@Composable
private fun TagList(
  manga: Manga,
  onSelectedTag: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier) {
    LazyRow(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(2.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      items(manga.genres) { tag ->
        TagItem(
          tag = tag,
          onSelectedTag = onSelectedTag,
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp)
        )
      }
    }
  }
}

@Composable
private fun TagItem(
  tag: String,
  onSelectedTag: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = MaterialTheme.shapes.large,
    elevation = CardDefaults.cardElevation(4.dp),
    onClick = { onSelectedTag(tag) }) {
    Text(
      text = tag,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .padding(8.dp)
        .fillMaxSize()
    )
  }
}

@Composable
private fun ChapterItem(
  chapter: Chapter,
  onSelectedChapter: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = MaterialTheme.shapes.large,
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    elevation = CardDefaults.cardElevation(4.dp),
    onClick = { onSelectedChapter(chapter.id) }
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
      verticalArrangement = Arrangement.Center,
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = stringResource(R.string.volume, chapter.volume),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.ExtraBold,
        )
        Text(
          text = stringResource(R.string.separate),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.ExtraBold,
          modifier = Modifier.padding(horizontal = 4.dp)
        )
        Text(
          text = stringResource(R.string.chapter, chapter.chapterNumber),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.ExtraBold,
        )
//        Icon(
//          imageVector = if (isRead) Icons.Default.CheckCircle else Icons.Default.Check,
//          contentDescription = if (isRead)
//            stringResource(R.string.chapter_read)
//          else
//            stringResource(R.string.chapter_unread),
//          tint = if (isRead) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
//        )
      }
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = chapter.title,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold,
          fontStyle = FontStyle.Italic,
          modifier = Modifier
            .weight(0.7f)
            .fillMaxWidth()
        )
        Text(
          text = chapter.publishAt,
          style = MaterialTheme.typography.bodyMedium,
          fontStyle = FontStyle.Italic,
          textAlign = TextAlign.End,
          modifier = Modifier
            .weight(0.3f)
            .fillMaxWidth()
        )
      }
    }
  }
}

@Composable
private fun LanguageDropdownMenu(
  languages: List<String>,
  selectedLanguage: String,
  onSelectedLanguage: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var isExpanded by rememberSaveable { mutableStateOf(false) }

  Box(modifier = modifier) {
    Text(
      text = selectedLanguage,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.End,
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { isExpanded = true }
    )
    DropdownMenu(
      expanded = isExpanded,
      onDismissRequest = { isExpanded = false },
      modifier = Modifier.fillMaxWidth(),
    ) {
      if (languages.isEmpty()) {
        DropdownMenuItem(
          text = {
            Text(
              text = stringResource(R.string.no_languages_available),
              style = MaterialTheme.typography.bodyMedium,
              fontStyle = FontStyle.Italic
            )
          },
          onClick = { isExpanded = false },
          modifier = Modifier.fillMaxWidth()
        )
      } else {
        languages.forEach { language ->
          LanguageDropdownItem(
            language = language,
            isSelected = language == selectedLanguage,
            onSelectedLanguage = {
              onSelectedLanguage(language)
              isExpanded = false
            },
            modifier = Modifier.fillMaxWidth()
          )
        }
      }
    }
  }
}

@Composable
private fun LanguageDropdownItem(
  language: String,
  isSelected: Boolean,
  onSelectedLanguage: () -> Unit,
  modifier: Modifier = Modifier
) {
  DropdownMenuItem(
    text = {
      Text(
        text = language,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal
      )
    },
    onClick = onSelectedLanguage,
    modifier = modifier
  )
}
