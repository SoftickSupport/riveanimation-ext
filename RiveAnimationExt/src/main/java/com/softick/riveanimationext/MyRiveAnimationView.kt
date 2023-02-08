package com.softick.riveanimationext

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import app.rive.runtime.kotlin.RiveAnimationView

open class MyRiveAnimationView(context: Context, attrs: AttributeSet?) : RiveAnimationView(context, attrs)
{
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return false
    }
}