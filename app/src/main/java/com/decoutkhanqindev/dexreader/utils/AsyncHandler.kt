package com.decoutkhanqindev.dexreader.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

/**
 * Async operations handler for coroutines and Flow
 * Provides utilities for error handling in suspend functions and Flow transformations
 */
object AsyncHandler {

  /**
   * Runs a suspend block and catches exceptions, returning a Result Properly handles
   * CancellationException by rethrowing it
   */
  suspend inline fun <T> runSuspendCatching(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> T,
  ): Result<T> = withContext(context) {
    try {
      Result.success(block())
    } catch (c: CancellationException) {
      throw c // Rethrow CancellationException to respect coroutine cancellation
    } catch (e: Throwable) {
      Result.failure(e)
    }
  }

  /**
   * Runs a suspend block and catches exceptions with CoroutineContext, returning a Result Properly handles
   * CancellationException by rethrowing it
   */

  suspend inline fun <T> runSuspendCatching(
    crossinline block: suspend () -> T,
  ): Result<T> = try {
    Result.success(block())
  } catch (c: CancellationException) {
    throw c // Rethrow CancellationException to respect coroutine cancellation
  } catch (e: Throwable) {
    Result.failure(e)
  }

  /**
   * Converts a Flow to a Flow of Results, catching exceptions Properly handles
   * CancellationException by rethrowing it
   */
  fun <T> Flow<T>.toFlowResult(): Flow<Result<T>> =
    this.map { Result.success(it) }
      .catch {
        if (it is CancellationException) throw it // Rethrow CancellationException to respect coroutine cancellation
        else emit(Result.failure(it))
      }
}
