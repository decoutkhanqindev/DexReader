package com.decoutkhanqindev.dexreader.domain.exception

sealed class InfrastructureException(
  message: String? = null,
  cause: Throwable? = null,
) : DomainException(message, cause) {
  class ServerUnavailable(cause: Throwable? = null) :
    InfrastructureException("Server is not responding", cause)

  class Unexpected(cause: Throwable? = null) :
    InfrastructureException("An unexpected error occurred", cause)
}
