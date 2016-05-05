package casso;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.widget.TextView;

import casso.util.FontUtil;

import com.casso.R;

/**
 * Created by stephrhee on 5/5/16.
 */

public class SplashActivity extends FragmentActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_layout);

        Typeface goudyStMRegularTypeface = FontUtil.getTypeface(
                this,
                FontUtil.mGoudyStMRegularTypefaceString);

        TextView appTitle = (TextView) findViewById(R.id.splash_activity_app_title_text_view);
        appTitle.setTypeface(goudyStMRegularTypeface);

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, SearchActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                SPLASH_DISPLAY_LENGTH);
    }

}
