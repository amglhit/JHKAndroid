package com.amglhit.mmap.gd.map.ui

import com.amap.api.maps.AMap
import com.amap.api.maps.model.MyLocationStyle

fun AMap.showMyLocation(
  showLocation: Boolean = true,
  centerInScreen: Boolean = true
) {
  this.isMyLocationEnabled = true
  this.myLocationStyle = MyLocationStyle().apply {
    myLocationType(
      if (centerInScreen) {
        MyLocationStyle.LOCATION_TYPE_LOCATE
      } else {
        MyLocationStyle.LOCATION_TYPE_SHOW
      }
    )
    showMyLocation(showLocation)
  }
}

fun AMap.followMyLocation(
  interval: Long = 3000,
  showLocation: Boolean = true,
  centerInScreen: Boolean = true
) {
  this.isMyLocationEnabled = true
  this.myLocationStyle = MyLocationStyle().apply {
    myLocationType(
      if (centerInScreen) {
        MyLocationStyle.LOCATION_TYPE_FOLLOW
      } else {
        MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER
      }
    )
    showMyLocation(showLocation)
  }
}