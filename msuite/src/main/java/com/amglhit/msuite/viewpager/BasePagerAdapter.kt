package com.amglhit.msuite.viewpager

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

abstract class BasePagerAdapter<T>(fm: FragmentManager) : FragmentPagerAdapter(fm) {
  private val pageList: ArrayList<T> = arrayListOf()

  fun setupPages(pages: List<T>) {
    pageList.clear()
    pageList.addAll(pages)
  }

  fun getPageData(position: Int): T? {
    if (position >= 0 && position < pageList.size) {
      return pageList[position]
    }
    return null
  }

  override fun getCount(): Int = pageList.size
}