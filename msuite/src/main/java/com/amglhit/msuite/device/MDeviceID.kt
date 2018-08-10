package com.amglhit.msuite.device

//fun Context.getGreenID(): String {
//  var androidId: String? = Settings.Secure.ANDROID_ID
//  if (androidId.isNullOrBlank()) {
//    val pref = MDeviceID(this)
//    androidId = pref.deviceId
//    if (androidId.isNullOrBlank()) {
//      androidId = UUID.randomUUID().toString()
//      pref.deviceId = androidId
//    }
//  }
//  return androidId ?: ""
//}

//class MDeviceID(context: Context, fileName: String) : Preference(context) {
//  private val prefs by lazy { context.getSharedPreferences(fileName, Context.MODE_PRIVATE) }
//  var deviceId: String
//    get() {
//      return prefs.getString("device_id", "")
//    }
//    set(value) {
//      prefs.edit().putString("device_id", value).apply()
//    }
//}
//
//abstract class PrefData
