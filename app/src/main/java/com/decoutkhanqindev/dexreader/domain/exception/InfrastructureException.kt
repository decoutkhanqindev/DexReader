package com.decoutkhanqindev.dexreader.domain.exception

sealed class InfrastructureException(
  message: String? = null,
  cause: Throwable? = null,
) : DomainException(message, cause) {
  data class NetworkUnavailable(val rootCause: Throwable? = null) :
    InfrastructureException("No internet connection", rootCause)

  data class ServerUnavailable(val rootCause: Throwable? = null) :
    InfrastructureException("Server is not responding", rootCause)

  data class Unexpected(val rootCause: Throwable? = null) :
    InfrastructureException("An unexpected error occurred", rootCause)
}
