package com.pangge.moontest.pageIndicator;

import com.pangge.moontest.pageIndicator.animation.AnimationManager;
import com.pangge.moontest.pageIndicator.animation.controller.ValueController;
import com.pangge.moontest.pageIndicator.animation.data.Value;
import com.pangge.moontest.pageIndicator.draw.DrawManager;
import com.pangge.moontest.pageIndicator.draw.data.Indicator;

/**
 * Created by iuuu on 17/6/7.
 */

public class IndicatorManager implements ValueController.UpdateListener{
    private DrawManager drawManager;
    private AnimationManager animationManager;
    private Listener listener;

    interface Listener{
        void onIndicatorUpdated();
    }

    public IndicatorManager(Listener listener) {
        this.drawManager = new DrawManager();
        this.animationManager = new AnimationManager(drawManager.indicator(), this);
        this.listener = listener;
    }

    public AnimationManager animate() {
        return animationManager;
    }

    public Indicator indicator(){
        return drawManager.indicator();
    }

    public DrawManager drawer() {
        return drawManager;
    }

    @Override
    public void onValueUpdated(Value value) {
        drawManager.updateValue(value);
        if (listener != null) {
            listener.onIndicatorUpdated();
        }
    }
}
