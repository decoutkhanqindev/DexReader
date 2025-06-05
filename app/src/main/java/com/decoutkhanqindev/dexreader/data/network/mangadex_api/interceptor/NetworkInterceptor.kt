package com.decoutkhanqindev.dexreader.data.network.mangadex_api.interceptor

import jakarta.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class NetworkInterceptor @Inject constructor() : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response =
    chain
      .request()
      .newBuilder()
      .build()
      .let(chain::proceed)
}