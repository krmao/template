import '../../settings/imports/flutter_imports_material.dart';

class OrderWidget extends StatelessWidget {
  final String message;

  OrderWidget({this.message});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Colors.orange,
        appBar: AppBar(
            title: Text('订单页面')
        ),
        body: CommonUtils.getColumn(context, null));
  }
}
