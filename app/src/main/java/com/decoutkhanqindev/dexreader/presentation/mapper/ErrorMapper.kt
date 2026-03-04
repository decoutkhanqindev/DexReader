package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.exception.DomainException
import com.decoutkhanqindev.dexreader.domain.exception.MangaException
import com.decoutkhanqindev.dexreader.domain.exception.UserException
import com.decoutkhanqindev.dexreader.presentation.model.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.model.error.UserError

object ErrorMapper {
  fun Throwable.toFeatureError(): FeatureError = when (this) {
    is DomainException.NetworkUnavailable -> FeatureError.Network
    is MangaException.NotFound -> FeatureError.MangaNotFound
    is MangaException.ChapterNotFound -> FeatureError.ChapterNotFound
    else -> FeatureError.Generic
  }

  fun Throwable.toUserError(): UserError? = when (this) {
    is UserException.NotFound -> UserError.UserNotFoundError
    is UserException.AlreadyExists -> UserError.EmailError.AlreadyInUse
    is UserException.Email.Empty -> UserError.EmailError.Required
    is UserException.Email.Invalid -> UserError.EmailError.Invalid
    is UserException.Password.Empty -> UserError.PasswordError.Required
    is UserException.Password.Weak -> UserError.PasswordError.Weak
    is UserException.Password.Incorrect -> UserError.PasswordError.Incorrect
    is UserException.ConfirmPassword.Empty -> UserError.ConfirmPasswordError.Required
    is UserException.ConfirmPassword.Mismatch -> UserError.ConfirmPasswordError.DoesNotMatch
    is UserException.Name.Empty -> UserError.NameError.Required
    else -> null
  }
}
