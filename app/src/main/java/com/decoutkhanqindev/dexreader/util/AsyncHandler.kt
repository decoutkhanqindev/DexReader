package com.decoutkhanqindev.dexreader.util

import com.decoutkhanqindev.dexreader.util.AsyncHandler.runSuspendResultCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

/**
 * Utilities for structured error handling in suspend functions and Flow transformations.
 *
 * **Why [CancellationException] is always rethrown:**
 * Since [CancellationException] is a subclass of [Throwable] (and [Exception]), a generic
 * catch block would swallow it — wrapping it into [Result.failure] instead of propagating it.
 * This breaks coroutine structured concurrency: the coroutine would continue running even after
 * its parent scope is cancelled ("zombie coroutine"), causing memory leaks and unpredictable behavior.
 * All functions in this object explicitly rethrow [CancellationException] to preserve the
 * coroutine cancellation signal.
 */
object AsyncHandler {

  /**
   * Overload of [runSuspendResultCatching] without a [CoroutineContext] — runs on the caller's dispatcher.
   */
  suspend inline fun <T> runSuspendResultCatching(
    crossinline onExecute: suspend () -> T,
  ): Result<T> = try {
    Result.success(onExecute())
  } catch (c: CancellationException) {
    throw c
  } catch (e: Throwable) {
    Result.failure(e)
  }


  /**
   * Executes [onExecute] inside the given [context] and wraps the outcome in a [Result].
   * Defaults to [EmptyCoroutineContext] (no dispatcher switch) if [context] is not provided.
   */
  suspend inline fun <T> runSuspendResultCatching(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline onExecute: suspend () -> T,
  ): Result<T> = withContext(context) {
    try {
      Result.success(onExecute())
    } catch (c: CancellationException) {
      throw c
    } catch (e: Throwable) {
      Result.failure(e)
    }
  }

  /**
   * Executes [onExecute] and returns [T] directly instead of [Result].
   * Optionally switches to [context] (defaults to [EmptyCoroutineContext] — no dispatcher switch).
   * Exceptions are passed to [onCatch] for custom mapping (e.g. Firebase → Domain exceptions).
   * If [onCatch] is not provided, exceptions are rethrown as-is — equivalent to [withContext].
   *
   * Prefer [withContext] directly when no exception mapping is needed.
   */
  suspend inline fun <T> runSuspendCatching(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline onExecute: suspend () -> T,
    crossinline onCatch: (Exception) -> T = { throw it },
  ): T = withContext(context) {
    try {
      onExecute()
    } catch (c: CancellationException) {
      throw c
    } catch (e: Exception) {
      onCatch(e)
    }
  }

  /**
   * Transforms a [Flow] into a [Flow] of [Result], emitting [Result.failure] on error.
   */
  fun <T> Flow<T>.toFlowResult(): Flow<Result<T>> =
    this.map { Result.success(it) }
      .catch { t ->
        if (t is CancellationException) throw t
        else emit(Result.failure(t))
      }
}