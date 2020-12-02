import '../../settings/imports/flutter_imports_material.dart';

class OrderWidget extends StatelessWidget {
  final String message;

  OrderWidget({this.message});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Colors.pink,
        appBar: AppBar(
            title: Text('ORDER')
        ),
        body: CommonUtils.getColumn(context, null));
  }
}
