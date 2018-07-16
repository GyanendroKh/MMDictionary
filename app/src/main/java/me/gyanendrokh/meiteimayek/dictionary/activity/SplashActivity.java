package me.gyanendrokh.meiteimayek.dictionary.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.gyanendrokh.meiteimayek.dictionary.R;

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    new Handler().postDelayed(
      () -> {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
      },
      1500
    );
  }

}
