package com.cat.view.number.clock

import android.graphics.Canvas

abstract class AbsComponents {

    abstract val type: ComponentsType

    abstract fun draw(canvas: Canvas)

}