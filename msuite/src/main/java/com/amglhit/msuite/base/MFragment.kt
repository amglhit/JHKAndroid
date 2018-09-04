package com.amglhit.msuite.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amglhit.msuite.utils.toast

abstract class MFragment : Fragment() {
  protected var activity: Activity? = null
    private set
  //onViewCreated执行完成
  var isViewCreated = false
    private set

  private lateinit var rootView: View

  abstract fun getLayoutId(): Int
  abstract fun initView(view: View)

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    rootView = inflater.inflate(getLayoutId(), container, false)
    return rootView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    isViewCreated = true
    initView(view)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    isViewCreated = false
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    activity = context as? Activity
  }

  override fun onDetach() {
    super.onDetach()
    activity = null
  }

  open fun onBackPressed(): Boolean {
    return false
  }

  protected fun toast(message: String, isLong: Boolean = false) {
    activity?.toast(message, isLong)
  }
}