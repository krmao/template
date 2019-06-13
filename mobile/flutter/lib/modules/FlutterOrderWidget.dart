import 'package:smart/settings/imports/flutter_imports_material.dart';

class FlutterOrderWidget extends StatelessWidget {
  final String message;

  FlutterOrderWidget({this.message});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
            title: Text('订单页面')
        ),
        body: CommonUtils.getColumn(context, null));
  }
}
