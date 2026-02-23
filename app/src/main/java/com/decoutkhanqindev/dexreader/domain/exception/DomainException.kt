package com.decoutkhanqindev.dexreader.domain.exception

/**
 * Base sealed class for all domain-specific exceptions. Replaces infrastructure exceptions
 * (Firebase, Retrofit, etc.) in the domain layer.
 *
 * Hierarchy:
 * - DomainException (base)
 * - AuthException (sealed)
 * - MangaException (sealed)
 * - FavoritesHistoryException (sealed)
 * - GenericException (sealed)
 */
sealed class DomainException(cause: Throwable? = null) : Exception(cause)
