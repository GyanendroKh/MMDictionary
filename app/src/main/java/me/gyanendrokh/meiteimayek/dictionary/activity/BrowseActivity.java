package me.gyanendrokh.meiteimayek.dictionary.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.adapter.BrowseAdapter;
import me.gyanendrokh.meiteimayek.dictionary.api.Request;
import me.gyanendrokh.meiteimayek.dictionary.api.Words;
import me.gyanendrokh.meiteimayek.dictionary.data.Language;
import me.gyanendrokh.meiteimayek.dictionary.data.Word;
import me.gyanendrokh.meiteimayek.dictionary.exception.LanguageNotExistException;
import me.gyanendrokh.meiteimayek.dictionary.ui.WordList;


public class BrowseActivity extends AppCompatActivity {

  private WordList mWordList;
  private ProgressBar mProgress;
  private TextView mError;

  private List<Word> mWords;
  private BrowseAdapter mListAdapter;

  private String mLang;

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

    mWords = new ArrayList<>();
    mListAdapter = new BrowseAdapter(mWords);

    initViews();
    fetchData();
  }

  private void initViews() {
    mWordList = findViewById(R.id.browse_items);
    mProgress = findViewById(R.id.browse_progress_bar);
    mError = findViewById(R.id.browse_error);

    mWordList.setAdapter(mListAdapter);
    mProgress.setIndeterminate(true);
  }

  private void fetchData() {
    Words words = Words.getInstance(BrowseActivity.this);
    words.setData(mLang);
    words.fetch(new Request.Listener<JSONArray>() {
      @Override
      public void onResponse(JSONArray response) {
        for(int i = 0; i <= response.length(); i++) {
          try {
            JSONObject data = (JSONObject) response.get(i);
            Word w = new Word(data.getInt("id"), data.getString("word"), mLang);
            mWords.add(w);
            mProgress.setVisibility(View.GONE);
            if(mWords.size() == 0) {
              mError.setText("There is no data right now...");
            }else {
              mListAdapter.notifyDataSetChanged();
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }

      @Override
      public void onError(VolleyError error) {

      }
    });
  }

}
