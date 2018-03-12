package com.example.glidetransformations;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.MessageDigest;

/**
 * Custom glide transformation to create round corners on an image
 */

public class RoundCornerTransformation implements Transformation<Bitmap> {

    @IntDef({ALL, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, TOP, BOTTOM, LEFT, RIGHT, OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT, DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CornerSides {
    }

    static final int ALL = 0;
    static final int TOP_LEFT = 1;
    static final int TOP_RIGHT = 2;
    static final int BOTTOM_LEFT = 3;
    static final int BOTTOM_RIGHT = 4;
    static final int TOP = 5;
    static final int BOTTOM = 6;
    static final int LEFT = 7;
    static final int RIGHT = 8;
    static final int OTHER_TOP_LEFT = 9;
    static final int OTHER_TOP_RIGHT = 10;
    static final int OTHER_BOTTOM_LEFT = 11;
    static final int OTHER_BOTTOM_RIGHT = 12;
    static final int DIAGONAL_FROM_TOP_LEFT = 13;
    static final int DIAGONAL_FROM_TOP_RIGHT = 14;


    private static final int VERSION = 1;
    private static final String ID = "RoundCornerTransformation" + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);


    private BitmapPool mBitmapPool;
    private int mRadius;
    private int mDiameter;
    private int mMargin;
    private @CornerSides
    int mCornerSides;

    public RoundCornerTransformation(final int radius, final int margin, final BitmapPool bitmapPool) {
        this(radius, margin, ALL, bitmapPool);
    }

    public RoundCornerTransformation(final Context context, final int radius, final int margin, final @CornerSides int cornerSides) {
        this(radius, margin, cornerSides, Glide.get(context).getBitmapPool());
    }

    public RoundCornerTransformation(final int radius, final int margin, final @CornerSides int cornerSides, final BitmapPool bitmapPool) {
        mRadius = radius;
        mDiameter = mRadius / 2;
        mMargin = margin;
        mCornerSides = cornerSides;
        mBitmapPool = bitmapPool;
    }

    @NonNull
    @Override
    public Resource<Bitmap> transform(@NonNull final Context context, @NonNull final Resource<Bitmap> resource, final int outWidth, final int outHeight) {

        final Bitmap source = resource.get();

        final int width = source.getWidth();
        final int height = source.getHeight();

        Bitmap bitmap = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888);

        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        drawRoundRectangle(canvas, paint, width, height);

        return BitmapResource.obtain(bitmap, mBitmapPool);
    }

    @Override
    public void updateDiskCacheKey(@NonNull final MessageDigest messageDigest) {
        messageDigest.digest(ID_BYTES);
    }

    private void drawRoundRectangle(final Canvas canvas, final Paint paint, final int width, final int height) {
        float right = width - mMargin;
        float bottom = height - mMargin;

        switch (mCornerSides) {
            case ALL:
                canvas.drawRoundRect(new RectF(mMargin, mMargin, right, bottom), mRadius, mRadius, paint);
                break;
            case TOP_LEFT:
                drawTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case TOP_RIGHT:
                drawTopRightRoundRect(canvas, paint, right, bottom);
            case BOTTOM_LEFT:
                drawBottomLeftRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM_RIGHT:
                drawBottomRightRoundRect(canvas, paint, right, bottom);
                break;
            case TOP:
                drawTopRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM:
                drawBottomRoundRect(canvas, paint, right, bottom);
                break;
            case LEFT:
                drawLeftRoundRect(canvas, paint, right, bottom);
                break;
            case RIGHT:
                drawRightRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_TOP_LEFT:
                drawOtherTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_TOP_RIGHT:
                drawOtherTopRightRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_BOTTOM_LEFT:
                drawOtherBottomLeftRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_BOTTOM_RIGHT:
                drawOtherBottomRightRoundRect(canvas, paint, right, bottom);
                break;
            case DIAGONAL_FROM_TOP_LEFT:
                drawDiagonalFromTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case DIAGONAL_FROM_TOP_RIGHT:
                drawDiagonalFromTopRightRoundRect(canvas, paint, right, bottom);
                break;
            default:
                canvas.drawRoundRect(new RectF(mMargin, mMargin, right, bottom), mRadius,
                        mRadius, paint);
                break;
        }

    }

