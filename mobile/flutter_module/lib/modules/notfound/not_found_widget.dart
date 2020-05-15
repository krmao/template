import 'package:flutter_module/settings/imports/flutter_imports_material.dart';

class NotFoundWidget extends StatelessWidget {
  final String route;

  NotFoundWidget({Key key, this.route = ""}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    print("SettingsWidget build");
    return Scaffold(
      backgroundColor: Colors.white70,
      appBar: AppBar(title: Text('NOT FOUND')),
      body: Container(
          padding: const EdgeInsets.all(8.0),
          margin: const EdgeInsets.all(8.0),
          child: Text(
            '未找到指定页面\n${this.route}',
            style: TextStyle(fontSize: 22.0, color: Colors.black),
          )),
      //onTap: () => Navigator.push(context, MaterialPageRoute(builder: (_) => STBaseStatefulWidget(state: MainTabWidgetState(),)))
    );
  }
}
