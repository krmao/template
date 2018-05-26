__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var InspectorAgent = _require(_dependencyMap[0], 'InspectorAgent');

  var JSInspector = _require(_dependencyMap[1], 'JSInspector');

  var Map = _require(_dependencyMap[2], 'Map');

  var XMLHttpRequest = _require(_dependencyMap[3], 'XMLHttpRequest');

  var Interceptor = function () {
    function Interceptor(agent) {
      babelHelpers.classCallCheck(this, Interceptor);
      this._agent = agent;
      this._requests = new Map();
    }

    babelHelpers.createClass(Interceptor, [{
      key: "getData",
      value: function getData(requestId) {
        return this._requests.get(requestId);
      }
    }, {
      key: "requestSent",
      value: function requestSent(id, url, method, headers) {
        var requestId = String(id);

        this._requests.set(requestId, '');

        var request = {
          url: url,
          method: method,
          headers: headers,
          initialPriority: 'Medium'
        };
        var event = {
          requestId: requestId,
          documentURL: '',
          frameId: '1',
          loaderId: '1',
          request: request,
          timestamp: JSInspector.getTimestamp(),
          initiator: {
            type: 'other'
          },
          type: 'Other'
        };

        this._agent.sendEvent('requestWillBeSent', event);
      }
    }, {
      key: "responseReceived",
      value: function responseReceived(id, url, status, headers) {
        var requestId = String(id);
        var response = {
          url: url,
          status: status,
          statusText: String(status),
          headers: headers,
          requestHeaders: {},
          mimeType: this._getMimeType(headers),
          connectionReused: false,
          connectionId: -1,
          encodedDataLength: 0,
          securityState: 'unknown'
        };
        var event = {
          requestId: requestId,
          frameId: '1',
          loaderId: '1',
          timestamp: JSInspector.getTimestamp(),
          type: 'Other',
          response: response
        };

        this._agent.sendEvent('responseReceived', event);
      }
    }, {
      key: "dataReceived",
      value: function dataReceived(id, data) {
        var requestId = String(id);
        var existingData = this._requests.get(requestId) || '';

        this._requests.set(requestId, existingData.concat(data));

        var event = {
          requestId: requestId,
          timestamp: JSInspector.getTimestamp(),
          dataLength: data.length,
          encodedDataLength: data.length
        };

        this._agent.sendEvent('dataReceived', event);
      }
    }, {
      key: "loadingFinished",
      value: function loadingFinished(id, encodedDataLength) {
        var event = {
          requestId: String(id),
          timestamp: JSInspector.getTimestamp(),
          encodedDataLength: encodedDataLength
        };

        this._agent.sendEvent('loadingFinished', event);
      }
    }, {
      key: "loadingFailed",
      value: function loadingFailed(id, error) {
        var event = {
          requestId: String(id),
          timestamp: JSInspector.getTimestamp(),
          type: 'Other',
          errorText: error
        };

        this._agent.sendEvent('loadingFailed', event);
      }
    }, {
      key: "_getMimeType",
      value: function _getMimeType(headers) {
        var contentType = headers['Content-Type'] || '';
        return contentType.split(';')[0];
      }
    }]);
    return Interceptor;
  }();

  var NetworkAgent = function (_InspectorAgent) {
    babelHelpers.inherits(NetworkAgent, _InspectorAgent);

    function NetworkAgent() {
      babelHelpers.classCallCheck(this, NetworkAgent);
      return babelHelpers.possibleConstructorReturn(this, (NetworkAgent.__proto__ || Object.getPrototypeOf(NetworkAgent)).apply(this, arguments));
    }

    babelHelpers.createClass(NetworkAgent, [{
      key: "enable",
      value: function enable(_ref) {
        var maxResourceBufferSize = _ref.maxResourceBufferSize,
            maxTotalBufferSize = _ref.maxTotalBufferSize;
        this._interceptor = new Interceptor(this);
        XMLHttpRequest.setInterceptor(this._interceptor);
      }
    }, {
      key: "disable",
      value: function disable() {
        XMLHttpRequest.setInterceptor(null);
        this._interceptor = null;
      }
    }, {
      key: "getResponseBody",
      value: function getResponseBody(_ref2) {
        var requestId = _ref2.requestId;
        return {
          body: this.interceptor().getData(requestId),
          base64Encoded: false
        };
      }
    }, {
      key: "interceptor",
      value: function interceptor() {
        if (this._interceptor) {
          return this._interceptor;
        } else {
          throw Error('_interceptor can not be null');
        }
      }
    }]);
    return NetworkAgent;
  }(InspectorAgent);

  NetworkAgent.DOMAIN = 'Network';
  module.exports = NetworkAgent;
},119,[120,118,54,75],"NetworkAgent");