package me.gyanendrokh.meiteimayek.dictionary.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class WordList extends RecyclerView {

  private OnBottomReached mOnBottomReached = null;

  public WordList(Context context) {
    super(context);
    addListener();
  }

  public WordList(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    setLayoutManager(getLayoutManager(context));
    addListener();
  }

  public WordList(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setLayoutManager(getLayoutManager(context));
    addListener();
  }

  private LinearLayoutManager getLayoutManager(Context context) {
    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
    layoutManager.setOrientation(VERTICAL);

    return layoutManager;
  }

  private void addListener() {
    final LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
    addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if(mOnBottomReached != null) {
          if(layoutManager.findLastVisibleItemPosition() == getAdapter().getItemCount() - 1) {
            mOnBottomReached.onBottomReached();
          }
        }
      }
    });
  }

  public void setOnBottomReachedListener(OnBottomReached onBottomReached) {
    this.mOnBottomReached = onBottomReached;
  }

  public static interface OnBottomReached {
    public void onBottomReached();
  }

}
