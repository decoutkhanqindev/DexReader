package com.decoutkhanqindev.dexreader.domain.exception

sealed class BusinessException(
  message: String? = null,
  cause: Throwable? = null,
) : DomainException(message, cause) {

  sealed class Auth(message: String, cause: Throwable? = null) : BusinessException(message, cause) {
    data class InvalidCredentials(val rootCause: Throwable? = null) :
      Auth("Invalid email or password", rootCause)

    data class UserNotFound(val rootCause: Throwable? = null) :
      Auth("User account not found", rootCause)

    data class UserAlreadyExists(val rootCause: Throwable? = null) :
      Auth("Email is already registered", rootCause)

    data class RegistrationFailed(val rootCause: Throwable? = null) :
      Auth("Registration failed", rootCause)
  }

  sealed class Resource(message: String, cause: Throwable? = null) :
    BusinessException(message, cause) {
    data class MangaNotFound(val rootCause: Throwable? = null) :
      Resource("Manga not found", rootCause)

    data class ChapterNotFound(val rootCause: Throwable? = null) :
      Resource("Chapter not found", rootCause)

    data class ChapterDataNotFound(val rootCause: Throwable? = null) :
      Resource("Chapter data not found", rootCause)

    data class AccessDenied(val rootCause: Throwable? = null) :
      Resource("Access denied", rootCause)
  }
}
