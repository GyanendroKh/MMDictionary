package me.gyanendrokh.meiteimayek.dictionary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import me.gyanendrokh.meiteimayek.dictionary.fragment.BrowseFragment;
import me.gyanendrokh.meiteimayek.dictionary.fragment.FavoriteFragment;

public class HomePagerAdapter extends FragmentPagerAdapter{

  public HomePagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {
    return Factory.FRAGMENTS[position];
  }

  @Override
  public int getCount() {
    return Factory.FRAGMENTS.length;
  }

  private static class Factory {

    private static Fragment FRAGMENTS[] = {
      new FavoriteFragment(),
      new BrowseFragment()
    };

  }

}
