package com.cat.view.ui.animation

import com.cat.view.databinding.ActivityAnimationFireExploreViewBinding
import com.cat.view.ui.ViewBindingActivity

class FireExploreViewActivity : ViewBindingActivity<ActivityAnimationFireExploreViewBinding>() {

    override fun init() {
        initView()
    }

    private fun initView() {
        binding.btnExplore.setOnClickListener { binding.fireExploreView.startExplore() }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.fireExploreView.release()
    }

}