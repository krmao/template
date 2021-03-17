import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

// ignore: must_be_immutable
abstract class BasePageStateless extends StatelessWidget {
  final String argumentsJsonString;

  const BasePageStateless({Key key, this.argumentsJsonString})
      : super(key: key);
}
