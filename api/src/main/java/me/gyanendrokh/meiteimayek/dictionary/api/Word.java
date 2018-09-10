package me.gyanendrokh.meiteimayek.dictionary.api;

import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Word extends Request {

  private static Word mSelf = null;

  private String mLang = "";
  private int mId = 0;

  public static synchronized Word getInstance(Context context) {
    if(mSelf == null) mSelf = new Word(context);
    return mSelf;
  }

  private Word(@NonNull Context context) {
    super(context);
  }

  public void setData(int id, String lang) {
    this.mId = id;
    this.mLang = lang;
  }

  @Override
  protected String setEndPoint() {
    return "/g/w/:lang/:id"
      .replace(":lang", this.mLang)
      .replace(":id", String.valueOf(this.mId));
  }

  public void fetch(OnResponseListener<JSONObject> res, OnErrorListener err) {
    super.fetch(super.getRequest(response -> {
      try {
        res.onResponse(response.getJSONObject("data"));
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
