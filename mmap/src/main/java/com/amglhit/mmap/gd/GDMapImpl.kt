package com.amglhit.mmap.gd

import android.os.Bundle
import android.support.v4.util.ArrayMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.Marker
import com.amglhit.mmap.base.IMapView
import com.amglhit.mmap.base.MMarker
import com.amglhit.mmap.gd.ui.*
import timber.log.Timber

class GDMapImpl(private val mapView: MapView) : IMapView {
  init {
    mapView.map.setOnMyLocationChangeListener {
      Timber.d("my location: $it")
    }

    mapView.map.setOnMarkerClickListener {
      Timber.d("marker click $it")
      //返回 true 则表示接口已响应事件，否则返回false
      true
    }

    mapView.map.myUiSettings()
  }

  private val markerList = ArrayMap<String, Marker>()

  override fun getMyLocation(): Pair<Double, Double> {
    val loc = mapView.map.myLocation
    return Pair(loc.latitude, loc.longitude)
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

  override fun addMarker(
    data: MMarker
  ): String {
    val marker = mapView.map.addMarker(data.lat, data.lng, data.title, data.snippet, data.icon)
    markerList[marker.id] = marker
    return marker.id
  }

  override fun removeMarker(id: String) {
    markerList[id]?.let {
      it.remove()
      markerList.remove(id)
    }
  }

  @Synchronized
  override fun clearMarkers() {
    val iterator = markerList.values.iterator()
    while (iterator.hasNext()) {
      val item = iterator.next()
      item.remove()
      iterator.remove()
    }
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
}