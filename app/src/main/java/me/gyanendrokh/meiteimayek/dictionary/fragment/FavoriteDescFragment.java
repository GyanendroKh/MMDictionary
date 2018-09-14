package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.os.Bundle;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.data.Word;
import me.gyanendrokh.meiteimayek.dictionary.database.FavoriteDatabase;

public class FavoriteDescFragment extends WordDescFragment {

  public static FavoriteDescFragment createFragment(Word word) {
    Bundle bundle = new Bundle();
    bundle.putSerializable(WORD, word);

    FavoriteDescFragment frag = new FavoriteDescFragment();
    frag.setArguments(bundle);

    return frag;
  }

  @Override
  public void init() {
    setActBtnIcon(R.drawable.ic_delete);
    setBtnAct((view) ->
      new CompositeDisposable().add(Observable.just(FavoriteDatabase.getInstance(getActivity()).getDao()).subscribeOn(Schedulers.newThread()).subscribe(
        fav -> fav.deleteWord(getData().getWordId()),
        error -> Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show()
      ))
    );
  }

  @Override
  public void fetch(OnDataFetched fetch) {
    fetch.onFetched(super.getData());
  }

}
