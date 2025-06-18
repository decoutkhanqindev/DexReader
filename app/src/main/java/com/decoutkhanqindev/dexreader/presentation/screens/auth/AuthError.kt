package com.decoutkhanqindev.dexreader.presentation.screens.auth

sealed class AuthError(val message: String) {
  sealed class EmailError(message: String) : AuthError(message) {
    object Invalid : EmailError("Invalid email format.")
    object Required : EmailError("Email is required.")
    object AlreadyInUse : EmailError("Email already in use.")
  }

  sealed class PasswordError(message: String) : AuthError(message) {
    object Weak : PasswordError("Password must be at least 8 characters.")
    object Incorrect : PasswordError("Incorrect password.")
    object Required : PasswordError("Password is required.")
  }

  sealed class ConfirmPasswordError(message: String) : AuthError(message) {
    object DoesNotMatch : ConfirmPasswordError("Passwords do not match.")
    object Required : ConfirmPasswordError("Confirm password is required.")
  }

  sealed class NameError(message: String) : AuthError(message) {
    object Required : NameError("Name is required.")
  }

  object UserNotFoundError: AuthError("User not found.")

  object SendResetPasswordError: AuthError("Error sending reset password email.")

  object UnknownError : AuthError("An unexpected error occurred.")
}