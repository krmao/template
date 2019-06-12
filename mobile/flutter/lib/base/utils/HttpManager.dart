import 'package:dio/dio.dart';

class HttpManager {
  static Dio _dio;

  static Dio getClient() {
    if (_dio == null) {
      _dio = new Dio();
      _dio.options.baseUrl = "http://www.smart.com/api";
      _dio.options.connectTimeout = 5000; //5s
      _dio.options.receiveTimeout = 3000;

      _dio.interceptor.request.onSend = (Options options) {
        // Do something before request is sent
        print("\n[request]\n-------------------------------------------------------->\n${options.baseUrl}${options.path}\n${options.method}\t${options.contentType}\n${options.data}");
        return options; //continue
        // If you want to resolve the request with some custom data，
        // you can return a `Response` object or return `dio.resolve(data)`.
        // If you want to reject the request with a error message,
        // you can return a `DioError` object or return `dio.reject(errMsg)`
      };
      _dio.interceptor.response.onSuccess = (Response response) {
        // Do something with response data
        print("\n[response]\n-------------------------------------------------------->\n$response");
        return response; // continue
      };
      _dio.interceptor.response.onError = (DioError e) {
        // Do something with response error
        print("\n[error]\n-------------------------------------------------------->\n$e");
        return e; //continue
      };
    }
    return _dio;
  }
}
