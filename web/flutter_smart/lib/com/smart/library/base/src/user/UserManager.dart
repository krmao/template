import 'dart:async';
import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

//import 'package:smart/com/smart/library/base/user/LoginOrLogoutEvent.dart';
import 'package:smart/com/smart/library/base/src/utils/RxBus.dart';

class UserManager {
  static UserModel _user;

  static Future<UserModel> getUser() async {
    if (_user == null) {
      SharedPreferences prefs = await SharedPreferences.getInstance();
      String userJsonString = prefs.getString("user");
      var userInfo = json.decode(userJsonString);
      UserModel userModeL = new UserModel(userInfo["id"], userInfo["user_id"], userInfo["score"], userInfo["createtime"], userInfo["expiretime"], userInfo["expires_in"], userInfo["username"], userInfo["nickname"], userInfo["mobile"], userInfo["avatar"], userInfo["token"]);
      _user = userModeL;
    }
    print('get user $_user');
    return _user;
  }

  static Future saveUser(UserModel userModel) async {
    _user = userModel;
    SharedPreferences prefs = await SharedPreferences.getInstance();
    print('save user $userModel');
    await prefs.setString('user', userModel == null ? null : userModel.toJsonString());
  }

  static void logout() {
    saveUser(null);
//    RxBus.post(LoginOrLogoutEvent(false));
  }

  static Future<bool> isLoginSuccess() {
    Completer<bool> completer = new Completer();
    getUser().then((model) {
      completer.complete(model != null);
    }).catchError((error) {
      completer.completeError(error);
    });
    return completer.future;
  }

  static Future<UserModel> ensureLogin(BuildContext context) async {
    Completer<UserModel> completer = new Completer();

    getUser().then((UserModel model) {
      if (model != null) {
        completer.complete(model);
      } else {
        requestLogin(context).then((userModel) {
          completer.complete(userModel);
        }).catchError(() {
          completer.completeError(Object());
        });
      }
    }).catchError((error) {
      requestLogin(context).then((userModel) {
        completer.complete(userModel);
      }).catchError(() {
        completer.completeError(error);
      });
    });
    return completer.future;
  }

  static Future<UserModel> requestLogin(BuildContext context) async {
    Completer<UserModel> completer = new Completer();
    final UserModel userModel = await Navigator.of(context).push(CupertinoPageRoute(builder: (context) => Container()));
    if (userModel != null) {
      completer.complete(userModel);
    } else {
      completer.completeError(Object());
    }
    return completer.future;
  }
}

class UserModel {
  int id;
  int user_id;
  int score;
  int createtime;
  int expiretime;
  int expires_in;
  String username;
  String nickname;
  String mobile;
  String avatar;
  String token;

  UserModel(this.id, this.user_id, this.score, this.createtime, this.expiretime, this.expires_in, this.username, this.nickname, this.mobile, this.avatar, this.token);

  String toJsonString() {
    return json.encode({
      "id": id,
      "user_id": user_id,
      "score": score,
      "createtime": createtime,
      "expiretime": expiretime,
      "expires_in": expires_in,
      "username": username,
      "nickname": nickname,
      "mobile": mobile,
      "avatar": avatar,
      "token": token,
    });
  }

  @override
  String toString() {
    return 'UserModel{id: $id, user_id: $user_id, score: $score, createtime: $createtime, expiretime: $expiretime, expires_in: $expires_in, username: $username, nickname: $nickname, mobile: $mobile, avatar: $avatar, token: $token}';
  }
}
