package com.amglhit.mmap.gd.view

import com.amglhit.mmap.R
import com.amglhit.mmap.base.BaseMapFragment
import com.amglhit.mmap.base.IMapView
import com.amglhit.mmap.gd.MMap
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
