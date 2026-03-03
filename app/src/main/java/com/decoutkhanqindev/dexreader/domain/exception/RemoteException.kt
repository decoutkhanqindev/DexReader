package com.decoutkhanqindev.dexreader.domain.exception

sealed class RemoteException(override val cause: Throwable? = null) : DomainException(cause) {
  data class NetworkUnavailable(override val cause: Throwable? = null) : RemoteException(cause)
  data class RequestFailed(override val cause: Throwable? = null) : RemoteException(cause)
}
