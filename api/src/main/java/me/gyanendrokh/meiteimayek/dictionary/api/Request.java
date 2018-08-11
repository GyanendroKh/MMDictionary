package me.gyanendrokh.meiteimayek.dictionary.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public abstract class Request {

  private static final String BASE = "https://api.dictionary.meiteimayek.gyanendrokh.me";
  private static final String PREF_FILE = "me.gyanendrokh.meiteimayek.dictionary.api";

  private int mMethod = com.android.volley.Request.Method.GET;
  private String mEndPoint = "";

  private Map<String, String> mHeaders = new HashMap<>();

  private RequestQueue mQueue;
  private SharedPreferences mPref;

  protected Request(@NonNull Context context) {
    this.mQueue = Volley.newRequestQueue(context.getApplicationContext());
    this.mPref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
  }

  public Request setMethod(int method) {
    this.mMethod = method;
    return this;
  }

  abstract protected String setEndPoint();

  public String getFullUrl() {
    return BASE + (this.mEndPoint = setEndPoint());
  }

  public int getMethod() {
    return this.mMethod;
  }

  private void addHeader(String key, String value) {
    this.mHeaders.put(key, value);
  }

  public void addAuthHeaders(Token token) {
    addHeader("Authorization", "Bearer " + token.getTokenFromPref());
    addHeader("t", token.getTokenKeyFromPref());
  }

  public JSONObjectRequest getJsonObjReq(Response.Listener<org.json.JSONObject> listener, Response.ErrorListener errorListener) {
    return new JSONObjectRequest(getMethod(), getFullUrl(), null, listener, errorListener) {
      @Override
      public Map<String, String> getHeaders() {
        return mHeaders;
      }
    };
  }

  public JSONArrayRequest getJsonArrayReq(Response.Listener<org.json.JSONArray> listener, Response.ErrorListener errorListener) {
    return new JSONArrayRequest(getMethod(), getFullUrl(), null, listener, errorListener) {
      @Override
      public Map<String, String> getHeaders() {
        return mHeaders;
      }
    };
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

  public Map<String, String> getHeaders() {
    return this.mHeaders;
  }

  protected SharedPreferences getSharedPref() {
    return this.mPref;
  }

  public static class JSONObjectRequest extends JsonObjectRequest {

    public JSONObjectRequest(int method, String url, org.json.JSONObject jsonRequest, Response.Listener<org.json.JSONObject> listener, Response.ErrorListener errorListener) {
      super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<org.json.JSONObject> parseNetworkResponse(NetworkResponse response) {
      if(response.statusCode == 400) {
        try {
          org.json.JSONObject jsonString = new org.json.JSONObject(new String(response.data,
            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)));
          Log.e("RequestError", jsonString.getString("error"));
          return Response.error(new VolleyError(jsonString.getString("error")));
        } catch (UnsupportedEncodingException | JSONException e) {
          return Response.error(new ParseError(e));
        }
      }
      return super.parseNetworkResponse(response);
    }
  }

  public static class JSONArrayRequest extends JsonArrayRequest {

    public JSONArrayRequest(int method, String url, org.json.JSONArray jsonRequest, Response.Listener<org.json.JSONArray> listener, Response.ErrorListener errorListener) {
      super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
      if(response.statusCode == 400) {
        try {
          JSONObject jsonString = new JSONObject(new String(response.data,
            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)));
          Log.e("RequestError", jsonString.getString("error"));
          return Response.error(new VolleyError(jsonString.getString("error")));
        } catch (UnsupportedEncodingException | JSONException e) {
          return Response.error(new ParseError(e));
        }
      }
      return super.parseNetworkResponse(response);
    }

  }

  public static interface Listener<E> {

    void onResponse(E response);

    void onError(VolleyError error);

  }

}
