package com.asiantech.intern.painter.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.ScriptIntrinsicConvolve3x3;

import java.nio.IntBuffer;

/**
 * Copyright © 2016 AsianTech inc.
 * Created by HungTQB on 08/09/2016.
 */

public class ImageFilterUtils {
    private static final int DEFAULT_HIGHLIGHT_WIDTH = 96;
    private static final int DEFAULT_MASK_FILTER_RADIUS = 15;

    /**
     * @param sourceBitmap sourceBitmap
     * @return Bitmap out
     */
    public static Bitmap doHighLightImage(Bitmap sourceBitmap) {
        Bitmap outBitmap = Bitmap.createBitmap(sourceBitmap.getWidth() + DEFAULT_HIGHLIGHT_WIDTH, sourceBitmap.getHeight() + DEFAULT_HIGHLIGHT_WIDTH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        Paint paintBlur = new Paint();
        paintBlur.setMaskFilter(new BlurMaskFilter(DEFAULT_MASK_FILTER_RADIUS, BlurMaskFilter.Blur.NORMAL));
        int[] offsetXY = new int[2];
        Bitmap bitmapAlpha = sourceBitmap.extractAlpha(paintBlur, offsetXY);
        Paint paintAlphaColor = new Paint();
        paintAlphaColor.setColor(0xFFFFFF);
        canvas.drawBitmap(bitmapAlpha, offsetXY[0], offsetXY[1], paintAlphaColor);
        bitmapAlpha.recycle();
        canvas.drawBitmap(sourceBitmap, 0, 0, null);
        return outBitmap;
    }

    /**
     * @param sourceBitmap sourceBitmap
     * @return invertBitmap
     */
    public static Bitmap doInvertImage(Bitmap sourceBitmap) {
        ColorMatrix colorMatrix_Inverted =
                new ColorMatrix(new float[]{
                        -1, 0, 0, 0, 255,
                        0, -1, 0, 0, 255,
                        0, 0, -1, 0, 255,
                        0, 0, 0, 1, 0});

        ColorFilter ColorFilter_Sepia = new ColorMatrixColorFilter(colorMatrix_Inverted);
        Bitmap outBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(ColorFilter_Sepia);
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return outBitmap;
    }

    /**
     * @param sourceBitmap sourceBitmap
     * @return bitmap
     */
    public static Bitmap doGrayScale(Bitmap sourceBitmap) {
        Bitmap outBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(filter);
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return outBitmap;
    }

    /**
     * @param sourceBitmap sourceBitmap
     * @param brightness   gamma value
     * @return bitmap
     */
    public static Bitmap doBrightness(Bitmap sourceBitmap, float brightness) {
        Bitmap outBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.RGB_565);
        float contrast = 1;
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                contrast, 0, 0, 0, brightness,
                0, contrast, 0, 0, brightness,
                0, 0, contrast, 0, brightness,
                0, 0, 0, 1, 0});
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return outBitmap;
    }

    /**
     * @param sourceBitmap sourceBitmap
     * @param intensity    intensity
     * @return bitmap
     */
    public static Bitmap doSepia(Bitmap sourceBitmap, float intensity) {
        Bitmap outBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrix colorScale = new ColorMatrix();
        colorScale.setScale(1, 1, intensity, 1);
        colorMatrix.postConcat(colorScale);
        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return outBitmap;
    }

    /**
     * @param sourceBitmap sourceBitmap
     * @param contrast     contrast value
     * @return bitmap
     */
    public static Bitmap doContrast(Bitmap sourceBitmap, float contrast) {
        Bitmap outBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        float brightness = 0;
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                contrast, 0, 0, 0, brightness,
                0, contrast, 0, 0, brightness,
                0, 0, contrast, 0, brightness,
                0, 0, 0, 1, 0});
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return outBitmap;
    }

    /**
     * @param sourceBitmap sourceBitmap
     * @param context      context
     * @return bitmap
     */
    public static Bitmap doSharpen(Bitmap sourceBitmap, Context context) {
        float weight = 0.8f;
        Bitmap outBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
        RenderScript renderScript = RenderScript.create(context);
        Allocation inAllocation = Allocation.createFromBitmap(renderScript, outBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        Allocation outAllocation = Allocation.createTyped(renderScript, inAllocation.getType());
        ScriptIntrinsicConvolve3x3 d = ScriptIntrinsicConvolve3x3.create(renderScript, Element.U8_4(renderScript));
        d.setCoefficients(new float[]{-weight, -weight, -weight, -weight, 8 * weight + 1, -weight, -weight, -weight, -weight});
        d.setInput(inAllocation);
        d.forEach(outAllocation);
        outAllocation.copyTo(outBitmap);
        renderScript.destroy();
        return outBitmap;
    }

    /**
     * @param sourceBitmap sourceBitmap
     * @param value        value
     * @return bitmap
     */
    public static Bitmap doHue(Bitmap sourceBitmap, int value) {
        Bitmap outBitmap = Bitmap.createBitmap(sourceBitmap);
        float cosVal = (float) Math.cos(value);
        float sinVal = (float) Math.sin(value);
        float lumR = 0.213f;
        float lumG = 0.715f;
        float lumB = 0.072f;
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                lumR + cosVal * (1 - lumR) + sinVal * (-lumR), lumG + cosVal * (-lumG) + sinVal * (-lumG), lumB + cosVal * (-lumB) + sinVal * (1 - lumB), 0, 0,
                lumR + cosVal * (-lumR) + sinVal * (0.143f), lumG + cosVal * (1 - lumG) + sinVal * (0.140f), lumB + cosVal * (-lumB) + sinVal * (-0.283f), 0, 0,
                lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR)), lumG + cosVal * (-lumG) + sinVal * (lumG), lumB + cosVal * (1 - lumB) + sinVal * (lumB), 0, 0,
                0f, 0f, 0f, 1f, 0f,
                0f, 0f, 0f, 0f, 1f});
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return outBitmap;
    }

    /**
     * @param sourceBitmap sourceBitmap
     * @param context      context
     * @return sketchBitmap
     */
    public static Bitmap doSketch(Bitmap sourceBitmap, Context context) {
        Bitmap copyBitmap, invertBitmap;
        copyBitmap = doGrayScale(sourceBitmap);
        invertBitmap = doInvertImage(copyBitmap);
        invertBitmap = doGaussianBlur(invertBitmap, context);
        try {
            return ColorDodgeBlend(invertBitmap, copyBitmap);
        } finally {
            copyBitmap.recycle();
            invertBitmap.recycle();
        }
    }

    private static int colorDodge(int in1, int in2) {
        float image = (float) in2;
        float mask = (float) in1;
        return ((int) ((image == 255) ? image : Math.min(255, (((long) mask << 8) / (255 - image)))));
    }

    /**
     * @param source sourceBitmap
     * @param layer  layerBitmap
     * @return blendBitmap
     */
    private static Bitmap ColorDodgeBlend(Bitmap source, Bitmap layer) {
        Bitmap base = source.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap blend = layer.copy(Bitmap.Config.ARGB_8888, false);

        IntBuffer buffBase = IntBuffer.allocate(base.getWidth() * base.getHeight());
        base.copyPixelsToBuffer(buffBase);
        buffBase.rewind();

        IntBuffer buffBlend = IntBuffer.allocate(blend.getWidth() * blend.getHeight());
        blend.copyPixelsToBuffer(buffBlend);
        buffBlend.rewind();

        IntBuffer buffOut = IntBuffer.allocate(base.getWidth() * base.getHeight());
        buffOut.rewind();

        while (buffOut.position() < buffOut.limit()) {
            int filterInt = buffBlend.get();
            int srcInt = buffBase.get();

            int redValueFilter = Color.red(filterInt);
            int greenValueFilter = Color.green(filterInt);
            int blueValueFilter = Color.blue(filterInt);

            int redValueSrc = Color.red(srcInt);
            int greenValueSrc = Color.green(srcInt);
            int blueValueSrc = Color.blue(srcInt);

            int redValueFinal = colorDodge(redValueFilter, redValueSrc);
            int greenValueFinal = colorDodge(greenValueFilter, greenValueSrc);
            int blueValueFinal = colorDodge(blueValueFilter, blueValueSrc);

            int pixel = Color.argb(255, redValueFinal, greenValueFinal, blueValueFinal);

            buffOut.put(pixel);
        }

        buffOut.rewind();

        base.copyPixelsFromBuffer(buffOut);
        blend.recycle();

        return base;
    }

    /**
     * @param sourceBitmap sourceBitmap
     * @param context      context
     * @return bitmap
     */
    public static Bitmap doGaussianBlur(Bitmap sourceBitmap, Context context) {
        final float BLUR_RADIUS = 25f;
        Bitmap outputBitmap = Bitmap.createBitmap(sourceBitmap);
        final RenderScript renderScript = RenderScript.create(context);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, sourceBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    /**
     * @param sourceBitmap sourceBitmap
     * @param radius       radius of Vignette
     * @return bitmap
     */
    public static Bitmap doVignette(Bitmap sourceBitmap, float radius) {
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();
        Bitmap outBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        radius = (float) (width / 2) * radius;
        if (radius == 0) {
            radius = 1;
        }
        RadialGradient gradient = new RadialGradient(width / 2, height / 2, radius, Color.TRANSPARENT, Color.BLACK, Shader.TileMode.CLAMP);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawARGB(1, 0, 0, 0);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setShader(gradient);
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectf = new RectF(rect);
        canvas.drawRect(rectf, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sourceBitmap, rect, rect, paint);
        return outBitmap;
    }
}
