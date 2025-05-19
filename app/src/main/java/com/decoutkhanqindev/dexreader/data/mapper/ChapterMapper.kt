package com.decoutkhanqindev.dexreader.data.mapper

import com.decoutkhanqindev.dexreader.data.network.dto.ChapterDto
import com.decoutkhanqindev.dexreader.domain.model.Chapter

fun ChapterDto.toDomain(): Chapter {
  val mangaId = relationships?.find { it.type == "manga" }?.id ?: "0"
  val title = attributes.title ?: "Unknown Title"
  val chapterNumber = attributes.chapter ?: "0"
  val volume = attributes.volume ?: "0"
  val publishAt = attributes.publishAt ?: "0"
  val translatedLanguage = attributes.translatedLanguage ?: "en"

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