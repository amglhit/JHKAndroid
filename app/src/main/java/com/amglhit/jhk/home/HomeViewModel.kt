package com.amglhit.jhk.home

import android.arch.lifecycle.ViewModel
import com.amglhit.mlocation.LocationLiveData
import com.amglhit.msuite.data.ObservableData
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class HomeViewModel : ViewModel() {
  val locationLiveData = LocationLiveData()
  val homeSP = HomeSP()
  val userState = ObservableData("default-state")

  private val disposables = CompositeDisposable()

  init {
    disposables.add(
      userState.observe { old, new ->
        Timber.d("user state changed: $old -> $new   at|${Thread.currentThread()} ")
      })
  }

  override fun onCleared() {
    super.onCleared()
    disposables.clear()
  }
}