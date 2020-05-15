import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'package:flutter_module/base/widgets/stbase_widget_manager.dart';
import '../../repository/repository.dart';
import 'detail.dart';

class ListPage extends StatefulWidget {
  final String name;

  @override
  createState() => new ListPageState(name: this.name);

  ListPage({Key key, @required this.name}) : super(key: key);
}

class ListPageState extends State<ListPage> {
  final String name;

  ListPageState({Key key, @required this.name}) : super();

  @override
  void initState() {
    super.initState();

    _request(this.name);
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
    return new Container(
      child: listWidget(),
      padding: EdgeInsets.only(top: 15.0, bottom: 10.0),
    );
  }

  Widget listWidget() {
    return new ListView.builder(
      itemBuilder: (context, i) {
        return new Column(
          children: <Widget>[
            _buildRow(_dataList[i]),
            i == _dataList.length - 1
                ? new Container()
                : STBaseWidgetManager.getHorizontalLine(
                    margin:
                        EdgeInsets.only(bottom: 10.0, left: 5.0, right: 10.0)),
          ],
        );
      },
      itemCount: _dataList.length,
    );
  }

  Widget _buildRow(dynamic model) {
    print("itemModel = $model");

    return new Material(
      type: MaterialType.transparency,
      child: new InkWell(
          onTap: () {
            STBaseWidgetManager.goTo(
                context,
                DetailPage(
                  name: "苹果专题报告",
                  id: model["id"],
                ));
          },
          child: new Container(
            child: new Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                new Container(
                  margin: const EdgeInsets.only(
                      left: 5.0, right: 10.0, top: 0.0, bottom: 10.0),
                  child: STBaseWidgetManager.getNetworkImageWidget(
                      model["image"], 120.0, 120.0),
                ),
                new Expanded(
                  flex: 1,
                  child: new Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: <Widget>[
                      new Container(
                        margin: const EdgeInsets.only(top: 5.0, right: 7.0),
                        child: new Text(
                          model["title"],
                          style: new TextStyle(
                              color: Color(0xFF15171b),
                              fontSize: 18.0,
                              fontWeight: FontWeight.bold),
                          maxLines: 2,
                          overflow: TextOverflow.ellipsis,
                          textAlign: TextAlign.start,
                        ),
                        height: 60.0,
                      ),
                      new Container(
                        margin: const EdgeInsets.only(
                            left: 0.0, right: 0.0, top: 5.0, bottom: 7.0),
                        child: new Text(model["auth"],
                            style: new TextStyle(
                                color: Color(0xFF15171b),
                                fontSize: 15.0,
                                fontWeight: FontWeight.bold),
                            maxLines: 1,
                            overflow: TextOverflow.ellipsis),
                      ),
                      new Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: <Widget>[
                          new Image.asset(
                            "images/icon_time.png",
                            width: 15.0,
                            height: 15.0,
                          ),
                          new Container(
                            width: 60.0,
                            margin: const EdgeInsets.only(
                                left: 5.0, right: 10.0, top: 0.0, bottom: 0.0),
                            child: new Text(
                              "${model["create_time"]?.split(" ")[0]}",
                              style: new TextStyle(
                                  color: Color(0xFF999999),
                                  fontSize: 14.0,
                                  fontWeight: FontWeight.normal),
                              maxLines: 1,
                            ),
                          ),
                          new Image.asset(
                            "images/icon_people.png",
                            width: 15.0,
                            height: 15.0,
                          ),
                          new Text(
                            " ${model["read"]}阅读",
                            style: new TextStyle(
                                color: Color(0xFF999999),
                                fontSize: 14.0,
                                fontWeight: FontWeight.normal),
                          ),
                        ],
                      ),
                    ],
                  ),
                )
              ],
            ),
          )),
    );
  }

  dynamic _dataList = [];

  void _request(String name) {
    _showLoading();
    Repository.requestList(name).then((value) {
      setState(() {
        this._dataList = value;
      });
      _hideLoading();
    }).catchError((error) {
      _hideLoading();
      _showToast("$error");
    });
  }

// --CUSTOM ----------------------
}
