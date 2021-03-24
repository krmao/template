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
        print(
            "[page] $BRIDGE_EVENT_NAME method callback methodName=$methodName, arguments=$arguments");
        print(
            "[page] $BRIDGE_EVENT_NAME method callback arguments['eventKey']=${arguments['eventKey']}, arguments['eventInfo']=${arguments['eventInfo']}");

        print(
            "[page] $BRIDGE_EVENT_NAME arguments['eventKey'] != null=${arguments['eventKey'] != null}, arguments['eventInfo'] != null=${arguments['eventInfo'] != null}");

        if (arguments['eventKey'] != null && arguments['eventInfo'] != null) {

          dynamic returnEventKey = arguments['eventKey'];
          dynamic returnEventInfo = arguments['eventInfo'];

          print(
              "[page] _invokeEvent returnEventKey=$returnEventKey, returnEventInfo=$returnEventInfo");

          eventMap.forEach((itemEventId, itemMap) {
            print(
                "[page] _invokeEvent ---- itemEventId=$itemEventId, itemMap=$itemMap");
            itemMap.forEach((itemEventKey, listener) {
              print(
                  "[page] _invokeEvent -------- itemEventKey=$itemEventKey, listener=$listener");
              if (returnEventKey == itemEventKey) {
                (listener as List)?.forEach((callback) {
                  print(
                      "[page] _invokeEvent **** do listener eventCallBack=$eventCallBack, callback=$callback");
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
