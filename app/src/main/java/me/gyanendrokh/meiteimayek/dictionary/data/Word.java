package me.gyanendrokh.meiteimayek.dictionary.data;

public class Word {

  private int mWordId;
  private String mWord;
  private String mLang;
  private String mDesc = "";
  private String mReadAs = "";

  public Word(int wordId, String word, String lang) {
    this.mWordId = wordId;
    this.mWord = word;
    this.mLang = lang;
  }

  public void setDesc(String desc) {
    this.mDesc = desc;
  }

  public void setReadAs(String readAs) {
    this.mReadAs = readAs;
  }

  public int getWordId() {
    return this.mWordId;
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
