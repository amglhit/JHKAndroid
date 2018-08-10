package com.amglhit.jhk.app

import android.content.Context
import android.support.multidex.MultiDex
import com.amglhit.jhk.BuildConfig
import com.amglhit.mlocation.client.LocationClient
import com.amglhit.msuite.base.GlobalExceptionHandler
import com.amglhit.msuite.base.MApplication
import com.amglhit.msuite.isMainProcess
import com.google.firebase.analytics.FirebaseAnalytics
import com.tencent.bugly.crashreport.CrashReport
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
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }

    if (BuildConfig.GooglePlay) {
      Timber.d("for GooglePlay")
      firebase = FirebaseAnalytics.getInstance(this)
    } else {
      Timber.d("not for GooglePlay")
    }

    initBugly()

    Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler())

    if (isMainProcess()) {
      initLocationClient()
    }
  }

  private fun initBugly() {
    val strategy = CrashReport.UserStrategy(this)
    strategy.appChannel = "0"
    strategy.appPackageName = this.packageName
    strategy.appVersion = BuildConfig.VERSION_NAME
    CrashReport.initCrashReport(this, "a5432c7cab", false, strategy)
  }

  private fun initLocationClient() {
    LocationClient.init(this)
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