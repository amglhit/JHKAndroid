package com.amglhit.jhk.app

import com.amglhit.msuite.base.GlobalExceptionHandler
import com.amglhit.msuite.base.MApplication
import com.amglhit.msuite.isMainProcess
import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber

class JHKApplication : MApplication() {
  companion object {
    @JvmStatic
    lateinit var application: JHKApplication
      private set
    @JvmStatic
    lateinit var firebase: FirebaseAnalytics
      private set
  }

  override fun onCreate() {
    super.onCreate()
    application = this
    firebase = FirebaseAnalytics.getInstance(this)
    Timber.plant(Timber.DebugTree())
    Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler())
    if (isMainProcess()) {

    }
  }

  override fun onAppForeground() {

  }

  override fun onAppBackground() {

  }

  inner class ExceptionHandler : GlobalExceptionHandler() {
    override fun handleException(t: Thread?, e: Throwable?): Boolean {
      Timber.w(e)
      return false
    }
  }
}