package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.mangadex_api.dto.ChapterDto
import com.decoutkhanqindev.dexreader.domain.model.Chapter
import com.decoutkhanqindev.dexreader.utils.toFullLanguageName
import com.decoutkhanqindev.dexreader.utils.toTimeAgo

fun ChapterDto.toDomain(): Chapter {
  val mangaId = relationships?.find { it.type == "manga" }?.id ?: "0"
  val title = attributes.title ?: "Untitled"
  val chapterNumber = attributes.chapter ?: "0"
  val volume = attributes.volume ?: "0"
  val publishAt = attributes.publishAt.toTimeAgo()
  val translatedLanguage = attributes.translatedLanguage?.toFullLanguageName() ?: "en"

  return Chapter(
    id = id,
    mangaId = mangaId,
    title = title,
    chapterNumber = chapterNumber,
    volume = volume,
    publishAt = publishAt,
    translatedLanguage = translatedLanguage
  )
}