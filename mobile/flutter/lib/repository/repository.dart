import 'dart:async';
import 'dart:io';

import 'package:dio/dio.dart';

//import 'package:smart/com/smart/library/base/utils/HttpManager.dart';
import 'package:smart/repository/response_model.dart';

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
            data: new FormData.from({"mobile": mobile, "password": password}))
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
        data: new FormData.from({
          "id": id,
          "surname": surname,
          "name": name,
          "image1": new UploadFileInfo(image1, "image1"),
          "image2": new UploadFileInfo(image2, "image2"),
          "image3": new UploadFileInfo(image3, "image3"),
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
    _requestFormPost("/user/my_deal", data: new FormData.from({"id": id}))
        .then((ResponseModel responseModel) {
      completer.complete(responseModel.data);
    }).catchError((error) {
      completer.completeError(error);
    });

    return completer.future;
  }
}
