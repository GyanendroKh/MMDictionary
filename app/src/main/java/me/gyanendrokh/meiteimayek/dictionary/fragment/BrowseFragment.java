package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.gyanendrokh.meiteimayek.dictionary.R;

public class BrowseFragment extends Fragment implements View.OnClickListener {

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_browse, container, false);

    view.findViewById(R.id.browse_english).setOnClickListener(this);
    view.findViewById(R.id.browse_mani).setOnClickListener(this);
    view.findViewById(R.id.browse_bengali).setOnClickListener(this);

    return view;
  }


  @Override
  public void onClick(View view) {

  }

}
