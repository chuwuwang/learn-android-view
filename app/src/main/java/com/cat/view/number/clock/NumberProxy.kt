package com.cat.view.number.clock

import android.graphics.Canvas

/**
 * 指的是数字时钟中的一个数字单位
 * 从设计图中可以看出 一个数字单位 由6个圆点组成
 * 然后需要具体数字的绘制参数
 * @param array 传入的6个圆点信息数组
 * @param number 当前数字图形（绘制）参数
 */
class NumberProxy(private val array: Array< Array<CircleDrawer> >, private val number: AbsNumberDrawParam) {

    // 绘制数字
    fun draw(canvas: Canvas) {
        for (index in array.indices) {
            val minimalUhrCircles = array[index]
            for (pos in minimalUhrCircles.indices) {
                val circle = minimalUhrCircles[pos]
                val param = number.params[index * 2 + pos]
                circle.drawBaseCircle(canvas)
                circle.drawByCircleDrawParam(canvas, param)
            }
        }
    }

}