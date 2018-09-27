package me.gyanendrokh.meiteimayek.dictionary.api;

import android.content.Context;
import android.support.annotation.NonNull;

import org.json.JSONObject;

public class Version extends Request {

  private static Version mSelf = null;

  public static synchronized Version getInstance(Context context) {
    if(mSelf == null) mSelf = new Version(context);
    return mSelf;
  }

  private Version(@NonNull Context context) {
    super(context);
  }

  @Override
  protected String setEndPoint() {
    return "/g/a-a/v";
  }

  public void fetch(OnResponseListener<JSONObject> res, OnErrorListener err) {
    super.fetch(super.getRequest(res::onResponse, err::onError));
  }

}
