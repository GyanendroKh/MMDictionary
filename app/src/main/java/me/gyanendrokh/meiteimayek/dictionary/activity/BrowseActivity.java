package me.gyanendrokh.meiteimayek.dictionary.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.adapter.BrowseAdapter;
import me.gyanendrokh.meiteimayek.dictionary.api.Words;
import me.gyanendrokh.meiteimayek.dictionary.data.Language;
import me.gyanendrokh.meiteimayek.dictionary.data.Word;
import me.gyanendrokh.meiteimayek.dictionary.database.FavoriteDatabase;
import me.gyanendrokh.meiteimayek.dictionary.exception.LanguageNotExistException;
import me.gyanendrokh.meiteimayek.dictionary.fragment.BrowseDescFragment;
import me.gyanendrokh.meiteimayek.dictionary.ui.WordList;

public class BrowseActivity extends AppCompatActivity {

  private WordList mWordList;
  private ProgressBar mProgress;
  private TextView mError;

  private List<Word> mResults;
  private BrowseAdapter mListAdapter;
  private Words mWords;

  private String mLang;
  private int mPag = 1;

  private Boolean mIsLoading = false;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_browse);

    mLang = Objects.requireNonNull(getIntent().getExtras()).getString("lang");
    try {
      Objects.requireNonNull(getSupportActionBar()).setTitle(Language.getLanguages()[Language.Code.get(mLang)]);
    } catch (LanguageNotExistException e) {
      e.printStackTrace();
    }

    mResults = new ArrayList<>();
    mListAdapter = new BrowseAdapter(mResults);
    mWords = Words.getInstance(this);

    initViews();
    fetchData();

    mWordList.setOnBottomReachedListener(() -> {
      if(mIsLoading) return;

      mPag++;
      mIsLoading = true;
      mResults.remove(null);
      fetchData();
    });

    mListAdapter.setOnItemClickListener(
      (view, position) -> BrowseDescFragment.createFragment(mListAdapter.getItem(position)).show(getSupportFragmentManager(), getClass().getName())
    );

    mListAdapter.setOnActBtnClicked(
      position -> new CompositeDisposable().add(Observable.just(FavoriteDatabase.getInstance(getApplicationContext()))
        .subscribeOn(Schedulers.newThread())
        .subscribe(
          fav -> {
            fav.getDao().addWord(Word.convert(mResults.get(position)));
            runOnUiThread(() ->
              Toast.makeText(BrowseActivity.this, "Added to Favorite...", Toast.LENGTH_SHORT).show()
            );
          }, error -> runOnUiThread(
            () -> Toast.makeText(BrowseActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show()
          )
        )
      )
    );
  }

  private void initViews() {
    mWordList = findViewById(R.id.browse_items);
    mProgress = findViewById(R.id.browse_progress_bar);
    mError = findViewById(R.id.browse_error);

    mWordList.setAdapter(mListAdapter);
    mProgress.setIndeterminate(true);
  }

  private void fetchData() {
    mWords.setData(mLang, mPag);
    mWords.fetch(response -> {
      for (int i = 0; i <= response.length(); i++) {
        try {
          JSONObject data = (JSONObject) response.get(i);

          Word w = new Word(
            data.getInt("id"),
            data.getString("word"),
            mLang
          ).setDesc(data.getString("description"))
            .setReadAs(data.getString("read_as"));

          mResults.add(w);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      mProgress.setVisibility(View.GONE);
      if (mResults.size() == 0) {
        mError.setText(R.string.no_data);
      } else {
        mResults.add(null);
        mListAdapter.notifyDataSetChanged();
      }

      mIsLoading = false;
    }, e -> {
      mProgress.setVisibility(View.GONE);
      mError.setVisibility(View.VISIBLE);
      mError.setText(R.string.unable_to_fetch_data);
    });
  }
}
