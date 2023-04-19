package com.example0.checkmachine;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomPatternView extends View {
    private class Bubble {
        float x;
        float y;
        float radius;
        float speed;
        int alpha;

        Bubble(float x, float y, float radius, float speed, int alpha) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.speed = speed;
            this.alpha = alpha;
        }
    }
    private int girlDrawingProgress;
    private int girlDrawingMaxProgress = 5;

    private Handler handler;
    private Paint paint;
    private Random random;
    private List<Bubble> bubbles;
    private void createBubbles() {
        bubbles.clear();
        int bubbleCount = 60;
        for (int i = 0; i < bubbleCount; i++) {
            float x = random.nextInt(getWidth());
            float y = getHeight() - random.nextFloat() * getHeight() / 2;
            float radius = random.nextFloat() * 20 + 10;
            float speed = random.nextFloat() * 3 + 1;
            int alpha = random.nextInt(300) + 50;
            bubbles.add(new Bubble(x, y, radius, speed, alpha));
        }
    }


    public CustomPatternView(Context context) {
        super(context);
        init();
    }

    public CustomPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        random = new Random();
        girlDrawingProgress = 0;
        handler = new Handler();
        bubbles = new ArrayList<>();
//        createBubbles();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createBubbles();
    }
    private void updateBubbles() {
        for (Bubble bubble : bubbles) {
            bubble.y -= bubble.speed;
            if (bubble.y + bubble.radius < 0) {
                bubble.y = getHeight() + bubble.radius;
                bubble.x = random.nextInt(getWidth());
            }
        }
    }


    private void drawGirl(Canvas canvas) {
        float x = getWidth() - 100;
        float y = getHeight() - 200;

        if (girlDrawingProgress >= 1) {
            // Draw head
            paint.setColor(Color.parseColor("#FFC107"));
            canvas.drawCircle(x, y, 30, paint);
        }

        if (girlDrawingProgress >= 2) {
            // Draw body
            paint.setColor(Color.parseColor("#4CAF50"));
            canvas.drawRect(new RectF(x - 15, y + 30, x + 15, y + 100), paint);
        }

        if (girlDrawingProgress >= 3) {
            // Draw arms
            paint.setColor(Color.parseColor("#4CAF50"));
            canvas.drawRect(new RectF(x - 35, y + 30, x - 15, y + 60), paint);
            canvas.drawRect(new RectF(x + 15, y + 30, x + 35, y + 60), paint);
        }

        if (girlDrawingProgress >= 4) {
            // Draw legs
            paint.setColor(Color.parseColor("#795548"));
            canvas.drawRect(new RectF(x - 10, y + 100, x, y + 150), paint);
            canvas.drawRect(new RectF(x, y + 100, x + 10, y + 150), paint);
        }
    }

    private void updateGirlDrawingProgress() {
        if (girlDrawingProgress < girlDrawingMaxProgress) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    girlDrawingProgress++;
                    invalidate();
                }
            }, 200); // 200 ms delay
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.parseColor("#80BEE1"));
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        for (Bubble bubble : bubbles) {
            float scaleFactor = 1.0f - (bubble.y / getHeight());
            float scaledRadius = bubble.radius * scaleFactor;
            int color = Color.argb(bubble.alpha, 187, 222, 251);
            paint.setColor(color);
            paint.setShadowLayer(10, 0, 0, color);
            setLayerType(LAYER_TYPE_SOFTWARE, paint);
            canvas.drawCircle(bubble.x, bubble.y, scaledRadius, paint);
        }

        drawGirl(canvas);

        updateBubbles();
        updateGirlDrawingProgress();
        invalidate();
    }

}
