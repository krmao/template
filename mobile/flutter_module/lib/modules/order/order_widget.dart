import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_module/modules/common/common_util.dart';

class OrderWidget extends StatelessWidget {
  final String message;

  OrderWidget({this.message});

  @override
  Widget build(BuildContext context) {
    return Scaffold(backgroundColor: Colors.pink, appBar: AppBar(title: Text('ORDER')), body: CommonUtils.getColumn(context, null));
  }
}
