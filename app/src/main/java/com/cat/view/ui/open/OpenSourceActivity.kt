package com.cat.view.ui.open

import com.cat.view.databinding.ActivityOpenMainBinding
import com.cat.view.ui.ViewBindingActivity
import com.cat.view.utils.openActivity

class OpenSourceActivity : ViewBindingActivity<ActivityOpenMainBinding>() {

    override fun init() {
        initView()
    }

    private fun initView() {
        binding.btnLineNumberClock.setOnClickListener { openActivity<LineNumberClockActivity>() }
    }

}