import 'base_bridge.dart';

typedef EventCallBack = void Function(String eventName, Map eventData);

class Event extends BaseBridge {
  // ignore: non_constant_identifier_names
  static String BRIDGE_EVENT_NAME = "__codesdancing_flutter_event__";
  static Map<String, Map> eventListeners = new Map();
  static bool registerMethodCall = false;

  static void addEventListener<T>(String eventName, EventCallBack eventCallBack,
      {String containerId}) {
    String containerIdInner = containerId;
    if (!registerMethodCall) {
      BaseBridge.registerMethodCallBack(BRIDGE_EVENT_NAME,
          (String methodName, dynamic arguments) {
        if (arguments['eventName'] != null && arguments['eventInfo'] != null) {
          _invokeEvent(arguments['eventName'], arguments['eventInfo']);
        }
      });
      registerMethodCall = true;
    }

    if (!eventListeners.containsKey(eventName)) {
      eventListeners[eventName] = {};
    }
    if (!eventListeners[eventName].containsKey(containerIdInner)) {
      eventListeners[eventName][containerIdInner] = [];
    }
    eventListeners[eventName][containerIdInner].add(eventCallBack);

    BaseBridge.callNativeStatic("Event", "addEventListener",
        {"eventName": eventName, "containerId": containerIdInner});
  }

  static void removeEventListener<T>(String eventName, {String containerId}) {
    String containerIdInner = containerId;
    if (eventListeners.containsKey(eventName)) {
      eventListeners[eventName].remove(containerIdInner);
      if (eventListeners[eventName].isEmpty) {
        eventListeners.remove(eventName);
      }
    }
    BaseBridge.callNativeStatic("Event", "removeEventListener",
        {"eventName": eventName, "containerId": containerId});
  }

  static void sendEvent(String eventName, Map eventData) {
    BaseBridge.callNativeStatic(
        "Event", "sendEvent", {"eventName": eventName, "eventInfo": eventData});
  }

  static void _invokeEvent(String eventName, Map eventData) {
    String containerId = eventData['containerId'] ?? "";
    eventListeners[eventName].forEach((key, value) {
      if (key == containerId) {
        (value as List)?.forEach((eventCallBack) {
          eventCallBack(eventName, eventData);
        });
      }
    });
  }

  @override
  String getPluginName() {
    return "Event";
  }
}
