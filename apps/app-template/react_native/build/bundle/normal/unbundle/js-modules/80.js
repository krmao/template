__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var MissingNativeEventEmitterShim = _require(_dependencyMap[0], 'MissingNativeEventEmitterShim');

  var NativeEventEmitter = _require(_dependencyMap[1], 'NativeEventEmitter');

  var RCTNetworkingNative = _require(_dependencyMap[2], 'NativeModules').Networking;

  var convertRequestBody = _require(_dependencyMap[3], 'convertRequestBody');

  function convertHeadersMapToArray(headers) {
    var headerArray = [];

    for (var name in headers) {
      headerArray.push([name, headers[name]]);
    }

    return headerArray;
  }

  var _requestId = 1;

  function generateRequestId() {
    return _requestId++;
  }

  var RCTNetworking = function (_NativeEventEmitter) {
    babelHelpers.inherits(RCTNetworking, _NativeEventEmitter);

    function RCTNetworking() {
      babelHelpers.classCallCheck(this, RCTNetworking);

      var _this = babelHelpers.possibleConstructorReturn(this, (RCTNetworking.__proto__ || Object.getPrototypeOf(RCTNetworking)).call(this, RCTNetworkingNative));

      _this.isAvailable = true;
      return _this;
    }

    babelHelpers.createClass(RCTNetworking, [{
      key: "sendRequest",
      value: function sendRequest(method, trackingName, url, headers, data, responseType, incrementalUpdates, timeout, callback, withCredentials) {
        var body = convertRequestBody(data);

        if (body && body.formData) {
          body.formData = body.formData.map(function (part) {
            return babelHelpers.extends({}, part, {
              headers: convertHeadersMapToArray(part.headers)
            });
          });
        }

        var requestId = generateRequestId();
        RCTNetworkingNative.sendRequest(method, url, requestId, convertHeadersMapToArray(headers), babelHelpers.extends({}, body, {
          trackingName: trackingName
        }), responseType, incrementalUpdates, timeout, withCredentials);
        callback(requestId);
      }
    }, {
      key: "abortRequest",
      value: function abortRequest(requestId) {
        RCTNetworkingNative.abortRequest(requestId);
      }
    }, {
      key: "clearCookies",
      value: function clearCookies(callback) {
        RCTNetworkingNative.clearCookies(callback);
      }
    }]);
    return RCTNetworking;
  }(NativeEventEmitter);

  if (__DEV__ && !RCTNetworkingNative) {
    var MissingNativeRCTNetworkingShim = function (_MissingNativeEventEm) {
      babelHelpers.inherits(MissingNativeRCTNetworkingShim, _MissingNativeEventEm);

      function MissingNativeRCTNetworkingShim() {
        babelHelpers.classCallCheck(this, MissingNativeRCTNetworkingShim);
        return babelHelpers.possibleConstructorReturn(this, (MissingNativeRCTNetworkingShim.__proto__ || Object.getPrototypeOf(MissingNativeRCTNetworkingShim)).call(this, 'RCTNetworking', 'Networking'));
      }

      babelHelpers.createClass(MissingNativeRCTNetworkingShim, [{
        key: "sendRequest",
        value: function sendRequest() {
          this.throwMissingNativeModule();
        }
      }, {
        key: "abortRequest",
        value: function abortRequest() {
          this.throwMissingNativeModule();
        }
      }, {
        key: "clearCookies",
        value: function clearCookies() {
          this.throwMissingNativeModule();
        }
      }]);
      return MissingNativeRCTNetworkingShim;
    }(MissingNativeEventEmitterShim);

    RCTNetworking = new MissingNativeRCTNetworkingShim();
  } else {
    RCTNetworking = new RCTNetworking();
  }

  module.exports = RCTNetworking;
},80,[81,82,24,83],"RCTNetworking");