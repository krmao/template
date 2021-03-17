import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

import '../../modules/common/common_util.dart';

// ignore: must_be_immutable
class OrderWidget extends BasePageStateless {
  final String message;

  OrderWidget({this.message, String argumentsJsonString})
      : super(argumentsJsonString: argumentsJsonString){
    print(
        "[page] ---- OrderWidget constructor argumentsJsonString=$argumentsJsonString");
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Colors.pink,
        appBar: AppBar(title: Text('ORDER')),
        body: CommonUtils.getColumn(context, null));
  }
}
