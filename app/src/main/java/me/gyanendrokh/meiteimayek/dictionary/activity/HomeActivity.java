package me.gyanendrokh.meiteimayek.dictionary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lapism.searchview.Search;
import com.lapism.searchview.widget.SearchView;

import org.json.JSONException;

import me.gyanendrokh.meiteimayek.dictionary.BuildConfig;
import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.adapter.HomePagerAdapter;
import me.gyanendrokh.meiteimayek.dictionary.api.Version;
import me.gyanendrokh.meiteimayek.dictionary.data.Language;
import me.gyanendrokh.meiteimayek.dictionary.fragment.VersionUpdateFragment;

public class HomeActivity extends AppCompatActivity
  implements NavigationView.OnNavigationItemSelectedListener {

  private DrawerLayout mDrawer;
  private SearchView mSearchView;

  private long mLastBackPressed = 0;
  private String mSearchLang = Language.ENGLISH;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    initView();
    checkVersion();
  }

  private void initView() {
    this.mDrawer = findViewById(R.id.drawer_layout);

    initSearchView();
    initPager();

    ((NavigationView)findViewById(R.id.nav_view))
      .setNavigationItemSelectedListener(this);
  }

  private void initSearchView() {
    this.mSearchView = findViewById(R.id.search_view);
    this.mSearchView.setOnLogoClickListener(() -> mDrawer.openDrawer(GravityCompat.START));
    Menu m = this.mSearchView.setMenu(R.menu.search_options);
    this.mSearchView.setOnMenuItemClickListener(item -> {
      if(!item.isChecked()) item.setChecked(true);
      for(int i = 0; i < 3; i++) {
        if(item != m.getItem(i)) {
          if(m.getItem(i).isChecked()) m.getItem(i).setChecked(false);
        }
      }

      switch(item.getItemId()) {
        case R.id.search_eng:
          mSearchLang = Language.ENGLISH;
          break;
        case R.id.search_mani:
          mSearchLang = Language.MEITEI_MAYEK;
          break;
        case R.id.search_beng:
          mSearchLang = Language.BENGALI;
          break;
      }

      return true;
    });

    this.mSearchView.setOnQueryTextListener(new Search.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(CharSequence query) {
        mSearchView.setQuery("", false);
        mSearchView.close();

        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        intent.putExtra(SearchActivity.KEYWORD, query.toString());
        intent.putExtra(SearchActivity.LANG, mSearchLang);
        startActivity(intent);

        return true;
      }

      @Override
      public void onQueryTextChange(CharSequence newText) {

      }
    });
  }

  private void initPager() {
    ViewPager viewPager = findViewById(R.id.home_view_pager);
    TabLayout tabLayout = findViewById(R.id.home_tab_layout);

    viewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
  }

  @Override
  public void onBackPressed() {
    if (mDrawer.isDrawerOpen(GravityCompat.START))
      mDrawer.closeDrawer(GravityCompat.START);
    else {
      if(mLastBackPressed == 0) {
        mLastBackPressed = System.currentTimeMillis();
        Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
      }else {
        if((System.currentTimeMillis() - mLastBackPressed) < 3000) {
          super.onBackPressed();
        }else {
          mLastBackPressed = 0;
          onBackPressed();
        }
      }
    }
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    mDrawer.closeDrawer(GravityCompat.START);

    switch(item.getItemId()) {
      case R.id.nav_about:
        startActivity(new Intent(getApplicationContext(), AboutMeActivity.class));
        return false;
      case R.id.nav_share:
        share();
        return false;
    }

    return true;
  }

  public void checkVersion() {
    Version ver = Version.getInstance(HomeActivity.this);

    ver.fetch(res -> {
      try {
        int versionCode = res.getInt("code");

        if(versionCode > BuildConfig.VERSION_CODE) {
          VersionUpdateFragment.createFragment().show(getSupportFragmentManager(), VersionUpdateFragment.class.getSimpleName());
        }

      }catch(JSONException e) {
        e.printStackTrace();
      }
    }, err -> Toast.makeText(HomeActivity.this, err.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
  }

  private void share() {
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_TEXT, "https://meiteimayek.gyanendrokh.me/dictionary");
    intent.putExtra(Intent.EXTRA_TITLE, "Check this out");

    startActivity(Intent.createChooser(intent, "Share"));
  }
}
