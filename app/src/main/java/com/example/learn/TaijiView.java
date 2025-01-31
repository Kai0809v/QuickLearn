package com.example.learn;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class TaijiView extends View {
    private Paint paint;

    public TaijiView(Context context) {
        super(context);
        init();
    }

    public TaijiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);  // 启用抗锯齿
        paint.setStyle(Paint.Style.FILL);  // 填充
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;

        // 画圆形外框
        paint.setColor(Color.BLACK);
        canvas.drawCircle(width / 2, height / 2, radius, paint);

        // 画黑色部分（上半部分）
        paint.setColor(Color.BLACK);
        RectF topCircleRect = new RectF(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2);
        canvas.drawArc(topCircleRect, 180, 180, true, paint);

        // 画白色部分（下半部分）
        paint.setColor(Color.WHITE);
        RectF bottomCircleRect = new RectF(width / 2 - radius, height / 2, width / 2 + radius, height / 2 + radius);
        canvas.drawArc(bottomCircleRect, 0, 180, true, paint);

        // 画小黑圆
        paint.setColor(Color.WHITE);
        canvas.drawCircle(width / 2, height / 2 - radius / 2, radius / 5, paint);

        // 画小白圆
        paint.setColor(Color.BLACK);
        canvas.drawCircle(width / 2, height / 2 + radius / 2, radius / 5, paint);
    }
}
