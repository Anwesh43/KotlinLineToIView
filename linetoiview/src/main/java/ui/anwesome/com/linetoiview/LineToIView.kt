package ui.anwesome.com.linetoiview

/**
 * Created by anweshmishra on 28/04/18.
 */

import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint.Cap.*
import android.view.View
import android.view.MotionEvent

class LineToIView (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class LTIState (var prevScale : Float = 0f, var dir : Float = 0f, var j : Int = 0) {

        val scales : Array<Float> = arrayOf(0f, 0f, 0f)

        fun update(stopcb : (Float) -> Unit) {
            scales[j] += 0.1f * dir
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += dir.toInt()
                if (j == scales.size || j == -1) {
                    j -= dir.toInt()
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(prevScale)
                }

            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class LTIAnimator (var view : View, var animated : Boolean = false) {

        fun update(updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch (ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class LineToI (var i : Int, val state : LTIState = LTIState()) {

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val size : Float = Math.min(w,h)/10
            val updatedSize : Float = (size/2) * state.scales[0]
            paint.color = Color.parseColor("#4527A0")
            paint.strokeCap = ROUND
            paint.strokeWidth = size/6
            canvas.save()
            canvas.translate(w/2, h/2)
            canvas.rotate(90f * state.scales[2])
            for (i in 0..1) {
                canvas.drawLine(0f, 0f, size * state.scales[1], 0f, paint)
                canvas.drawLine(0f, -updatedSize, 0f, updatedSize, paint)
            }
            canvas.restore()
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }

}