package com.amglhit.mnetwork

import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.io.IOException
import java.io.InputStream
import java.security.*
import java.security.cert.CertificateException
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

    clientConfig.sslParams?.let {
      builder.sslSocketFactory(it.sslSocketFactory, it.x509TrustManager)
    }

    return builder.build()
  }

  fun createRetrofit(baseUrl: HttpUrl, httpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(httpClient)
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
  }

  fun createSSLParams(keyFile: InputStream, password: String, unsafe: Boolean = false): SSLParams? {
    try {
      var keyManagers: Array<KeyManager>? = null
      var trustManager = NetworkUtils.unsafeTrustManager
      try {
        val keyStore = NetworkUtils.createKeyStore(keyFile, password)
        keyManagers = NetworkUtils.prepareKeyManagers(keyStore, password)
      } catch (e: Exception) {
        Timber.e(e)
      }

      if (!unsafe) {
        try {
          trustManager = NetworkUtils.prepareTrustManager(null)
        } catch (e: Exception) {
          Timber.e(e)
        }
      }

      val sslContext = createSSlContext(keyManagers, trustManager)

      return SSLParams(sslContext.socketFactory, trustManager)
    } catch (e: Exception) {
      Timber.e(e)
      return null
    }
  }

  @Throws(
    NoSuchAlgorithmException::class,
    KeyManagementException::class
  )
  fun createSSlContext(keyManagers: Array<KeyManager>?, trustManager: TrustManager): SSLContext {
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(keyManagers, arrayOf(trustManager), SecureRandom())
    return sslContext
  }

  @Throws(
    KeyStoreException::class,
    NoSuchAlgorithmException::class,
    IOException::class,
    CertificateException::class
  )
  fun createKeyStore(inputStream: InputStream, password: String): KeyStore {
    val keyStore = KeyStore.getInstance("BKS")
    keyStore.load(inputStream, password.toCharArray())
    return keyStore
  }

  @Throws(
    NoSuchAlgorithmException::class,
    KeyStoreException::class,
    UnrecoverableKeyException::class
  )
  fun prepareKeyManagers(keyStore: KeyStore, password: String): Array<KeyManager> {
    val keyManagerFactory =
      KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())

    keyManagerFactory.init(keyStore, password.toCharArray())

    return keyManagerFactory.keyManagers
  }

  @Throws(NoSuchAlgorithmException::class, KeyStoreException::class)
  fun prepareTrustManager(trustStore: KeyStore?): X509TrustManager {
    val trustManagerFactory =
      TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    trustManagerFactory.init(trustStore)

    return chooseTrustManager(trustManagerFactory.trustManagers) ?: unsafeTrustManager
  }

  @Throws(GeneralSecurityException::class)
  fun createTrustStore(certificate: InputStream, password: String): KeyStore {
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

  val unsafeTrustManager = object : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
      return arrayOf()
    }
  }
}

data class SSLParams(
  val sslSocketFactory: SSLSocketFactory,
  val x509TrustManager: X509TrustManager
)

open class HttpClientConfig {
  open val readTimeout: Long = 10L
  open val writeTimeout: Long = 10L
  open val connectTimeout: Long = 5L

  open fun interceptors(): ArrayList<Interceptor> = arrayListOf()
  open fun networkInterceptors(): ArrayList<Interceptor> = arrayListOf()

  open val cookieJar: CookieJar? = null

  open val sslParams: SSLParams? = null
}