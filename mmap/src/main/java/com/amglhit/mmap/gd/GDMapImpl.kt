package com.amglhit.mmap.gd

import android.os.Bundle
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amglhit.mmap.base.IMapView
import com.amglhit.mmap.gd.ui.MyCameraUpdate
import com.amglhit.mmap.gd.ui.animate
import com.amglhit.mmap.gd.ui.myUiSettings
import com.amglhit.mmap.gd.ui.showMyLocation
import timber.log.Timber

class GDMapImpl(private val mapView: MapView) : IMapView {
  init {
    mapView.map.setOnMyLocationChangeListener {
      Timber.d("my location: $it")
    }
    mapView.map.myUiSettings()
  }


  override fun showMyLocation(resetZoomLevel: Boolean) {
    Timber.d("show current location")
    mapView.map.showMyLocation()
    if (resetZoomLevel) {
      setZoom()
    }
  }

  override fun setZoom(level: Float) {
    CameraUpdateFactory.zoomTo(level).animate(mapView.map)
  }

  override fun zoomIn() {
    CameraUpdateFactory.zoomIn().animate(mapView.map)
  }

  override fun zoomOut() {
    CameraUpdateFactory.zoomOut().animate(mapView.map)
  }

  override fun setCenter(lat: Double, lng: Double, zoomLevel: Float) {
    MyCameraUpdate.centerPosition(lat, lng, zoomLevel).animate(mapView.map)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    mapView.onCreate(savedInstanceState)
  }

  override fun onDestroy() {
    mapView.onDestroy()
  }

  override fun onPause() {
    mapView.onPause()
  }

  override fun onResume() {
    mapView.onResume()
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    mapView.onSaveInstanceState(outState)
  }

  fun addMarker() {
//    mapView.map.addMarker()
  }
}