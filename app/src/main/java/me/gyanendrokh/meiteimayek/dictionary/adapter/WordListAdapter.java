package me.gyanendrokh.meiteimayek.dictionary.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.data.Word;
import me.gyanendrokh.meiteimayek.dictionary.ui.WordListItem;

public abstract class WordListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final int LOADING_ITEM = 1;

  private List<Word> mWords;

  public WordListAdapter(List<Word> words) {
    this.mWords = words;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if(viewType == LOADING_ITEM) {
      return new WordListItem.Loading(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.loading_item, parent, false));
    }

    return new WordListItem(parent.getContext(), LayoutInflater.from(parent.getContext())
      .inflate(R.layout.word_list_item, parent, false));
  }

  @Override
  public int getItemViewType(int position) {
    return this.mWords.get(position) == null ? LOADING_ITEM : 0;
  }

  @Override
  public int getItemCount() {
    return mWords.size();
  }

  public Word getItem(int index) {
    return mWords.get(index);
  }

}
