package com.decoutkhanqindev.dexreader.presentation.screens.manga_details.components.chapters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decoutkhanqindev.dexreader.presentation.model.manga.ChapterModel
import com.decoutkhanqindev.dexreader.presentation.model.user.ReadingHistoryModel
import com.decoutkhanqindev.dexreader.presentation.screens.common.animateItemOnAppear
import com.decoutkhanqindev.dexreader.presentation.screens.common.onScalableClick
import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme

@Composable
fun MangaChapterItem(
    lastChapter: String,
    chapter: ChapterModel,
    readingHistory: ReadingHistoryModel? = null,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    onChapterClick: (
        chapterId: String,
        lastReadPage: Int,
        mangaId: String,
    ) -> Unit,
) {
    val volume = chapter.volume
    val number = chapter.number
    val onChapterClick = remember(chapter.id, readingHistory?.lastReadPage) {
        { onChapterClick(chapter.id, readingHistory?.lastReadPage ?: 0, chapter.mangaId) }
    }

    Surface(
        modifier = modifier
            .animateItemOnAppear()
            .onScalableClick(shape = shape) { onChapterClick() },
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Ch. $number",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    if (volume != "null" && volume.isNotEmpty()) {
                        Text(
                            text = " • Vol. $volume",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }

                if (readingHistory != null) {
                    val progress = (readingHistory.lastReadPage.toFloat() / readingHistory.pageCount.toFloat())
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = chapter.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (lastChapter == number) {
                        Text(
                            text = "NEWEST",
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.Black,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }

                    Text(
                        text = chapter.publishedAt,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}

private val previewChapter = ChapterModel(
    id = "c-001",
    mangaId = "m-001",
    title = "Romance Dawn",
    number = "1",
    volume = "1",
    publishedAt = "2024-01-01",
)

@Preview
@Composable
private fun MangaChapterItemNoHistoryPreview() {
    DexReaderTheme {
        MangaChapterItem(
            lastChapter = "1110",
            chapter = previewChapter,
            readingHistory = null,
            modifier = Modifier.fillMaxWidth(),
            onChapterClick = { _, _, _ -> }
        )
    }
}

@Preview
@Composable
private fun MangaChapterItemWithHistoryPreview() {
    DexReaderTheme {
        MangaChapterItem(
            lastChapter = "1110",
            chapter = previewChapter,
            readingHistory = ReadingHistoryModel(
                id = "rh-001",
                mangaId = "m-001",
                mangaTitle = "One Piece",
                mangaCoverUrl = "",
                chapterId = "c-001",
                chapterTitle = "Romance Dawn",
                chapterNumber = "1",
                chapterVolume = "1",
                lastReadPage = 12,
                pageCount = 46,
                lastReadAt = "2 hours ago",
            ),
            modifier = Modifier.fillMaxWidth(),
            onChapterClick = { _, _, _ -> }
        )
    }
}
