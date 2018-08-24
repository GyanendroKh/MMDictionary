package me.gyanendrokh.meiteimayek.dictionary.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.data.Word;
import me.gyanendrokh.meiteimayek.dictionary.ui.WordListItem;

public abstract class WordListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final int LOADING_ITEM = 1;

  private List<Word> mWords;
  private OnItemClickListener mOnClickListener = null;
  private OnActBtnClickListener mOnActBtnClickListener = null;

  WordListAdapter(List<Word> words) {
    this.mWords = words;
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
    if(viewHolder instanceof WordListItem.Loading) {
      WordListItem.Loading holder = (WordListItem.Loading) viewHolder;
      holder.vProgressBar.setIndeterminate(true);
      return;
    }

    WordListItem item = (WordListItem)viewHolder;

    item.setBtnImg(getBtnImg());
    item.setPrimaryText(getPrimaryText(position));
    item.setSecondaryText(getSecondaryText(position));

    item.setOnClickListener((view) -> {
      if(getItemClickListener() != null) getItemClickListener().onClick(view, position);
    });

    item.setOnBtnClicked((view) -> {
      if(getActBtnClickListener() != null) getActBtnClickListener().onClick(position);
    });
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

  public interface OnItemClickListener {
    void onClick(View view, int position);
  }

  public interface OnActBtnClickListener {
    void onClick(int position);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.mOnClickListener = listener;
  }

  public void setOnActBtnClicked(OnActBtnClickListener l) {
    this.mOnActBtnClickListener = l;
  }

  abstract int getBtnImg();

  abstract String getPrimaryText(int position);

  abstract String getSecondaryText(int position);

  OnItemClickListener getItemClickListener() {
    return this.mOnClickListener;
  }

  OnActBtnClickListener getActBtnClickListener() {
    return this.mOnActBtnClickListener;
  }

}
