package com.softick.riveanimationext

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import androidx.annotation.RawRes
import app.rive.runtime.kotlin.RiveAnimationView
import app.rive.runtime.kotlin.core.Alignment
import app.rive.runtime.kotlin.core.Direction
import app.rive.runtime.kotlin.core.Fit
import app.rive.runtime.kotlin.core.Loop
import java.util.*

class RiveAnimation
{
    var ANIMATIONDELAY_VICTORY = 666

    var ctx: Context? = null
    var frameLayout: FrameLayout? = null
    var handlerAnim: Handler? = null

    var animList: ArrayList<MyRiveAnimationView>? = null

    constructor (context: Context)
    {
        ctx = context
        handlerAnim = Handler()
        animList = ArrayList<MyRiveAnimationView>()
        frameLayout = FrameLayout(ctx!!)
        val layoutparams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        frameLayout!!.setLayoutParams(layoutparams)
    }

    fun init(rootView: View?): View?
    {
        //!
        if (frameLayout == null) return rootView

        //add rootView to the begin of the frameLayout in order for all animations be on the top
        //
        frameLayout!!.addView(rootView, 0)
        return frameLayout
    }

    private fun setViewMetrics(view: View, width: Int, height: Int, top: Int, bottom: Int, start: Int, end: Int)
    {
        val lp = view.layoutParams as MarginLayoutParams
        if (width >= 0) lp.width = width
        if (height >= 0) lp.height = height
        if (top >= 0) lp.topMargin = top
        if (bottom >= 0) lp.bottomMargin = bottom
        if (start >= 0) lp.marginStart = start
        if (end >= 0) lp.marginEnd = end
        view.layoutParams = lp
    }

    fun addAnimation(@RawRes id: Int, animParams: IntArray)
    {
        //!
        if (frameLayout == null) return

        //clear previous?
        if (animParams.size < 6) return
        val anim = MyRiveAnimationView(ctx!!, null)
        frameLayout!!.addView(anim)

        setViewMetrics(anim, animParams[0], animParams[1], animParams[2], animParams[3], animParams[4], animParams[5])
        anim.alignment = Alignment.CENTER
        anim.fit = Fit.FILL
        anim.setRiveResource(id, null, null, null, false, Fit.FILL, Alignment.CENTER, Loop.ONESHOT)
        anim.visibility = View.INVISIBLE

        //frameLayout.addView(anim);
        animList!!.add(anim)
    }

    fun addAnimationsRandom(@RawRes id: Int, count: Int, animWidth: Int, animHeight: Int, screenWidth: Int, screenHeight: Int)
    {
        //!
        if (frameLayout == null) return
        val rand = Random()

        for (i in 0 until count) {
            val anim = MyRiveAnimationView(ctx!!, null)
            frameLayout!!.addView(anim)
            val marginStart = rand.nextInt(screenWidth - animWidth)
            val marginTop = rand.nextInt(screenHeight - animHeight)
            val marginEnd = screenWidth - marginStart
            val marginBottom = screenHeight - marginTop

            setViewMetrics(anim, animWidth, animHeight, marginTop, marginBottom, marginStart, marginEnd)

            anim.alignment = Alignment.CENTER
            anim.fit = Fit.FILL
            anim.setRiveResource(id, null, null, null, false, Fit.FILL, Alignment.CENTER, Loop.ONESHOT)
            anim.visibility = View.INVISIBLE

            animList!!.add(anim)
        }
    }

    private fun randomizeAnimationSequence()
    {
        val rand = Random()

        for (i in animList!!.indices)
        {
            val rand1 = rand.nextInt(animList!!.size)
            val rand2 = rand.nextInt(animList!!.size)
            if (rand1 != rand2)
            {
                //swap rand1 and rand2 elements of the animation list
                val tmpView = animList!![rand1]
                animList!![rand1] = animList!![rand2]
                animList!![rand2] = tmpView
            }
        }
    }

    fun playRandomOrder()
    {
        randomizeAnimationSequence()
        play()
    }

    fun play()
    {
        for (i in animList!!.indices)
        {
            val anim: RiveAnimationView = animList!![i]
            val r = Runnable {
                anim.visibility = View.VISIBLE
                anim.play(Loop.ONESHOT, Direction.AUTO, true)
            }
            handlerAnim!!.postDelayed(r, i.toLong() * ANIMATIONDELAY_VICTORY)
        }
    }

    fun setAnimationDelay(delay: Int)
    {
        ANIMATIONDELAY_VICTORY = delay
    }

    fun stopAll()
    {
        handlerAnim!!.removeCallbacksAndMessages(null)
    }
}