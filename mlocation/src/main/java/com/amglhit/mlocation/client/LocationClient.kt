package com.amglhit.mlocation.client

import android.app.Application
import android.content.Context
import com.amap.api.location.AMapLocationClient

object LocationClient {
  private lateinit var context: Context

  val client: AMapLocationClient by lazy { AMapLocationClient(context) }

  fun initClient(application: Application) {
    this.context = application
  }

  fun foregroundMode() {

  }

  fun backgroundMode() {

  }
}