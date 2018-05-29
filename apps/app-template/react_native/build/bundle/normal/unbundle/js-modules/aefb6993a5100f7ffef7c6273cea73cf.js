__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var NativeEventEmitter = _require(_dependencyMap[0], 'NativeEventEmitter');

  var RCTPushNotificationManager = _require(_dependencyMap[1], 'NativeModules').PushNotificationManager;

  var invariant = _require(_dependencyMap[2], 'fbjs/lib/invariant');

  var PushNotificationEmitter = new NativeEventEmitter(RCTPushNotificationManager);

  var _notifHandlers = new Map();

  var DEVICE_NOTIF_EVENT = 'remoteNotificationReceived';
  var NOTIF_REGISTER_EVENT = 'remoteNotificationsRegistered';
  var NOTIF_REGISTRATION_ERROR_EVENT = 'remoteNotificationRegistrationError';
  var DEVICE_LOCAL_NOTIF_EVENT = 'localNotificationReceived';

  var PushNotificationIOS = function () {
    babelHelpers.createClass(PushNotificationIOS, null, [{
      key: "presentLocalNotification",
      value: function presentLocalNotification(details) {
        RCTPushNotificationManager.presentLocalNotification(details);
      }
    }, {
      key: "scheduleLocalNotification",
      value: function scheduleLocalNotification(details) {
        RCTPushNotificationManager.scheduleLocalNotification(details);
      }
    }, {
      key: "cancelAllLocalNotifications",
      value: function cancelAllLocalNotifications() {
        RCTPushNotificationManager.cancelAllLocalNotifications();
      }
    }, {
      key: "removeAllDeliveredNotifications",
      value: function removeAllDeliveredNotifications() {
        RCTPushNotificationManager.removeAllDeliveredNotifications();
      }
    }, {
      key: "getDeliveredNotifications",
      value: function getDeliveredNotifications(callback) {
        RCTPushNotificationManager.getDeliveredNotifications(callback);
      }
    }, {
      key: "removeDeliveredNotifications",
      value: function removeDeliveredNotifications(identifiers) {
        RCTPushNotificationManager.removeDeliveredNotifications(identifiers);
      }
    }, {
      key: "setApplicationIconBadgeNumber",
      value: function setApplicationIconBadgeNumber(number) {
        RCTPushNotificationManager.setApplicationIconBadgeNumber(number);
      }
    }, {
      key: "getApplicationIconBadgeNumber",
      value: function getApplicationIconBadgeNumber(callback) {
        RCTPushNotificationManager.getApplicationIconBadgeNumber(callback);
      }
    }, {
      key: "cancelLocalNotifications",
      value: function cancelLocalNotifications(userInfo) {
        RCTPushNotificationManager.cancelLocalNotifications(userInfo);
      }
    }, {
      key: "getScheduledLocalNotifications",
      value: function getScheduledLocalNotifications(callback) {
        RCTPushNotificationManager.getScheduledLocalNotifications(callback);
      }
    }, {
      key: "addEventListener",
      value: function addEventListener(type, handler) {
        invariant(type === 'notification' || type === 'register' || type === 'registrationError' || type === 'localNotification', 'PushNotificationIOS only supports `notification`, `register`, `registrationError`, and `localNotification` events');
        var listener;

        if (type === 'notification') {
          listener = PushNotificationEmitter.addListener(DEVICE_NOTIF_EVENT, function (notifData) {
            handler(new PushNotificationIOS(notifData));
          });
        } else if (type === 'localNotification') {
          listener = PushNotificationEmitter.addListener(DEVICE_LOCAL_NOTIF_EVENT, function (notifData) {
            handler(new PushNotificationIOS(notifData));
          });
        } else if (type === 'register') {
          listener = PushNotificationEmitter.addListener(NOTIF_REGISTER_EVENT, function (registrationInfo) {
            handler(registrationInfo.deviceToken);
          });
        } else if (type === 'registrationError') {
          listener = PushNotificationEmitter.addListener(NOTIF_REGISTRATION_ERROR_EVENT, function (errorInfo) {
            handler(errorInfo);
          });
        }

        _notifHandlers.set(type, listener);
      }
    }, {
      key: "removeEventListener",
      value: function removeEventListener(type, handler) {
        invariant(type === 'notification' || type === 'register' || type === 'registrationError' || type === 'localNotification', 'PushNotificationIOS only supports `notification`, `register`, `registrationError`, and `localNotification` events');

        var listener = _notifHandlers.get(type);

        if (!listener) {
          return;
        }

        listener.remove();

        _notifHandlers.delete(type);
      }
    }, {
      key: "requestPermissions",
      value: function requestPermissions(permissions) {
        var requestedPermissions = {};

        if (permissions) {
          requestedPermissions = {
            alert: !!permissions.alert,
            badge: !!permissions.badge,
            sound: !!permissions.sound
          };
        } else {
          requestedPermissions = {
            alert: true,
            badge: true,
            sound: true
          };
        }

        return RCTPushNotificationManager.requestPermissions(requestedPermissions);
      }
    }, {
      key: "abandonPermissions",
      value: function abandonPermissions() {
        RCTPushNotificationManager.abandonPermissions();
      }
    }, {
      key: "checkPermissions",
      value: function checkPermissions(callback) {
        invariant(typeof callback === 'function', 'Must provide a valid callback');
        RCTPushNotificationManager.checkPermissions(callback);
      }
    }, {
      key: "getInitialNotification",
      value: function getInitialNotification() {
        return RCTPushNotificationManager.getInitialNotification().then(function (notification) {
          return notification && new PushNotificationIOS(notification);
        });
      }
    }]);

    function PushNotificationIOS(nativeNotif) {
      var _this = this;

      babelHelpers.classCallCheck(this, PushNotificationIOS);
      this._data = {};
      this._remoteNotificationCompleteCallbackCalled = false;
      this._isRemote = nativeNotif.remote;

      if (this._isRemote) {
        this._notificationId = nativeNotif.notificationId;
      }

      if (nativeNotif.remote) {
        Object.keys(nativeNotif).forEach(function (notifKey) {
          var notifVal = nativeNotif[notifKey];

          if (notifKey === 'aps') {
            _this._alert = notifVal.alert;
            _this._sound = notifVal.sound;
            _this._badgeCount = notifVal.badge;
            _this._category = notifVal.category;
            _this._contentAvailable = notifVal['content-available'];
            _this._threadID = notifVal['thread-id'];
          } else {
            _this._data[notifKey] = notifVal;
          }
        });
      } else {
        this._badgeCount = nativeNotif.applicationIconBadgeNumber;
        this._sound = nativeNotif.soundName;
        this._alert = nativeNotif.alertBody;
        this._data = nativeNotif.userInfo;
        this._category = nativeNotif.category;
      }
    }

    babelHelpers.createClass(PushNotificationIOS, [{
      key: "finish",
      value: function finish(fetchResult) {
        if (!this._isRemote || !this._notificationId || this._remoteNotificationCompleteCallbackCalled) {
          return;
        }

        this._remoteNotificationCompleteCallbackCalled = true;
        RCTPushNotificationManager.onFinishRemoteNotification(this._notificationId, fetchResult);
      }
    }, {
      key: "getMessage",
      value: function getMessage() {
        return this._alert;
      }
    }, {
      key: "getSound",
      value: function getSound() {
        return this._sound;
      }
    }, {
      key: "getCategory",
      value: function getCategory() {
        return this._category;
      }
    }, {
      key: "getAlert",
      value: function getAlert() {
        return this._alert;
      }
    }, {
      key: "getContentAvailable",
      value: function getContentAvailable() {
        return this._contentAvailable;
      }
    }, {
      key: "getBadgeCount",
      value: function getBadgeCount() {
        return this._badgeCount;
      }
    }, {
      key: "getData",
      value: function getData() {
        return this._data;
      }
    }, {
      key: "getThreadID",
      value: function getThreadID() {
        return this._threadID;
      }
    }]);
    return PushNotificationIOS;
  }();

  PushNotificationIOS.FetchResult = {
    NewData: 'UIBackgroundFetchResultNewData',
    NoData: 'UIBackgroundFetchResultNoData',
    ResultFailed: 'UIBackgroundFetchResultFailed'
  };
  module.exports = PushNotificationIOS;
},"aefb6993a5100f7ffef7c6273cea73cf",["522e0292cd937e7e7dc15e8d27ea9246","ce21807d4d291be64fa852393519f6c8","8940a4ad43b101ffc23e725363c70f8d"],"PushNotificationIOS");