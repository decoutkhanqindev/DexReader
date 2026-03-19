package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.exception.InfrastructureException
import com.decoutkhanqindev.dexreader.domain.exception.ValidationException
import com.decoutkhanqindev.dexreader.presentation.error.FeatureError
import com.decoutkhanqindev.dexreader.presentation.error.UserError

object ErrorMapper {
  fun Throwable.toFeatureError() =
    when (this) {
      is InfrastructureException.NetworkUnavailable -> FeatureError.NetworkUnavailable
      is InfrastructureException.ServerUnavailable -> FeatureError.ServerUnavailable
      is BusinessException.Resource.AccessDenied -> FeatureError.AccessDenied
      is BusinessException.Resource.MangaNotFound -> FeatureError.MangaNotFound
      is BusinessException.Resource.ChapterNotFound -> FeatureError.ChapterNotFound
      is BusinessException.Resource.ChapterDataNotFound -> FeatureError.ChapterNotFound
      else -> FeatureError.Generic
    }

  fun Throwable.toUserError() =
    when (this) {
      is ValidationException.Email.Empty -> UserError.Email.Required
      is ValidationException.Email.Invalid -> UserError.Email.Invalid
      is ValidationException.Password.Empty -> UserError.Password.Required
      is ValidationException.Password.TooWeak -> UserError.Password.Weak
      is ValidationException.ConfirmPassword.Empty -> UserError.ConfirmPassword.Required
      is ValidationException.ConfirmPassword.Mismatch -> UserError.ConfirmPassword.DoesNotMatch
      is ValidationException.Name.Empty -> UserError.Name.Required
      is BusinessException.Auth.UserNotFound -> UserError.NotFound
      is BusinessException.Auth.UserAlreadyExists -> UserError.Email.AlreadyInUse
      is BusinessException.Auth.InvalidCredentials -> UserError.Password.Incorrect
      is BusinessException.Auth.RegistrationFailed -> UserError.RegistrationFailed
      else -> null
    }
}
