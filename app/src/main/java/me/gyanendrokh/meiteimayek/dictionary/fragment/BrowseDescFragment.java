package me.gyanendrokh.meiteimayek.dictionary.fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import me.gyanendrokh.meiteimayek.dictionary.api.Request;
import me.gyanendrokh.meiteimayek.dictionary.api.Word;


public class BrowseDescFragment extends WordDescFragment {

  public static BrowseDescFragment createFragment(me.gyanendrokh.meiteimayek.dictionary.data.Word word) {
    Bundle bundle = new Bundle();
    bundle.putSerializable(WORD, word);

    BrowseDescFragment frag = new BrowseDescFragment();
    frag.setArguments(bundle);

    return frag;
  }

  @Override
  public void fetch(OnDataFetched fetch) {
    Word w = Word.getInstance(getActivity());
    w.setData(super.getData().getWord(), super.getData().getLang());


    w.fetch(new Request.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        try {
          String desc = response.getString("description");
          String readAs = response.getString("read_as");

          fetch.onFetched(getData().setDesc(desc).setReadAs(readAs));
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onError(VolleyError error) {
        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
        error.printStackTrace();
      }
    });
  }

}
