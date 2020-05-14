import 'package:dio/dio.dart';

class HttpManager {
  static Dio _dio;

  static Dio getClient() {
    if (_dio == null) {
      _dio = new Dio();
      _dio.options.baseUrl = "http://xxx/api";
      _dio.options.connectTimeout = 5000; //5s
      _dio.options.receiveTimeout = 3000;

      _dio.interceptors
          .add(InterceptorsWrapper(onRequest: (RequestOptions options) {
        print(
            "\n[request]\n-------------------------------------------------------->\n${options.baseUrl}${options.path}\n${options.method}\t${options.contentType}\n${options.data}");
      }, onResponse: (Response response) {
        print(
            "\n[response]\n-------------------------------------------------------->\n$response");
      }, onError: (DioError error) {
        print(
            "\n[error]\n-------------------------------------------------------->\n$error");
      }));
    }
    return _dio;
  }
}
