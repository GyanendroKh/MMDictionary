package me.gyanendrokh.meiteimayek.dictionary.ui;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.gyanendrokh.meiteimayek.dictionary.R;

public class WordListItem extends RecyclerView.ViewHolder {

  private Context mContext;
  private View mItemView;
  private TextView mTextPrimary;
  private TextView mTextSecondary;
  private AppCompatImageButton mImgBtn;

  public WordListItem(Context context, View itemView) {
    super(itemView);

    this.mContext = context;
    this.mItemView = itemView;
    this.mTextPrimary = itemView.findViewById(R.id.text_primary);
    this.mTextSecondary = itemView.findViewById(R.id.text_secondary);
    this.mImgBtn = itemView.findViewById(R.id.img_btn);
  }

  public void setPrimaryText(String text) {
    this.mTextPrimary.setText(text);
  }

  public void setSecondaryText(String text) {
    this.mTextSecondary.setText(text);
  }

  public void setBtnImg(@DrawableRes int resId) {
    this.mImgBtn.setImageDrawable(this.mContext.getDrawable(resId));
  }

  public void setOnBtnClicked(View.OnClickListener onClickListener) {
    this.mImgBtn.setOnClickListener(onClickListener);
  }

  public void setOnClickListener(View.OnClickListener onClickListener) {
    this.mItemView.setOnClickListener(onClickListener);
  }

  public static class Loading extends RecyclerView.ViewHolder {

    public ProgressBar vProgressBar;

    public Loading(View item) {
      super(item);
      this.vProgressBar = item.findViewById(R.id.progressBar);
    }

  }

}
