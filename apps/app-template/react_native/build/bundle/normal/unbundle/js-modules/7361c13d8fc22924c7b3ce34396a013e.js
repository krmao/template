__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var RCTWebSocketModule = _require(_dependencyMap[0], 'NativeModules').WebSocketModule;

  var NativeEventEmitter = _require(_dependencyMap[1], 'NativeEventEmitter');

  var base64 = _require(_dependencyMap[2], 'base64-js');

  var originalRCTWebSocketConnect = RCTWebSocketModule.connect;
  var originalRCTWebSocketSend = RCTWebSocketModule.send;
  var originalRCTWebSocketSendBinary = RCTWebSocketModule.sendBinary;
  var originalRCTWebSocketClose = RCTWebSocketModule.close;
  var eventEmitter = void 0;
  var subscriptions = void 0;
  var closeCallback = void 0;
  var sendCallback = void 0;
  var connectCallback = void 0;
  var onOpenCallback = void 0;
  var onMessageCallback = void 0;
  var onErrorCallback = void 0;
  var onCloseCallback = void 0;
  var _isInterceptorEnabled = false;
  var WebSocketInterceptor = {
    setCloseCallback: function setCloseCallback(callback) {
      closeCallback = callback;
    },
    setSendCallback: function setSendCallback(callback) {
      sendCallback = callback;
    },
    setConnectCallback: function setConnectCallback(callback) {
      connectCallback = callback;
    },
    setOnOpenCallback: function setOnOpenCallback(callback) {
      onOpenCallback = callback;
    },
    setOnMessageCallback: function setOnMessageCallback(callback) {
      onMessageCallback = callback;
    },
    setOnErrorCallback: function setOnErrorCallback(callback) {
      onErrorCallback = callback;
    },
    setOnCloseCallback: function setOnCloseCallback(callback) {
      onCloseCallback = callback;
    },
    isInterceptorEnabled: function isInterceptorEnabled() {
      return _isInterceptorEnabled;
    },
    _unregisterEvents: function _unregisterEvents() {
      subscriptions.forEach(function (e) {
        return e.remove();
      });
      subscriptions = [];
    },
    _registerEvents: function _registerEvents() {
      subscriptions = [eventEmitter.addListener('websocketMessage', function (ev) {
        if (onMessageCallback) {
          onMessageCallback(ev.id, ev.type === 'binary' ? WebSocketInterceptor._arrayBufferToString(ev.data) : ev.data);
        }
      }), eventEmitter.addListener('websocketOpen', function (ev) {
        if (onOpenCallback) {
          onOpenCallback(ev.id);
        }
      }), eventEmitter.addListener('websocketClosed', function (ev) {
        if (onCloseCallback) {
          onCloseCallback(ev.id, {
            code: ev.code,
            reason: ev.reason
          });
        }
      }), eventEmitter.addListener('websocketFailed', function (ev) {
        if (onErrorCallback) {
          onErrorCallback(ev.id, {
            message: ev.message
          });
        }
      })];
    },
    enableInterception: function enableInterception() {
      if (_isInterceptorEnabled) {
        return;
      }

      eventEmitter = new NativeEventEmitter(RCTWebSocketModule);

      WebSocketInterceptor._registerEvents();

      RCTWebSocketModule.connect = function (url, protocols, options, socketId) {
        if (connectCallback) {
          connectCallback(url, protocols, options, socketId);
        }

        originalRCTWebSocketConnect.apply(this, arguments);
      };

      RCTWebSocketModule.send = function (data, socketId) {
        if (sendCallback) {
          sendCallback(data, socketId);
        }

        originalRCTWebSocketSend.apply(this, arguments);
      };

      RCTWebSocketModule.sendBinary = function (data, socketId) {
        if (sendCallback) {
          sendCallback(WebSocketInterceptor._arrayBufferToString(data), socketId);
        }

        originalRCTWebSocketSendBinary.apply(this, arguments);
      };

      RCTWebSocketModule.close = function () {
        if (closeCallback) {
          if (arguments.length === 3) {
            closeCallback(arguments[0], arguments[1], arguments[2]);
          } else {
            closeCallback(null, null, arguments[0]);
          }
        }

        originalRCTWebSocketClose.apply(this, arguments);
      };

      _isInterceptorEnabled = true;
    },
    _arrayBufferToString: function _arrayBufferToString(data) {
      var value = base64.toByteArray(data).buffer;

      if (value === undefined || value === null) {
        return '(no value)';
      }

      if (typeof ArrayBuffer !== 'undefined' && typeof Uint8Array !== 'undefined' && value instanceof ArrayBuffer) {
        return "ArrayBuffer {" + String(Array.from(new Uint8Array(value))) + "}";
      }

      return value;
    },
    disableInterception: function disableInterception() {
      if (!_isInterceptorEnabled) {
        return;
      }

      _isInterceptorEnabled = false;
      RCTWebSocketModule.send = originalRCTWebSocketSend;
      RCTWebSocketModule.sendBinary = originalRCTWebSocketSendBinary;
      RCTWebSocketModule.close = originalRCTWebSocketClose;
      RCTWebSocketModule.connect = originalRCTWebSocketConnect;
      connectCallback = null;
      closeCallback = null;
      sendCallback = null;
      onOpenCallback = null;
      onMessageCallback = null;
      onCloseCallback = null;
      onErrorCallback = null;

      WebSocketInterceptor._unregisterEvents();
    }
  };
  module.exports = WebSocketInterceptor;
},"7361c13d8fc22924c7b3ce34396a013e",["ce21807d4d291be64fa852393519f6c8","522e0292cd937e7e7dc15e8d27ea9246","350961b4c78178ca4358ee1e601c5e2f"],"WebSocketInterceptor");