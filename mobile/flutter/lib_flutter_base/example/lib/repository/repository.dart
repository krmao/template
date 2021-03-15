import 'dart:async';
import 'dart:io';

import 'package:dio/dio.dart';

//import 'package:smart/com/smart/library/base/utils/stbase_http_manager.dart';
import 'response_model.dart';

class Repository {
  /// base request
  static Future<ResponseModel> _requestFormPost(String path,
      {data, Options options, CancelToken cancelToken}) async {
    Completer<ResponseModel> completer = new Completer();
    /* HttpManager.getClient().post(path, data: data, options: options, cancelToken: cancelToken).then((response) {
      var responseData = response.data;
      var responseModel = ResponseModel(responseData["code"], responseData["msg"], responseData["time"], responseData["data"]);
      if (responseModel.code == 1) {
        completer.complete(responseModel);
      } else {
        completer.completeError("${responseModel.msg}");
      }
    }).catchError((error) => completer.completeError(error));*/

    return completer.future;
  }

  // -------------------------------------------------------------------------------------------------------------------------
  // business request
  // -------------------------------------------------------------------------------------------------------------------------

  static Future<dynamic> requestLogin(String mobile, String password) async {
    Completer<dynamic> completer = new Completer();

    _requestFormPost("/user/login",
            data: new FormData.fromMap({"mobile": mobile, "password": password}))
        .then((ResponseModel responseModel) {
      completer.complete(responseModel.data);
    }).catchError((error) {
      completer.completeError(error);
    });

    return completer.future;
  }

  static Future<dynamic> requestAuth(int id, String surname, String name,
      String idcard, File image1, File image2, File image3) async {
    Completer<dynamic> completer = new Completer();
    _requestFormPost("/user/auth_man",
        data: new FormData.fromMap({
          "id": id,
          "surname": surname,
          "name": name,
          "image1":  MultipartFile.fromFileSync(image1.path, filename:"image1"),
          "image2": MultipartFile.fromFileSync(image2.path, filename:"image2"),
          "image3": MultipartFile.fromFileSync(image3.path, filename:"image3"),
          "idcard": idcard,
        })).then((ResponseModel responseModel) {
      completer.complete(responseModel.data);
    }).catchError((error) {
      completer.completeError(error);
    });

    return completer.future;
  }

  static Future<dynamic> requestMyDeal(int id) async {
    Completer<dynamic> completer = new Completer();
    _requestFormPost("/user/my_deal", data: new FormData.fromMap({"id": id}))
        .then((ResponseModel responseModel) {
      completer.complete(responseModel.data);
    }).catchError((error) {
      completer.completeError(error);
    });

    return completer.future;
  }
}
