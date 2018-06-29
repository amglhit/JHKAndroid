package com.amglhit.mmap.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.amglhit.mmap.R
import timber.log.Timber
import java.util.*

class MapControllerFragment : Fragment() {
  companion object {
    @JvmStatic
    fun newInstance() =
      MapControllerFragment()
  }

  private lateinit var rootView: View

  private lateinit var mmap: IMapView

  fun initMap(map: IMapView) {
    mmap = map
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    rootView = inflater.inflate(R.layout.fragment_map_controller, container, false)
    return rootView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initView()
  }

  private var lastMarkerId: String = ""
  private var index: Int = 0
  private fun initView() {
    rootView.findViewById<Button>(R.id.btn_current_location).setOnClickListener {
      Timber.d("my location")
      mmap.showMyLocation()
    }

    rootView.findViewById<Button>(R.id.btn_zoom_in).setOnClickListener {
      mmap.zoomIn()
    }

    rootView.findViewById<Button>(R.id.btn_zoom_out).setOnClickListener {
      mmap.zoomOut()
    }

    rootView.findViewById<Button>(R.id.btn_set_center).setOnClickListener {
      mmap.setCenter()
    }

    rootView.findViewById<Button>(R.id.btn_add_marker).setOnClickListener {
      //      lastMarkerId =
      val p = generateMarker()
      val data = MMarker(p.first, p.second, index++.toString(), "", null)
      lastMarkerId = mmap.addMarker(data)
    }

    rootView.findViewById<Button>(R.id.btn_remove_marker).setOnClickListener {
      mmap.removeMarker(lastMarkerId)
    }

    rootView.findViewById<Button>(R.id.btn_clear_marker).setOnClickListener {
      mmap.clearMarkers()
    }
  }

  private fun generateMarker(): Pair<Double, Double> {
    val loc = mmap.getMyLocation()
    val lat = loc.first.plus(Random().nextDouble() / 1000)
    val lng = loc.second.plus(Random().nextDouble() / 1000)
    return Pair(lat, lng)
  }
}
