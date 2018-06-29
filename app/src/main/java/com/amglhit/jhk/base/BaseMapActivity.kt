package com.amglhit.jhk.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.amglhit.jhk.R
import com.amglhit.mmap.base.BaseMapFragment
import com.amglhit.mmap.base.IMapView
import com.amglhit.mmap.gd.MMapFragment
import com.amglhit.mmap.base.MapControllerFragment
import timber.log.Timber

class BaseMapActivity : BaseActivity(), BaseMapFragment.MapFragmentCallback {
  companion object {
    fun newIntent(context: Context): Intent {
      return Intent(context, BaseMapActivity::class.java)
    }
  }

  private lateinit var mapFragment: BaseMapFragment
  private lateinit var controllerFragment: MapControllerFragment

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.base_map_activity)
    if (savedInstanceState == null) {
      mapFragment = MMapFragment.newInstance()
      controllerFragment = MapControllerFragment.newInstance()

      supportFragmentManager.beginTransaction()
        .replace(R.id.map_container, mapFragment)
        .replace(R.id.view_container, controllerFragment)
        .commitNow()
    }
  }

  override fun onMapCreate(map: IMapView) {
    Timber.d("on map ready")
    controllerFragment.initMap(map)
    map.showMyLocation(true)
  }

  override fun onMapDestroy() {
    Timber.d("on map destroy")
  }
}
