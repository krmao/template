import 'package:smart/settings/imports/flutter_imports_material.dart';

class FlutterSettingsWidget extends StatelessWidget {
  final Map params;

  FlutterSettingsWidget(this.params);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
            title: Text('设置页面')
        ),
        body: CommonUtils.getColumn(context, params));
  }
}