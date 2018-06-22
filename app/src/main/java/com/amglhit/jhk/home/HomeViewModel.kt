package com.amglhit.jhk.home

import android.arch.lifecycle.ViewModel
import com.amglhit.mlocation.LocationLiveData

class HomeViewModel : ViewModel() {
  val locationLiveData = LocationLiveData()
}