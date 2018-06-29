package com.amglhit.mmap.gd

import com.amglhit.mmap.R
import com.amglhit.mmap.base.BaseMapFragment
import com.amglhit.mmap.base.IMapView
import kotlinx.android.synthetic.main.mmap_fragment.*

class MMapFragment : BaseMapFragment() {
  companion object {
    fun newInstance() = MMapFragment()
  }

  override val layoutId: Int = R.layout.mmap_fragment

  override fun createMapView(): IMapView = MMap(map_view)

  override fun initView() {
  }
}
