package com.amglhit.mnetwork

import com.amglhit.mnetwork.utils.HttpClientConfig
import com.amglhit.mnetwork.utils.NetworkUtils
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class MHttpClient(config: HttpClientConfig, baseUrl: String) {
  val retrofit: Retrofit
  val httpClient: OkHttpClient
  val activeHost: HttpUrl?

  init {
    activeHost = HttpUrl.parse(baseUrl)
    httpClient = NetworkUtils.createHttpClient(config)
    retrofit = NetworkUtils.createRetrofit(baseUrl, httpClient)
  }

  fun <T> createApi(service: Class<T>): T {
    return retrofit.create(service)
  }

}