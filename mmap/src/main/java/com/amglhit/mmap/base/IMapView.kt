package com.amglhit.mmap.base

import android.os.Bundle

interface IMapView {
  companion object {
    const val default_zoom_level = 15f
    const val default_center_lat = 39.904989
    const val default_center_lng = 116.405285
  }

  fun showMyLocation(resetZoomLevel: Boolean = false)
  fun setZoom(level: Float = default_zoom_level)
  fun zoomIn()
  fun zoomOut()
  fun setCenter(
    lat: Double = default_center_lat,
    lng: Double = default_center_lng,
    zoomLevel: Float = default_zoom_level
  )

  fun onCreate(savedInstanceState: Bundle? = null)
  fun onDestroy()
  fun onResume()
  fun onPause()
  fun onSaveInstanceState(outState: Bundle?)
}