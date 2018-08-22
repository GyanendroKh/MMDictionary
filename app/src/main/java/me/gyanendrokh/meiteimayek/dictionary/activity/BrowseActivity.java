package me.gyanendrokh.meiteimayek.dictionary.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
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
import me.gyanendrokh.meiteimayek.dictionary.api.Request;
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
  private int mStart = 1;
  private int mLimit = 20;

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

      mStart += mLimit;
      mIsLoading = true;
      mResults.remove(null);
      fetchData();
    });

    mListAdapter.setOnItemClickListener(
      (view, position) -> BrowseDescFragment.createFragment(mListAdapter.getItem(position)).show(getSupportFragmentManager(), getClass().getName())
    );

    mListAdapter.setOnActBtnClicked(
      position -> {
        Word w = mResults.get(position);

        me.gyanendrokh.meiteimayek.dictionary.api.Word wA = me.gyanendrokh.meiteimayek.dictionary.api.Word.getInstance(BrowseActivity.this);
        wA.setData(w.getWord(), w.getLang());
        wA.fetch(new Request.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            try {
              String desc = response.getString("description");
              String readAs = response.getString("read_as");
              new CompositeDisposable().add(Observable.just(FavoriteDatabase.getInstance(getApplicationContext()))
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                  fav -> {
                    fav.getDao().addWord(Word.convert(mResults.get(position).setDesc(desc).setReadAs(readAs)));
                    runOnUiThread(() ->
                      Toast.makeText(BrowseActivity.this, "Added to Favorite...", Toast.LENGTH_SHORT).show()
                    );
                  }, error -> runOnUiThread(
                    () -> Toast.makeText(BrowseActivity.this, error.getMessage(), Toast.LENGTH_LONG).show()
                  )));
            } catch (JSONException e) {
              Toast.makeText(BrowseActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
          }

          @Override
          public void onError(VolleyError error) {
            Toast.makeText(BrowseActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
          }
        });
      }
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
    mWords.setData(mLang, mStart, mLimit);
    mWords.fetch(new Request.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray response) {
        for(int i = 0; i <= response.length(); i++) {
          try {
            JSONObject data = (JSONObject) response.get(i);
            Word w = new Word(data.getInt("id"), data.getString("word"), mLang);
            mResults.add(w);
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }

        mProgress.setVisibility(View.GONE);
        if(mResults.size() == 0) {
          mError.setText(R.string.no_data);
        }else {
          mResults.add(null);
          mListAdapter.notifyDataSetChanged();
        }

        mIsLoading = false;
      }

      @Override
      public void onError(VolleyError error) {
        Toast.makeText(BrowseActivity.this, String.valueOf(error.getNetworkTimeMs()), Toast.LENGTH_SHORT).show();
        Toast.makeText(BrowseActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
      }
    });
  }

}
