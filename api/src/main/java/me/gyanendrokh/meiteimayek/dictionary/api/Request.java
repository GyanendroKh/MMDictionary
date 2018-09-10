package me.gyanendrokh.meiteimayek.dictionary.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public abstract class Request {

  private static final String BASE = "https://api.dictionary.meiteimayek.gyanendrokh.me";

  private String mEndPoint = "";

  private RequestQueue mQueue;

  protected Request(@NonNull Context context) {
    this.mQueue = Volley.newRequestQueue(context.getApplicationContext());
  }

  abstract protected String setEndPoint();

  public String getFullUrl() {
    return BASE + (this.mEndPoint = setEndPoint());
  }

  public JsonObjectRequest getRequest(Response.Listener<org.json.JSONObject> listener, Response.ErrorListener errorListener) {
    return new JsonObjectRequest(com.android.volley.Request.Method.GET, getFullUrl(), null, listener, errorListener);
  }

  protected <T> void fetch(com.android.volley.Request<T> req) {
    this.mQueue.add(req);
  }

  public String getBaseUrl() {
    return BASE;
  }

  public String getEndPoint() {
    return this.mEndPoint;
  }

  public static interface OnResponseListener<E> {
    void onResponse(E response);
  }

  public static interface OnErrorListener {
    void onError(Exception error);
  }

}
