package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.gyanendrokh.meiteimayek.dictionary.R;

public class FavoriteFragment extends Fragment {

  private RecyclerView mListView;
  private ProgressBar mProgressBar;
  private TextView mTextNone;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_favorite, container, false);

    mListView = view.findViewById(R.id.favorite_list);
    mProgressBar = view.findViewById(R.id.favorite_progress);
    mTextNone = view.findViewById(R.id.favorite_none);

    mListView.setVisibility(View.GONE);
    mProgressBar.setVisibility(View.GONE);

    return view;
  }
}
