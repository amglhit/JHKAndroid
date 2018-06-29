package com.amglhit.mlocation.client

import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationListener
import com.amglhit.msuite.data.ObservableData
import timber.log.Timber

class LocationClient(context: Context) {
  companion object {
    lateinit var instance: LocationClient
      private set

    fun init(context: Context) {
      instance = LocationClient(context)
    }
  }

  val locationData = ObservableData<AMapLocation?>(null)

  private val client = AMapLocationClient(context)

  private val locationListener = AMapLocationListener {
    locationData.value = it
  }

  fun startUpdate() {
    Timber.d("start location update")
    client.setLocationListener(locationListener)
    client.startLocation()
  }

  fun stopUpdate() {
    Timber.d("stop location update")
    client.unRegisterLocationListener(locationListener)
    client.stopLocation()
  }

  fun destroyClient() {
    client.onDestroy()
  }

  fun foregroundMode() {
    Timber.d("foreground $client")
  }

  fun backgroundMode() {
    Timber.d("background $client")
  }
}