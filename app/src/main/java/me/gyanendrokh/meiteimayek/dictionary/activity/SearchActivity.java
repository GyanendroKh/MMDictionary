package me.gyanendrokh.meiteimayek.dictionary.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lapism.searchview.widget.SearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.adapter.BrowseAdapter;
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
  private int mPagination = 1;

  private Search mSearch;
  private List<Word> mResult;
  private BrowseAdapter mAdapter;

  private boolean mIsLoading = false;
  private boolean mIsLast = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    Bundle bundle = getIntent().getExtras();

    if(bundle == null) throw new IllegalArgumentException();

    Window window = getWindow();
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

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

        mPagination = 1;
        mIsLast = false;

        fetch();

        return true;
      }

      @Override
      public void onQueryTextChange(CharSequence newText) {}
    });

    mAdapter.setOnItemClickListener(
      (view, position) -> BrowseDescFragment.createFragment(mAdapter.getItem(position)).show(getSupportFragmentManager(), getClass().getName())
    );

    mListView.setOnBottomReachedListener(() -> {
      if(mIsLoading || mIsLast) return;

      mPagination++;

      mIsLoading = true;
      mResult.remove(null);

      fetch();
    });
  }

  public void fetch() {
    mNoResultText.setVisibility(View.GONE);
    if(mPagination == 1) mProgressBar.setVisibility(View.VISIBLE);

    mSearch.setData(mLang, mKeyword, mPagination);

    mSearch.fetch(response ->  {
      mProgressBar.setVisibility(View.GONE);

      if(response.length() == 0) {
        if(mPagination == 1) mNoResultText.setVisibility(View.VISIBLE);
        mIsLast = true;
      }else {
        for(int i = 0; i < response.length(); i++) {
          try {
            JSONObject obj = (JSONObject) response.get(i);
            mResult.add(
              new Word(
                obj.getInt("id"),
                obj.getString("word"),
                mLang
              ).setDesc(obj.getString("description"))
                .setReadAs(obj.getString("read_as"))
            );
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }

        if(mResult.contains(null))
          mResult.remove(null);
        mResult.add(null);
      }

      mAdapter.notifyDataSetChanged();

      mIsLoading = false;
    }, e -> {
      mProgressBar.setVisibility(View.GONE);
      mNoResultText.setVisibility(View.VISIBLE);
      mNoResultText.setText(R.string.unable_to_fetch_data);
    });
  }

}
