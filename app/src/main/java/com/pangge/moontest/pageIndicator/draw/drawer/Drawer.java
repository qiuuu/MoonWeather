package com.pangge.moontest.pageIndicator.draw.drawer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.pangge.moontest.pageIndicator.animation.data.Value;
import com.pangge.moontest.pageIndicator.draw.data.Indicator;
import com.pangge.moontest.pageIndicator.draw.drawer.type.BasicDrawer;
import com.pangge.moontest.pageIndicator.draw.drawer.type.ColorDrawer;

/**
 * Created by iuuu on 17/6/7.
 */

public class Drawer {
    private BasicDrawer basicDrawer;
    private ColorDrawer colorDrawer;
    private int position;
    private int coordinateX;
    private int coordinateY;

    public Drawer(@NonNull Indicator indicator) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        basicDrawer = new BasicDrawer(paint, indicator);
        colorDrawer = new ColorDrawer(paint, indicator);
    }

    public void setup(int position, int coordinateX, int coordinateY) {
        this.position = position;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public void drawBasic(@NonNull Canvas canvas, boolean isSelectedItem) {
        if (colorDrawer != null) {
            basicDrawer.draw(canvas, position, isSelectedItem, coordinateX, coordinateY);
        }
    }

    public void drawColor(@NonNull Canvas canvas, @NonNull Value value) {
        if (colorDrawer != null) {
            colorDrawer.draw(canvas, value, position, coordinateX, coordinateY);
        }
    }

}
