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
import casso.http.OnStartFetchHandler;
import com.casso.R;

import java.util.Arrays;
import java.util.List;

public class SearchActivity extends FragmentActivity {

    private EditText mArtistSearchField;
    private EditText mArtworkSearchField;
    private ListView mArtistSuggestionsListView;
    private ListView mArtworkSuggestionsListView;

    private final List<String> mArtistSuggestions = Arrays.asList("Richard Cosway", "Francis Danby", "Benjamin van der Gucht", "Francis Hayman", "Thomas Hickey", "William Hodges", "Edward Lear");
    private final List<String> mArtworkSuggestions = Arrays.asList("Portrait of an Armenian", "The Mountain Torrent", "Henry Woodward as Petruchio in Catherine and Petruchio", "The Good Samaritan", "Purniya, Chief Minister of Mysore", "Storm on the Ganges, with Mrs. Hastings near the Col-gon Rocks", "Philae, Egypt");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_layout);

        OnStartFetchHandler.fetchSuggestedArtworks(this, this);
        OnStartFetchHandler.fetchObjectIds(this, this);
        OnStartFetchHandler.fetchArtworks(this, this);

        init();
    }

    private void init() {
        mArtistSearchField = (EditText) findViewById(R.id.search_artist_search_field);
        mArtworkSearchField = (EditText) findViewById(R.id.search_artwork_search_field);
        mArtistSuggestionsListView = (ListView) findViewById(R.id.search_artist_suggestions_list_view);
        mArtworkSuggestionsListView = (ListView) findViewById(R.id.search_artwork_suggestions_list_view);

        mArtistSearchField.setHint(R.string.search_artist_question_string);
        mArtworkSearchField.setHint(R.string.search_artwork_question_string);

        mArtistSearchField.addTextChangedListener(getTextWatcher(
                mArtistSearchField,
                mArtistSuggestionsListView,
                mArtistSuggestions));
        mArtworkSearchField.addTextChangedListener(getTextWatcher(
                mArtworkSearchField,
                mArtworkSuggestionsListView,
                mArtworkSuggestions));

        mArtistSearchField.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mArtworkSearchField.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        mArtistSearchField.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_NEXT) {
                            hideSuggestions(mArtistSuggestionsListView);
                            mArtworkSearchField.requestFocus();
                            return true;
                        }
                        return false;
                    }
                });
        mArtworkSearchField.setOnEditorActionListener(
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

        mArtistSearchField.setOnFocusChangeListener(getOnFocusChangeListener(
                mArtistSuggestionsListView,
                mArtworkSuggestionsListView,
                mArtistSearchField,
                mArtistSuggestions));
        mArtworkSearchField.setOnFocusChangeListener(getOnFocusChangeListener(
                mArtworkSuggestionsListView,
                mArtistSuggestionsListView,
                mArtworkSearchField,
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
