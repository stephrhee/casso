package casso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import com.casso.R;

import java.security.KeyException;

public class SearchActivity extends FragmentActivity {

    private EditText mArtistNameSearchField;
    private EditText mArtworkTitleSearchField;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_layout);

        init();
    }

    private void init() {
        mArtistNameSearchField = (EditText) findViewById(R.id.search_artist_search_field);
        mArtworkTitleSearchField = (EditText) findViewById(R.id.search_artwork_title_search_field);

        mArtistNameSearchField.setHint(R.string.search_artist_name_question_string);
        mArtworkTitleSearchField.setHint(R.string.search_artwork_title_question_string);
        mArtworkTitleSearchField.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mArtworkTitleSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    return true;
                }
                return false;
            }
        });
    }

    private void doSearch() {
        Intent intent = new Intent(this, ArtworkProfileActivity.class);
        startActivity(intent);
    }

}
