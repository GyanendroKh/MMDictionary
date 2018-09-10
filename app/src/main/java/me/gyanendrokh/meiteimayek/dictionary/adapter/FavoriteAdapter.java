package me.gyanendrokh.meiteimayek.dictionary.adapter;

import java.util.List;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.data.Language;
import me.gyanendrokh.meiteimayek.dictionary.data.Word;
import me.gyanendrokh.meiteimayek.dictionary.exception.LanguageNotExistException;

public class FavoriteAdapter extends WordListAdapter {

  public FavoriteAdapter(List<Word> words) {
    super(words);
  }

  @Override
  int getBtnImg() {
    return R.drawable.ic_delete_black;
  }

  @Override
  String getPrimaryText(int position) {
    return super.getItem(position).getWord();
  }

  @Override
  String getSecondaryText(int position) {
    try {
      return Language.getLanguages()[Language.Code.get(super.getItem(position).getLang())];
    } catch (LanguageNotExistException e) {
      e.printStackTrace();
    }
    return "";
  }

}
