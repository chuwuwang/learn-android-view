package com.cat.view.ui.animation

import com.cat.view.databinding.ActivityAnimationMainBinding
import com.cat.view.ui.ViewBindingActivity
import com.cat.view.utils.openActivity

class AnimationActivity : ViewBindingActivity<ActivityAnimationMainBinding>() {

    override fun init() {
        initView()
    }

    private fun initView() {
        binding.btnRippleLike.setOnClickListener { openActivity<RippleLikeViewActivity>() }
        binding.btnFireExplore.setOnClickListener { openActivity<FireExploreViewActivity>() }
        binding.btnDragRestored.setOnClickListener { openActivity<DragRestoredViewActivity>() }
        binding.btnStereoBallSport.setOnClickListener { openActivity<StereoBallSportViewActivity>() }
        binding.btnCircleStickIndicator.setOnClickListener { openActivity<CircleStickIndicatorActivity>() }
    }

}