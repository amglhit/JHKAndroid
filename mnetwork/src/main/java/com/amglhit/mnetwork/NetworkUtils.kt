package com.amglhit.mnetwork

import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.InputStream
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

object NetworkUtils {
  fun createHttpClient(clientConfig: HttpClientConfig): OkHttpClient {
    val builder = OkHttpClient.Builder()
      .connectTimeout(clientConfig.connectTimeout, TimeUnit.SECONDS)
      .readTimeout(clientConfig.readTimeout, TimeUnit.SECONDS)
      .writeTimeout(clientConfig.writeTimeout, TimeUnit.SECONDS)

    clientConfig.networkInterceptors().forEach {
      builder.addNetworkInterceptor(it)
    }

    clientConfig.interceptors().forEach {
      builder.addInterceptor(it)
    }

    clientConfig.cookieJar?.let {
      builder.cookieJar(it)
    }

    clientConfig.keyFile?.let {
      val param = createSSLParam(it, clientConfig.password)
      builder.sslSocketFactory(param.sslSocketFactory, param.x509TrustManager)
    }

    return builder.build()
  }

  fun createRetrofit(baseUrl: String, httpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(httpClient)
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
  }

  private fun createSSLParam(key: InputStream, password: String): SSLParams {
//    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
//    keyStore.load(key, password.toCharArray())
    val keyStore = createClientKeyStore(key, password)

    val keyManagers = prepareKeyManagers(keyStore, password)
    val trustManager = prepareTrustManager(keyStore)

    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(
      keyManagers,
      arrayOf(trustManager),
      SecureRandom()
    )

    return SSLParams(sslContext.socketFactory, trustManager)
  }

  private fun prepareKeyManagers(keyStore: KeyStore, password: String): Array<KeyManager> {
    val keyManagerFactory =
      KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
    keyManagerFactory.init(keyStore, password.toCharArray())
    return keyManagerFactory.keyManagers
  }

  private fun prepareTrustManager(keyStore: KeyStore): X509TrustManager {
    val trustManagerFactory =
      TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())

    trustManagerFactory.init(keyStore)

    return chooseTrustManager(trustManagerFactory.trustManagers) ?: unsafeTrustManager
  }

  private fun chooseTrustManager(trustManagers: Array<TrustManager>?): X509TrustManager? {
    if (trustManagers == null)
      return null
    for (trustManager in trustManagers) {
      if (trustManager is X509TrustManager) {
        return trustManager
      }
    }
    return null
  }

  @Throws(GeneralSecurityException::class)
  private fun createClientKeyStore(certificate: InputStream, password: String): KeyStore {
    val certificateFactory = CertificateFactory.getInstance("X.509")
    val certificates = certificateFactory.generateCertificates(certificate)

    // Put the certificates a key store.
    val keyStore = newEmptyKeyStore(password)

    certificates.forEachIndexed { index, cert ->
      keyStore.setCertificateEntry(index.toString(), cert)
    }

    return keyStore
  }

  private fun newEmptyKeyStore(password: String): KeyStore {
    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
    keyStore.load(null, password.toCharArray())
    return keyStore
  }

  private val unsafeTrustManager = object : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
      return arrayOf()
    }
  }
}

data class SSLParams(val sslSocketFactory: SSLSocketFactory, val x509TrustManager: X509TrustManager)

open class HttpClientConfig {
  open val readTimeout: Long = 10L
  open val writeTimeout: Long = 10L
  open val connectTimeout: Long = 5L

  open fun interceptors(): ArrayList<Interceptor> = arrayListOf()
  open fun networkInterceptors(): ArrayList<Interceptor> = arrayListOf()

  open val cookieJar: CookieJar? = null

  open val keyFile: InputStream? = null
  open val password: String = ""
}