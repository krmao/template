import 'dart:convert';

class STBaseJsonUtil {
  static String jsonToString(Object value) => jsonEncode(value);

  static dynamic stringToJson(String source) => jsonDecode(source);
}
