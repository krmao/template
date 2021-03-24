import 'base_bridge.dart';

typedef EventCallBack = void Function(String eventKey, Map eventInfo);

class BaseBridgeEvent extends BaseBridge {
  static const String BRIDGE_EVENT_NAME = "__codesdancing_flutter_event__";
  static Map<String, Map> eventMap = new Map();
  static bool registerMethodCall = false;

  static void addEventListener<T>(
      String eventId, String eventKey, EventCallBack eventCallBack) {
    if (!registerMethodCall) {
      BaseBridge.registerMethodCallBack(BRIDGE_EVENT_NAME,
          (String methodName, dynamic arguments) {
        dynamic returnEventKey = arguments['eventKey'];
        dynamic returnEventInfo = arguments['eventInfo'];
        print(
            "[page] $BRIDGE_EVENT_NAME method callback methodName=$methodName, arguments=$arguments, returnEventKey=$returnEventKey, returnEventInfo=$returnEventInfo");
        if (returnEventKey != null && returnEventInfo != null) {
          eventMap.forEach((itemEventId, itemMap) {
            itemMap.forEach((itemEventKey, listener) {
              if (returnEventKey == itemEventKey) {
                (listener as List)?.forEach((callback) {
                  callback(returnEventKey, returnEventInfo);
                });
              }
            });
          });
        }
      });
      registerMethodCall = true;
    }

    if (!eventMap.containsKey(eventId)) {
      eventMap[eventId] = {};
    }
    if (!eventMap[eventId].containsKey(eventKey)) {
      eventMap[eventId][eventKey] = [];
    }
    eventMap[eventId][eventKey].add(eventCallBack);

    BaseBridge.callNativeStatic("Event", "addEventListener",
        {"eventId": eventId, "eventKey": eventKey});
  }

  static void removeEventListener<T>(String eventId, String eventKey) {
    if (eventMap.containsKey(eventId)) {
      eventMap[eventId].remove(eventKey);
      if (eventMap[eventId].isEmpty) {
        eventMap.remove(eventId);
      }
    }
    BaseBridge.callNativeStatic("Event", "removeEventListener",
        {"eventId": eventId, "eventKey": eventKey});
  }

  static void sendEvent(String eventKey, Map eventInfo) {
    BaseBridge.callNativeStatic(
        "Event", "sendEvent", {"eventKey": eventKey, "eventInfo": eventInfo});
  }

  @override
  String getPluginName() {
    return "Event";
  }
}
