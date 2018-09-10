package me.gyanendrokh.meiteimayek.dictionary.api;

import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

public class Search extends Request {

  private static Search mSelf = null;

  private String mLang = "";
  private String mKeyword = "";
  private int mPag = 1;

  public static synchronized Search getInstance(Context context) {
    if(mSelf == null) mSelf = new Search(context);
    return mSelf;
  }

  private Search(@NonNull Context context) {
    super(context);
  }

  public void setData(String lang, String keyword) {
    this.mKeyword = keyword;
    this.mLang = lang;
  }

  public void setData(String lang, String keyword, int pag) {
    setData(lang, keyword);
    this.mPag = pag;
  }

  @Override
  protected String setEndPoint() {
    return "/g/s/:lang/:keyword?pag=:pag"
      .replace(":lang", this.mLang)
      .replace(":keyword", this.mKeyword)
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
