class ResponseModel {
  int code;
  String msg;
  String time;
  dynamic data;

  ResponseModel(this.code, this.msg, this.time, this.data);

  @override
  String toString() {
    return 'ResponseModel{code: $code, msg: $msg, time: $time, data: $data}';
  }
}
