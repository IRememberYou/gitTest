package com.xky.emptyproject.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import androidx.core.view.ViewConfigurationCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Copyright (C), 2018-2019
 * Author: ziqimo
 * Date: 2019-12-06 19:54
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class PolylineView extends View {
    private int width, height;
    public List<String> mYHearts;
    public List<String> mXDates;
    public List<String> mXTimes;
    public Map<String, PolylineData> mDatas1;
    public Map<String, PolylineData> mDatas2;
    private TextPaint yTextPaint = null;
    private Paint yLinePaint = null;
    private Paint xLinePaint = null;
    private TextPaint xTextPaint = null;
    /**
     * 曲线图的画
     */
    private Paint contentPaint = null;
    private Paint contentPaint2 = null;
    private Path contentPath = null;
    private Path contentPath2 = null;
    private int radius;
    private int radiusCenter;
    private Paint normalPointPaint;
    private Paint normalPointPaint2;
    private Paint normalWhilePointPaint;
    private List<Float> xWeizi = new ArrayList<>();
    private Map<Float, Float> pointMaps = new HashMap<>();
    private Map<Float, Float> pointsMap2 = new HashMap<>();
    private int mTouchSlop;
    private ClickListener clickListener;
    /**
     * 滑动的线在这2个区间内
     */
    float startX = 0;
    float endX = 0;
    private float lastCurX;
    private float lastX;
    private float lastY;
    volatile int selectPos = 0;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public PolylineView(Context context) {
        this(context, null);
    }

    public PolylineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolylineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制y轴上的文字
        int ySize = mYHearts.size();
        if (ySize <= 0) {
            return;
        }
        float textXHeight = xTextPaint.descent() - xTextPaint.ascent();
        float textYHeight = yTextPaint.descent() - yTextPaint.ascent();
        float realHeight = height - getPaddingBottom() - getPaddingTop() - textXHeight * 2;
        float yEqually = realHeight / ySize;
        drawY(canvas, ySize, textYHeight, yEqually);
        //绘制x轴上的文字
        int xSize = mXTimes.size();
        if (xSize <= 0) {
            return;
        }
        float xEqually = (endX - startX) * 1f / xSize;
        float minY = parseFloat4Str(mYHearts.get(0));
        float maxY = parseFloat4Str(mYHearts.get(ySize - 1));
        float yUnit = realHeight * 1f / ySize;
        /**
         * 刷新的时候，实现清除数据
         */
        pointMaps.clear();
        pointsMap2.clear();
        xWeizi.clear();
        for (int i = 0; i < xSize; i++) {
            float nextX = (int) (xEqually * i + startX) + getPaddingLeft();
            //记录滑动线的最大值和最小值
            if (i == 0) {
                startX = Math.max(nextX, startX);
            }
            if (i == xSize - 1) {
                endX = Math.min(nextX, endX);
            }
            String xDate = mXDates.get(i);
            String xText = mXTimes.get(i);
            String getMapKey = String.format("%s-%s", xDate, xText);
            xWeizi.add(nextX);
            //float textHeight = xTextPaint.descent() - xTextPaint.ascent();
            if (xSize % 2 == 0) {
                //承载偶数
                if (i == 0
                        || i == xSize / 2
                        || i == xSize / 2 - 1
                        || i == xSize - 1) {
                    drawXText(canvas, textXHeight, nextX, xDate, xText);
                }
            } else {
                //承载奇数
                if (i == 0
                        || i == xSize / 2
                        || i == xSize - 1) {
                    drawXText(canvas, textXHeight, nextX, xDate, xText);
                }
            }
            lineCalc(realHeight, minY, maxY, yUnit, i, nextX, getMapKey, mDatas1, pointMaps, contentPath);
            //暂时先简单画吧，凑合用
            if (!mDatas2.isEmpty()) {
                lineCalc(realHeight, minY, maxY, yUnit, i, nextX, getMapKey, mDatas2, pointsMap2, contentPath2);
            }
        }

        drawLine(canvas, contentPath, contentPaint, pointMaps, normalPointPaint);

        //暂时先简单画吧，凑合用
        if (!mDatas2.isEmpty()) {
            drawLine(canvas, contentPath2, contentPaint2, pointsMap2, normalPointPaint2);
        }
        //垂直的线
        canvas.drawLine(lastCurX, realHeight, lastCurX, 0, xLinePaint);
    }

    /**
     * 绘制y轴的文字
     */
    private void drawY(Canvas canvas, int ySize, float textYHeight, float yEqually) {
        for (int i = 0; i < ySize; i++) {
            String yText = mYHearts.get(i);
            float nextY = yEqually * (ySize - i);
            float yTextMeasureText = yTextPaint.measureText(yText);
            PointF txtP = new PointF(getPaddingLeft(), nextY + textYHeight / 2.f);
            //文字
            canvas.drawText(yText,txtP.x, txtP.y, yTextPaint);
            //这里画条线
            startX = getPaddingLeft() + yTextMeasureText + dip2px(getContext(), 10);
            endX = width - getPaddingRight();
            canvas.drawLine(startX, nextY, endX, nextY, yLinePaint);
        }
    }

    /**
     * 画折线的位置
     */
    private void drawLine(Canvas canvas, Path contentPath, Paint contentPaint, Map<Float, Float> pointMaps, Paint normalPointPaint) {
        canvas.drawPath(contentPath, contentPaint);
        //画点
        Set<Map.Entry<Float, Float>> entries = pointMaps.entrySet();
        for (Map.Entry<Float, Float> next : entries) {
            canvas.drawCircle(next.getKey(), next.getValue(), radius, normalPointPaint);
            canvas.drawCircle(next.getKey(), next.getValue(), radiusCenter, normalWhilePointPaint);//用于消除交际部分，我菜只能这样做了
        }
    }

    /**
     * 折线的计算位置
     */
    private void lineCalc(float realHeight, float minY, float maxY, float yUnit, int i, float nextX, String getMapKey, Map<String, PolylineData> mDatas1, Map<Float, Float> pointMaps, Path contentPath) {
        //记录下线的位置
        float currentY1 = 0;
        try {
            PolylineData polylineData = mDatas1.get(getMapKey);
            if (polylineData != null) {
                currentY1 = Float.parseFloat(polylineData.value);
            }
        } catch (Exception e) {

        } finally {
            //计算y的位置
//                point1.y = realHeight - yUnit * (currentY1 - minY) / singleY + textXHeight * 2;
            float y = realHeight - (realHeight - yUnit) * (currentY1 - minY) / (maxY - minY);

            pointMaps.put(nextX, y);
            //LogUtil.i("mk", "currentY1:" + currentY1 + ",nextY1:" + point1.y);
            if (i == 0) {
                contentPath.moveTo(nextX, y);
            } else {
                contentPath.lineTo(nextX, y);
            }
        }
    }

    /**
     * 绘制x轴的文字
     */
    private void drawXText(Canvas canvas, float textXHeight, float nextX, String xDate, String xText) {
        xTextPaint.setColor(Color.parseColor("#666666"));
        canvas.drawText(xDate, nextX, height - getPaddingBottom() - textXHeight, xTextPaint);

        xTextPaint.setColor(Color.parseColor("#999999"));
        canvas.drawText(xText, nextX, height - getPaddingBottom(), xTextPaint);
    }

    private void init(AttributeSet attrs) {
        yTextPaint = new TextPaint();
        yTextPaint.setColor(Color.parseColor("#A0A4AA"));
        yTextPaint.setTextSize(24);
        yTextPaint.setTextAlign(Paint.Align.LEFT);

        xTextPaint = new TextPaint();
        xTextPaint.setColor(Color.parseColor("#666666"));
        xTextPaint.setTextSize(22);
        xTextPaint.setTextAlign(Paint.Align.CENTER);

        yLinePaint = new Paint();
        yLinePaint.setColor(Color.parseColor("#416180"));

        xLinePaint = new Paint();
        xLinePaint.setColor(Color.parseColor("#999999"));
        xLinePaint.setStrokeWidth(dip2px(getContext(), 1));

        contentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        contentPaint.setPathEffect(new CornerPathEffect(dip2px(getContext(), 3)));
        contentPaint.setColor(Color.parseColor("#13CC9F"));
        contentPaint.setStrokeWidth(dip2px(getContext(), 1));
        contentPaint.setStyle(Paint.Style.STROKE);

        contentPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        contentPaint2.setPathEffect(new CornerPathEffect(dip2px(getContext(), 3)));
        contentPaint2.setColor(Color.parseColor("#008CFF"));
        contentPaint2.setStrokeWidth(dip2px(getContext(), 1));
        contentPaint2.setStyle(Paint.Style.STROKE);

        contentPath = new Path();

        contentPath2 = new Path();

        radius = dip2px(getContext(), 2);//圆半径

        radiusCenter = radius - dip2px(getContext(), 1);//圆半径

        normalPointPaint = new Paint();
        normalPointPaint.setColor(Color.parseColor("#13CC9F"));
        normalPointPaint.setStrokeWidth(dip2px(getContext(), 2));
        normalPointPaint.setStyle(Paint.Style.STROKE);

        normalPointPaint2 = new Paint();
        normalPointPaint2.setColor(Color.parseColor("#008CFF"));
        normalPointPaint2.setStrokeWidth(dip2px(getContext(), 2));
        normalPointPaint2.setStyle(Paint.Style.STROKE);

        normalWhilePointPaint = new Paint();
        normalWhilePointPaint.setColor(Color.parseColor("#FFFFFF"));
        normalWhilePointPaint.setStrokeWidth(dip2px(getContext(), 1));
        normalWhilePointPaint.setStyle(Paint.Style.FILL);

        mYHearts = new ArrayList<>();
        mXDates = new ArrayList<>();
        mXTimes = new ArrayList<>();

        mDatas1 = new HashMap<>();
        mDatas2 = new HashMap<>();

        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        // 获取TouchSlop值
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }

    public void setData(List<String> yLists,
                        List<String> xListDates,
                        List<String> xListTimes,
                        Map<String, PolylineData> dataMaps1,
                        Map<String, PolylineData> dataMaps2) {

        mYHearts.clear();
        mXDates.clear();
        mXTimes.clear();
        mDatas1.clear();
        mDatas2.clear();

        mYHearts.addAll(yLists);

        mXDates.addAll(xListDates);

        mXTimes.addAll(xListTimes);

        mDatas1.putAll(dataMaps1);
        mDatas2.putAll(dataMaps2);

        invalidate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    /**
     * //https://blog.csdn.net/u012481172/article/details/51280995
     * //true表示子View不要父View拦截事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final ViewParent parent = getParent();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = event.getY();
                lastX = event.getX();

                lastCurX = event.getX();

                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                downAndMove();
                break;
            case MotionEvent.ACTION_MOVE:

                lastCurX = event.getX();

                float y = event.getY();
                float x = event.getX();

                float dy = y - lastY;
                float dx = x - lastX;

                float diffY = Math.abs(dy);
                float diffX = Math.abs(dx);
//
//                if (diffY > diffX && diffY > dip2px(getContext(), 50)) {
//                    //滑动幅度大的时候才允许
//                    if (parent != null) {
//                        parent.requestDisallowInterceptTouchEvent(false);
//                    }
//                } else {
//                    if (parent != null) {
//                        parent.requestDisallowInterceptTouchEvent(true);
//                    }
//                    downAndMove();
//                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                lastCurX = -1;
                invalidate();
                if (clickListener != null) {
                    clickListener.hideClick();
                }
                break;
        }
        return true;
    }

    private void downAndMove() {
        int size = xWeizi.size();
        if (size <= 0) {
            return;
        }
        if (lastCurX >= endX) {
            lastCurX = endX;
        }
        if (lastCurX <= startX) {
            lastCurX = startX;
        }
        float max = xWeizi.get(size - 1);
        if (lastCurX >= max) {
            lastCurX = max;
        }
        if (size == 1) {
            lastCurX = xWeizi.get(0);
            if (clickListener != null) {
                clickListener.onClick(0);
            }
            invalidate();
        } else {
            for (int i = 0; i < size; i++) {
                //每个点的x与touch的x求绝对值……
                //哪个最小就是哪个点近。。
                float tempCurX = xWeizi.get(i);
                if (i == 0 && lastCurX < xWeizi.get(i + 1) / 2 && lastCurX > 0) {
                    selectPos = i;
                    break;
                } else if (lastCurX <= tempCurX) {
                    selectPos = i;
                    break;
                }
            }
            lastCurX = xWeizi.get(selectPos);
            if (clickListener != null) {
                clickListener.onClick(selectPos);
            }
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(null);
    }

    public interface ClickListener {
        void onClick(int pos);

        void hideClick();
    }

    public static class PolylineData {
        public String value;
        public String date;
        public String pId;
    }

    /**
     * 将dp转换成px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将像素转换成dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将字符串转为float
     */
    public static float parseFloat4Str(String str) {
        float result = -1f;
        try {
            result = Float.parseFloat(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }
}
