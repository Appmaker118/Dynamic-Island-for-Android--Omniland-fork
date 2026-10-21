package com.theglitchh.NothingLand.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Custom Drawable to render the slider track.
 * [UPDATED] Infused with isolated spring-damper physics to replicate pure iOS fluid motion.
 */
public class DotSliderDrawable extends Drawable {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF trackRect = new RectF();
    private final RectF progressRect = new RectF();
    
    private int maxProgress = 100;
    private float currentProgress = 0f;  // The actual progress calculated by system
    private float visualProgress = 0f;   // The smooth "fluid" progress actively drawn on screen

    // Physics parameters for the iOS fluid feel
    private static final float SPRING_STIFFNESS = 0.24f; // Controls how fast it snaps to your finger
    private static final float DAMPING_RATIO = 0.70f;    // Lower values = more bouncy, Higher = smoother
    private float velocity = 0f;

    private final Handler animationHandler = new Handler(Looper.getMainLooper());
    private boolean isAnimating = false;

    private final Runnable animationRunnable = new Runnable() {
        @Override
        public void run() {
            // Hooke's Law Spring Equation: Force = Stiffness * Displacement
            float force = SPRING_STIFFNESS * (currentProgress - visualProgress);
            
            // Apply damping (friction) to control the settle stabilization
            velocity = (velocity + force) * DAMPING_RATIO;
            visualProgress += velocity;

            // If the spring settles perfectly close to the target, stop the loop to save CPU
            if (Math.abs(currentProgress - visualProgress) < 0.01f && Math.abs(velocity) < 0.01f) {
                visualProgress = currentProgress;
                isAnimating = false;
                invalidateSelf();
            } else {
                invalidateSelf();
                // 60 Frames Per Second rendering loop updates
                animationHandler.postDelayed(this, 16); 
            }
        }
    };

    public DotSliderDrawable() {
        paint.setStyle(Paint.Style.FILL);
    }

    public void setMax(int max) {
        this.maxProgress = Math.max(1, max);
        invalidateSelf();
    }

    public void setProgress(int progress) {
        this.currentProgress = Math.max(0, Math.min(maxProgress, progress));
        
        // Safely trigger the physics engine loop if it isn't running already
        if (!isAnimating) {
            isAnimating = true;
            animationHandler.post(animationRunnable);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        if (bounds.isEmpty()) return;

        float width = bounds.width();
        float height = bounds.height();
        float cornerRadius = height / 2f; 

        // 1. Draw Background Track (iOS System Dark Grey)
        trackRect.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
        paint.setColor(Color.parseColor("#2C2C2E"));
        canvas.drawRoundRect(trackRect, cornerRadius, cornerRadius, paint);

        // 2. Draw Active Progress Layer (iOS System Pure White) using our fluid visual value
        if (visualProgress > 0) {
            float progressPercentage = visualProgress / maxProgress;
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
