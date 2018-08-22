package me.gyanendrokh.meiteimayek.dictionary.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.data.Language;
import me.gyanendrokh.meiteimayek.dictionary.data.Word;
import me.gyanendrokh.meiteimayek.dictionary.exception.LanguageNotExistException;
import me.gyanendrokh.meiteimayek.dictionary.ui.WordListItem;

public class FavoriteAdapter extends WordListAdapter {

  private OnActBtnClicked mOnClicked = null;

  public FavoriteAdapter(List<Word> words) {
    super(words);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    WordListItem item = (WordListItem)holder;

    item.setBtnImg(R.drawable.ic_delete);
    item.setPrimaryText(super.getItem(position).getWord());
    try {
      item.setSecondaryText(Language.getLanguages()[Language.Code.get(super.getItem(position).getLang())]);
    } catch (LanguageNotExistException e) {
      e.printStackTrace();
    }

    item.setOnBtnClicked((view) -> {
      if(mOnClicked != null) mOnClicked.onClick(position);
    });

    item.setOnClickListener((view) -> {
      if(getItemClickListener() != null) getItemClickListener().onClick(view, position);
    });
  }

  public void setOnActBtnClicked(OnActBtnClicked l) {
    this.mOnClicked = l;
  }

  public static interface OnActBtnClicked {
    public void onClick(int position);
  }

}
