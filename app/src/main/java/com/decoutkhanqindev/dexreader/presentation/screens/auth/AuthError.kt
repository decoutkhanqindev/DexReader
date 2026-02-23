package com.decoutkhanqindev.dexreader.presentation.screens.auth

import androidx.annotation.StringRes
import com.decoutkhanqindev.dexreader.R

sealed class AuthError(@StringRes val message: Int) {
  sealed class EmailError(@StringRes message: Int) : AuthError(message) {
    data object Invalid : EmailError(R.string.invalid_email_format)
    data object Required : EmailError(R.string.email_required)
    data object AlreadyInUse : EmailError(R.string.error_email_already_in_use)
  }

  sealed class PasswordError(@StringRes message: Int) : AuthError(message) {
    data object Weak : PasswordError(R.string.password_min_length)
    data object Incorrect : PasswordError(R.string.error_incorrect_password)
    data object Required : PasswordError(R.string.password_required)
  }

  sealed class ConfirmPasswordError(@StringRes message: Int) : AuthError(message) {
    data object DoesNotMatch : ConfirmPasswordError(R.string.password_dont_match)
    data object Required : ConfirmPasswordError(R.string.confirm_password_required)
  }

  sealed class NameError(@StringRes message: Int) : AuthError(message) {
    data object Required : NameError(R.string.name_required)
  }

  data object UserNotFoundError : AuthError(R.string.error_user_not_found)

  data object SendResetPasswordError : AuthError(R.string.error_send_reset_password)

  data object UnknownError : AuthError(R.string.error_unknown)
}
