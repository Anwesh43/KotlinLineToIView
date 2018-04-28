package ui.anwesome.com.linetoiview

/**
 * Created by anweshmishra on 28/04/18.
 */

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint.Cap.*
import android.view.View
import android.view.MotionEvent

class LineToIView (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val renderer : LTIRenderer = LTIRenderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class LTIState (var prevScale : Float = 0f, var dir : Float = 0f, var j : Int = 0, var delay : Int = 0) {

        val scales : Array<Float> = arrayOf(0f, 0f, 0f)

        val MAX_DELAY = 10

        fun update(stopcb : (Float) -> Unit) {

            if (delay == 0) {
                scales[j] += 0.1f * dir
                if (Math.abs(scales[j] - prevScale) > 1) {
                    scales[j] = prevScale + dir
                    delay++
                }
            } else {
                delay++
                if (delay == MAX_DELAY) {
                    j += dir.toInt()
                    delay = 0
                    if (j == scales.size || j == -1) {
                        j -= dir.toInt()
                        dir = 0f
                        prevScale = scales[j]
                        stopcb(prevScale)
                    }
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
            val size : Float =  2 * Math.min(w,h) / 5
            val updatedSize : Float = (size/2) * state.scales[0]
            paint.color = Color.parseColor("#4527A0")
            paint.strokeCap = ROUND
            paint.strokeWidth = size/6
            canvas.save()
            canvas.translate(w/2, h/2)
            canvas.rotate(90f * state.scales[2])
            for (i in 0..1) {
                val x : Float = size * state.scales[1] * (1 - 2 * i)
                canvas.drawLine(0f, 0f, x, 0f, paint)
                canvas.drawLine(x, -updatedSize, x, updatedSize, paint)
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

    data class LTIRenderer (var view : LineToIView) {

        private val lti : LineToI = LineToI(0)

        private val animator : LTIAnimator = LTIAnimator(view)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            lti.draw(canvas, paint)
            animator.update {
                lti.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            lti.startUpdating {
                animator.start()
            }
        }
    }

    companion object {
        fun create(activity : Activity) : LineToIView {
            val view : LineToIView = LineToIView(activity)
            activity.setContentView(view)
            return view
        }
    }
}