package com.amglhit.msuite.base

abstract class GlobalExceptionHandler :
  Thread.UncaughtExceptionHandler {
  private val prevHandler = Thread.getDefaultUncaughtExceptionHandler()

  override fun uncaughtException(t: Thread?, e: Throwable?) {
    val handled = handleException(t, e)
    if (!handled && prevHandler != null) {
      prevHandler.uncaughtException(t, e)
    }
  }

  abstract fun handleException(t: Thread?, e: Throwable?): Boolean
}