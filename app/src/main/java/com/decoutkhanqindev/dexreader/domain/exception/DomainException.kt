package com.decoutkhanqindev.dexreader.domain.exception

sealed class DomainException(message: String? = null, cause: Throwable? = null) :
  Exception(message, cause) {
  class NetworkUnavailable(cause: Throwable? = null) : DomainException(cause = cause)
  class ServiceRequestFailed(cause: Throwable? = null) : DomainException(cause = cause)
  class Unknown(cause: Throwable? = null) : DomainException(cause = cause)
}
