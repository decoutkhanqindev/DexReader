package com.decoutkhanqindev.dexreader.presentation.error

import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.decoutkhanqindev.dexreader.R

@Stable
sealed class UserError(@param:StringRes val messageRes: Int) {
  sealed class Email(@StringRes messageRes: Int) : UserError(messageRes) {
    data object Required : Email(R.string.email_required)
    data object Invalid : Email(R.string.invalid_email_format)
    data object AlreadyInUse : Email(R.string.error_email_already_in_use)
  }

  sealed class Password(@StringRes messageRes: Int) : UserError(messageRes) {
    data object Required : Password(R.string.password_required)
    data object Weak : Password(R.string.password_min_length)
    data object Incorrect : Password(R.string.error_incorrect_password)
  }

  sealed class ConfirmPassword(@StringRes messageRes: Int) : UserError(messageRes) {
    data object Required : ConfirmPassword(R.string.confirm_password_required)
    data object DoesNotMatch : ConfirmPassword(R.string.password_dont_match)
  }

  sealed class Name(@StringRes messageRes: Int) : UserError(messageRes) {
    data object Required : Name(R.string.name_required)
  }

  data object NotFound : UserError(R.string.error_user_not_found)
  data object NetworkUnavailable : UserError(R.string.no_internet_connection)
  data object RegistrationFailed : UserError(R.string.oops_something_went_wrong_please_try_again)
  data object Unexpected : UserError(R.string.oops_something_went_wrong_please_try_again)
}
