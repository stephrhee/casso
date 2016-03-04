package casso;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import casso.http.FirebaseRequestHandler;
import casso.http.YCBARequestHandler;

import com.casso.R;

import java.util.List;

public class ArtworkProfileActivity extends FragmentActivity implements
        DownloadImageAsyncTask.Callback,
        FirebaseRequestHandler.Callback,
        YCBARequestHandler.Callback {

    private FirebaseRequestHandler mFirebaseRequestHandler;
    private YCBARequestHandler mYCBARequestHandler;

    private final String mObjectId = "499";

    private ImageView mImage;
    private TextView mTitle;
    private TextView mArtist;
    private TextView mYear;
    private TextView mDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.artwork_profile_layout);

        init();
        mFirebaseRequestHandler.getObjectIdsList();
        mYCBARequestHandler.execute();
    }

    private void init() {
        mFirebaseRequestHandler = new FirebaseRequestHandler(
                this,
                FirebaseRequestHandler.DATA_URL,
                this);
        mYCBARequestHandler = new YCBARequestHandler(this, mObjectId, this);

        mImage = (ImageView) findViewById(R.id.artwork_profile_image);
        mTitle = (TextView) findViewById(R.id.artwork_profile_title);
        mArtist = (TextView) findViewById(R.id.artwork_profile_artist);
        mYear = (TextView) findViewById(R.id.artwork_profile_year);
        mDescription = (TextView) findViewById(R.id.artwork_profile_description);

        DownloadImageAsyncTask downloadImageAsyncTask = new DownloadImageAsyncTask(this);
        String string = "http://www.pablopicasso.org/images/paintings/guernica3.jpg";
        downloadImageAsyncTask.execute(string);

        mTitle.setText("Guernica");
        mArtist.setText("Pablo Picasso");
        mYear.setText("1937");
        mDescription.setText("The Starry Night is an oil on canvas by the Dutch post-impressionist painter Vincent van Gogh. Painted in June, 1889, it depicts the view from the east-facing window of his asylum room at Saint-Rémy-de-Provence, just before sunrise, with the addition of an idealized village.[1][2][3] It has been in the permanent collection of the Museum of Modern Art in New York City since 1941, acquired through the Lillie P. Bliss Bequest. It is regarded as among Van Gogh's finest works,[4] and is one of the most recognized paintings in the history of Western culture.[5][6]");
    }

    @Override
    public void onBitmapFetched(Bitmap bitmap) {
        if (bitmap != null) {
            mImage.setImageBitmap(bitmap);
        } else {
            Log.e("Error", "bitmap could not be fetched");
        }
    }

    @Override
    public void onObjectIdsFetched(List<Integer> objectIdsList) {
    }

    @Override
    public void onObjectIdsFetchFailed() {
    }

    @Override
    public void onYCBAResponseFetched(YCBARequestHandler.Result result) {
    }

    @Override
    public void onYCBARequestFailed() {
    }

}
