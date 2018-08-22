package me.gyanendrokh.meiteimayek.dictionary.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.lapism.searchview.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.adapter.BrowseAdapter;
import me.gyanendrokh.meiteimayek.dictionary.api.Request;
import me.gyanendrokh.meiteimayek.dictionary.api.Search;
import me.gyanendrokh.meiteimayek.dictionary.data.Word;
import me.gyanendrokh.meiteimayek.dictionary.fragment.BrowseDescFragment;
import me.gyanendrokh.meiteimayek.dictionary.ui.WordList;

public class SearchActivity extends AppCompatActivity {

  public static final String KEYWORD = "keyword";
  public static final String LANG = "lang";

  private SearchView mSearchView;
  private WordList mListView;
  private ProgressBar mProgressBar;
  private TextView mNoResultText;

  private String mKeyword;
  private String mLang;

  private Search mSearch;
  private List<Word> mResult;
  private BrowseAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    Bundle bundle = getIntent().getExtras();

    if(bundle == null) throw new IllegalArgumentException();

    mKeyword = bundle.getString(KEYWORD);
    mLang = bundle.getString(LANG);

    mSearchView = findViewById(R.id.search_view);
    mListView = findViewById(R.id.search_results);
    mProgressBar = findViewById(R.id.progress_bar);
    mNoResultText = findViewById(R.id.no_result_text);

    mSearch = Search.getInstance(SearchActivity.this);
    mResult = new ArrayList<>();
    mAdapter = new BrowseAdapter(mResult);

    init();
    fetch();
  }

  public void init() {
    mListView.setAdapter(mAdapter);

    mSearchView.setQuery(mKeyword, false);
    mSearchView.setOnLogoClickListener(this::onBackPressed);
    mSearchView.setOnQueryTextListener(new com.lapism.searchview.Search.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(CharSequence query) {
        mKeyword = query.toString();
        mResult.clear();
        mSearchView.close();

        mProgressBar.setVisibility(View.VISIBLE);
        fetch();

        return true;
      }

      @Override
      public void onQueryTextChange(CharSequence newText) {}
    });

    mAdapter.setOnItemClickListener(
      (view, position) -> BrowseDescFragment.createFragment(mAdapter.getItem(position)).show(getSupportFragmentManager(), getClass().getName())
    );
  }

  public void fetch() {
    mSearch.setData(mKeyword, mLang);

    mSearch.fetch(new Request.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray response) {
        if(response.length() == 0) {
          mProgressBar.setVisibility(View.GONE);
          mNoResultText.setVisibility(View.VISIBLE);
        }else mNoResultText.setVisibility(View.INVISIBLE);


        for(int i = 0; i < response.length(); i++) {
          try {
            JSONObject obj = (JSONObject) response.get(i);
            mResult.add(new Word(obj.getInt("id"), obj.getString("word"), mLang));

            mProgressBar.setVisibility(View.GONE);

            mAdapter.notifyDataSetChanged();
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }

      @Override
      public void onError(VolleyError error) {
        Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
      }
    });
  }

}