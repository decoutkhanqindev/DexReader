package com.decoutkhanqindev.dexreader.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkInterceptor @Inject constructor() : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
}