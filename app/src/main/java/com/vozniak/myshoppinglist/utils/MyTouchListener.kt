package com.vozniak.myshoppinglist.utils

import android.view.MotionEvent
import android.view.View

class MyTouchListener : View.OnTouchListener {
    var xDelta = 0.0f
    var yDelta = 0.0f
    override fun onTouch(p0: View, p1: MotionEvent?): Boolean {
        when(p1?.action){
            MotionEvent.ACTION_DOWN -> {
                xDelta = p0.x - p1.rawX
                yDelta = p0.y - p1.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                p0.x = xDelta + p1.rawX
                p0.y = yDelta + p1.rawY
            }
        }
        return true
    }
}