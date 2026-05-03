package com.decoutkhanqindev.dexreader.presentation.error

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.decoutkhanqindev.dexreader.R

@Immutable
sealed class UserError(@param:StringRes val messageRes: Int) {

  @Immutable
  sealed class Email(@StringRes messageRes: Int) : UserError(messageRes) {

    @Immutable
    data object Required : Email(R.string.email_required)

    @Immutable
    data object Invalid : Email(R.string.invalid_email_format)

    @Immutable
    data object AlreadyInUse : Email(R.string.error_email_already_in_use)
  }

  @Immutable
  sealed class Password(@StringRes messageRes: Int) : UserError(messageRes) {

    @Immutable
    data object Required : Password(R.string.password_required)

    @Immutable
    data object Weak : Password(R.string.password_min_length)

    @Immutable
    data object Incorrect : Password(R.string.error_incorrect_password)
  }

  @Immutable
  sealed class ConfirmPassword(@StringRes messageRes: Int) : UserError(messageRes) {
    @Immutable
    data object Required : ConfirmPassword(R.string.confirm_password_required)

    @Immutable
    data object DoesNotMatch : ConfirmPassword(R.string.password_dont_match)
  }

  @Immutable
  sealed class Name(@StringRes messageRes: Int) : UserError(messageRes) {

    @Immutable
    data object Required : Name(R.string.name_required)
  }

  @Immutable
  data object NotFound : UserError(R.string.error_user_not_found)

  @Immutable
  data object NetworkUnavailable : UserError(R.string.no_internet_connection)

  @Immutable
  data object RegistrationFailed : UserError(R.string.oops_something_went_wrong_please_try_again)

  @Immutable
  data object Unexpected : UserError(R.string.oops_something_went_wrong_please_try_again)
}
