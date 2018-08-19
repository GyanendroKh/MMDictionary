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
import android.view.MenuItem;
import android.widget.Toast;

import com.lapism.searchview.Search;
import com.lapism.searchview.widget.SearchView;

import me.gyanendrokh.meiteimayek.dictionary.R;
import me.gyanendrokh.meiteimayek.dictionary.adapter.HomePagerAdapter;
import me.gyanendrokh.meiteimayek.dictionary.data.Language;

public class HomeActivity extends AppCompatActivity
  implements NavigationView.OnNavigationItemSelectedListener {

  private DrawerLayout mDrawer;
  private SearchView mSearchView;

  private long mLastBackPressed = 0;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    initView();
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

    this.mSearchView.setOnQueryTextListener(new Search.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(CharSequence query) {
        mSearchView.setQuery("", false);
        mSearchView.close();

        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        intent.putExtra(SearchActivity.KEYWORD, query.toString());
        intent.putExtra(SearchActivity.LANG, Language.ENGLISH);
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
    }

    return true;
  }
}
