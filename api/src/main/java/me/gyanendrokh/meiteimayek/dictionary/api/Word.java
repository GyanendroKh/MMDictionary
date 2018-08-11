package me.gyanendrokh.meiteimayek.dictionary.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;

import org.json.JSONException;
import org.json.JSONObject;

public class Word extends Request {

  private static Word mSelf = null;
  private Token mToken;

  private String mLang = "";
  private String mWord = "";

  public static synchronized Word getInstance(Context context) {
    if(mSelf == null) mSelf = new Word(context);
    return mSelf;
  }

  private Word(@NonNull Context context) {
    super(context);
    this.mToken = Token.getInstance(context);
  }

  public void setData(String word, String lang) {
    this.mWord = word;
    this.mLang = lang;
  }

  private String getEncodedData() {
    JSONObject data = new JSONObject();
    try {
      data.put("lang", this.mLang);
      data.put("word", this.mWord);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return Encoding.Base64.encode(data.toString());
  }

  @Override
  protected String setEndPoint() {
    return "/w/g/:data".replace(":data", getEncodedData());
  }

  public void fetch(Listener<org.json.JSONObject> listener) {
    addAuthHeaders(mToken);
    JSONObjectRequest req = super.getJsonObjReq(
      listener::onResponse,
      listener::onError
    );

    super.fetch(req);
  }

}
