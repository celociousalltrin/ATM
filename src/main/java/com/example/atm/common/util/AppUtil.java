package com.example.atm.common.util;

import java.util.UUID;

public final class AppUtil {
  private AppUtil() {}

  public static String generateUUID() {
    return UUID.randomUUID().toString();
  }
}
