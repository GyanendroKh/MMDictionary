package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.os.Bundle;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.data.Word;

public class FavoriteDescFragment extends WordDescFragment {

  public static FavoriteDescFragment createFragment(Word word) {
    Bundle bundle = new Bundle();
    bundle.putSerializable(WORD, word);

    FavoriteDescFragment frag = new FavoriteDescFragment();
    frag.setArguments(bundle);
    frag.setActBtnIcon(R.drawable.ic_delete);

    return frag;
  }

  @Override
  public void fetch(OnDataFetched fetch) {
    fetch.onFetched(super.getData());
  }

}
