package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.os.Bundle;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.gyanendrokh.meiteimayek.dictionary.data.Word;
import me.gyanendrokh.meiteimayek.dictionary.database.FavoriteDatabase;

public class BrowseDescFragment extends WordDescFragment {

  public static BrowseDescFragment createFragment(me.gyanendrokh.meiteimayek.dictionary.data.Word word) {
    Bundle bundle = new Bundle();
    bundle.putSerializable(WORD, word);

    BrowseDescFragment frag = new BrowseDescFragment();
    frag.setArguments(bundle);

    return frag;
  }

  @Override
  public void init() {
    setBtnAct(view ->
      new CompositeDisposable().add(Observable.just(FavoriteDatabase.getInstance(getActivity()))
        .subscribeOn(Schedulers.newThread())
        .subscribe(
          fav -> {
            fav.getDao().addWord(Word.convert(getData()));
            getActivity().runOnUiThread(() ->
              Toast.makeText(getActivity(), "Added to Favorite...", Toast.LENGTH_SHORT).show()
            );
          }, error -> getActivity().runOnUiThread(
            () -> Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show()
          )
        )
      )
    );
  }

  @Override
  public void fetch(OnDataFetched fetch) {
    fetch.onFetched(super.getData());
  }

}
