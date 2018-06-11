package com.amglhit.msuite.livedata

/**
 * 使用 LiveData 时，通过这个封装处理类似跳转、Toast等事件。
 * 调用getIfNotHandled 获取数据以保证只处理一次。
 */
open class MLiveEvent<out T>(val data: T) {
  var isHandled = false
    private set

  /**
   * return null if handled
   */
  fun getAndHandleIfNotHandled(): T? {
    return if (isHandled) null
    else {
      isHandled = true
      data
    }
  }
}