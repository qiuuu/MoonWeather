package com.pangge.moontest.pageIndicator.animation.controller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pangge.moontest.pageIndicator.animation.data.ColorAnimationValue;
import com.pangge.moontest.pageIndicator.animation.data.Value;
import com.pangge.moontest.pageIndicator.animation.type.ColorAnimation;


public class ValueController {

    private ColorAnimation colorAnimation;


    private UpdateListener updateListener;

    public interface UpdateListener {
        /**
        //void onValueUpdated(@Nullable Value value);
         */
        void onValueUpdated(@Nullable Value value);
    }

    public ValueController(@Nullable UpdateListener listener) {
        updateListener = listener;
    }

    @NonNull
    public ColorAnimation color() {
        if (colorAnimation == null) {
            colorAnimation = new ColorAnimation(updateListener);
        }

        return colorAnimation;
    }


}
