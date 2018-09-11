package com.caojing.kotlinone.fkcamera.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.caojing.kotlinone.R;
import com.caojing.kotlinone.fkcamera.utils.Constants;
import com.caojing.kotlinone.fkcamera.utils.RxBus;

import rx.functions.Action1;

public class LineDirectionSensorView extends View {

    private Paint mPaint;
    /**
     * 图像比例. 比例=宽/高
     */
    private float mRatio;
    Point point1 = null, point2 = null;
    int angle1 = 0, angle2 = 180;
    int normAngle = 270;

    public LineDirectionSensorView(Context context) {
        super(context);
    }

    public LineDirectionSensorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth((float) 2.0);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.RX_JAVA_TYPE_LINE + "");
        context.registerReceiver(receiver, intentFilter);
    }

    public LineDirectionSensorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置是否显示水平仪
     *
     * @param isVis
     */
    public void setViseLine(boolean isVis) {
        if (isVis) {
            mPaint.setColor(Color.WHITE);
        } else {
            mPaint.setColor(Color.TRANSPARENT);
        }
        invalidate();
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int orientation = intent.getIntExtra("orientation", 0);
            if (orientation < normAngle && orientation > 225) {
                angle1 = 0 - 2 * (normAngle - orientation);
                angle2 = 180 - 2 * (normAngle - orientation);
            } else if (orientation > normAngle && orientation < 315) {
                angle1 = (orientation - normAngle) * 2 + 0;
                angle2 = (orientation - normAngle) * 2 + 180;
            } else {
                angle1 = 0;
                angle2 = 180;
            }
            Log.d("caojingcaojing", orientation + "");
            invalidate();
        }
    };

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        RxBus.getInstance().registerMain(Constants.RX_JAVA_TYPE_LINE, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                int orientation = integer;
                if (orientation < normAngle && orientation > 225) {
                    angle1 = 0 - 2 * (normAngle - orientation);
                    angle2 = 180 - 2 * (normAngle - orientation);
                } else if (orientation > normAngle && orientation < 315) {
                    angle1 = (orientation - normAngle) * 2 + 0;
                    angle2 = (orientation - normAngle) * 2 + 180;
                } else {
                    angle1 = 0;
                    angle2 = 180;
                }
                Log.d("caojingcaojing", orientation + "");
                invalidate();
            }
        });
        canvas.drawColor(Color.TRANSPARENT);                  //设置背景颜色
        point1 = computeCoordinates(angle1);
        point2 = computeCoordinates(angle2);
        canvas.drawLine(point1.x, point1.y, point2.x, point2.y, mPaint);
        canvas.drawLine(point1.x, point1.y, point2.x, point2.y, mPaint);
//        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight() / 3, mPaint);
//        canvas.drawLine(getWidth() / 2, getHeight() * 2 / 3, getWidth() / 2, getHeight(), mPaint);
        canvas.drawLine(0, getHeight() / 2, getWidth() / 3,getHeight() / 2, mPaint);
        canvas.drawLine(getWidth() * 2 / 3, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);
    }


    /**
     * 初始化
     *
     * @param context 上下文
     * @param attrs   属性
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.LoweImageView);
        mRatio = typedArray.getFloat(R.styleable.LoweImageView_ratio, 0);
        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 宽模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        // 宽大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        // 高大小
        int heightSize;
        // 只有宽的值是精确的才对高做精确的比例校对
        if (widthMode == MeasureSpec.EXACTLY && mRatio > 0) {
            heightSize = (int) (widthSize / mRatio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,
                    MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 根据角度来计算圆上各点的坐标
     *
     * @param angle 角度
     * @return
     */
    public Point computeCoordinates(int angle) {
        float mRadius = getWidth() / 6;
        float mPointX = getWidth() / 2, mPointY = getHeight() / 2;
        float PointX = mPointX + (float) (mRadius * Math.cos(angle * Math.PI / 180));

        float PointY = mPointY + (float) (mRadius * Math.sin(angle * Math.PI / 180));
        return new Point((int) PointX, (int) PointY);
    }
}
