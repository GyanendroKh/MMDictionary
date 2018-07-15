package me.gyanendrokh.meiteimayek.dictionary.data;

import me.gyanendrokh.meiteimayek.dictionary.exception.LanguageNotExistException;

public class Language {

  public static final String MEITEI_MAYEK = "mani";
  public static final String ENGLISH = "eng";
  public static final String BENGALI = "beng";

  private static final String[] mLangs = new String[] {
    "mani",
    "eng",
    "beng"
  };

  public static String getLangs(int code) throws LanguageNotExistException {
    try {
      return mLangs[code];
    }catch(ArrayIndexOutOfBoundsException e) {
      throw new LanguageNotExistException(code);
    }
  }

  public static class Code {
    public static final int MEITEI_MAYEK = 0;
    public static final int ENGLISH = 1;
    public static final int BENGALI = 2;

    public static int get(String lang) throws LanguageNotExistException {
      switch (lang) {
        case Language.MEITEI_MAYEK:
          return MEITEI_MAYEK;
        case Language.ENGLISH:
          return ENGLISH;
        case Language.BENGALI:
          return BENGALI;
        default:
          throw new LanguageNotExistException(lang);
      }
    }

  }

  public static String[] getLanguages() {
    return new String[] {
      "Meitei Mayek",
      "English",
      "Bengali"
    };
  }

}
