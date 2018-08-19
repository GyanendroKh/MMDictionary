package me.gyanendrokh.meiteimayek.dictionary.api;

import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Search extends Request {

  private static Search mSelf = null;
  private Token mToken;

  private String mLang = "";
  private String mKeyword = "";

  public static synchronized Search getInstance(Context context) {
    if(mSelf == null) mSelf = new Search(context);
    return mSelf;
  }

  private Search(@NonNull Context context) {
    super(context);
    this.mToken = Token.getInstance(context);
  }

  public void setData(String keyword, String lang) {
    this.mKeyword = keyword;
    this.mLang = lang;
  }

  private String getEncodedData() {
    JSONObject data = new JSONObject();
    try {
      data.put("lang", this.mLang);
      data.put("keyword", this.mKeyword);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return Encoding.Base64.encode(data.toString());
  }

  @Override
  protected String setEndPoint() {
    return "/w/g/s/:data".replace(":data", getEncodedData());
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
