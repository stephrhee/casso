package casso.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.casso.R;

/**
 * Created by stephrhee on 4/21/16.
 */

public class ImageViewAndLoadingScreen extends SquareFrameLayout {

    private CircularProgressView mCircularProgressView;
    private ImageView mImageView;

    public ImageViewAndLoadingScreen(Context context) {
        this(context, null);
    }

    public ImageViewAndLoadingScreen(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewAndLoadingScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.image_view_and_loading_screen, this);
        initViews();
    }

    private void initViews() {
        mCircularProgressView = (CircularProgressView)
                findViewById(R.id.image_view_and_loading_screen_circular_progress_view);
        mImageView = (ImageView) findViewById(R.id.image_view_and_loading_screen_image_view);
        showCircularProgressView();
    }

    public void setImageView(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
        showImageView();
    }

    private void showCircularProgressView() {
        mCircularProgressView.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.GONE);
    }

    private void showImageView() {
        mCircularProgressView.setVisibility(View.GONE);
        mImageView.setVisibility(View.VISIBLE);
    }

}
