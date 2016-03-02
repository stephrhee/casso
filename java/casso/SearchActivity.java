package casso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.TextView;
import com.casso.R;

import java.util.Arrays;
import java.util.List;

public class SearchActivity extends FragmentActivity {

    private EditText mArtistNameSearchField;
    private EditText mArtworkTitleSearchField;
    private ListView mArtistSuggestionsListView;
    private ListView mArtworkSuggestionsListView;

    private final List<String> mArtistSuggestions = Arrays.asList("Picasso", "Van Gogh", "Kandinsky", "Mondrian", "Warhol", "Monet", "Da Vinci");
    private final List<String> mArtworkSuggestions = Arrays.asList("The Starry Night", "Mona Lisa", "Composition C");

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
        mArtistSuggestionsListView = (ListView) findViewById(R.id.search_artist_suggestions_list_view);
        mArtworkSuggestionsListView = (ListView) findViewById(R.id.search_artwork_suggestions_list_view);

        mArtistNameSearchField.setHint(R.string.search_artist_name_question_string);
        mArtworkTitleSearchField.setHint(R.string.search_artwork_title_question_string);

        mArtistNameSearchField.addTextChangedListener(getTextWatcher(
                mArtistNameSearchField,
                mArtistSuggestionsListView,
                mArtistSuggestions));
        mArtworkTitleSearchField.addTextChangedListener(getTextWatcher(
                mArtworkTitleSearchField,
                mArtworkSuggestionsListView,
                mArtworkSuggestions));

        mArtistNameSearchField.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mArtworkTitleSearchField.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        mArtistNameSearchField.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_NEXT) {
                            hideSuggestions(mArtistSuggestionsListView);
                            mArtworkTitleSearchField.requestFocus();
                            return true;
                        }
                        return false;
                    }
                });
        mArtworkTitleSearchField.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            doSearch();
                            return true;
                        }
                        return false;
                    }
                });

        mArtistNameSearchField.setOnFocusChangeListener(getOnFocusChangeListener(
                mArtistSuggestionsListView,
                mArtworkSuggestionsListView,
                mArtistNameSearchField,
                mArtistSuggestions));
        mArtworkTitleSearchField.setOnFocusChangeListener(getOnFocusChangeListener(
                mArtworkSuggestionsListView,
                mArtistSuggestionsListView,
                mArtworkTitleSearchField,
                mArtworkSuggestions));
    }

    private void doSearch() {
        Intent intent = new Intent(this, ArtworkProfileActivity.class);
        startActivity(intent);
    }

    private TextWatcher getTextWatcher(
            final EditText editText,
            final ListView listView,
            final List<String> suggestions) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showOrHideSuggestionsBasedOnEditText(editText, listView, suggestions);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private void showOrHideSuggestionsBasedOnEditText(
            EditText editText,
            ListView listView,
            List<String> suggestions) {
        if (editText.getText().length() > 0) {
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(new ArrayAdapter<>(
                    this, R.layout.search_suggestions_row_view, suggestions));
        } else {
            hideSuggestions(listView);
        }
    }

    private void hideSuggestions(ListView listView) {
        listView.setVisibility(View.GONE);
    }

    private View.OnFocusChangeListener getOnFocusChangeListener(
            final ListView thisSuggestionsListView,
            final ListView otherSuggestionsListView,
            final EditText thisEditText,
            final List<String> thisSuggestions) {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideSuggestions(otherSuggestionsListView);
                showOrHideSuggestionsBasedOnEditText(
                        thisEditText,
                        thisSuggestionsListView,
                        thisSuggestions);
            }
        };
    }

}
