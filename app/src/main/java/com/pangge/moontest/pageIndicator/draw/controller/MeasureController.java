package com.pangge.moontest.pageIndicator.draw.controller;

import android.util.Pair;
import android.view.View;

import com.pangge.moontest.pageIndicator.draw.data.Indicator;

/**
 * Created by iuuu on 17/6/6.
 */

public class MeasureController {
    public Pair<Integer, Integer> measureViewSize(Indicator indicator, int widthMeasureSpec, int heightMeasureSpec){
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

        int count = indicator.getCount();
        int radius = indicator.getRadius();
        int stroke = indicator.getStroke();

        int padding = indicator.getPadding();
        int paddingLeft = indicator.getPadding();
        int paddingTop = indicator.getPaddingTop();
        int paddingRight = indicator.getPaddingRight();
        int paddingBottom = indicator.getPaddingBottom();

        int circleDiameterPx = radius * 2;
        int desiredWidth = 0;
        int desiredHeight = 0;

        int width;
        int height;

        if(count != 0){
            int diameterSum = circleDiameterPx * count;
            int strokeSum = (stroke * 2) * count;

            int paddingSum = padding * (count - 1);
            int w = diameterSum + strokeSum + paddingSum;
            int h = circleDiameterPx + stroke;
            desiredWidth = w;
            desiredHeight = h;
        }


        //orientation-->horizontal
        int horizontalPadding = paddingLeft + paddingRight;
        int verticalPadding = paddingTop + paddingBottom;
        desiredWidth += horizontalPadding;
        desiredHeight += verticalPadding;



        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        if (width < 0) {
            width = 0;
        }

        if (height < 0) {
            height = 0;
        }

        indicator.setWidth(width);
        indicator.setHeight(height);

        return new Pair<>(width, height);
    }
}
