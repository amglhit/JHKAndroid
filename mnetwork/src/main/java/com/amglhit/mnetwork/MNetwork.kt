package com.amglhit.mnetwork

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class MNetwork {
  companion object {
    lateinit var instance: MNetwork
      private set

    fun init(baseUrl: String, clientConfig: HttpClientConfig): Boolean {
      instance = MNetwork()
      return instance.init(baseUrl, clientConfig)
    }
  }

  lateinit var httpClient: OkHttpClient
  lateinit var retrofit: Retrofit

  fun <T> createApi(service: Class<T>): T {
    return retrofit.create(service)
  }

  fun init(baseUrl: String, clientConfig: HttpClientConfig): Boolean {
    HttpUrl.parse(baseUrl)?.let {
      httpClient = NetworkUtils.createHttpClient(clientConfig)
      retrofit = NetworkUtils.createRetrofit(it, httpClient)
      return true
    }
    return false
  }

}