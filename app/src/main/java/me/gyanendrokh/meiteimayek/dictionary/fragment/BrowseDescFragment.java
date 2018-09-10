package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.os.Bundle;

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

  }

  @Override
  public void fetch(OnDataFetched fetch) {
    fetch.onFetched(super.getData());
  }

}
