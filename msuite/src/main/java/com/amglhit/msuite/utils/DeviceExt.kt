package com.amglhit.msuite.utils

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import java.net.Inet4Address
import java.net.NetworkInterface

private fun Int.toIpString(): String {
  return (this and 0xFF).toString() + "." +
      (this shr 8 and 0xFF) + "." +
      (this shr 16 and 0xFF) + "." +
      (this shr 24 and 0xFF)
}

@SuppressLint("MissingPermission")
@Throws(SecurityException::class)
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

fun getMobileIp(): String {
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

@SuppressWarnings("MissingPermission")
fun Context.isBleEnabled(): Boolean {
  if (this.hasPermission(Manifest.permission.BLUETOOTH)) {
    return BluetoothAdapter.getDefaultAdapter()?.isEnabled ?: false
  }
  return false
}

@SuppressWarnings("MissingPermission")
fun Context.openBle(): Boolean {
  if (this.hasPermission(Manifest.permission.BLUETOOTH_ADMIN)) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      return BluetoothAdapter.getDefaultAdapter()?.enable() ?: false
    }
  }
  return false
}

@SuppressWarnings("MissingPermission")
fun Context.closeBle(): Boolean {
  if (this.hasPermission(Manifest.permission.BLUETOOTH) && this.hasPermission(Manifest.permission.BLUETOOTH_ADMIN)) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      val btAdapter = BluetoothAdapter.getDefaultAdapter()
      if (btAdapter?.isEnabled == true) {
        return btAdapter.disable()
      }
    }
  }
  return false
}

@SuppressWarnings("MissingPermission")
fun Context.getIMEI(): String {
  try {
    if (this.hasPermission(Manifest.permission.READ_PHONE_STATE)) {
      val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        return tm?.imei ?: ""
      } else {
        return tm?.deviceId ?: ""
      }
    }
  } catch (e: Exception) {
    e.printStackTrace()
  }
  return ""
}

fun Context.getAndroidId(): String {
  return try {
    val androidId = Settings.Secure.getString(
      this.contentResolver,
      Settings.Secure.ANDROID_ID
    )
    if (androidId != null) androidId else ""
  } catch (e: Exception) {
    e.printStackTrace()
    ""
  }
}
