package me.gyanendrokh.meiteimayek.dictionary.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import me.gyanendrokh.meiteimayek.dictionary.api.util.Random;

public class Token extends Request {

  private String mPrefToken = "api_token";
  private String mPrefTokenKey = "api_token_key";

  private static Token mSelf = null;

  private String mKey;

  public synchronized static Token getInstance(Context context) {
    if(mSelf == null) mSelf = new Token(context);
    return mSelf;
  }

  private Token(@NonNull Context context) {
    super(context);
    this.mKey = Random.generateRandomString();
  }

  @Override
  protected String setEndPoint() {
    return "/t/g/:token".replace(":token", this.mKey);
  }

  public void fetch(final Listener<org.json.JSONObject> listener) {
    JsonObjectRequest request = super.getJsonObjReq((response) -> {
      try {
        String t = response.getString("token");
        storeTokenInPref(t, mKey);
        Log.v("Token", "Token : " + t);
        Log.v("Token", "TokenKey : " + mKey);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      listener.onResponse(response);
    }, listener::onError);
    super.fetch(request);
  }

  private void storeTokenInPref(String token, String tokenKey) {
    super.getSharedPref().edit().putString(mPrefToken, token).apply();
    super.getSharedPref().edit().putString(mPrefTokenKey, tokenKey).apply();
  }

  public String getTokenFromPref() {
    return super.getSharedPref().getString(mPrefToken, "");
  }

  public String getTokenKeyFromPref() {
    return super.getSharedPref().getString(mPrefTokenKey, "");
  }

}
