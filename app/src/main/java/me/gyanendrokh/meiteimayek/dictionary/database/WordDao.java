package me.gyanendrokh.meiteimayek.dictionary.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface WordDao {

  @Query("SELECT * FROM word")
  Flowable<List<Word>> getAll();

  @Query("DELETE FROM word WHERE id=:id")
  void deleteWord(int id);

  @Insert()
  void addWord(Word word);

}
