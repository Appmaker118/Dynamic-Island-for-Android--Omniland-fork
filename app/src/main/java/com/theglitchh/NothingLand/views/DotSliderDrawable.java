package com.theglitchh.NothingLand.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Custom Drawable to render the slider track.
 * [REWRITTEN] Converted from Nothing OS dot-matrix style to a 1:1 pure iOS dynamic fluid capsule track.
 */
public class DotSliderDrawable extends Drawable {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF trackRect = new RectF();
    private final RectF progressRect = new RectF();
    
    private int maxProgress = 100;
    private int currentProgress = 0;

    public DotSliderDrawable() {
        paint.setStyle(Paint.Style.FILL);
    }

    public void setMax(int max) {
        this.maxProgress = Math.max(1, max);
        invalidateSelf();
    }

    public void setProgress(int progress) {
        this.currentProgress = Math.max(0, Math.min(maxProgress, progress));
        invalidateSelf();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        if (bounds.isEmpty()) return;

        // Calculate layout geometry
        float width = bounds.width();
        float height = bounds.height();
        
        // iOS design uses a distinct corner radius relative to the component height to build perfect squircles
        float cornerRadius = height / 2f; 

        // 1. Draw the Background Track (iOS System Dark Grey)
        trackRect.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
        paint.setColor(Color.parseColor("#2C2C2E"));
        canvas.drawRoundRect(trackRect, cornerRadius, cornerRadius, paint);

        // 2. Draw the Active Progress Layer (iOS System Pure White)
        if (currentProgress > 0) {
            float progressPercentage = (float) currentProgress / maxProgress;
            float progressWidth = width * progressPercentage;

            progressRect.set(bounds.left, bounds.top, bounds.left + progressWidth, bounds.bottom);
            paint.setColor(Color.WHITE);
            canvas.drawRoundRect(progressRect, cornerRadius, cornerRadius, paint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
