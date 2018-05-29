__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Blob = _require(_dependencyMap[0], 'Blob');

  var EventTarget = _require(_dependencyMap[1], 'event-target-shim');

  var NativeEventEmitter = _require(_dependencyMap[2], 'NativeEventEmitter');

  var BlobManager = _require(_dependencyMap[3], 'BlobManager');

  var NativeModules = _require(_dependencyMap[4], 'NativeModules');

  var Platform = _require(_dependencyMap[5], 'Platform');

  var WebSocketEvent = _require(_dependencyMap[6], 'WebSocketEvent');

  var base64 = _require(_dependencyMap[7], 'base64-js');

  var binaryToBase64 = _require(_dependencyMap[8], 'binaryToBase64');

  var invariant = _require(_dependencyMap[9], 'fbjs/lib/invariant');

  var WebSocketModule = NativeModules.WebSocketModule;
  var CONNECTING = 0;
  var OPEN = 1;
  var CLOSING = 2;
  var CLOSED = 3;
  var CLOSE_NORMAL = 1000;
  var WEBSOCKET_EVENTS = ['close', 'error', 'message', 'open'];
  var nextWebSocketId = 0;

  var WebSocket = function (_EventTarget) {
    babelHelpers.inherits(WebSocket, _EventTarget);

    function WebSocket(url, protocols, options) {
      babelHelpers.classCallCheck(this, WebSocket);

      var _this = babelHelpers.possibleConstructorReturn(this, (WebSocket.__proto__ || Object.getPrototypeOf(WebSocket)).call(this));

      _this.CONNECTING = CONNECTING;
      _this.OPEN = OPEN;
      _this.CLOSING = CLOSING;
      _this.CLOSED = CLOSED;
      _this.readyState = CONNECTING;

      if (typeof protocols === 'string') {
        protocols = [protocols];
      }

      var _ref = options || {},
          _ref$headers = _ref.headers,
          headers = _ref$headers === undefined ? {} : _ref$headers,
          unrecognized = babelHelpers.objectWithoutProperties(_ref, ["headers"]);

      if (unrecognized && typeof unrecognized.origin === 'string') {
        console.warn('Specifying `origin` as a WebSocket connection option is deprecated. Include it under `headers` instead.');
        headers.origin = unrecognized.origin;
        delete unrecognized.origin;
      }

      if (Object.keys(unrecognized).length > 0) {
        console.warn('Unrecognized WebSocket connection option(s) `' + Object.keys(unrecognized).join('`, `') + '`. ' + 'Did you mean to put these under `headers`?');
      }

      if (!Array.isArray(protocols)) {
        protocols = null;
      }

      if (!WebSocket.isAvailable) {
        throw new Error('Cannot initialize WebSocket module. ' + 'Native module WebSocketModule is missing.');
      }

      _this._eventEmitter = new NativeEventEmitter(WebSocketModule);
      _this._socketId = nextWebSocketId++;

      _this._registerEvents();

      WebSocketModule.connect(url, protocols, {
        headers: headers
      }, _this._socketId);
      return _this;
    }

    babelHelpers.createClass(WebSocket, [{
      key: "close",
      value: function close(code, reason) {
        if (this.readyState === this.CLOSING || this.readyState === this.CLOSED) {
          return;
        }

        this.readyState = this.CLOSING;

        this._close(code, reason);
      }
    }, {
      key: "send",
      value: function send(data) {
        if (this.readyState === this.CONNECTING) {
          throw new Error('INVALID_STATE_ERR');
        }

        if (data instanceof Blob) {
          invariant(BlobManager.isAvailable, 'Native module BlobModule is required for blob support');
          BlobManager.sendOverSocket(data, this._socketId);
          return;
        }

        if (typeof data === 'string') {
          WebSocketModule.send(data, this._socketId);
          return;
        }

        if (data instanceof ArrayBuffer || ArrayBuffer.isView(data)) {
          WebSocketModule.sendBinary(binaryToBase64(data), this._socketId);
          return;
        }

        throw new Error('Unsupported data type');
      }
    }, {
      key: "ping",
      value: function ping() {
        if (this.readyState === this.CONNECTING) {
          throw new Error('INVALID_STATE_ERR');
        }

        WebSocketModule.ping(this._socketId);
      }
    }, {
      key: "_close",
      value: function _close(code, reason) {
        if (Platform.OS === 'android') {
          var statusCode = typeof code === 'number' ? code : CLOSE_NORMAL;
          var closeReason = typeof reason === 'string' ? reason : '';
          WebSocketModule.close(statusCode, closeReason, this._socketId);
        } else {
          WebSocketModule.close(this._socketId);
        }

        if (BlobManager.isAvailable && this._binaryType === 'blob') {
          BlobManager.removeWebSocketHandler(this._socketId);
        }
      }
    }, {
      key: "_unregisterEvents",
      value: function _unregisterEvents() {
        this._subscriptions.forEach(function (e) {
          return e.remove();
        });

        this._subscriptions = [];
      }
    }, {
      key: "_registerEvents",
      value: function _registerEvents() {
        var _this2 = this;

        this._subscriptions = [this._eventEmitter.addListener('websocketMessage', function (ev) {
          if (ev.id !== _this2._socketId) {
            return;
          }

          var data = ev.data;

          switch (ev.type) {
            case 'binary':
              data = base64.toByteArray(ev.data).buffer;
              break;

            case 'blob':
              data = BlobManager.createFromOptions(ev.data);
              break;
          }

          _this2.dispatchEvent(new WebSocketEvent('message', {
            data: data
          }));
        }), this._eventEmitter.addListener('websocketOpen', function (ev) {
          if (ev.id !== _this2._socketId) {
            return;
          }

          _this2.readyState = _this2.OPEN;

          _this2.dispatchEvent(new WebSocketEvent('open'));
        }), this._eventEmitter.addListener('websocketClosed', function (ev) {
          if (ev.id !== _this2._socketId) {
            return;
          }

          _this2.readyState = _this2.CLOSED;

          _this2.dispatchEvent(new WebSocketEvent('close', {
            code: ev.code,
            reason: ev.reason
          }));

          _this2._unregisterEvents();

          _this2.close();
        }), this._eventEmitter.addListener('websocketFailed', function (ev) {
          if (ev.id !== _this2._socketId) {
            return;
          }

          _this2.readyState = _this2.CLOSED;

          _this2.dispatchEvent(new WebSocketEvent('error', {
            message: ev.message
          }));

          _this2.dispatchEvent(new WebSocketEvent('close', {
            message: ev.message
          }));

          _this2._unregisterEvents();

          _this2.close();
        })];
      }
    }, {
      key: "binaryType",
      get: function get() {
        return this._binaryType;
      },
      set: function set(binaryType) {
        if (binaryType !== 'blob' && binaryType !== 'arraybuffer') {
          throw new Error('binaryType must be either \'blob\' or \'arraybuffer\'');
        }

        if (this._binaryType === 'blob' || binaryType === 'blob') {
          invariant(BlobManager.isAvailable, 'Native module BlobModule is required for blob support');

          if (binaryType === 'blob') {
            BlobManager.addWebSocketHandler(this._socketId);
          } else {
            BlobManager.removeWebSocketHandler(this._socketId);
          }
        }

        this._binaryType = binaryType;
      }
    }]);
    return WebSocket;
  }(EventTarget.apply(undefined, WEBSOCKET_EVENTS));

  WebSocket.CONNECTING = CONNECTING;
  WebSocket.OPEN = OPEN;
  WebSocket.CLOSING = CLOSING;
  WebSocket.CLOSED = CLOSED;
  WebSocket.isAvailable = !!WebSocketModule;
  module.exports = WebSocket;
},"b54f8d6a8fec03e3197a9d378e32fbd2",["096007123c886a641b5b7e629326a442","720d63c49f383f153c021e592c9e6989","522e0292cd937e7e7dc15e8d27ea9246","6432c6a42bdc71908935daf61e70dba8","ce21807d4d291be64fa852393519f6c8","9493a89f5d95c3a8a47c65cfed9b5542","ca06df4ad17f8f158e9a75d28b8dff00","350961b4c78178ca4358ee1e601c5e2f","2c5c0b8cc147ed9fc3c48f037910a196","8940a4ad43b101ffc23e725363c70f8d"],"WebSocket");