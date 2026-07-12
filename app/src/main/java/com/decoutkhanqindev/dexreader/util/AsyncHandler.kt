package com.decoutkhanqindev.dexreader.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

object AsyncHandler {

  suspend inline fun <T> runSuspendResultCatching(
    crossinline block: suspend () -> T,
  ): Result<T> = try {
    Result.success(block())
  } catch (c: CancellationException) {
    throw c
  } catch (e: Throwable) {
    Result.failure(e)
  }

  suspend inline fun <T> runSuspendResultCatching(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> T,
  ): Result<T> = withContext(context) {
    try {
      Result.success(block())
    } catch (c: CancellationException) {
      throw c
    } catch (e: Throwable) {
      Result.failure(e)
    }
  }

  suspend inline fun <T> runSuspendCatching(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline block: suspend () -> T,
    crossinline catch: (Exception) -> T = { throw it },
  ): T = withContext(context) {
    try {
      block()
    } catch (c: CancellationException) {
      throw c
    } catch (e: Exception) {
      catch(e)
    }
  }

  fun <T> Flow<T>.toFlowResult(): Flow<Result<T>> =
    this.map { Result.success(it) }
      .catch { t ->
        if (t is CancellationException) throw t
        else emit(Result.failure(t))
      }
}