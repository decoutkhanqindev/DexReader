package com.decoutkhanqindev.dexreader.presentation.mapper

import com.decoutkhanqindev.dexreader.domain.exception.BusinessException
import com.decoutkhanqindev.dexreader.domain.exception.InfrastructureException
import com.decoutkhanqindev.dexreader.domain.exception.ValidationException
import com.decoutkhanqindev.dexreader.presentation.model.error.FeatureUiError
import com.decoutkhanqindev.dexreader.presentation.model.error.UserUiError

object ErrorMapper {
  fun Throwable.toFeatureUiError(): FeatureUiError =
    when (this) {
      is InfrastructureException.NetworkUnavailable -> FeatureUiError.NetworkUnavailable
      is InfrastructureException.ServerUnavailable -> FeatureUiError.ServerUnavailable
      is BusinessException.Resource.AccessDenied -> FeatureUiError.AccessDenied
      is BusinessException.Resource.MangaNotFound -> FeatureUiError.MangaNotFound
      is BusinessException.Resource.ChapterNotFound -> FeatureUiError.ChapterNotFound
      is BusinessException.Resource.ChapterDataNotFound -> FeatureUiError.ChapterNotFound
      else -> FeatureUiError.Generic
    }

  fun Throwable.toUserUiError(): UserUiError? =
    when (this) {
      is ValidationException.Email.Empty -> UserUiError.Email.Required
      is ValidationException.Email.Invalid -> UserUiError.Email.Invalid
      is ValidationException.Password.Empty -> UserUiError.Password.Required
      is ValidationException.Password.TooWeak -> UserUiError.Password.Weak
      is ValidationException.ConfirmPassword.Empty -> UserUiError.ConfirmPassword.Required
      is ValidationException.ConfirmPassword.Mismatch -> UserUiError.ConfirmPassword.DoesNotMatch
      is ValidationException.Name.Empty -> UserUiError.Name.Required
      is BusinessException.Auth.UserNotFound -> UserUiError.NotFound
      is BusinessException.Auth.UserAlreadyExists -> UserUiError.Email.AlreadyInUse
      is BusinessException.Auth.InvalidCredentials -> UserUiError.Password.Incorrect
      is BusinessException.Auth.RegistrationFailed -> UserUiError.RegistrationFailed
      else -> null
    }
}
