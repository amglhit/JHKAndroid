package com.amglhit.mmap.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amglhit.mmap.MMap

import com.amglhit.mmap.R
import kotlinx.android.synthetic.main.mmap_fragment.*

abstract class MMapFragment : Fragment() {
  private lateinit var viewModel: MmapViewModel
  private lateinit var mmap: MMap

  private lateinit var rootView: View

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    rootView = inflater.inflate(R.layout.mmap_fragment, container, false)
    return rootView
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(MmapViewModel::class.java)
    bindViewModel()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    mmap = MMap(lifecycle, map_view)
    mmap.onCreate(savedInstanceState)
    initView()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mmap.onSaveInstanceState(outState)
  }

  abstract fun initView()

  private fun bindViewModel() {

  }
}
