package com.amglhit.mmap.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amglhit.mmap.gd.MLifecycleMap

abstract class BaseMapFragment : Fragment() {
  interface MapFragmentCallback {
    fun onMapCreate(map: IMapView)
    fun onMapDestroy()
  }

  private var _lifecycleMap: MLifecycleMap? = null
  var isMapReady = false
    private set

  private lateinit var rootView: View

  abstract val layoutId: Int
  abstract fun createMapView(): IMapView

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    isMapReady = false
    rootView = inflater.inflate(layoutId, container, false)
    return rootView
  }

  override fun onDestroyView() {
    super.onDestroyView()
    isMapReady = false
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    _lifecycleMap = MLifecycleMap(this.lifecycle, createMapView())
    isMapReady = true

    _lifecycleMap?.onCreate(savedInstanceState)
    _lifecycleMap?.let {
      callback?.onMapCreate(it.mapView)
    }
    initView()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    _lifecycleMap?.onSaveInstanceState(outState)
  }

  abstract fun initView()

  private var callback: MapFragmentCallback? = null

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    if (context is MapFragmentCallback) {
      callback = context
      _lifecycleMap?.let {
        callback?.onMapCreate(it.mapView)
      }
    }
  }

  override fun onDetach() {
    super.onDetach()
    callback?.onMapDestroy()
    callback = null
  }
}