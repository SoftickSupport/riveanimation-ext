package com.softick.riveanimationext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import android.os.Handler;

import androidx.annotation.RawRes;

import java.util.ArrayList;
import java.util.Random;

import app.rive.runtime.kotlin.RiveAnimationView;
import app.rive.runtime.kotlin.core.Alignment;
import app.rive.runtime.kotlin.core.Direction;
import app.rive.runtime.kotlin.core.Fit;
import app.rive.runtime.kotlin.core.Loop;

public class RiveAnimationJava
{
    int ANIMATIONDELAY_VICTORY     = 666;

    Context ctx;
    FrameLayout frameLayout;
    Handler handlerAnim;

    ArrayList<MyRiveAnimationViewJava> animList;

    public RiveAnimationJava(Context context)
    {
        ctx = context;
        handlerAnim = new Handler();
        animList = new ArrayList<>();

        frameLayout = new FrameLayout(ctx);
        FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.setLayoutParams(layoutparams);
    }

    public View init(View rootView)
    {
        //!
        if (frameLayout == null) return rootView;

        //add rootView to the begin of the frameLayout in order for all animations be on the top
        //
        frameLayout.addView(rootView, 0);

        return frameLayout;
    }

    private void setViewMetrics(View view, int width, int height, int top, int bottom, int start, int end) {
        final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (width >= 0) lp.width = width;
        if (height >= 0) lp.height = height;
        if (top >= 0) lp.topMargin = top;
        if (bottom >= 0) lp.bottomMargin = bottom;
        if (start >= 0) lp.setMarginStart(start);
        if (end >= 0) lp.setMarginEnd(end);
        view.setLayoutParams(lp);
    }

    public void addAnimation(@RawRes int id, int[] animParams)
    {
        //!
        if (frameLayout == null) return;

        //clear previous?
        if (animParams.length < 6) return;

        MyRiveAnimationViewJava anim = new MyRiveAnimationViewJava(ctx, null);
        frameLayout.addView(anim);

        setViewMetrics(anim, animParams[0], animParams[1], animParams[2], animParams[3], animParams[4], animParams[5]);

        anim.setAlignment(Alignment.CENTER);
        anim.setFit(Fit.FILL);
        anim.setRiveResource(id, null, null, null, false, Fit.FILL, Alignment.CENTER, Loop.ONESHOT);
        anim.setVisibility(View.INVISIBLE);

        //frameLayout.addView(anim);

        animList.add(anim);
    }

    public void addAnimationsRandom(@RawRes int id, int count, int animWidth, int animHeight, int screenWidth, int screenHeight)
    {
        //!
        if (frameLayout == null) return;

        Random rand = new Random();

        for (int i = 0; i < count; i++)
        {
            MyRiveAnimationViewJava anim = new MyRiveAnimationViewJava(ctx, null);
            frameLayout.addView(anim);

            int marginStart = rand.nextInt(screenWidth - animWidth);
            int marginTop = rand.nextInt(screenHeight - animHeight);

            int marginEnd = screenWidth - marginStart;
            int marginBottom = screenHeight - marginTop;

            setViewMetrics(anim, animWidth, animHeight, marginTop, marginBottom, marginStart, marginEnd);

            anim.setAlignment(Alignment.CENTER);
            anim.setFit(Fit.FILL);
            anim.setRiveResource(id, null, null, null, false, Fit.FILL, Alignment.CENTER, Loop.ONESHOT);
            anim.setVisibility(View.INVISIBLE);

            animList.add(anim);
        }
    }

    private void randomizeAnimationSequence()
    {
        Random rand = new Random();
        for (int i = 0; i < animList.size(); i++)
        {
            int rand1 = rand.nextInt(animList.size());
            int rand2 = rand.nextInt(animList.size());
            if (rand1 != rand2) {
                //swap rand1 and rand2 elements of the animation list
                MyRiveAnimationViewJava tmpView = animList.get(rand1);
                animList.set(rand1, animList.get(rand2));
                animList.set(rand2, tmpView);
            }
        }
    }

    public void playRandomOrder()
    {
        randomizeAnimationSequence();
        play();
    }
    
    public void play()
    {
        for (int i = 0; i < animList.size(); i++)
        {
            RiveAnimationView anim = animList.get(i);
            final Runnable r = new Runnable() {
                @Override
                public void run() {
                    anim.setVisibility(View.VISIBLE);
                    anim.play(Loop.ONESHOT, Direction.AUTO, true);
                }
            };
            handlerAnim.postDelayed(r, (long) i * ANIMATIONDELAY_VICTORY);
        }
    }

    public void setAnimationDelay(int delay)
    {
        ANIMATIONDELAY_VICTORY = delay;
    }

    public void stopAll()
    {
        handlerAnim.removeCallbacksAndMessages(null);
    }
}
