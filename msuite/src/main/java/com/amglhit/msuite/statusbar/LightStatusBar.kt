package com.amglhit.msuite.statusbar

import android.app.Activity
import android.os.Build
import android.view.WindowManager
import com.amglhit.msuite.systemProperty

object LightStatusBar {
  fun getLightStatusBarRomType(): RomType =
    if (isAboveMIUIV6())
      RomType.MIUI
    else if (isAboveFlyMeV4())
      RomType.FlyMe
    else if (isLightStatusSupported())
      RomType.AndroidNative
    else
      RomType.NA

  private fun isLightStatusBarAvailable(): Boolean {
    return isAboveMIUIV6() || isAboveFlyMeV4() || isLightStatusSupported()
  }

  //Android Api 23以上
  fun isLightStatusSupported(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
  }

  //MIUI V6对应的versionCode是4
  //MIUI V7对应的versionCode是5
  private fun isAboveMIUIV6(): Boolean {
    val version = systemProperty("ro.miui.ui.version.code")
    if (!version.isBlank()) {
      try {
        if (version.toInt() >= 4) {
          return true
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
    return false
  }

  //Flyme V4的displayId格式为 [Flyme OS 4.x.x.xA]
  //Flyme V5的displayId格式为 [Flyme 5.x.x.x beta]
  private fun isAboveFlyMeV4(): Boolean {
    val displayId = Build.DISPLAY
    if (!displayId.isNullOrBlank() && displayId.contains("flyme", true)) {
      displayId.split(" ").forEach {
        if (it.matches(Regex("^[4-9]\\.(\\d+\\.)+\\S*"))) {
          return true
        }
      }
    }
    return false
  }

  /**
   * MIUI的沉浸支持透明白色字体和透明黑色字体
   * https://dev.mi.com/console/doc/detail?pId=1159
   */
  fun miuiSetStatusBarLightMode(activity: Activity, isLight: Boolean): Boolean {
    try {
      val clazz = activity.window::class.java
      val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
      val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
      val darkModeFlag = field.getInt(layoutParams)
      val extraFlagField =
        clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
      extraFlagField.invoke(activity.window, if (isLight) 0 else darkModeFlag, darkModeFlag)
      return true
    } catch (e: Exception) {
      e.printStackTrace()
      return false
    }
  }

  /**
   * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
   */
  fun flymeSetStatusBarLightMode(activity: Activity, isLight: Boolean): Boolean {
    try {
      val lp = activity.window.attributes
      val darkFlag = WindowManager.LayoutParams::class.java
        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
      val meizuFlags = WindowManager.LayoutParams::class.java
        .getDeclaredField("meizuFlags")
      darkFlag.isAccessible = true
      meizuFlags.isAccessible = true
      val bit = darkFlag.getInt(null)
      var value = meizuFlags.getInt(lp)
      if (isLight) {
        value = value and bit.inv()
      } else {
        value = value or bit
      }
      meizuFlags.setInt(lp, value)
      activity.window.attributes = lp
      return true
    } catch (e: Exception) {
      e.printStackTrace()
      return false
    }
  }
}

sealed class RomType {
  abstract val id: Int
  abstract val name: String

  object MIUI : RomType() {
    override val id = 0
    override val name = "MIUI"
  }

  object FlyMe : RomType() {
    override val id = 1
    override val name = "FlyMe"
  }

  object AndroidNative : RomType() {
    override val id = 2
    override val name = "AndroidNative"
  }

  object NA : RomType() {
    override val id = 3
    override val name = "NA"
  }
}