package me.gyanendrokh.meiteimayek.dictionary.api;

public class Encoding {

  public static class Base64 {

    public static String encode(String data) {
      return android.util.Base64.encodeToString(data.getBytes(), android.util.Base64.DEFAULT);
    }

    public static String decode(String encoded) {
      StringBuilder decoded = new StringBuilder();

      for (Byte b : android.util.Base64.decode(encoded, android.util.Base64.DEFAULT)) {
        decoded.append(b.toString());
      }

      return decoded.toString();
    }

  }

}
