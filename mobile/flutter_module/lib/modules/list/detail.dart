import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_html/flutter_html.dart';

import 'package:flutter_module/base/widgets/stbase_widget_manager.dart';
import '../../repository/repository.dart';

class DetailPage extends StatefulWidget {
  final String name;
  final int id;

  @override
  createState() => new DetailPageState(name: this.name, id: this.id);

  DetailPage({Key key, @required this.name, @required this.id})
      : super(key: key);
}

class DetailPageState extends State<DetailPage> {
  final String name;
  final int id;

  DetailPageState({Key key, @required this.name, @required this.id}) : super();

  @override
  void initState() {
    super.initState();
    _request(this.name, this.id);
  }

  bool _isShow = false;

  void _showLoading() {
    scheduleMicrotask(() {
      setState(() {
        _isShow = true;
      });
    });
  }

  void _hideLoading() {
    Future.delayed(Duration(milliseconds: 500), () {
      setState(() {
        _isShow = false;
      });
    });
  }

  BuildContext _scaffoldContext;

  void _showToast(String msg) => Scaffold.of(_scaffoldContext)
      .showSnackBar(new SnackBar(content: new Text(msg)));

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      backgroundColor: Colors.blue,
      body: new Builder(builder: (BuildContext context) {
        _scaffoldContext = context;
        return STBaseWidgetManager.getCommonPageWidget(
          context,
          new Container(
            margin: EdgeInsets.only(top: title_height),
            child: _body(),
          ),
          showLoading: _isShow,
          title: this.name,
          titleBackgroundColor: Colors.blue,
        );
      }),
    );
  }

  // --CUSTOM ----------------------

  Widget _body() {
    return new SingleChildScrollView(
      child: new Column(
        children: <Widget>[
          new AspectRatio(
            aspectRatio: 2.2,
            child: STBaseWidgetManager.getNetworkImageWidget(
                _data == null ? "" : _data["image"]),
          ),
          new Container(
            alignment: Alignment.centerLeft,
            margin: EdgeInsets.all(15.0),
            child: new Text(
              _data == null ? "" : _data["title"],
              style: TextStyle(color: Color(0xff292929), fontSize: 24.0),
            ),
          ),
          new Container(
            margin: EdgeInsets.all(15.0),
            alignment: Alignment.centerLeft,
            child: new Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: <Widget>[
                new SizedBox(
                  child: new Text(
                    _data == null ? "" : _data["auth"],
                    overflow: TextOverflow.ellipsis,
                    style: TextStyle(color: Color(0xff8b8585), fontSize: 17.0),
                    maxLines: 1,
                  ),
                ),
                new Text("发表于: ${_data == null ? "" : _data["create_time"]}"),
              ],
            ),
          ),
          STBaseWidgetManager.getHorizontalLine(
              margin: EdgeInsets.only(left: 10.0, right: 10.0)),
          new Container(
            margin: EdgeInsets.all(15.0),
            alignment: Alignment.topLeft,
            child: Html(
                data: _data == null ||
                        _data["content"] == null ||
                        _data["content"].length <= 0
                    ? ""
                    : _data["content"],
                //Optional parameters:
                padding: EdgeInsets.all(0.0),
                backgroundColor: Colors.white,
                defaultTextStyle: TextStyle(
                    fontFamily: 'serif',
                    fontSize: 18.0,
                    color: Color(0xff333333)),
                onLinkTap: (url) {
                  // open url in a webview
                }),
          ),
        ],
      ),
    );
  }

  dynamic _data;

  void _request(String name, int id) {
    _showLoading();
    Repository.requestDetail(name, id).then((value) {
      setState(() {
        this._data = value;
      });
      _hideLoading();
    }).catchError((error) {
      _hideLoading();
      _showToast("$error");
    });
  }
// --CUSTOM ----------------------
}
