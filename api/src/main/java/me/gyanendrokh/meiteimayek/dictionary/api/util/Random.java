package me.gyanendrokh.meiteimayek.dictionary.api.util;

public class Random {

  public static String generateRandomString() {
    java.util.Random rand = new java.util.Random();
    String letters = getAllLetters();
    StringBuilder builder = new StringBuilder();

    for(int i = 0; i < 16; i++) {
      builder.append(letters.charAt(rand.nextInt(letters.length())));
    }

    return builder.toString();
  }

  private static String getAllLetters() {
    StringBuilder builder = new StringBuilder();

    for(int i = 'A'; i <= 'Z'; i++) builder.append((char)i);
    for(int i = 'a'; i <= 'z'; i++) builder.append((char)i);

    return builder.toString();
  }

}
