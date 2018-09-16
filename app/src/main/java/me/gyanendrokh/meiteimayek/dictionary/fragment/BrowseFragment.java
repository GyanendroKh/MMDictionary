package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.activity.BrowseActivity;
import me.gyanendrokh.meiteimayek.dictionary.data.Language;

public class BrowseFragment extends Fragment implements View.OnClickListener {

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_browse, container, false);

    view.findViewById(R.id.browse_english).setOnClickListener(this);
    view.findViewById(R.id.browse_mani).setOnClickListener(this);
    view.findViewById(R.id.browse_bengali).setOnClickListener(this);
    ((AdView) view.findViewById(R.id.adView)).loadAd(new AdRequest.Builder().build());

    return view;
  }


  @Override
  public void onClick(View view) {
    Intent intent = new Intent(getActivity(), BrowseActivity.class);
    switch (view.getId()) {
      case R.id.browse_english:
        intent.putExtra("lang", Language.ENGLISH);
        break;
      case R.id.browse_bengali:
        intent.putExtra("lang", Language.BENGALI);
        break;
      case R.id.browse_mani:
        intent.putExtra("lang", Language.MEITEI_MAYEK);
        break;
    }
    startActivity(intent);
  }

}
