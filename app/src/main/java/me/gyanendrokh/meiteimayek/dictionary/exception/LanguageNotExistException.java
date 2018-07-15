package me.gyanendrokh.meiteimayek.dictionary.exception;

import java.util.Locale;

public class LanguageNotExistException extends Exception {

  public LanguageNotExistException(String lang) {
    super(String.format("(%s) Lang not exist.", lang));
  }

  public LanguageNotExistException(int code) {
    super(String.format(Locale.US, "(%d) Lang not exist.", code));
  }

}
