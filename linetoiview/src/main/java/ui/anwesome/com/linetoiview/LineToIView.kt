package ui.anwesome.com.linetoiview

/**
 * Created by anweshmishra on 28/04/18.
 */

import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
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

}