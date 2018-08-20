package me.gyanendrokh.meiteimayek.dictionary.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = Word.class, version = 1)
public abstract class FavoriteDatabase extends RoomDatabase {

  private static volatile FavoriteDatabase mSelf = null;

  public abstract WordDao getDao();

  public static FavoriteDatabase getInstance(Context context) {
    if(mSelf == null) {
      mSelf = Room.databaseBuilder(context.getApplicationContext(), FavoriteDatabase.class,
        FavoriteDatabase.class.getSimpleName()).build();
    }

    return mSelf;
  }

}
