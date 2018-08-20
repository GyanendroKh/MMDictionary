package me.gyanendrokh.meiteimayek.dictionary.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "word")
public class Word {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  private int mId;

  @ColumnInfo(name = "word")
  private String mWord;

  @ColumnInfo(name = "lang")
  private String mLang;

  @ColumnInfo(name = "description")
  private String mDesc;

  @ColumnInfo(name = "read_as")
  private String mReadAs;

  public void setId(int mId) {
    this.mId = mId;
  }

  public void setWord(String word) {
    this.mWord = word;
  }

  public void setLang(String lang) {
    this.mLang = lang;
  }

  public void setDesc(String desc) {
    this.mDesc = desc;
  }

  public void setReadAs(String readAs) {
    this.mReadAs = readAs;
  }

  public int getId() {
    return this.mId;
  }

  public String getWord() {
    return this.mWord;
  }

  public String getLang() {
    return this.mLang;
  }

  public String getDesc() {
    return this.mDesc;
  }

  public String getReadAs() {
    return this.mReadAs;
  }

}
