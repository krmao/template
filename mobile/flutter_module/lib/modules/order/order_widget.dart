import 'package:flutter_module/settings/imports/flutter_imports_material.dart';

class OrderWidget extends StatelessWidget {
  final String message;

  OrderWidget({this.message});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Colors.orange,
        appBar: AppBar(title: Text('ORDER')),
        body: Container(
            padding: const EdgeInsets.all(8.0),
            margin: const EdgeInsets.all(8.0),
            color: Colors.yellow,
            child: Text(
              'ORDER',
              style: TextStyle(fontSize: 22.0, color: Colors.black),
            )));
  }
}
