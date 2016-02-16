package com.example.casso;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class SearchActivity extends FragmentActivity {

    private EditText mArtistNameSearchBox;
    private EditText mPieceTitleSearchBox;
    private Button mSearchButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.search_layout);

        init();
    }

    private void init() {
        mArtistNameSearchBox = (EditText) findViewById(R.id.search_artist_name_edit_text);
        mPieceTitleSearchBox = (EditText) findViewById(R.id.search_piece_title_edit_text);
        mSearchButton = (Button) findViewById(R.id.search_button);
        mArtistNameSearchBox.setHint(R.string.artist_name);
        mPieceTitleSearchBox.setHint(R.string.piece_title);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });
    }

    private void doSearch() {

    }

}
