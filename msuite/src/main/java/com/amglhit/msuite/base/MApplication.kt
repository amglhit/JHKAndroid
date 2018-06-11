package com.amglhit.msuite.base

import android.app.Application
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ProcessLifecycleOwner
import timber.log.Timber


abstract class MApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
  }

  abstract fun onAppForeground()
  abstract fun onAppBackground()

  private val lifecycleObserver = object : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() {
      Timber.d("app foreground")
      onAppForeground()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
      Timber.d("app background")
      onAppBackground()
    }
  }
}