package com.decoutkhanqindev.dexreader.utils

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