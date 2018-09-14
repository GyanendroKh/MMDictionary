package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.data.Word;

public abstract class WordDescFragment extends BottomSheetDialogFragment {

  public static final String WORD = "bundle_word";

  private Word mWord;
  private TextView mViewWord;
  private TextView mViewLang;
  private TextView mViewDesc;
  private TextView mViewReadAs;
  private ProgressBar mProgressBar;
  private FloatingActionButton mActBtn;
  private AdView mAdView;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle b = getArguments();

    if(b != null && b.getSerializable(WORD) != null) {
      this.mWord = (Word) b.getSerializable(WORD);
    }else {
      throw new IllegalArgumentException("No Valid Argument passed in WordDescFragment.");
    }

  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_bottom_sheet_desc, container, false);
    this.mViewWord = view.findViewById(R.id.word_desc_word);
    this.mViewLang = view.findViewById(R.id.word_desc_lang);
    this.mViewDesc = view.findViewById(R.id.word_desc_desc);
    this.mViewReadAs = view.findViewById(R.id.word_desc_read_as);
    this.mProgressBar = view.findViewById(R.id.word_desc_progress);
    this.mActBtn = view.findViewById(R.id.sheet_action_btn);
    this.mAdView = view.findViewById(R.id.adView);

    setData();
    init();
    initAd();
    return view;
  }

  private void setData() {
    this.mProgressBar.setIndeterminate(true);
    this.mViewWord.setText(this.mWord.getWord());
    this.mViewLang.setText(this.mWord.getLang());

    this.fetch(
      word -> {
        this.mWord = word;

        setLoaded();
        this.mViewDesc.setText(this.mWord.getDesc());
        this.mViewReadAs.setText(this.mWord.getReadAs());
      }
    );
  }

  public abstract void init();

  private void initAd() {
    AdRequest adReq = new AdRequest.Builder().build();

    this.mAdView.loadAd(adReq);
  }

  public void setLoaded() {
    this.mProgressBar.setVisibility(View.GONE);
  }

  public void setBtnAct(View.OnClickListener l) {
    this.mActBtn.setOnClickListener(l);
  }

  public void setActBtnIcon(@DrawableRes int resId) {
    this.mActBtn.setImageResource(resId);
  }

  public Word getData() {
    return this.mWord;
  }

  public abstract void fetch(OnDataFetched fetch);

  public interface OnDataFetched {
    void onFetched(Word word);
  }

}
