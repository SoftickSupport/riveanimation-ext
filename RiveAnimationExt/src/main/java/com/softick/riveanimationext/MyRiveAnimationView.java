package com.softick.riveanimationext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import app.rive.runtime.kotlin.RiveAnimationView;

public class MyRiveAnimationView extends RiveAnimationView {
    public MyRiveAnimationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(@Nullable MotionEvent event) {
        return false;
    }
}
