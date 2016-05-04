package casso.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import com.casso.R;

/**
 * Created by stephrhee on 5/4/16.
 */

public class RoundedBackgroundSpan extends ReplacementSpan {

    private final Resources mResources;
    private final int mBackgroundColorId;
    private final int mTextColorId;

    public RoundedBackgroundSpan(Context context, int backgroundColorId, int textColorId) {
        mResources = context.getResources();
        mBackgroundColorId = backgroundColorId;
        mTextColorId = textColorId;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text,
                     int start, int end, float x, int top, int y, int bottom,
                     Paint paint) {
        int cornerRadius =
                mResources.getDimensionPixelSize(R.dimen.artwork_profile_topics_corner_radius);
        int topBottomSpacingDimen =
                mResources.getDimensionPixelSize(R.dimen.artwork_profile_topics_top_bottom_spacing);
        paint.setColor(mResources.getColor(mBackgroundColorId));
        RectF smallerRect = new RectF(
                x,
                top,
                x + measureText(paint, text, start, end),
                bottom - topBottomSpacingDimen);
        canvas.drawRoundRect(
                smallerRect,
                cornerRadius,
                cornerRadius,
                paint);
        paint.setColor(mResources.getColor(mTextColorId));
        canvas.drawText(
                text,
                start,
                end,
                x,
                y,
                paint);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return Math.round(measureText(paint, text, start, end));
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }

}
