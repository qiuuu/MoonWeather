package com.pangge.moontest.pageIndicator;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.pangge.moontest.pageIndicator.draw.data.Indicator;

/**
 * Created by iuuu on 17/6/7.
 */

public class PageIndicatorView extends View implements ViewPager.OnPageChangeListener, IndicatorManager.Listener{
    private IndicatorManager manager;

    private ViewPager viewPager;
    private DataSetObserver setObserver;


    public PageIndicatorView(Context context) {
        super(context);
    }

    public PageIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        findViewPager();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Pair<Integer, Integer> pair = manager.drawer().measureViewSize(widthMeasureSpec,heightMeasureSpec);


        setMeasuredDimension(pair.first,pair.second);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        manager.drawer().draw(canvas);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        selectInteractiveIndicator(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {

        setSelection(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void findViewPager(){
        if(getContext() instanceof Activity){
            Activity activity = (Activity) getContext();
            int viewPagerId = manager.indicator().getViewPagerId();

            View view = activity.findViewById(viewPagerId);
            if(view != null && view instanceof ViewPager){
                setViewPager((ViewPager)view);
            }
        }
    }

    public void setViewPager(@Nullable ViewPager pager){
        releaseViewPager();
        if (pager == null) {
            return;
        }

        viewPager = pager;
        viewPager.addOnPageChangeListener(this);
        manager.indicator().setViewPagerId(viewPager.getId());
       // Log.i("viewPager id",""+viewPager.getId());


        setDynamicCount(manager.indicator().isDynamicCount());
        int count = getViewPagerCount();
      //  Log.i("viewPager count",""+count);

        setCount(count);

    }
    public void releaseViewPager() {
        if (viewPager != null) {
            viewPager.removeOnPageChangeListener(this);
            viewPager = null;
        }
    }

    public void setDynamicCount(boolean dynamicCount) {
        manager.indicator().setDynamicCount(dynamicCount);

        if (dynamicCount) {
            registerSetObserver();
        } else {
            unRegisterSetObserver();
        }
    }

    private int getViewPagerCount() {
        if (viewPager != null && viewPager.getAdapter() != null) {
            return viewPager.getAdapter().getCount();
        } else {
            return manager.indicator().getCount();
        }
    }

    public void setCount(int count) {
        if (count >= 0 && manager.indicator().getCount() != count) {
            manager.indicator().setCount(count);
            updateVisibility();

            requestLayout();
        }
    }

    private void updateVisibility() {
        if (manager.indicator().isAutoVisibility()) {
            return;
        }

        int count = manager.indicator().getCount();
        int visibility = getVisibility();

        if (visibility != VISIBLE && count > Indicator.MIN_COUNT) {
            setVisibility(VISIBLE);

        } else if (visibility != INVISIBLE && count <= Indicator.MIN_COUNT) {
            setVisibility(View.INVISIBLE);
        }
    }

    private void registerSetObserver() {
        if (setObserver != null || viewPager == null || viewPager.getAdapter() == null) {
            return;
        }

        setObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                updateCount();
            }
        };

        try {
            viewPager.getAdapter().registerDataSetObserver(setObserver);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void updateCount() {
        if (viewPager == null || viewPager.getAdapter() == null) {
            return;
        }

        int newCount = viewPager.getAdapter().getCount();
        int currItem = viewPager.getCurrentItem();

        manager.indicator().setSelectedPosition(currItem);
        manager.indicator().setSelectingPosition(currItem);
        manager.indicator().setLastSelectedPosition(currItem);
        // manager.animate().end();

        setCount(newCount);
    }

    private void unRegisterSetObserver() {
        if (setObserver == null || viewPager == null || viewPager.getAdapter() == null) {
            return;
        }

        try {
            viewPager.getAdapter().unregisterDataSetObserver(setObserver);
            setObserver = null;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        manager = new IndicatorManager(this);
        manager.drawer().initAttributes(context,attrs);


        Indicator indicator = manager.indicator();
        indicator.setPaddingLeft(getPaddingLeft());
        indicator.setPaddingTop(getPaddingTop());
        indicator.setPaddingRight(getPaddingRight());
        indicator.setPaddingBottom(getPaddingBottom());

    }

    @Override
    public void onIndicatorUpdated() {
        invalidate();

    }

    public void setSelection(int position) {
        Indicator indicator = manager.indicator();
       /* if (indicator.isInteractiveAnimation() && indicator.getAnimationType() != AnimationType.NONE) {
            return;
        }*/

        int selectedPosition = indicator.getSelectedPosition();
        int count = indicator.getCount();
        int lastPosition = count - 1;

        if (position < 0) {
            position = 0;
        } else if (position > lastPosition) {
            position = lastPosition;
        }

        if (selectedPosition == position) {
            return;
        }

        indicator.setLastSelectedPosition(indicator.getSelectedPosition());
        indicator.setSelectedPosition(position);
        manager.animate().basic();
    }

    private void selectInteractiveIndicator(int position, float positionOffset) {
        Indicator indicator = manager.indicator();
        //AnimationType animationType = indicator.getAnimationType();
        boolean interactiveAnimation = indicator.isInteractiveAnimation();
     /*   boolean canSelectIndicator = isViewMeasured() && interactiveAnimation && animationType != AnimationType.NONE;

        if (!canSelectIndicator) {
            return;
        }*/
/**Rtl--right-to-left
       // Pair<Integer, Float> progressPair = CoordinatesUtils.getProgress(indicator, position, positionOffset, isRtl());
        int selectingPosition = progressPair.first;
        float selectingProgress = progressPair.second;
        setProgress(selectingPosition, selectingProgress);*/
    }

   /* public void setProgress(int selectingPosition, float progress) {
        Indicator indicator = manager.indicator();
        if (!indicator.isInteractiveAnimation()) {
            return;
        }

        int count = indicator.getCount();
        if (count <= 0 || selectingPosition < 0) {
            selectingPosition = 0;

        } else if (selectingPosition > count - 1) {
            selectingPosition = count - 1;
        }

        if (progress < 0) {
            progress = 0;

        } else if (progress > 1) {
            progress = 1;
        }

        if (progress == 1) {
            indicator.setLastSelectedPosition(indicator.getSelectedPosition());
            indicator.setSelectedPosition(selectingPosition);
        }

        indicator.setSelectingPosition(selectingPosition);
        manager.animate().interactive(progress);
    }*/


}
