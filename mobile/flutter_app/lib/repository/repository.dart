import 'dart:async';

import 'package:dio/dio.dart';

import 'http_manager.dart';
import 'response_model.dart';

class Repository {
  /// base request
  static Future<ResponseModel> _requestFormPost(String path,
      {data, Options options, CancelToken cancelToken}) async {
    Completer<ResponseModel> completer = new Completer();
    HttpManager.getClient()
        .post(path, data: data, options: options, cancelToken: cancelToken)
        .then((response) {
      var responseData = response.data;
      var responseModel = ResponseModel(responseData["code"],
          responseData["msg"], responseData["time"], responseData["data"]);
      if (responseModel.code == 1) {
        completer.complete(responseModel);
      } else {
        completer.completeError("${responseModel.msg}");
      }
    }).catchError((error) => completer.completeError(error));

    return completer.future;
  }

  // -------------------------------------------------------------------------------------------------------------------------
  // business request
  // -------------------------------------------------------------------------------------------------------------------------

  static Future<dynamic> requestDetail(String name, int id) async {
    Completer<dynamic> completer = new Completer();
    _requestFormPost("/index/detail",
        data: FormData.fromMap({
          "id": id,
          "name": name,
        })).then((ResponseModel responseModel) {
      completer.complete(responseModel.data);
    }).catchError((error) {
      // completer.completeError(error);
      // 模拟数据
      completer.complete({
        "image":
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2312761599,1510261600&fm=26&gp=0.jpg",
        "title": "几种苹果种类介绍",
        "auth": "大毛",
        "create_time": "2020/05 00:00:00",
        "content": "<body> "
            "1、红富士</br>"
            "富士苹果的特点是体积很大，遍体通红，形状很圆，平均大小像棒球一样。果实的重量中，有9到11%是单糖，并且它果肉紧密，比其他很多苹果变种都要甜美与清脆，因此受到全世界消费者的广泛喜爱。富士苹果与另外苹果相比有更长的食用日期，甚至无需放入冰箱保存。室温下可保存4个月，要是放入冰箱，富士苹果能保存5到7个月。"
            "</br>"
            "</br>"
            "2、黄元帅/金冠</br>"
            "金冠的果形呈长圆锥形，斤果三个左右；成熟后果皮呈金苋，阳面带有红晕，皮薄无锈斑，有光泽；肉质细密，呈黄白色，汁液较多，味深醇香，甜酸适口。"
            "</br>"
            "</br>"
            "3、嘎啦</br>"
            "金冠果实中等大，单果重180—200克，短圆锥形，果面金黄色。阳面具浅红晕，有红色断续宽条纹，果型端正美观。果顶有五棱，果梗细长，果皮薄，有光泽。果肉浅黄色，肉质致密、汁多、细脆，味甜微酸，非常适口。品质上乘，较耐贮藏。幼树结果早，坐果率高，丰产稳产，容易管理"
            "</br>"
            "</br>"
            " </body>",
      });
    });

    return completer.future;
  }

  static Future<dynamic> requestList(String name) async {
    Completer<dynamic> completer = new Completer();
    _requestFormPost("/index/getmenuinfo",
        data: new FormData.fromMap({
          "name": name,
          "page": 1,
          "limit": 10000,
        })).then((ResponseModel responseModel) {
      completer.complete(responseModel.data);
    }).catchError((error) {
      // completer.completeError(error);
      // 模拟数据
      completer.complete([
        {
          "id": 1,
          "type": "",
          "image":
              "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2312761599,1510261600&fm=26&gp=0.jpg",
          "title": "苹果专题",
          "auth": "大毛",
          "create_time": "2020/05 00:00:00",
          "read": "999",
        },
        {
          "id": 2,
          "type": "苹果列表",
          "image":
              "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1855700197,1255975288&fm=26&gp=0.jpg",
          "title": "苹果专题",
          "auth": "大毛",
          "create_time": "2020/05 00:00:00",
          "read": "999",
        },
        {
          "id": 3,
          "type": "苹果列表",
          "image":
              "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2970417308,706591008&fm=26&gp=0.jpg",
          "title": "苹果专题",
          "auth": "大毛",
          "create_time": "2020/05 00:00:00",
          "read": "999",
        },
        {
          "id": 4,
          "type": "苹果列表",
          "image":
              "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3428352364,4139523953&fm=26&gp=0.jpg",
          "title": "苹果专题",
          "auth": "大毛",
          "create_time": "2020/05 00:00:00",
          "read": "999",
        },
        {
          "id": 5,
          "type": "苹果列表",
          "image":
              "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2312761599,1510261600&fm=26&gp=0.jpg",
          "title": "苹果专题",
          "auth": "大毛",
          "create_time": "2020/05 00:00:00",
          "read": "999",
        },
        {
          "id": 6,
          "type": "苹果列表",
          "image":
              "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2312761599,1510261600&fm=26&gp=0.jpg",
          "title": "苹果专题",
          "auth": "大毛",
          "create_time": "2020/05 00:00:00",
          "read": "999",
        },
        {
          "id": 7,
          "type": "苹果列表",
          "image":
              "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2312761599,1510261600&fm=26&gp=0.jpg",
          "title": "苹果专题",
          "auth": "大毛",
          "create_time": "2020/05 00:00:00",
          "read": "999",
        },
        {
          "id": 8,
          "type": "苹果列表",
          "image":
              "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2312761599,1510261600&fm=26&gp=0.jpg",
          "title": "苹果专题",
          "auth": "大毛",
          "create_time": "2020/05 00:00:00",
          "read": "999",
        },
      ]);
    });

    return completer.future;
  }
}
