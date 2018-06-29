package com.amglhit.mmap.gd

import com.amglhit.mmap.R
import com.amglhit.mmap.base.BaseMapFragment
import com.amglhit.mmap.base.IMapView
import kotlinx.android.synthetic.main.mmap_fragment.*

class GDMapFragment : BaseMapFragment() {
  companion object {
    fun newInstance() = GDMapFragment()
  }

  override val layoutId: Int = R.layout.mmap_fragment

  override fun createMapView(): IMapView = GDMapImpl(map_view)

  override fun initView() {
  }
}
