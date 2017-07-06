package com.pangge.moontest.pageIndicator.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Pair;

import com.pangge.moontest.pageIndicator.animation.data.Value;
import com.pangge.moontest.pageIndicator.draw.controller.AttributeController;
import com.pangge.moontest.pageIndicator.draw.controller.DrawController;
import com.pangge.moontest.pageIndicator.draw.controller.MeasureController;
import com.pangge.moontest.pageIndicator.draw.data.Indicator;

/**
 * Created by iuuu on 17/6/7.
 */

public class DrawManager {
    private Indicator indicator;
    private DrawController drawController;
    private MeasureController measureController;
    private AttributeController attributeController;

    public DrawManager() {
        this.indicator = new Indicator();
        this.drawController = new DrawController(indicator);
        this.measureController = new MeasureController();
        this.attributeController = new AttributeController(indicator);
    }

    @NonNull
    public Indicator indicator() {
        if (indicator == null) {
            indicator = new Indicator();
        }

        return indicator;
    }

    public void updateValue(@Nullable Value value) {
        drawController.updateValue(value);
    }

    public void draw(@NonNull Canvas canvas) {
        drawController.draw(canvas);
    }

    public Pair<Integer, Integer> measureViewSize(int widthMeasureSpec, int heightMeasureSpec) {
        return measureController.measureViewSize(indicator, widthMeasureSpec, heightMeasureSpec);
    }

    public void initAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
        attributeController.init(context, attrs);
    }

}
