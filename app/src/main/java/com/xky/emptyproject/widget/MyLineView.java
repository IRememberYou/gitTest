package com.xky.emptyproject.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyLineView extends View {
    /**
     * View的宽高
     */
    private int mWidth;
    private int mHeight;
    /**
     * 左边区域宽度和高度
     */
    private float mLeftW;
    private float mLeftH;
    /**
     * 左边文字的高度和宽度
     */
    float mLeftTextH;
    float mLeftTextW;
    /**
     * 底部区域的宽度和高度
     */
    private float mBottomW;
    private float mBottomH;
    /**
     * 底部文字一行的高度和宽度
     */
    private float mBottomTextH;
    private float mBottomTextW;
    /**
     * 画笔Y轴
     */
    private TextPaint mPaintY;
    /**
     * 画笔X轴
     */
    private TextPaint mPaintX;
    /**
     * 画笔点和线
     */
    private Paint mPaintP0rLine;
    /**
     * 数据
     */
    private List<String> mLeftYList = new ArrayList<>();
    private List<String> mBottomXList = new ArrayList<>();
    private List<BodyPointInfo> mPointList = new ArrayList<>();
    /**
     * 线
     */
    private Path mPath;
    /**
     * 选中位置
     */
    private int selectPos = -1;

    public MyLineView(Context context) {
        this(context, null);
    }

    public MyLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化画笔
        mPaintY = new TextPaint();
        mPaintY.setAntiAlias(true);
        mPaintY.setStyle(Paint.Style.FILL);
        mPaintY.setTextSize(20);
        mPaintY.setColor(Color.parseColor("#A0A4AA"));
        //
        mPaintX = new TextPaint();
        mPaintX.setAntiAlias(true);
        mPaintX.setStyle(Paint.Style.FILL);
        mPaintX.setColor(Color.BLACK);
//        mPaintX.setTextAlign(Paint.Align.RIGHT);
        //
        mPaintP0rLine = new Paint();
        mPaintP0rLine.setAntiAlias(true);
        mPaintP0rLine.setStyle(Paint.Style.STROKE);
        mPaintP0rLine.setStrokeWidth(3);
        mPaintP0rLine.setColor(Color.parseColor("#13CC9F"));
        mPath = new Path();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();

        //初始化基本值
        if (mLeftYList != null && mLeftYList.size() > 0
                && mBottomXList != null && mBottomXList.size() > 0) {
            //底部文字的高度
            mBottomTextH = mPaintX.descent() - mPaintX.ascent();
            //底部文字的宽度
            String xMaxTextW = mBottomXList.get(mBottomXList.size() - 1);
            mBottomTextW = mPaintX.measureText(xMaxTextW);
            //左边文字的高度
            mLeftTextH = mPaintY.descent() - mPaintY.ascent();
            //左边文字的宽度
            String yMaxTextW = mLeftYList.get(mLeftYList.size() - 1);
            mLeftTextW = mPaintY.measureText(yMaxTextW);
            //左边宽度
            mLeftW = mLeftTextW + getPaddingLeft() + px2dip(getContext(), 10);
            //底部宽度
            mBottomW = mWidth - mLeftW - mBottomTextW / 2 - getPaddingRight();
            //底部高度
            mBottomH = mBottomTextH * 2;
            //左边高度
            mLeftH = mHeight - getPaddingTop() - getPaddingBottom() - mBottomH - px2dip(getContext(), 10);

            System.out.println("mWidth: " + mWidth +
                    "  mHeight: " + mHeight +
                    "  mBottomTextH: " + mBottomTextH +
                    "  mBottomTextW: " + mBottomTextW +
                    "  mLeftTextH: " + mLeftTextH +
                    "  mLeftTextW: " + mLeftTextW +
                    "  mLeftW: " + mLeftW +
                    "  mLeftH: " + mLeftH +
                    "  mBottomW: " + mBottomW +
                    "  mBottomH: " + mBottomH +
                    "");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制Y轴和横向直线
        drawY(canvas);
        //绘制X轴文字和绘制点
        drawX(canvas);
        //绘制手触摸的线
        if (selectPos != -1) {
            BodyPointInfo pointInfo = mPointList.get(selectPos);
            canvas.drawLine(str2float(pointInfo.pointX), 0, str2float(pointInfo.pointX), mHeight, mPaintP0rLine);
        }
    }

    private void drawY(Canvas canvas) {
        int size = mLeftYList.size();
        float preH = size <= 1 ? mLeftH : mLeftH / (size - 1.f);
        for (int i = 0; i < size; i++) {
            String text = mLeftYList.get(i);
            PointF txtP = new PointF(getPaddingLeft(), mLeftH - preH * i);
            canvas.drawText(text, txtP.x, txtP.y, mPaintY);
            //绘制横向的线
            PointF lineStartP = new PointF(mLeftW, txtP.y);
            PointF lineEndP = new PointF(mWidth - getPaddingRight(), txtP.y);
            canvas.drawLine(lineStartP.x, lineStartP.y, lineEndP.x, lineEndP.y, mPaintX);
        }
    }

    private void drawX(Canvas canvas) {
        mPath.reset();
        int size = mBottomXList.size();
        float preW = size <= 1 ? mBottomW : mBottomW / (size - 1.f);
        for (int i = 0; i < size; i++) {
            String text = mBottomXList.get(i);

            PointF txtP = new PointF(mLeftW - mBottomTextW / 2 + preW * i, mHeight - mBottomH + mBottomTextH);
            canvas.drawText(text, txtP.x, txtP.y, mPaintX);
            //绘制点
            BodyPointInfo pointInfo = mPointList.get(i);
            float minValueY = str2float(mLeftYList.get(0));
            float maxValueY = str2float(mLeftYList.get(mLeftYList.size() - 1));
            float currValueY = str2float(pointInfo.pointY);
            final PointF centerP = new PointF(txtP.x + mBottomTextW / 2, mLeftH - (currValueY - minValueY) / maxValueY * mLeftH * 1.f);
            canvas.drawCircle(centerP.x, centerP.y, 10, mPaintP0rLine);
            //绘制线
            if (i == 0) {
                mPath.moveTo(centerP.x, centerP.y);
            } else {
                mPath.lineTo(centerP.x, centerP.y);
            }
            pointInfo.pointX = centerP.x + "";
        }
        canvas.drawPath(mPath, mPaintP0rLine);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewParent parent = getParent();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                calcuSelectPos(event.getX());
                break;
            case MotionEvent.ACTION_MOVE:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                calcuSelectPos(event.getX());
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                selectPos = -1;
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    private void calcuSelectPos(float x) {
        float preW = mBottomXList.size() <= 1 ? mBottomW : mBottomW / (mBottomXList.size() - 1.f);
        int position = Math.round((x - mLeftW) / preW);
        if (position >= 0 && position < mPointList.size()) {
            this.selectPos = position;
        }
//        invalidate();
        requestLayout();
    }

    public void setNewData(BodyDataIndexInfo info) {
        mLeftYList.clear();
        mBottomXList.clear();
        mPointList.clear();
        mLeftYList.addAll(info.leftY);
        mBottomXList.addAll(info.bottomX);
        mPointList.addAll(info.pointList);
        invalidate();
    }

    public float str2float(String str) {
        float result = -1f;
        try {
            result = Float.parseFloat(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
}
