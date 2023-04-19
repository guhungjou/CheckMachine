package com.example0;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
public class DashedRectSurfaceView extends View {
    private Paint paint;
    private Paint textPaint;
    private Path path;

    public DashedRectSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DashedRectSurfaceView(Context context) {
        super(context);
        init();
    }

    private void init() {
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        PathEffect effects = new DashPathEffect(new float[]{10, 5}, 0);
        paint.setPathEffect(effects);
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30f * 3);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }
    float handHeight ;
    float fingerWidth;
    float fingerHeight;
    float handWidth;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
//        scaleFactor = Math.min(w / 720f, h / 1280f); // 根据屏幕尺寸缩放人像
        createPath();
    }
    private float viewWidth;
    private float viewHeight;
    private void createPath() {
        path = new Path();

        float scaleFactor = 3; // 缩放因子

        float headWidth = 500f * scaleFactor * 1 / 3; // 头部宽度
        float headHeight = 300f * scaleFactor * 2 / 3; // 头部高度
        float neckWidth = 30f * scaleFactor; // 脖子宽度
        float neckHeight = 20f * scaleFactor; // 脖子高度
        float upperBodyWidth = 150f * scaleFactor * 4 / 3; // 上半身宽度
        float upperBodyHeight = 80f * scaleFactor / 3; // 上半身高度
        handWidth = 40f * scaleFactor; // 手掌宽度
        handHeight = 60f * scaleFactor; // 手掌高度
        fingerWidth = 8f * scaleFactor; // 手指宽度
        fingerHeight = 40f * scaleFactor; // 手指高度
        float centerX = getWidth() / 2f; // 视图中心 X 坐标
        float startY = getHeight() - (headHeight + neckHeight + upperBodyHeight); // 开始绘制的 Y 坐标

        // 头部椭圆形
        path.addOval(new RectF(centerX - headWidth / 2, startY, centerX + headWidth / 2, startY + headHeight), Path.Direction.CW);

        // 脖子
        path.moveTo(centerX - neckWidth / 2, startY + headHeight);
        path.lineTo(centerX - neckWidth / 2, startY + headHeight + neckHeight);
        path.moveTo(centerX + neckWidth / 2, startY + headHeight);
        path.lineTo(centerX + neckWidth / 2, startY + headHeight + neckHeight);

        // 上半身（梯形）
        path.moveTo(centerX - upperBodyWidth * 0.7f / 2, startY + headHeight + neckHeight);
        path.lineTo(centerX - upperBodyWidth / 2, startY + headHeight + neckHeight + upperBodyHeight);
        path.lineTo(centerX + upperBodyWidth / 2, startY + headHeight + neckHeight + upperBodyHeight);
        path.lineTo(centerX + upperBodyWidth * 0.7f / 2, startY + headHeight + neckHeight);
        path.close(); // 连接最后一条边

        // 画手掌和手指的函数
        drawHand(5, viewHeight/2);
        drawHand(viewWidth - handWidth*3-5, viewHeight/2);
    }


    private void drawHand(float startX, float startY) {
        // 手（椭圆形）
        float handWidth = 120f * 3;
        float handHeight = 200f * 3;
        path.addOval(new RectF(startX, startY - handHeight / 2, startX + handWidth, startY + handHeight / 2), Path.Direction.CW);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (path != null) {
            canvas.drawPath(path, paint);

            // 绘制左手文字
            float leftHandStartX = 5;
            float leftHandStartY = viewHeight / 2;
            canvas.drawText("左手", leftHandStartX + handWidth / 2, leftHandStartY - handHeight / 2 + textPaint.getTextSize() * 1.2f, textPaint);

            // 绘制右手文字
            float rightHandStartX = viewWidth - handWidth * 3 - 5;
            float rightHandStartY = viewHeight / 2;
            canvas.drawText("右手", rightHandStartX + handWidth / 2, rightHandStartY - handHeight / 2 + textPaint.getTextSize() * 1.2f, textPaint);
        }
        drawDashedRect(canvas);
    }

    private void drawDashedRect(Canvas canvas) {
        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // 清除背景颜色
            canvas.drawPath(path, paint);
        }
    }

}
