package com.amglhit.mlocation

import android.arch.lifecycle.LiveData
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationListener
import com.amglhit.mlocation.client.LocationClient
import timber.log.Timber

class LocationLiveData : LiveData<AMapLocation>() {
  private val locationListener = object : AMapLocationListener {
    override fun onLocationChanged(p0: AMapLocation?) {
      postValue(p0)
      if (!enableUpdate) {
        stopUpdate()
      }
    }
  }

  private var isActive = false
  private var enableUpdate = false

  override fun onActive() {
    super.onActive()
    isActive = true

    if (enableUpdate) {
      startUpdate()
    }
  }

  override fun onInactive() {
    super.onInactive()
    isActive = false

    enableUpdate = false
    stopUpdate()
  }

  fun requestLocation(): AMapLocation? {
    Timber.i("request location")
    if (isActive) {
      val location = LocationClient.client.lastKnownLocation
      startUpdate()
      return location
    }
    return null
  }

  fun requestLocationUpdate() {
    Timber.i("request location update")
    enableUpdate = true
    if (isActive) {
      startUpdate()
    }
  }

  private fun startUpdate() {
    Timber.d("start location update")
    LocationClient.client.setLocationListener(locationListener)
    LocationClient.client.startLocation()
  }

  private fun stopUpdate() {
    Timber.d("stop location update")
    LocationClient.client.unRegisterLocationListener(locationListener)
    LocationClient.client.stopLocation()
  }

}