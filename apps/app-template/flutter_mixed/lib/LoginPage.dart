import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'CommonWidgetManager.dart';

class LoginPage extends StatefulWidget {
  @override
  createState() => new LoginPageState();
}

class LoginPageState extends State<LoginPage> {
  @override
  void initState() => super.initState();

  bool _isShow = false;

  void _showLoading() => setState(() => _isShow = true);

  void _hideLoading() => setState(() => _isShow = false);

  BuildContext _scaffoldContext;

  void _showToast(String msg) => Scaffold.of(_scaffoldContext).showSnackBar(new SnackBar(content: new Text(msg)));

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      body: new Builder(builder: (BuildContext context) {
        _scaffoldContext = context;
        return CommonWidgetManager.getCommonPageWidget(
          context,
          new Container(
            child: _body(),
          ),
          showLoading: _isShow,
          title: "登录",
          titleBackgroundColor: Color(0xFF0f0544),
        );
      }),
    );
  }

  // --CUSTOM ----------------------

  Widget _body() {
    return new Container();
  }

  void _request() {}

// --CUSTOM ----------------------
}
