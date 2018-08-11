package me.gyanendrokh.meiteimayek.dictionary.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.data.Word;
import me.gyanendrokh.meiteimayek.dictionary.ui.WordListItem;

public class BrowseAdapter extends WordListAdapter {

  private WordListItem mItem;

  public BrowseAdapter(List<Word> words) {
    super(words);
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    if(viewHolder instanceof WordListItem.Loading) {
      WordListItem.Loading holder = (WordListItem.Loading) viewHolder;
      holder.vProgressBar.setIndeterminate(true);
      return;
    }

    this.mItem = (WordListItem)viewHolder;

    this.mItem.setBtnImg(R.drawable.ic_add);
    this.mItem.setPrimaryText(super.getItem(position).getWord());
    this.mItem.setSecondaryText("");
  }

}
