package com.amglhit.msuite.base

import android.os.Bundle
import android.view.View

/**
 * 延迟加载的Fragment
 *
 */
abstract class MLazyFragment : MFragment() {
  protected open fun onFragmentShow(first: Boolean = false) {
  }

  protected open fun onFragmentHide() {
  }

  //是否展示过一次
  private var hasShown = false
  //当前是否可见，为了防止重复回调onFragmentShow和onFragmentHide。
  private var isFragmentVisible = false

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    if (!isHidden && userVisibleHint) {
      onPossibleShow()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    hasShown = false
  }

  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
    if (isViewCreated) {
      if (isVisibleToUser) {
        onPossibleShow()
      } else {
        onPossibleHide()
      }
    }
    super.setUserVisibleHint(isVisibleToUser)
  }

  override fun onResume() {
    super.onResume()
    if (!isHidden && userVisibleHint) {
      onPossibleShow()
    }
  }

  override fun onPause() {
    super.onPause()
    if (!isHidden && userVisibleHint) {
      onPossibleHide()
    }
  }

  override fun onHiddenChanged(hidden: Boolean) {
    super.onHiddenChanged(hidden)
    if (hidden) {
      onPossibleHide()
    } else {
      onPossibleShow()
    }
  }

  private fun onPossibleShow() {
    if (!isFragmentVisible) {
      this.isFragmentVisible = true
      val isFirstShow = !hasShown
      hasShown = true
      onFragmentShow(isFirstShow)
    }
  }

  private fun onPossibleHide() {
    if (isFragmentVisible) {
      isFragmentVisible = false
      onFragmentHide()
    }
  }

  fun isFragmentVisible(): Boolean {
    val parent = parentFragment as? MLazyFragment
    return if (parent != null) {
      isFragmentVisible && parent.isFragmentVisible()
    } else {
      isFragmentVisible
    }
  }
}