package com.cat.view.customize.animation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.core.graphics.BlendModeCompat;
import androidx.core.graphics.PaintCompat;

import java.util.ArrayList;
import java.util.List;

public class FireExploreView extends TextureView implements TextureView.SurfaceTextureListener, Runnable {

    private Surface surface;
    private TextPaint mPaint;
    private BitmapCanvas mBitmapCanvas;
    private SurfaceTexture surfaceTexture;

    private volatile boolean isRunning = false;

    private boolean updateOnSizeChanged = false;

    private final Object lockSurface = new Object();

    {
        initPaint();
    }

    public FireExploreView(Context context) {
        this(context, null);
    }

    public FireExploreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setSurfaceTextureListener(this);
    }

    private void initPaint() {
        // 否则提供给外部纹理绘制
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL);
        PaintCompat.setBlendMode(mPaint, BlendModeCompat.PLUS);
    }

    static final float speed = 60f;
    static final float gravity = 0.21f;
    static final float fraction = 0.88f;
    static final int maxParticleCount = 300;

    Thread drawThread = null;

    float[] hsl = new float[3];

    volatile boolean isCommand = false;

    Matrix matrix = new Matrix();
    List<Particle> particles = new ArrayList<>(maxParticleCount);

    public void startExplore() {
        isCommand = true;
    }

    // 初始化粒子
    void explode(float x, float y) {
        double value = (Math.PI * 2) / maxParticleCount;
        float angleIncrement = (float) value;
        for (int i = 0; i < maxParticleCount; i++) {
            hsl[0] = (float) (Math.random() * 360);
            hsl[1] = 0.5f;
            hsl[2] = 0.5f;
            int hslToColor = HSLToColor(hsl);
            Particle p = null;
            if (particles.size() > i) {
                p = particles.get(i);
            }
            if (p == null) {
                p = new Particle();
                particles.add(p);
            }
            float speedX = (float) (Math.cos(angleIncrement * i) * Math.random() * speed);
            float speedY = (float) (Math.sin(angleIncrement * i) * Math.random() * speed);
            p.init(x, y, 4f, hslToColor, speedX, speedY);
        }
    }

    protected void drawParticles(Canvas canvas) {
        canvas.drawColor(0x10000000);
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            if (particle.opacity > 0) {
                particle.draw(canvas, mPaint);
                particle.update();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    this.wait(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            boolean bool = ! isRunning || Thread.currentThread().isInterrupted();
            if (bool) {
                synchronized (lockSurface) {
                    boolean isValid = surface != null && surface.isValid();
                    if (isValid) {
                        surface.release();
                    }
                    surface = null;
                }
                break;
            }
            Canvas canvas = null;
            synchronized (lockSurface) {
                if (mBitmapCanvas == null || updateOnSizeChanged) {
                    updateOnSizeChanged = false;
                    int width = getWidth();
                    int height = getHeight();
                    mBitmapCanvas = createBitmapCanvas(width, height);
                }
                if (isCommand) {
                    mBitmapCanvas.bitmap.eraseColor(0x00000000);
                    explode(mBitmapCanvas.getWidth() / 2f, mBitmapCanvas.getHeight() / 2f);
                    isCommand = false;
                }
                // 这里其实目前没有加锁的必要, 考虑到如果有其他 SurfaceTexture 相关操作会加锁, 这里先加锁吧
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    canvas = surface.lockHardwareCanvas();
                } else {
                    canvas = surface.lockCanvas(null);
                }
                Bitmap bitmap = mBitmapCanvas.getBitmap();
                drawParticles(mBitmapCanvas);
                int width = getWidth() - bitmap.getWidth();
                int height = getHeight() - bitmap.getHeight();
                matrix.reset();
                matrix.setTranslate(width / 2f, height / 2f);
                canvas.drawBitmap(mBitmapCanvas.getBitmap(), matrix, null);
                surface.unlockCanvasAndPost(canvas);
            }
        }
    }

    private BitmapCanvas createBitmapCanvas(int width, int height) {
        if (mBitmapCanvas != null) {
            mBitmapCanvas.recycle();
        }
        int size = Math.max(Math.min(width, height), 1);
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        return new BitmapCanvas(bitmap);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        this.drawThread = new Thread(this);
        this.surfaceTexture = surfaceTexture;
        this.surface = new Surface(this.surfaceTexture);
        this.isRunning = true;
        this.drawThread.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        updateOnSizeChanged = true;
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        isRunning = false;
        if (drawThread != null) {
            try {
                drawThread.interrupt();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        drawThread = null;
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @ColorInt
    public static int HSLToColor(float[] hsl) {
        final float h = hsl[0];
        final float s = hsl[1];
        final float l = hsl[2];
        final float c = (1f - Math.abs(2 * l - 1f)) * s;
        final float m = l - 0.5f * c;
        float av = (h / 60f % 2f) - 1f;
        float abs = Math.abs(av);
        final float x = c * (1f - abs);
        final int hueSegment = (int) h / 60;
        int r = 0, g = 0, b = 0;
        switch (hueSegment) {
            case 0:
                r = Math.round(255 * (c + m) * 1);
                g = Math.round(255 * (x + m) * 1);
                b = Math.round(255 * m);
                break;
            case 1:
                r = Math.round(255 * (x + m) * 1);
                g = Math.round(255 * (c + m) * 1);
                b = Math.round(255 * m);
                break;
            case 2:
                r = Math.round(255 * m);
                g = Math.round(255 * (c + m) * 1);
                b = Math.round(255 * (x + m) * 1);
                break;
            case 3:
                r = Math.round(255 * m);
                g = Math.round(255 * (x + m) * 1);
                b = Math.round(255 * (c + m) * 1);
                break;
            case 4:
                r = Math.round(255 * (x + m) * 1);
                g = Math.round(255 * m);
                b = Math.round(255 * (c + m) * 1);
                break;
            case 5:
            case 6:
                r = Math.round(255 * (c + m) * 1);
                g = Math.round(255 * m);
                b = Math.round(255 * (x + m) * 1);
                break;
        }
        r = constrain(r, 0, 255);
        g = constrain(g, 0, 255);
        b = constrain(b, 0, 255);
        return Color.rgb(r, g, b);
    }

    private static int constrain(int amount, int low, int high) {
        return amount < low ? low : Math.min(amount, high);
    }

    public static int argb(@IntRange(from = 0, to = 255) int alpha, @IntRange(from = 0, to = 255) int red, @IntRange(from = 0, to = 255) int green, @IntRange(from = 0, to = 255) int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public void release() {
        synchronized (lockSurface) {
            isRunning = false;
            updateOnSizeChanged = false;
            boolean isValid = surface != null && surface.isValid();
            if (isValid) {
                surface.release();
            }
            surface = null;
        }
    }

    static class BitmapCanvas extends Canvas {

        Bitmap bitmap;

        public BitmapCanvas(Bitmap bitmap) {
            super(bitmap);
            this.bitmap = bitmap;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void recycle() {
            boolean isRecycled = bitmap == null || bitmap.isRecycled();
            if (isRecycled) {
                return;
            }
            bitmap.recycle();
        }

    }

    static class Particle {

        private float dy;
        private float dx;
        private int color;
        private float radius;
        private float opacity;

        private float y;
        private float x;

        public void init(float x, float y, float r, int color, float speedX, float speedY) {
            this.x = x;
            this.y = y;
            this.radius = r;
            this.color = color;
            this.dx = speedX;
            this.dy = speedY;
            this.opacity = 1f;
        }

        void draw(Canvas canvas, Paint paint) {
            int opacity = (int) (this.opacity * 255);
            int r = Color.red(this.color);
            int g = Color.green(this.color);
            int b = Color.blue(this.color);
            int argb = argb(opacity, r, g, b);
            int save = canvas.save();
            paint.setColor(argb);
            canvas.drawCircle(this.x, this.y, this.radius, paint);
            canvas.restoreToCount(save);
        }

        void update() {
            this.dy += gravity;
            this.dx *= fraction;
            this.dy *= fraction;
            this.opacity -= 0.02;
            this.x += this.dx;
            this.y += this.dy;
        }

    }

}