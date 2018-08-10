package com.amglhit.msuite.viewpager

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * 作为容器的ViewPager，去掉了手势操作的功能
 */
class MViewPager : ViewPager {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

  override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
    return false
  }


  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(ev: MotionEvent?): Boolean {
    return false
  }
}