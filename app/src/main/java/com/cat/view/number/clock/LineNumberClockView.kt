package com.cat.view.number.clock

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

class LineNumberClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet ? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr), DefaultLifecycleObserver {

    private var progress = 999
    private val manager = ComponentsManager()
    private val mainLooper = Looper.getMainLooper()
    private val handler = Handler(mainLooper)

    init {
        manager.initConfigureInfo(context, attrs)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 计算最小宽高值
        manager.initComponents(w, h)
    }

    private val showRunnable = Runnable {
        showInAnim()
        showNext()
    }

    private fun showNext() {
        handler.postDelayed(showRunnable, 1000L - System.currentTimeMillis() % 1000L)
    }

    fun bindLifecycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    private fun showInAnim() {
        progress = 0
        val anim = ValueAnimator.ofInt(0, 999).apply {
            addUpdateListener {
                progress = it.animatedValue as Int
                invalidate()
            }
            duration = 650L
        }
        anim.start()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        postDelayed(showRunnable, 1000L - System.currentTimeMillis() % 1000L)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        handler.removeCallbacks(showRunnable)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        manager.draw(canvas, progress)
    }

}