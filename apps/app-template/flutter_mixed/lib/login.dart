import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'CommonWidgetManager.dart';
import 'LoginOrLogoutEvent.dart';
import 'Repository.dart';
import 'RxBus.dart';
import 'UserManager.dart';

class LoginPage extends StatefulWidget {
  @override
  createState() => new LoginPageState();
}

class LoginPageState extends State<LoginPage> {
  @override
  void initState() {
    super.initState();
  }

  BuildContext _scaffoldContext;

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      body: new Builder(builder: (BuildContext context) {
        _scaffoldContext = context;
        return _body();
      }),
    );
  }

  Widget _body() {
    return new SafeArea(
        child: new Container(
      width: double.infinity,
      height: double.infinity,
      decoration: new BoxDecoration(
        image: new DecorationImage(
            fit: BoxFit.fill,
            image: new AssetImage(
              "images/login_bg.png",
            )),
      ),
      child: new Stack(
        children: <Widget>[
          new Container(
            margin: EdgeInsets.only(top: 80.0, left: 60.0),
            alignment: Alignment.topLeft,
            child: new Image.asset(
              "images/login_logo.png",
              width: 116.0,
              height: 36.0,
            ),
          ),
          new SingleChildScrollView(
//            margin: EdgeInsets.only(top: 300.0),
            child: new Column(
              children: <Widget>[
                new Container(
                  height: 300.0,
                ),
                new Container(
                  margin: EdgeInsets.only(left: 40.0, right: 40.0),
                  child: new TextField(
                    keyboardType: TextInputType.phone,
                    textInputAction: TextInputAction.next,
                    decoration: new InputDecoration(hintText: "请输入手机号码", contentPadding: const EdgeInsets.all(17.0), hintStyle: new TextStyle(color: Color(0xffcccccc))),
                    maxLines: 1,
                    style: TextStyle(color: Colors.black, fontSize: 18.0),
                    onChanged: (text) {
                      _mobile = text;
                    },
                  ),
                ),
                new Container(
                  margin: EdgeInsets.only(left: 40.0, right: 40.0),
                  child: new TextField(
                    obscureText: true,
                    decoration: new InputDecoration(hintText: "请输入密码", contentPadding: const EdgeInsets.all(20.0), hintStyle: new TextStyle(color: Color(0xffcccccc))),
                    maxLines: 1,
                    style: TextStyle(color: Colors.black, fontSize: 18.0),
                    onChanged: (text) {
                      _pwd = text;
                    },
                  ),
                ),
                new Container(
                  margin: EdgeInsets.only(top: 50.0, left: 40.0, right: 40.0),
                  width: double.infinity,
                  height: 50.0,
                  decoration: new BoxDecoration(
                    image: new DecorationImage(
                        fit: BoxFit.fill,
                        image: new AssetImage(
                          "images/login_submit_bg.png",
                        )),
                  ),
                  child: new FlatButton(
                      onPressed: () {
                        _requestLogin();
                      },
                      child: new Container(
                        child: new Text(
                          "登录",
                          style: TextStyle(color: Colors.white, fontSize: 20.0),
                        ),
                      )),
                ),
                new Container(
                  margin: EdgeInsets.only(top: 0.0, left: 15.0, right: 30.0),
                  width: double.infinity,
                  child: new Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: <Widget>[
                      new FlatButton(
                          onPressed: () {},
                          child: new Text(
                            "注册",
                            style: new TextStyle(color: Color(0xff0c0435), fontSize: 17.0, fontWeight: FontWeight.normal),
                            textAlign: TextAlign.center,
                            maxLines: 1,
                          )),
                      new FlatButton(
                          onPressed: () {},
                          child: new Text(
                            "忘记密码",
                            style: new TextStyle(color: Color(0xff0c0435), fontSize: 17.0, fontWeight: FontWeight.normal),
                            textAlign: TextAlign.center,
                            maxLines: 1,
                          )),
                    ],
                  ),
                ),
              ],
            ),
          ),
          CommonWidgetManager.getTitleWidget(context, title: "登录"),
        ],
      ),
    ));
  }

  var _mobile;
  var _pwd;

  void _requestLogin() {
    Repository.requestLogin(_mobile, _pwd).then((data) {
      print("requestLogin success: data=$data");
      var userInfo = data["userinfo"];
      if (userInfo != null) {
        UserModel userModeL = new UserModel(userInfo["id"], userInfo["user_id"], userInfo["score"], userInfo["createtime"], userInfo["expiretime"], userInfo["expires_in"], userInfo["username"], userInfo["nickname"], userInfo["mobile"], userInfo["avatar"], userInfo["token"]);
        UserManager.saveUser(userModeL).then((dynamic) {
          print("登录成功:");
          showToast("登录成功");

          RxBus.post(LoginOrLogoutEvent(true));

          Navigator.pop(context, userModeL);
        }).catchError((error) {
          print("登录失败:$error");
          showToast("登录失败");
        });
      } else {
        Scaffold.of(_scaffoldContext).showSnackBar(new SnackBar(
          content: new Text("用户信息为空, 请重新登录"),
        ));
      }
    }).catchError((error) {
      print("requestLogin failure: error=$error");
      Scaffold.of(_scaffoldContext).showSnackBar(new SnackBar(
        content: new Text("$error"),
      ));
    });
  }

  void showToast(String msg) {
    Scaffold.of(_scaffoldContext).showSnackBar(new SnackBar(
      content: new Text(msg),
    ));
  }
}