    private void drawTopLeftRoundRect(final Canvas canvas, final Paint paint, final float right,
                                      final float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, mMargin +
                mDiameter), mRadius, mRadius, paint);
        canvas.drawRect(new RectF(mMargin, mMargin + mRadius, mMargin + mRadius, bottom), paint);
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom), paint);
    }

    private void drawTopRightRoundRect(final Canvas canvas, final Paint paint, final float right,
                                       final float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, mMargin +
                mDiameter), mRadius, mRadius, paint);
        canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom), paint);
        canvas.drawRect(new RectF(right + mRadius, mMargin + mRadius, right, bottom), paint);
    }

    private void drawBottomLeftRoundRect(final Canvas canvas, final Paint paint, final float
            right, final float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, mMargin + mDiameter, bottom),
                mRadius, mRadius, paint);
        canvas.drawRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom - mRadius), paint);
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom - mRadius), paint);
    }

    private void drawBottomRightRoundRect(final Canvas canvas, final Paint paint, final float right, final float bottom) {
        canvas.drawRoundRect(new RectF(right - mDiameter, bottom - mDiameter, right,
                bottom), mRadius, mRadius, paint);
        canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom), paint);
        canvas.drawRect(new RectF(right - mRadius, mMargin, right, bottom - mRadius), paint);
    }

    private void drawTopRoundRect(final Canvas canvas, final Paint paint, final float right, final float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius,
                mRadius, paint);
        canvas.drawRect(new RectF(mMargin, mMargin + mRadius, right, bottom), paint);
    }

    private void drawBottomRoundRect(final Canvas canvas, final Paint paint, final float right, final float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, right, bottom), mRadius,
                mRadius, paint);
        canvas.drawRect(new RectF(mMargin, mMargin, right, bottom - mRadius), paint);
    }

    private void drawLeftRoundRect(final Canvas canvas, final Paint paint, final float right, final float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius,
                mRadius, paint);
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom), paint);
    }

    private void drawRightRoundRect(final Canvas canvas, final Paint paint, final float right, final float bottom) {
        canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, bottom), mRadius, mRadius,
                paint);
        canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom), paint);
    }

    private void drawOtherTopLeftRoundRect(final Canvas canvas, final Paint paint, final float right, final float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, right, bottom), mRadius, mRadius,
                paint);
        canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, bottom), mRadius, mRadius,
                paint);
        canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom - mRadius), paint);
    }

    private void drawOtherTopRightRoundRect(final Canvas canvas, final Paint paint, final float right, final float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius, mRadius,
                paint);
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, right, bottom), mRadius, mRadius,
                paint);
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin, right, bottom - mRadius), paint);
    }

    private void drawOtherBottomLeftRoundRect(final Canvas canvas, final Paint paint, final float right, final float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius, mRadius,
                paint);
        canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, bottom), mRadius, mRadius, paint);
        canvas.drawRect(new RectF(mMargin, mMargin + mRadius, right - mRadius, bottom), paint);
    }

    private void drawOtherBottomRightRoundRect(final Canvas canvas, final Paint paint, final float right, final float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, mMargin, right, mMargin + mDiameter), mRadius, mRadius,
                paint);
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, bottom), mRadius, mRadius,
                paint);
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin + mRadius, right, bottom), paint);
    }

    private void drawDiagonalFromTopLeftRoundRect(final Canvas canvas, final Paint paint, final float right, final float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + mDiameter, mMargin + mDiameter), mRadius,
                mRadius, paint);
        canvas.drawRoundRect(new RectF(right - mDiameter, bottom - mDiameter, right, bottom), mRadius,
                mRadius, paint);
        canvas.drawRect(new RectF(mMargin, mMargin + mRadius, right - mDiameter, bottom), paint);
        canvas.drawRect(new RectF(mMargin + mDiameter, mMargin, right, bottom - mRadius), paint);
    }

    private void drawDiagonalFromTopRightRoundRect(final Canvas canvas, final Paint paint, final float right, final float bottom) {
        canvas.drawRoundRect(new RectF(right - mDiameter, mMargin, right, mMargin + mDiameter), mRadius,
                mRadius, paint);
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, mMargin + mDiameter, bottom), mRadius,
                mRadius, paint);
        canvas.drawRect(new RectF(mMargin, mMargin, right - mRadius, bottom - mRadius), paint);
        canvas.drawRect(new RectF(mMargin + mRadius, mMargin + mRadius, right, bottom), paint);
    }
}
