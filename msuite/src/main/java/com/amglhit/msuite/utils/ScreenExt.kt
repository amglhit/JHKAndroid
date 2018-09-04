package com.amglhit.msuite.utils

import android.content.Context

fun Context.screenHeight() = this.resources.displayMetrics.heightPixels

fun Context.screenWidth() = this.resources.displayMetrics.widthPixels

fun Int.dp2Px(context: Context) = (context.resources.displayMetrics.density * this + 0.5F).toInt()
fun Float.dp2Px(context: Context) = context.resources.displayMetrics.density * this + 0.5F
fun Int.px2DP(context: Context) = (context.resources.displayMetrics.density * this + 0.5F).toInt()
fun Float.px2DP(context: Context) = (context.resources.displayMetrics.density * this + 0.5F).toInt()

fun Int.dp2SP(context: Context) =
  (context.resources.displayMetrics.scaledDensity * this + 0.5F).toInt()

fun Float.dp2SP(context: Context) =
  (context.resources.displayMetrics.scaledDensity * this + 0.5F).toInt()

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