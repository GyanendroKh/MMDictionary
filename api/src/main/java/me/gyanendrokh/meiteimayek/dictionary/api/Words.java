package me.gyanendrokh.meiteimayek.dictionary.api;

import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Words extends Request {

  private static Words mSelf = null;
  private Token mToken;

  private String mLang = "";
  private int mStart = 1;
  private int mLimit = 20;


  public static synchronized Words getInstance(Context context) {
    if(mSelf == null) mSelf = new Words(context);
    return mSelf;
  }

  private Words(@NonNull Context context) {
    super(context);
    this.mToken = Token.getInstance(context);
  }

  public void setData(String lang, int start, int limit) {
    setData(lang);
    this.mStart = start;
    this.mLimit = limit;
  }

  public void setData(String lang) {
    this.mLang = lang;
  }

  private String getEncodedData() {
    JSONObject data = new JSONObject();
    try {
      data.put("lang", this.mLang);
      data.put("start", this.mStart);
      data.put("limit", this.mLimit);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return Encoding.Base64.encode(data.toString());
  }

  @Override
  protected String setEndPoint() {
    return "/w/g-a/:data".replace(":data", getEncodedData());
  }

  public void fetch(Listener<JSONArray> listener) {
    addAuthHeaders(mToken);
    JSONArrayRequest req = super.getJsonArrayReq(
      listener::onResponse,
      listener::onError
    );

    super.fetch(req);
  }

}
