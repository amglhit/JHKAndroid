package com.amglhit.msuite.statusbar

import android.app.Activity
import android.os.Build
import android.support.annotation.ColorInt
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.widget.Toolbar
import android.view.View

/**
 * 设置状态栏颜色和浅色模式
 * return 是否浅色模式
 * false 表示不支持浅色模式，为了体验应该把statusBar设置成深色
 */
fun Activity.setStatusBarLightMode(color: Int, isLight: Boolean = true): Boolean {
  if (LightStatusBar.isLightStatusSupported()) {
    if (isLight) {
      setStatusBarColor(color)
      //先设置颜色
      this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
    return true
  } else if (LightStatusBar.miuiSetStatusBarLightMode(
      this,
      isLight
    ) || LightStatusBar.flymeSetStatusBarLightMode(this, isLight)
  ) {
    setStatusBarColor(color)
    return true
  }
  return false
}

fun Activity.setTranslucentStatus(hideStatusBarBackground: Boolean = false) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    StatusBarLollipop.translucentStatusBar(this, hideStatusBarBackground)
  } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    StatusBarKitkat.translucentStatusBar(this)
  }
}

fun Activity.setStatusBarColor(color: Int) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    StatusBarLollipop.setStatusBarColor(this, color)
  } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    StatusBarKitkat.setStatusBarColor(this, color)
  }
}

fun Activity.setStatusBarColorForCollapsingToolbar(
  appBarLayout: AppBarLayout, collapsingToolbarLayout: CollapsingToolbarLayout,
  toolbar: Toolbar, @ColorInt statusColor: Int
) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
    StatusBarLollipop.setStatusBarColorForCollapsingToolbar(
      this,
      appBarLayout, collapsingToolbarLayout,
      toolbar, statusColor
    )

  } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    StatusBarKitkat.setStatusBarColorForCollapsingToolbar(
      this,
      appBarLayout, collapsingToolbarLayout,
      toolbar, statusColor
    )
  }
}

