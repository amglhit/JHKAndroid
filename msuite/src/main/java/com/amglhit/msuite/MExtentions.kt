package com.amglhit.msuite

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.widget.Toast
import com.amglhit.msuite.toast.ToastUtils
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import java.io.BufferedReader
import java.io.InputStreamReader

fun Context.isMainProcess() = this.packageName == this.getAppName()

fun Context.getAppName(): String {
  val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
  am?.runningAppProcesses?.forEach {
    if (it.pid == android.os.Process.myPid()) {
      return it.processName
    }
  }
  return ""
}

fun Context.versionCode(): Int {
  try {
    return this.packageManager.getPackageInfo(this.packageName, 0).versionCode
  } catch (e: Exception) {
    e.printStackTrace()
    return 1
  }
}

fun Context.versionName(): String {
  try {
    return this.packageManager.getPackageInfo(this.packageName, 0).versionName
  } catch (e: Exception) {
    e.printStackTrace()
    return ""
  }
}

fun Context.isAppInstalled(name: String): Boolean {
  try {
    val info = this.packageManager.getPackageInfo(name, 0)
    return info != null
  } catch (e: Exception) {
    e.printStackTrace()
    return false
  }
}

fun Activity.isFinishingSafe(): Boolean {
  if (isFinishing) {
    return true
  }
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
    if (isDestroyed) {
      return true
    }
  }
  return false
}

fun Int.dp2Px(context: Context) = (context.resources.displayMetrics.density * this + 0.5F).toInt()
fun Float.dp2Px(context: Context) = context.resources.displayMetrics.density * this + 0.5F
fun Int.px2DP(context: Context) = (context.resources.displayMetrics.density * this + 0.5F).toInt()
fun Float.px2DP(context: Context) = (context.resources.displayMetrics.density * this + 0.5F).toInt()

fun Int.dp2SP(context: Context) =
  (context.resources.displayMetrics.scaledDensity * this + 0.5F).toInt()

fun Float.dp2SP(context: Context) =
  (context.resources.displayMetrics.scaledDensity * this + 0.5F).toInt()

fun Toast.safeShow() {
  ToastUtils.hook(this)
  this.show()
}

fun ViewPager.getCurrentFragment(): Fragment? {
  return this.adapter?.instantiateItem(this, this.currentItem) as? Fragment
}

fun ViewPager.getFragmentAt(index: Int): Fragment? {
  val count = this.adapter?.count
  if (count == null || count == 0) {
    return null
  }

  if (index in 0..count) {
    return this.adapter?.instantiateItem(this, index) as? Fragment
  }
  return null
}

fun Activity.makeCall(phone: String): Boolean {
  try {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
    return true
  } catch (e: Exception) {
    e.printStackTrace()
    return false
  }
}

fun Activity.gotoMarket(): Boolean {
  try {
    val pkgName = this.applicationContext.packageName
    val uri = Uri.parse("market://details?id=$pkgName")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    this.startActivity(intent)
    return true
  } catch (e: Exception) {
    e.printStackTrace()
    return false
  }
}

fun Context.hasPermission(permission: String): Boolean {
  return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

data class MAppInfo(
  val packageName: String,
  val name: String,
  val version: String,
  val isSystemApp: Boolean,
  val appInfo: ApplicationInfo
)

fun Context.listInstalledApp(): Single<ArrayList<MAppInfo>> {
  val subject = SingleSubject.create<ArrayList<MAppInfo>>()
  try {
    val list = arrayListOf<MAppInfo>()
    val pm = this.packageManager
    pm.getInstalledPackages(0).forEach {
      list.add(
        MAppInfo(
          it.packageName,
          it.applicationInfo.loadLabel(pm).toString(),
          it.versionName,
          it.applicationInfo.flags.and(ApplicationInfo.FLAG_SYSTEM) != 0,
          it.applicationInfo
        )
      )
    }
    subject.onSuccess(list)
  } catch (e: Exception) {
    subject.onError(e)
  }
  return subject
}

fun Activity.toast(message: String, isLong: Boolean = false) {
  if (message.isNotEmpty() && !isFinishingSafe()) {
    makeToast(message, isLong).safeShow()
  }
}

fun Context.makeToast(message: String, isLong: Boolean = false): Toast {
  return Toast.makeText(this, message, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
}

fun systemProperty(propName: String): String {
  var line = ""
  var input: BufferedReader? = null
  try {
    val prop = Runtime.getRuntime().exec("getprop $propName")
    input = BufferedReader(InputStreamReader(prop.inputStream), 1024)
    line = input.readLine()
  } catch (e: Exception) {
    e.printStackTrace()
  } finally {
    input?.close()
  }
  return line
}

fun Activity.color(id: Int): Int {
  return this.resources.getColor(id)
}