import '../../settings/imports/flutter_imports_material.dart';

class SettingsWidget extends StatelessWidget {
  final Map params;

  SettingsWidget(this.params);

  @override
  Widget build(BuildContext context) {
    print("SettingsWidget build");
    return Scaffold(
        backgroundColor: Colors.orange,
        appBar: AppBar(
            title: Text('SETTINGS')
        ),
        body: CommonUtils.getColumn(context, params));
  }
}