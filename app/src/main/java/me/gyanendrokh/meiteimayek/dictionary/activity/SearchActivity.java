package me.gyanendrokh.meiteimayek.dictionary.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

  private Search mSearch;
  private List<Word> mResult;
  private BrowseAdapter mAdapter;

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
    mSearch.setData(mLang, mKeyword);

    mSearch.fetch(response ->  {
      if(response.length() == 0) {
        mProgressBar.setVisibility(View.GONE);
        mNoResultText.setVisibility(View.VISIBLE);
      }else mNoResultText.setVisibility(View.INVISIBLE);


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

          mProgressBar.setVisibility(View.GONE);

          mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, e -> Toast.makeText(SearchActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
  }

}
