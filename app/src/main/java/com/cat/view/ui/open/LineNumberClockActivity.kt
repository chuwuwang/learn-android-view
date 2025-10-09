package com.cat.view.ui.open

import com.cat.view.databinding.ActivityOpenLineNumberClockBinding
import com.cat.view.ui.ViewBindingActivity

class LineNumberClockActivity : ViewBindingActivity<ActivityOpenLineNumberClockBinding>() {

    override fun init() {
        initView()
    }

    private fun initView() {
        binding.viewLineNumberClock.bindLifecycle(lifecycle)
    }

}