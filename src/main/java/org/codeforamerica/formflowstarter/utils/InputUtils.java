package org.codeforamerica.formflowstarter.utils;

import java.util.ArrayList;

public class InputUtils {
  public static boolean arrayOrStringContains(Object value, String target) {
    if (value instanceof String) {
      return value.equals(target);
    }

    if (value instanceof ArrayList) {
      return ((ArrayList<?>) value).contains(target);
    }

    return false;
  }
}
