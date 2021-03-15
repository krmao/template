import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import '../../modules/common/common_util.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

class SettingsState extends BaseStateDefault {
  final Map params;

  SettingsState(this.params);

  @override
  Widget buildBase(BuildContext context) {
    statusBarColor = Colors.orange;
    return super.buildBase(context);
  }

  @override
  Widget buildBaseChild(BuildContext context) {
    print("SettingsWidget build");
    return CommonUtils.getColumn(context, params);
  }
}
