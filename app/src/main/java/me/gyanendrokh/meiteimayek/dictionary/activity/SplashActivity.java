package me.gyanendrokh.meiteimayek.dictionary.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.api.Request;
import me.gyanendrokh.meiteimayek.dictionary.api.Token;

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    new Handler().post(
      () -> {
        final Token token = Token.getInstance(SplashActivity.this);
        token.fetch(new Request.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            startHome();
          }

          @Override
          public void onError(VolleyError error) {
            Toast.makeText(SplashActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            startHome();
          }
        });
      }
    );
  }

  private void startHome() {
    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
    finish();
  }

}
