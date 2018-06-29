package com.amglhit.jhk.app

import android.content.Context
import android.support.multidex.MultiDex
import com.amglhit.mlocation.client.LocationClient
import com.amglhit.msuite.base.GlobalExceptionHandler
import com.amglhit.msuite.base.MApplication
import com.amglhit.msuite.isMainProcess
import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber

open class JHKApplication : MApplication() {
  companion object {
    @JvmStatic
    lateinit var application: JHKApplication
      private set
    @JvmStatic
    lateinit var firebase: FirebaseAnalytics
      private set
  }

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }

  override fun onCreate() {
    super.onCreate()
    application = this
    firebase = FirebaseAnalytics.getInstance(this)
    Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler())
    if (isMainProcess()) {
      LocationClient.init(this)
    }
  }

  override fun onAppForeground() {
    LocationClient.instance.foregroundMode()
  }

  override fun onAppBackground() {
    LocationClient.instance.backgroundMode()
  }

  inner class ExceptionHandler : GlobalExceptionHandler() {
    override fun handleException(t: Thread?, e: Throwable?): Boolean {
      Timber.w(e)
      return false
    }
  }
}