package com.amglhit.mmap.gd.map.ui

import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions

fun AMap.myUiSettings() {
  this.uiSettings.apply {
    isZoomControlsEnabled = false
    isMyLocationButtonEnabled = false
    logoPosition = AMapOptions.LOGO_POSITION_BOTTOM_RIGHT
    isZoomGesturesEnabled = true //缩放手势
    isScrollGesturesEnabled = true   //滑动手势
    isRotateGesturesEnabled = false //旋转手势
    isTiltGesturesEnabled = false //倾斜手势
    setZoomInByScreenCenter(true)
  }
}