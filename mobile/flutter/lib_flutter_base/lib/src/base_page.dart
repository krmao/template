// ignore: must_be_immutable
import 'package:flutter/material.dart';

// ignore: must_be_immutable
abstract class BasePage extends StatefulWidget {
  final String argumentsJsonString;

  const BasePage({Key key, this.argumentsJsonString}) : super(key: key);
}
