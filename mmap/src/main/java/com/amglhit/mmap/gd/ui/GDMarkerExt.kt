package com.amglhit.mmap.gd.ui

import android.graphics.Bitmap
import com.amap.api.maps.AMap
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import android.view.animation.LinearInterpolator
import com.amap.api.maps.model.animation.RotateAnimation

fun AMap.addMarker(
  lat: Double,
  lng: Double,
  title: String,
  snippet: String,
  icon: Bitmap?
): Marker {
  val marker = MarkerOptions().position(LatLng(lat, lng))
    .apply {
      if (title.isNotEmpty()) {
        title(title)
      }
      if (snippet.isNotEmpty()) {
        snippet(snippet)
      }
      icon?.let {
        icon(BitmapDescriptorFactory.fromBitmap(icon))
      }
      draggable(false)
    }
  return this.addMarker(marker)
}

fun Marker.animate() {
  val animation = RotateAnimation(this.rotateAngle, this.rotateAngle + 180, 0f, 0f, 0f).apply {
    setDuration(1000L)
    setInterpolator(LinearInterpolator())
  }

  this.setAnimation(animation)
  this.startAnimation()
}