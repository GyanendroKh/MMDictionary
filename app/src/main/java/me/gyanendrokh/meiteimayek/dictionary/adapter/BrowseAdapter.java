package me.gyanendrokh.meiteimayek.dictionary.adapter;

import java.util.List;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.data.Word;

public class BrowseAdapter extends WordListAdapter {

  public BrowseAdapter(List<Word> words) {
    super(words);
  }

  @Override
  int getBtnImg() {
    return R.drawable.ic_add;
  }

  @Override
  String getPrimaryText(int position) {
    return super.getItem(position).getWord();
  }

  @Override
  String getSecondaryText(int position) {
    return "";
  }

}
