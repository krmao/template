import 'package:rxdart/rxdart.dart';

class STBaseRxBus {
  static final _bus = PublishSubject<dynamic>();

  static void post(dynamic event) => _bus.add(event);

  static Stream<T> toStream<T>() {
    if (T == dynamic) {
      return _bus.stream;
    } else {
      return _bus.stream.where((event) => event is T).cast<T>();
    }
  }
}
