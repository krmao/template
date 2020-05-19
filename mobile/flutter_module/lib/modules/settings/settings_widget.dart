import 'package:flutter_module/settings/imports/flutter_imports_material.dart';

class SettingsWidget extends StatelessWidget {
  final Map params;

  SettingsWidget(this.params);

  @override
  Widget build(BuildContext context) {
    print("SettingsWidget build");
    return Scaffold(
        backgroundColor: Colors.deepOrange,
        appBar: AppBar(title: Text('SETTINGS')),
        body: Container(
            padding: const EdgeInsets.all(8.0),
            margin: const EdgeInsets.all(8.0),
            color: Colors.yellow,
            child: Text(
              'SETTINGS',
              style: TextStyle(fontSize: 22.0, color: Colors.black),
            )));
  }
}
