import 'package:rxdart/rxdart.dart';

class RxBus {
  static final _bus = PublishSubject<dynamic>();

  static void post(dynamic event) => _bus.add(event);

  static Observable<T> toObservable<T>() => _bus.ofType(TypeToken<T>());
}
