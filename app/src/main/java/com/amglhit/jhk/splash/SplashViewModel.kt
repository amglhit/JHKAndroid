package com.amglhit.jhk.splash

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import android.os.Looper
import com.amglhit.msuite.livedata.MLiveEvent
import timber.log.Timber

class SplashViewModel : ViewModel() {
  companion object {
    /**
     * Splash等待时长
     */
    const val TIME_WAIT = 2000L
    /**
     * Splash广告展示时长
     */
    const val TIME_SPLASH = 3000L
  }

  val endSplashLiveData = MutableLiveData<MLiveEvent<Boolean>>()
  val splashTimeRemainsLiveData = MutableLiveData<Int>()

  private val handler = Handler(Looper.getMainLooper())

  override fun onCleared() {
    super.onCleared()
    handler.removeCallbacksAndMessages(null)
  }

  /**
   * 打开Splash的时间
   */
  private var splashStartTime: Long = -1

  /**
   * Splash广告展示的时间
   */
  private var splashAdShowTime: Long = -1

  fun startSplash() {
    Timber.d("start splash")
    if (splashStartTime < 0) {
      splashStartTime = System.currentTimeMillis()
      checkSplashTime()
    }
  }

  fun showSplashAd() {
    splashAdShowTime = System.currentTimeMillis()
  }

  private val splashTimeChecker = object : Runnable {
    override fun run() {
      checkSplashTime()
    }
  }

  private fun checkSplashTime() {
    handler.removeCallbacksAndMessages(null)
    //检测等待时长
    if (splashStartTime > 0 && System.currentTimeMillis() - splashStartTime >= TIME_WAIT) {
      endSplash(false)
      return
    }

    //检测展示时长
    if (splashAdShowTime > 0) {
      val timeSpend = System.currentTimeMillis() - splashAdShowTime
      val remains = TIME_SPLASH.minus(timeSpend).toInt()
      splashTimeRemainsLiveData.postValue(remains)
      if (remains <= 0) {
        endSplash(true)
        return
      }
    }

    handler.postDelayed(splashTimeChecker, 1000)
  }

  private fun endSplash(adShown: Boolean) {
    Timber.d("end splash, adShown: $adShown")
    handler.removeCallbacksAndMessages(null)
    endSplashLiveData.postValue(MLiveEvent(true))
  }
}