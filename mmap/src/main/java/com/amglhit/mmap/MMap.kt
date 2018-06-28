package com.amglhit.mmap

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.os.Bundle
import com.amap.api.maps.MapView

class MMap(lifecycle: Lifecycle, private val mapView: MapView) :
  LifecycleObserver {
  init {
    lifecycle.addObserver(this)
  }

//  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  /**
   * 因为需要InstanceState, 不能使用LifeCycleEvent
   */
  fun onCreate(savedInstanceState: Bundle? = null) {
    mapView.onCreate(savedInstanceState)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun onDestroy() {
    mapView.onDestroy()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  fun onResume() {
    mapView.onResume()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  fun onPause() {
    mapView.onPause()
  }

  fun onSaveInstanceState(outState: Bundle?) {
    mapView.onSaveInstanceState(outState)
  }
}