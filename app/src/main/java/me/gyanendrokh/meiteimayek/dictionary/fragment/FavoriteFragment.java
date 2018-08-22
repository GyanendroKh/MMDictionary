package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.adapter.FavoriteAdapter;
import me.gyanendrokh.meiteimayek.dictionary.database.FavoriteDatabase;
import me.gyanendrokh.meiteimayek.dictionary.database.WordDao;
import me.gyanendrokh.meiteimayek.dictionary.ui.WordList;

public class FavoriteFragment extends Fragment {

  private WordList mListView;
  private ProgressBar mProgressBar;
  private TextView mTextNone;

  private List<me.gyanendrokh.meiteimayek.dictionary.data.Word> mResults = new ArrayList<>();
  private FavoriteAdapter mAdapter = new FavoriteAdapter(mResults);

  private WordDao mFavDatabase;

  private CompositeDisposable mDisposable = new CompositeDisposable();

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_favorite, container, false);

    mListView = view.findViewById(R.id.favorite_list);
    mProgressBar = view.findViewById(R.id.favorite_progress);
    mTextNone = view.findViewById(R.id.favorite_none);

    mFavDatabase = FavoriteDatabase.getInstance(getActivity()).getDao();

    init();

    return view;
  }

  private void init() {
    mListView.setAdapter(mAdapter);

    mDisposable.add(mFavDatabase.getAll()
      .subscribeOn(Schedulers.newThread())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(words -> {
        mResults.clear();
        mProgressBar.setVisibility(View.GONE);

        if(words.size() == 0) {
          mTextNone.setVisibility(View.VISIBLE);
        }else {
          for(int i = 0; i < words.size(); i++) {
            mResults.add(me.gyanendrokh.meiteimayek.dictionary.data.Word.convert(words.get(i)));
          }

          mTextNone.setVisibility(View.GONE);
        }

        mAdapter.notifyDataSetChanged();
      }, throwable -> Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show())
    );

    mAdapter.setOnActBtnClicked(position ->
      mDisposable.add(Observable.just(mFavDatabase).subscribeOn(Schedulers.newThread()).subscribe(
        fav -> fav.deleteWord(mResults.get(position).getWordId()),
        error -> Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show()
      ))
    );

    mAdapter.setOnItemClickListener((view, position) ->
      FavoriteDescFragment.createFragment(mResults.get(position)).show(getActivity().getSupportFragmentManager(), getClass().getName())
    );
  }

}
