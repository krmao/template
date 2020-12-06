import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_module/libraries/codesdancing_bridge/codesdancing_bridge.dart';
import 'package:flutter_module/modules/common/common_util.dart';

class SettingsState extends PageState {
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
