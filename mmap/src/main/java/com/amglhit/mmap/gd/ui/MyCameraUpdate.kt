package com.amglhit.mmap.gd.map.ui

import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdate
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng

fun CameraUpdate.animate(map: AMap, duration: Long = 300): AMap.CancelableCallback {
  val callback = object : AMap.CancelableCallback {
    override fun onFinish() {
    }

    override fun onCancel() {
    }
  }
  map.animateCamera(this, duration, callback)
  return callback
}


object MyCameraUpdate {
  fun centerPosition(
    lat: Double,
    lng: Double,
    zoomLevel: Float
  ): CameraUpdate {
    // CameraPosition 第一个参数： 目标位置的屏幕中心点经纬度坐标。
    // CameraPosition 第二个参数： 目标可视区域的缩放级别
    // CameraPosition 第三个参数： 目标可视区域的倾斜度，以角度为单位。
    // CameraPosition 第四个参数： 可视区域指向的方向，以角度为单位，从正北向顺时针方向计算，从0度到360度
    //    mapOptions.camera(CameraPosition(centerBJPoint, 10f, 0f, 0f))
    val centerPotion = LatLng(lat, lng)
    return CameraUpdateFactory.newCameraPosition(
      CameraPosition(
        centerPotion,
        zoomLevel,
        0f,
        0f
      )
    )
  }
}


