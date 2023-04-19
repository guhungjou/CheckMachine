//package com.example0.checkmachine;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//
//import com.example0.checkmachine.YoloV5Ncnn;
//
//public class BoundingBoxView extends View {
//
//    private YoloV5Ncnn.Obj[] mObjects;
//    private Paint mPaint;
//    private Paint mTextPaint;
//    private int mPreviewWidth;
//    private int mPreviewHeight;
//
//    public BoundingBoxView(Context context) {
//        super(context);
//        init();
//    }
//
//    public BoundingBoxView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public BoundingBoxView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }
//
//    private void init() {
//        mPaint = new Paint();
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(4);
//
//        mTextPaint = new Paint();
//        mTextPaint.setColor(Color.BLACK);
//        mTextPaint.setTextSize(26);
//        mTextPaint.setTextAlign(Paint.Align.LEFT);
//    }
//
//    public void setObjects(YoloV5Ncnn.Obj[] objects) {
//        mObjects = objects;
//        if(objects==null||objects.length==0)
//            return;
//        this.draw(new Canvas());
//    }
//
//    public void setPreviewSize(int width, int height) {
//        mPreviewWidth = width;
//        mPreviewHeight = height;
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        Log.e("BoundingBoxView", "onDraw");
//        if (mObjects == null || mObjects.length == 0) {
//            return;
//        }
//
//        float scaleX = (float) getWidth() / mPreviewWidth;
//        float scaleY = (float) getHeight() / mPreviewHeight;
//
//        for (YoloV5Ncnn.Obj obj : mObjects) {
//            float left = obj.x * scaleX;
//            float top = obj.y * scaleY;
//            float right = (obj.x + obj.w) * scaleX;
//            float bottom = (obj.y + obj.h) * scaleY;
//
////            mPaint.setColor(obj.color);
//            canvas.drawRect(left, top, right, bottom, mPaint);
//
//            String label = obj.label + " " + String.format("%.1f", obj.prob * 100) + "%";
//            canvas.drawText(label, left, top - mTextPaint.ascent(), mTextPaint);
//        }
//    }
//}

package com.example0.checkmachine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example0.checkmachine.YoloV5Ncnn;

public class BoundingBoxView extends SurfaceView implements SurfaceHolder.Callback {

    private YoloV5Ncnn.Obj[] mObjects;
    private Paint mPaint;
    private Paint mTextPaint;
    private int mPreviewWidth;
    private int mPreviewHeight;

    public BoundingBoxView(Context context) {
        super(context);
        init();
    }

    public BoundingBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoundingBoxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(4);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(26);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        getHolder().addCallback(this);
    }

    public void setObjects(YoloV5Ncnn.Obj[] objects) {
        mObjects = objects;
        drawBoundingBoxes();
    }

    public void setPreviewSize(int width, int height) {
        mPreviewWidth = width;
        mPreviewHeight = height;
    }

    public void drawBoundingBoxes() {
        Log.e("BoundingBoxView", "正在绘制");
        try {
            Canvas canvas = getHolder().lockCanvas();
//            Log.e("BoundingBoxView", canvas.toString());
            if (canvas == null) {
                return;
            }

            // Clear the canvas
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            if (mObjects == null || mObjects.length == 0) {
                getHolder().unlockCanvasAndPost(canvas);
                return;
            }

            float scaleX = (float) getWidth() / mPreviewWidth;
            float scaleY = (float) getHeight() / mPreviewHeight;
            final int[] colors = new int[]{
                    Color.rgb(54, 67, 244),
                    Color.rgb(99, 30, 233),
                    Color.rgb(176, 39, 156),
                    Color.rgb(183, 58, 103),
                    Color.rgb(181, 81, 63),
                    Color.rgb(243, 150, 33),
                    Color.rgb(244, 169, 3),
                    Color.rgb(212, 188, 0),
                    Color.rgb(136, 150, 0),
                    Color.rgb(80, 175, 76),
                    Color.rgb(74, 195, 139),
                    Color.rgb(57, 220, 205),
                    Color.rgb(59, 235, 255),
                    Color.rgb(7, 193, 255),
                    Color.rgb(0, 152, 255),
                    Color.rgb(34, 87, 255),
                    Color.rgb(72, 85, 121),
                    Color.rgb(158, 158, 158),
                    Color.rgb(139, 125, 96)
            };
            for (YoloV5Ncnn.Obj obj : mObjects) {
                float left = obj.x * scaleX;
                float top = obj.y * scaleY;
                float right = (obj.x + obj.w) * scaleX;
                float bottom = (obj.y + obj.h) * scaleY;
                if (obj.label.equals("hand"))
                    mPaint.setColor(colors[0]);
                else if (obj.label.equals("eye"))
                    mPaint.setColor(colors[5]);
                else
                    mPaint.setColor(colors[2]);
                canvas.drawRect(left, top, right, bottom, mPaint);

                String label = obj.label;
//                Log.e("BoundingBoxView", label);
                canvas.drawText(label, left, top - mTextPaint.ascent(), mTextPaint);
            }

            getHolder().unlockCanvasAndPost(canvas);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void clearBoundingBoxes() {
        setObjects(null);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Start your drawing thread here, if needed
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Handle surface changes, if needed
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Stop your drawing thread here, if needed
    }
}
