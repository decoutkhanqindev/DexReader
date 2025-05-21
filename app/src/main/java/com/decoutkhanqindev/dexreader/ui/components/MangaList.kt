package com.decoutkhanqindev.dexreader.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.decoutkhanqindev.dexreader.R
import com.decoutkhanqindev.dexreader.domain.model.Manga
import com.decoutkhanqindev.dexreader.ui.theme.DexReaderTheme

@Composable
fun HorizontalMangaList(
  mangaList: List<Manga>,
  onMangaClick: (Manga) -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(modifier = modifier) {
    LazyRow(
      modifier = Modifier.wrapContentWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      items(mangaList, key = { it.id }) { manga ->
        MangaItem(
          manga = manga,
          onMangaClick = onMangaClick,
          modifier = Modifier
            .width(180.dp)
            .height(270.dp)
            .padding(4.dp)
        )
      }
    }
  }
}

@Composable
fun VerticalMangaList() {}

@Composable
private fun MangaItem(
  manga: Manga,
  onMangaClick: (Manga) -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier = modifier,
    onClick = { onMangaClick(manga) },
    elevation = CardDefaults.cardElevation(8.dp),
    shape = MaterialTheme.shapes.large,
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
          .data(manga.coverUrl)
          .crossfade(true)
          .build(),
        contentDescription = manga.title,
        error = painterResource(R.drawable.placeholder),
        placeholder = painterResource(R.drawable.placeholder),
        contentScale = ContentScale.FillHeight,
        modifier = Modifier
          .fillMaxSize()
          .weight(2f)
      )
      MangaInfo(
        manga = manga,
        modifier = Modifier
          .weight(1f)
          .fillMaxWidth()
      )
    }
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
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = manga.title,
      fontWeight = FontWeight.ExtraBold,
      style = MaterialTheme.typography.titleMedium,
      textAlign = TextAlign.Center,
      maxLines = 2,
      overflow = TextOverflow.Ellipsis
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = stringResource(R.string.by_author, manga.author),
      fontWeight = FontWeight.Light,
      fontStyle = FontStyle.Italic,
      style = MaterialTheme.typography.bodySmall,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = manga.status,
      fontWeight = FontWeight.Bold,
      fontStyle = FontStyle.Italic,
      style = MaterialTheme.typography.bodySmall,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
  }
}

@Preview(showBackground = true)
@Composable
fun MangaListPreview() {
  DexReaderTheme {
    HorizontalMangaList(
      modifier = Modifier.fillMaxWidth(),
      mangaList = listOf(
        Manga(
          id = "1",
          title = "Sample Manga",
          coverUrl = "",
          description = "This is a sample manga description.",
          author = "Sample Author",
          artist = "Sample Artist",
          genres = listOf("Action", "Adventure"),
          status = "Ongoing",
          lastChapter = "Chapter 10",
        ),
        Manga(
          id = "2",
          title = "Another Sample Manga",
          coverUrl = "",
          description = "This is another sample manga description.",
          author = "Another Author",
          artist = "Another Artist",
          genres = listOf("Fantasy", "Drama"),
          status = "Completed",
          lastChapter = "Chapter 20",
        )
      ),
      onMangaClick = {},
    )
  }
}
