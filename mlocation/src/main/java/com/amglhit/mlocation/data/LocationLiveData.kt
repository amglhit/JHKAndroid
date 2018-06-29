package com.amglhit.mlocation.data

import android.arch.lifecycle.LiveData
import com.amap.api.location.AMapLocation
import com.amglhit.mlocation.client.LocationClient
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class LocationLiveData : LiveData<AMapLocation>() {
  private var isActive = false
  private var enableUpdate = false

  private val disposables = CompositeDisposable()

  override fun onActive() {
    super.onActive()
    isActive = true

    disposables.add(LocationClient.instance.locationData.observe { old, new ->
      Timber.d("get location update $old -> $new")
      postValue(new)
    })

    if (enableUpdate) {
      LocationClient.instance.startUpdate()
    }
  }

  override fun onInactive() {
    super.onInactive()
    isActive = false
    enableUpdate = false

    LocationClient.instance.stopUpdate()

    disposables.clear()
  }

  fun requestLocationUpdate() {
    Timber.i("request location update")
    enableUpdate = true
    if (isActive) {
      LocationClient.instance.startUpdate()
    }
  }

  fun stopLocationUpdate() {
    Timber.d("stop location update")
    enableUpdate = false
    LocationClient.instance.stopUpdate()
  }
}