package com.cat.view.number.clock

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

// 一个圆单元参数
class CircleDrawer {

    var x: Float = 0.0f
    var y: Float = 0.0f
    var radius: Float = 0.0f
    var paint: Paint ? = null

    // 绘制基础圆
    fun drawBaseCircle(canvas: Canvas) {
        val pat = paint ?: return
        // 画圆
        pat.color = Color.parseColor("#F4EBEB")
        canvas.drawCircle(x, y, radius, pat)
        // 画小白点
        pat.color = Color.WHITE
        canvas.drawCircle(x, y, pat.strokeWidth / 2, pat)
    }

    // 绘制圆中心点
    fun drawBlackPoint(canvas: Canvas) {
        val pat = paint ?: return
        // 画小黑点
        pat.color = Color.BLACK
        canvas.drawCircle(x, y, pat.strokeWidth / 2, pat)
    }

    // 通过绘制参数绘制线段
    fun drawByCircleDrawParam(canvas: Canvas, param: CircleDrawParam) {
        val pat = paint ?: return
        // 画小黑点
        pat.color = Color.BLACK
        pat.alpha = param.line1Alpha
        canvas.drawCircle(x, y, pat.strokeWidth / 2, pat)

        // 画线1
        canvas.save()
        canvas.translate(x, y)
        canvas.rotate(param.line1Angle)
        pat.alpha = param.line1Alpha
        canvas.drawLine(0f, 0f, radius, 0f, pat)
        canvas.restore()

        canvas.save()
        canvas.translate(x, y)
        canvas.rotate(param.line2Angle)
        pat.alpha = param.line2Alpha
        canvas.drawLine(0f, 0f, radius, 0f, pat)
        canvas.restore()
    }

}