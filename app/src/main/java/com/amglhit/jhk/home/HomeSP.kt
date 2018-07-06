package com.amglhit.jhk.home

import com.amglhit.jhk.app.JHKApplication
import com.amglhit.msuite.sp.PrefJson
import com.amglhit.msuite.sp.PrefString
import com.amglhit.msuite.sp.PreferenceData
import com.google.gson.annotations.SerializedName

class HomeSP : PreferenceData(JHKApplication.application, "com.amglhit.jhk.home") {
  var mobikeHome by PrefString(default = "def-home")//PrefResource(default = "def-home")//
  //  var mobikeHome: String by PrefResource(default = "def-home")//
  var user: HomeUser by PrefJson(clazz = HomeUser::class.java, default = HomeUser.empty)
}

data class HomeUser(
  @SerializedName("name") val name: String,
  @SerializedName("gender") val gender: Int,
  @SerializedName("city") val city: UserCity
) {
  companion object {
    val empty = HomeUser("empty", -1, UserCity.empty)
  }
}

data class UserCity(@SerializedName("city") val city: String) {
  companion object {
    val empty = UserCity("empty")
  }
}