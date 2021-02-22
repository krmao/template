import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_module_template/modules/common/common_util.dart';
import 'package:flutter_codesdancing/flutter_codesdancing.dart';

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
