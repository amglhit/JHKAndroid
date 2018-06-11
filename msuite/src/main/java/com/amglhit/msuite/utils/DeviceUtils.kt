package com.amglhit.msuite.utils

import android.content.Context
import android.net.wifi.WifiManager
import com.amglhit.msuite.dp2Px
import com.amglhit.msuite.hasPermission
import java.net.Inet4Address
import java.net.NetworkInterface

fun Context.screenHeight() = this.resources.displayMetrics.heightPixels
fun Context.screenWidth() = this.resources.displayMetrics.widthPixels

fun Context.statusBarHeight(): Int {
  val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
  return if (resourceId > 0) {
    this.resources.getDimensionPixelSize(resourceId)
  } else {
    24.dp2Px(this)
  }
}

fun Context.navigationBarHeight(): Int {
  var result = 0
  val resourceId = this.resources.getIdentifier("navigation_bar_height", "dimen", "android")
  if (resourceId > 0) {
    result = this.resources.getDimensionPixelSize(resourceId)
  }
  return result
}

fun Context.hasNavigationBar(): Boolean {
  val id = this.resources.getIdentifier("config_showNavigationBar", "bool", "android")
  return id > 0 && this.resources.getBoolean(id)
}

private fun Int.toIpString(): String {
  return (this and 0xFF).toString() + "." +
      (this shr 8 and 0xFF) + "." +
      (this shr 16 and 0xFF) + "." +
      (this shr 24 and 0xFF)
}

fun Context.getWifiIp(): String {
  try {
    if (this.hasPermission(android.Manifest.permission.ACCESS_WIFI_STATE)) {
      val wifi = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
      wifi?.connectionInfo?.ipAddress?.let {
        return it.toIpString()
      }
    }
  } catch (e: SecurityException) {
    e.printStackTrace()
  }
  return ""
}

fun getMobileIpAddress(): String {
  try {
    val en = NetworkInterface.getNetworkInterfaces()
    while (en.hasMoreElements()) {
      val intf = en.nextElement()
      val enumIpAddr = intf.inetAddresses
      while (enumIpAddr.hasMoreElements()) {
        val inetAddress = enumIpAddr.nextElement()
        if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
          return inetAddress.getHostAddress()
        }
      }
    }
  } catch (e: Exception) {
    e.printStackTrace()
  }
  return ""
}

fun getMacAddress(): String {
  try {
    var ni = NetworkInterface.getByName("eth1")
    if (ni == null) {
      ni = NetworkInterface.getByName("wlan0")
    }
    if (ni == null)
      return ""

    val buf = StringBuffer()
    val ba = ni.hardwareAddress
    ba?.forEach {
      buf.append(String.format("%02X:", it))
    }
    if (buf.isNotEmpty()) {
      buf.deleteCharAt(buf.length - 1)
    }
    return buf.toString()
  } catch (e: Exception) {
    e.printStackTrace()
  }
  return ""
}
