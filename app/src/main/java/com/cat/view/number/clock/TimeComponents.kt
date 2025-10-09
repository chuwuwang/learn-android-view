package com.cat.view.number.clock

import android.graphics.Canvas

class TimeComponents(private val array: Array<Array<CircleDrawer>>) : AbsComponents() {

    override val type: ComponentsType
        get() = ComponentsType.COMPONENTS_TYPE_TIME

    private var newLeftNumber: AbsNumberDrawParam ? = null
    private var lastLeftNumber: AbsNumberDrawParam ? = null
    private var newRightNumber: AbsNumberDrawParam ? = null
    private var lastRightNumber: AbsNumberDrawParam ? = null

    private var drawProgress: Float = 0f

    fun setNumberAndProgress(number: Int, process: Float) {
        newLeftNumber = transformNumber(number / 10)
        newRightNumber = transformNumber(number % 10)
        drawProgress = process
    }

    override fun draw(canvas: Canvas) {
        if (drawProgress == 1f) {
            lastLeftNumber = newLeftNumber
            lastRightNumber = newRightNumber
        }
        val nLeftNumber = newLeftNumber ?: return
        val lLeftNumber = lastLeftNumber ?: return

        val leftTempNumber = lLeftNumber.transition(nLeftNumber, drawProgress)
        val rightTempNumber = lLeftNumber.transition(nLeftNumber, drawProgress)
        val leftNumber = NumberProxy(
            array = arrayOf(
                arrayOf(array[0][0], array[0][1]),
                arrayOf(array[1][0], array[1][1]),
                arrayOf(array[2][0], array[2][1]),
            ), leftTempNumber
        )
        leftNumber.draw(canvas)
        val rightNumber = NumberProxy(
            array = arrayOf(
                arrayOf(array[0][2], array[0][3]),
                arrayOf(array[1][2], array[1][3]),
                arrayOf(array[2][2], array[2][3]),
            ), rightTempNumber
        )
        rightNumber.draw(canvas)
    }

    private fun transformNumber(i: Int): AbsNumberDrawParam ? {
        return when (i) {
            0 -> Number0()
            1 -> Number1()
            2 -> Number2()
            3 -> Number3()
            4 -> Number4()
            5 -> Number5()
            6 -> Number6()
            7 -> Number7()
            8 -> Number8()
            9 -> Number9()
            else -> null
        }
    }

}