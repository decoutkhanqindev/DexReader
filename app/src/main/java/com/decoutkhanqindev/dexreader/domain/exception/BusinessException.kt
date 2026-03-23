package com.decoutkhanqindev.dexreader.domain.exception

sealed class BusinessException(
  message: String? = null,
  cause: Throwable? = null,
) : DomainException(message, cause) {

  sealed class Auth(message: String, cause: Throwable? = null) : BusinessException(message, cause) {
    class InvalidCredentials(cause: Throwable? = null) :
      Auth("Invalid email or password", cause)

    class UserNotFound(cause: Throwable? = null) :
      Auth("User account not found", cause)

    class UserAlreadyExists(cause: Throwable? = null) :
      Auth("Email is already registered", cause)

    class RegistrationFailed(cause: Throwable? = null) :
      Auth("Registration failed", cause)
  }

  sealed class Resource(message: String, cause: Throwable? = null) :
    BusinessException(message, cause) {
    class MangaNotFound(cause: Throwable? = null) :
      Resource("Manga not found", cause)

    class ChapterNotFound(cause: Throwable? = null) :
      Resource("Chapter not found", cause)

    class ChapterDataNotFound(cause: Throwable? = null) :
      Resource("Chapter data not found", cause)

    class AccessDenied(cause: Throwable? = null) :
      Resource("Access denied", cause)
  }
}
