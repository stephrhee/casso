package casso;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.casso.R;

public class ArtworkProfileActivity extends FragmentActivity implements DownloadImageAsyncTask.Callback {

    private ImageView mImage;
    private TextView mTitle;
    private TextView mArtist;
    private TextView mYear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.artwork_profile_layout);

        init();
    }

    private void init() {
        mImage = (ImageView) findViewById(R.id.artwork_profile_image);
        mTitle = (TextView) findViewById(R.id.artwork_profile_title);
        mArtist = (TextView) findViewById(R.id.artwork_profile_artist);
        mYear = (TextView) findViewById(R.id.artwork_profile_year);

        DownloadImageAsyncTask downloadImageAsyncTask = new DownloadImageAsyncTask(this);
        String string = "http://www.pablopicasso.org/images/paintings/guernica3.jpg";
        downloadImageAsyncTask.execute(string);

        mTitle.setText("Guernica");
        mArtist.setText("Pablo Picasso");
        mYear.setText("1937");
    }

    @Override
    public void onBitmapFetched(Bitmap bitmap) {
        if (bitmap != null) {
            mImage.setImageBitmap(bitmap);
        } else {
            Log.e("Error", "bitmap could not be fetched");
        }
    }

}
