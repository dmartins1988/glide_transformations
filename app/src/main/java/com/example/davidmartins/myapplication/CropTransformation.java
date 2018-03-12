package com.example.davidmartins.myapplication;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.MessageDigest;

/**
 * Crop transformation to be applied on Glide
 */

public class CropTransformation implements Transformation<Bitmap> {

    @IntDef({CROP_LEFT, CROP_RIGHT, CROP_TOP, CROP_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CropSide {
    }

    static final int CROP_LEFT = 0;
    static final int CROP_RIGHT = 1;
    static final int CROP_TOP = 2;
    static final int CROP_BOTTOM = 3;

    private static final int VERSION = 1;
    private static final String ID = "CropTransformation" + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private BitmapPool mBitmapPool;

    private @CropSide
    int mCropSide;

    public CropTransformation(final Context context, @CropSide int cropSide) {
        mCropSide = cropSide;
        mBitmapPool = Glide.get(context).getBitmapPool();
    }

    @SuppressLint("SwitchIntDef")
    @NonNull
    @Override
    public Resource<Bitmap> transform(@NonNull final Context context, @NonNull final Resource<Bitmap> resource, final int outWidth, final int outHeight) {

        final Bitmap originalBitmap = resource.get();
        Bitmap bitmapCropped = null;

        switch (mCropSide) {
            case CROP_LEFT:
                bitmapCropped = cropLeftSide(originalBitmap);
                break;
            case CROP_RIGHT:
                bitmapCropped = cropRightSide(originalBitmap);
                break;
            case CROP_TOP:
                bitmapCropped = cropTopSide(originalBitmap);
                break;
            case CROP_BOTTOM:
                bitmapCropped = cropBottomSide(originalBitmap);
                break;
        }

        return BitmapResource.obtain(bitmapCropped, mBitmapPool);
    }

    @Override
    public void updateDiskCacheKey(@NonNull final MessageDigest messageDigest) {
        messageDigest.digest(ID_BYTES);
    }

    /**
     * Crops bottom side of bitmap
     *
     * @param originalBitmap original bitmap
     * @return cropped bitmap
     */
    private Bitmap cropBottomSide(final Bitmap originalBitmap) {
        Bitmap cutBitmap = Bitmap.createBitmap(originalBitmap.getWidth(),
                originalBitmap.getHeight() / 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(cutBitmap);
        Rect desRect = new Rect(0, 0, originalBitmap.getWidth(), originalBitmap.getHeight() / 2);
        Rect srcRect = new Rect(0, originalBitmap.getHeight() / 2, originalBitmap.getWidth(),
                originalBitmap.getHeight());
        canvas.drawBitmap(originalBitmap, srcRect, desRect, null);
        return cutBitmap;
    }

    /**
     * Crops top side of bitmap
     *
     * @param originalBitmap original bitmap
     * @return cropped bitmap
     */
    private Bitmap cropTopSide(final Bitmap originalBitmap) {
        Bitmap cutBitmap = Bitmap.createBitmap(originalBitmap.getWidth(),
                originalBitmap.getHeight() / 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(cutBitmap);
        Rect desRect = new Rect(0, 0, originalBitmap.getWidth(), originalBitmap.getHeight() / 2);
        Rect srcRect = new Rect(0, 0, originalBitmap.getWidth(),
                originalBitmap.getHeight() / 2);
        canvas.drawBitmap(originalBitmap, srcRect, desRect, null);
        return cutBitmap;
    }

    /**
     * Crops right side of bitmap
     *
     * @param originalBitmap original bitmap
     * @return cropped bitmap
     */
    private Bitmap cropRightSide(final Bitmap originalBitmap) {
        Bitmap cutBitmap = Bitmap.createBitmap(originalBitmap.getWidth() / 2,
                originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(cutBitmap);
        Rect desRect = new Rect(0, 0, originalBitmap.getWidth() / 2, originalBitmap.getHeight());
        Rect srcRect = new Rect(originalBitmap.getWidth() / 2, 0, originalBitmap.getWidth(),
                originalBitmap.getHeight());
        canvas.drawBitmap(originalBitmap, srcRect, desRect, null);
        return cutBitmap;
    }

    /**
     * Crops lefts side of bitmap
     *
     * @param originalBitmap original bitmap
     * @return cropped bitmap
     */
    private Bitmap cropLeftSide(final Bitmap originalBitmap) {
        Bitmap cutBitmap = Bitmap.createBitmap(originalBitmap.getWidth() / 2,
                originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(cutBitmap);
        Rect desRect = new Rect(0, 0, originalBitmap.getWidth() / 2, originalBitmap.getHeight());
        Rect srcRect = new Rect(0, 0, originalBitmap.getWidth(),
                originalBitmap.getHeight());
        canvas.drawBitmap(originalBitmap, srcRect, desRect, null);
        return cutBitmap;
    }
}
