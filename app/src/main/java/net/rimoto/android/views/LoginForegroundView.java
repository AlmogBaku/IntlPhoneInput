package net.rimoto.android.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import net.rimoto.android.R;

public class LoginForegroundView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Thread mThread;
    private Bitmap mForeground;
    private SurfaceHolder mHolder;
    private Rect mDest;
    private Paint mPaint;

    public LoginForegroundView(Context context) {
        super(context);
        init();
    }
    public LoginForegroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.setBackgroundColor(Color.TRANSPARENT);
        getHolder().addCallback(this);
        setZOrderOnTop(true);

        mPaint = new Paint();
        mPaint.setFilterBitmap(true);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        mForeground = BitmapFactory.decodeResource(getResources(), R.drawable.login_foreground, options);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(mHolder == null) {
            mHolder = holder;
            mHolder.setFormat(PixelFormat.TRANSPARENT);
        }
        if(mDest == null) {
            mDest = new Rect(0, 0, this.getWidth(), this.getHeight());
        }

        mThread = new Thread(this);
        mThread.run();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mThread != null && mThread.isAlive()) {
            mThread.interrupt();
            mThread = null;
        }
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        if(mHolder.getSurface().isValid()) return;

        Canvas canvas = mHolder.lockCanvas();
        onDraw(canvas);
        mHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mForeground, null, mDest, mPaint);
    }
}
