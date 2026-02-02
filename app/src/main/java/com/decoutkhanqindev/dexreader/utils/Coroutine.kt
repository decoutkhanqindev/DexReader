package com.decoutkhanqindev.dexreader.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalContracts::class)
suspend inline fun <R> runSuspendCatching(
  context: CoroutineContext = EmptyCoroutineContext,
  crossinline block: suspend () -> R,
): Result<R> {
  contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }

  return try {
    Result.success(withContext(context) { block() })
  } catch (c: CancellationException) {
    throw c
  } catch (e: Throwable) {
    Result.failure(e)
  }
}

fun <T> Flow<T>.toResultFlow(): Flow<Result<T>> =
  this.map { Result.success(it) }
    .catch {
      if (it is CancellationException) throw it
      else emit(Result.failure(it))
    }
