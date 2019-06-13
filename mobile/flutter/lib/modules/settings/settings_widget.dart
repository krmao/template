import 'package:smart/settings/imports/flutter_imports_material.dart';

class SettingsWidget extends StatelessWidget {
  final Map params;

  SettingsWidget(this.params);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Colors.deepOrange,
        appBar: AppBar(
            title: Text('设置页面')
        ),
        body: CommonUtils.getColumn(context, params));
  }
}