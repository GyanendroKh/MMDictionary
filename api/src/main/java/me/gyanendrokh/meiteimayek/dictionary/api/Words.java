package me.gyanendrokh.meiteimayek.dictionary.api;

import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Words extends Request {

  private static Words mSelf = null;

  private String mLang = "";
  private int mPag = 1;


  public static synchronized Words getInstance(Context context) {
    if(mSelf == null) mSelf = new Words(context);
    return mSelf;
  }

  private Words(@NonNull Context context) {
    super(context);
  }

  public void setData(String lang) {
    this.mLang = lang;
  }

  public void setData(String lang, int pag) {
    setData(lang);
    this.mPag = pag;
  }

  @Override
  protected String setEndPoint() {
    return "/g/w-a/:lang?pag=:pag"
      .replace(":lang", this.mLang)
      .replace(":pag", String.valueOf(this.mPag));
  }

  public void fetch(OnResponseListener<JSONArray> res, OnErrorListener err) {
    super.fetch(super.getRequest(response -> {
      try {
        res.onResponse(response.getJSONArray("data"));
      }catch (JSONException e) {
        try {
          err.onError(new Exception(response.getString("error")));
        } catch (JSONException e1) {
          err.onError(e1);
        }
      }
    }, err::onError));
  }

}
