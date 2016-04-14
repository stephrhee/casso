package casso.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by stephrhee on 4/10/16.
 */

public class CenterLockHorizontalScrollview extends HorizontalScrollView {

    private final Context mContext;
    private int mPreviousIndex = 0;

    private LinearLayout mLinearLayout;

    public CenterLockHorizontalScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setSmoothScrollingEnabled(true);
    }

    public void setAdapter(CenterLockHorizontalScrollviewAdapter mAdapter) {
        if (getChildCount() != 0 && mAdapter != null) {
            mLinearLayout = (LinearLayout) getChildAt(0);
            mLinearLayout.removeAllViews();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                mLinearLayout.addView(mAdapter.getView(i, null, mLinearLayout));
            }
        }
    }

    public void setCenter(int index) {
        View preView = mLinearLayout.getChildAt(mPreviousIndex);
        preView.setBackgroundColor(Color.parseColor("#64CBD8"));
        android.widget.LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);
        preView.setLayoutParams(lp);

        View view = mLinearLayout.getChildAt(index);
        view.setBackgroundColor(Color.RED);

        int screenWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();

        int scrollX = (view.getLeft() - (screenWidth / 2)) + (view.getWidth() / 2);
        this.smoothScrollTo(scrollX, 0);
        mPreviousIndex = index;
    }

}
