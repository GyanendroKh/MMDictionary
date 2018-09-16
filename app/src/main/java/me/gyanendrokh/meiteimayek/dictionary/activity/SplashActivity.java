package me.gyanendrokh.meiteimayek.dictionary.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;

import me.gyanendrokh.meiteimayek.dictionary.R;

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    new Handler().postDelayed(this::startHome, 800);
    MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.banner_ad_unit_id));
  }

  private void startHome() {
    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
    finish();
  }

}
