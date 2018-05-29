__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var EventTarget = _require(_dependencyMap[0], 'event-target-shim');

  var RCTNetworking = _require(_dependencyMap[1], 'RCTNetworking');

  var base64 = _require(_dependencyMap[2], 'base64-js');

  var invariant = _require(_dependencyMap[3], 'fbjs/lib/invariant');

  var warning = _require(_dependencyMap[4], 'fbjs/lib/warning');

  var BlobManager = _require(_dependencyMap[5], 'BlobManager');

  if (BlobManager.isAvailable) {
    BlobManager.addNetworkingHandler();
  }

  var UNSENT = 0;
  var OPENED = 1;
  var HEADERS_RECEIVED = 2;
  var LOADING = 3;
  var DONE = 4;
  var SUPPORTED_RESPONSE_TYPES = {
    arraybuffer: typeof global.ArrayBuffer === 'function',
    blob: typeof global.Blob === 'function',
    document: false,
    json: true,
    text: true,
    '': true
  };
  var REQUEST_EVENTS = ['abort', 'error', 'load', 'loadstart', 'progress', 'timeout', 'loadend'];
  var XHR_EVENTS = REQUEST_EVENTS.concat('readystatechange');

  var XMLHttpRequestEventTarget = function (_EventTarget) {
    babelHelpers.inherits(XMLHttpRequestEventTarget, _EventTarget);

    function XMLHttpRequestEventTarget() {
      babelHelpers.classCallCheck(this, XMLHttpRequestEventTarget);
      return babelHelpers.possibleConstructorReturn(this, (XMLHttpRequestEventTarget.__proto__ || Object.getPrototypeOf(XMLHttpRequestEventTarget)).apply(this, arguments));
    }

    return XMLHttpRequestEventTarget;
  }(EventTarget.apply(undefined, REQUEST_EVENTS));

  var XMLHttpRequest = function (_EventTarget2) {
    babelHelpers.inherits(XMLHttpRequest, _EventTarget2);
    babelHelpers.createClass(XMLHttpRequest, null, [{
      key: "setInterceptor",
      value: function setInterceptor(interceptor) {
        XMLHttpRequest._interceptor = interceptor;
      }
    }]);

    function XMLHttpRequest() {
      babelHelpers.classCallCheck(this, XMLHttpRequest);

      var _this2 = babelHelpers.possibleConstructorReturn(this, (XMLHttpRequest.__proto__ || Object.getPrototypeOf(XMLHttpRequest)).call(this));

      _this2.UNSENT = UNSENT;
      _this2.OPENED = OPENED;
      _this2.HEADERS_RECEIVED = HEADERS_RECEIVED;
      _this2.LOADING = LOADING;
      _this2.DONE = DONE;
      _this2.readyState = UNSENT;
      _this2.status = 0;
      _this2.timeout = 0;
      _this2.withCredentials = true;
      _this2.upload = new XMLHttpRequestEventTarget();
      _this2._aborted = false;
      _this2._hasError = false;
      _this2._method = null;
      _this2._response = '';
      _this2._url = null;
      _this2._timedOut = false;
      _this2._trackingName = 'unknown';
      _this2._incrementalEvents = false;

      _this2._reset();

      return _this2;
    }

    babelHelpers.createClass(XMLHttpRequest, [{
      key: "_reset",
      value: function _reset() {
        this.readyState = this.UNSENT;
        this.responseHeaders = undefined;
        this.status = 0;
        delete this.responseURL;
        this._requestId = null;
        this._cachedResponse = undefined;
        this._hasError = false;
        this._headers = {};
        this._response = '';
        this._responseType = '';
        this._sent = false;
        this._lowerCaseResponseHeaders = {};

        this._clearSubscriptions();

        this._timedOut = false;
      }
    }, {
      key: "__didCreateRequest",
      value: function __didCreateRequest(requestId) {
        this._requestId = requestId;
        XMLHttpRequest._interceptor && XMLHttpRequest._interceptor.requestSent(requestId, this._url || '', this._method || 'GET', this._headers);
      }
    }, {
      key: "__didUploadProgress",
      value: function __didUploadProgress(requestId, progress, total) {
        if (requestId === this._requestId) {
          this.upload.dispatchEvent({
            type: 'progress',
            lengthComputable: true,
            loaded: progress,
            total: total
          });
        }
      }
    }, {
      key: "__didReceiveResponse",
      value: function __didReceiveResponse(requestId, status, responseHeaders, responseURL) {
        if (requestId === this._requestId) {
          this.status = status;
          this.setResponseHeaders(responseHeaders);
          this.setReadyState(this.HEADERS_RECEIVED);

          if (responseURL || responseURL === '') {
            this.responseURL = responseURL;
          } else {
            delete this.responseURL;
          }

          XMLHttpRequest._interceptor && XMLHttpRequest._interceptor.responseReceived(requestId, responseURL || this._url || '', status, responseHeaders || {});
        }
      }
    }, {
      key: "__didReceiveData",
      value: function __didReceiveData(requestId, response) {
        if (requestId !== this._requestId) {
          return;
        }

        this._response = response;
        this._cachedResponse = undefined;
        this.setReadyState(this.LOADING);
        XMLHttpRequest._interceptor && XMLHttpRequest._interceptor.dataReceived(requestId, response);
      }
    }, {
      key: "__didReceiveIncrementalData",
      value: function __didReceiveIncrementalData(requestId, responseText, progress, total) {
        if (requestId !== this._requestId) {
          return;
        }

        if (!this._response) {
          this._response = responseText;
        } else {
          this._response += responseText;
        }

        XMLHttpRequest._interceptor && XMLHttpRequest._interceptor.dataReceived(requestId, responseText);
        this.setReadyState(this.LOADING);

        this.__didReceiveDataProgress(requestId, progress, total);
      }
    }, {
      key: "__didReceiveDataProgress",
      value: function __didReceiveDataProgress(requestId, loaded, total) {
        if (requestId !== this._requestId) {
          return;
        }

        this.dispatchEvent({
          type: 'progress',
          lengthComputable: total >= 0,
          loaded: loaded,
          total: total
        });
      }
    }, {
      key: "__didCompleteResponse",
      value: function __didCompleteResponse(requestId, error, timeOutError) {
        if (requestId === this._requestId) {
          if (error) {
            if (this._responseType === '' || this._responseType === 'text') {
              this._response = error;
            }

            this._hasError = true;

            if (timeOutError) {
              this._timedOut = true;
            }
          }

          this._clearSubscriptions();

          this._requestId = null;
          this.setReadyState(this.DONE);

          if (error) {
            XMLHttpRequest._interceptor && XMLHttpRequest._interceptor.loadingFailed(requestId, error);
          } else {
            XMLHttpRequest._interceptor && XMLHttpRequest._interceptor.loadingFinished(requestId, this._response.length);
          }
        }
      }
    }, {
      key: "_clearSubscriptions",
      value: function _clearSubscriptions() {
        (this._subscriptions || []).forEach(function (sub) {
          if (sub) {
            sub.remove();
          }
        });
        this._subscriptions = [];
      }
    }, {
      key: "getAllResponseHeaders",
      value: function getAllResponseHeaders() {
        if (!this.responseHeaders) {
          return null;
        }

        var headers = this.responseHeaders || {};
        return Object.keys(headers).map(function (headerName) {
          return headerName + ': ' + headers[headerName];
        }).join('\r\n');
      }
    }, {
      key: "getResponseHeader",
      value: function getResponseHeader(header) {
        var value = this._lowerCaseResponseHeaders[header.toLowerCase()];

        return value !== undefined ? value : null;
      }
    }, {
      key: "setRequestHeader",
      value: function setRequestHeader(header, value) {
        if (this.readyState !== this.OPENED) {
          throw new Error('Request has not been opened');
        }

        this._headers[header.toLowerCase()] = String(value);
      }
    }, {
      key: "setTrackingName",
      value: function setTrackingName(trackingName) {
        this._trackingName = trackingName;
        return this;
      }
    }, {
      key: "open",
      value: function open(method, url, async) {
        if (this.readyState !== this.UNSENT) {
          throw new Error('Cannot open, already sending');
        }

        if (async !== undefined && !async) {
          throw new Error('Synchronous http requests are not supported');
        }

        if (!url) {
          throw new Error('Cannot load an empty url');
        }

        this._method = method.toUpperCase();
        this._url = url;
        this._aborted = false;
        this.setReadyState(this.OPENED);
      }
    }, {
      key: "send",
      value: function send(data) {
        var _this3 = this;

        if (this.readyState !== this.OPENED) {
          throw new Error('Request has not been opened');
        }

        if (this._sent) {
          throw new Error('Request has already been sent');
        }

        this._sent = true;
        var incrementalEvents = this._incrementalEvents || !!this.onreadystatechange || !!this.onprogress;

        this._subscriptions.push(RCTNetworking.addListener('didSendNetworkData', function (args) {
          return _this3.__didUploadProgress.apply(_this3, babelHelpers.toConsumableArray(args));
        }));

        this._subscriptions.push(RCTNetworking.addListener('didReceiveNetworkResponse', function (args) {
          return _this3.__didReceiveResponse.apply(_this3, babelHelpers.toConsumableArray(args));
        }));

        this._subscriptions.push(RCTNetworking.addListener('didReceiveNetworkData', function (args) {
          return _this3.__didReceiveData.apply(_this3, babelHelpers.toConsumableArray(args));
        }));

        this._subscriptions.push(RCTNetworking.addListener('didReceiveNetworkIncrementalData', function (args) {
          return _this3.__didReceiveIncrementalData.apply(_this3, babelHelpers.toConsumableArray(args));
        }));

        this._subscriptions.push(RCTNetworking.addListener('didReceiveNetworkDataProgress', function (args) {
          return _this3.__didReceiveDataProgress.apply(_this3, babelHelpers.toConsumableArray(args));
        }));

        this._subscriptions.push(RCTNetworking.addListener('didCompleteNetworkResponse', function (args) {
          return _this3.__didCompleteResponse.apply(_this3, babelHelpers.toConsumableArray(args));
        }));

        var nativeResponseType = 'text';

        if (this._responseType === 'arraybuffer') {
          nativeResponseType = 'base64';
        }

        if (this._responseType === 'blob') {
          nativeResponseType = 'blob';
        }

        invariant(this._method, 'Request method needs to be defined.');
        invariant(this._url, 'Request URL needs to be defined.');
        RCTNetworking.sendRequest(this._method, this._trackingName, this._url, this._headers, data, nativeResponseType, incrementalEvents, this.timeout, this.__didCreateRequest.bind(this), this.withCredentials);
      }
    }, {
      key: "abort",
      value: function abort() {
        this._aborted = true;

        if (this._requestId) {
          RCTNetworking.abortRequest(this._requestId);
        }

        if (!(this.readyState === this.UNSENT || this.readyState === this.OPENED && !this._sent || this.readyState === this.DONE)) {
          this._reset();

          this.setReadyState(this.DONE);
        }

        this._reset();
      }
    }, {
      key: "setResponseHeaders",
      value: function setResponseHeaders(responseHeaders) {
        this.responseHeaders = responseHeaders || null;
        var headers = responseHeaders || {};
        this._lowerCaseResponseHeaders = Object.keys(headers).reduce(function (lcaseHeaders, headerName) {
          lcaseHeaders[headerName.toLowerCase()] = headers[headerName];
          return lcaseHeaders;
        }, {});
      }
    }, {
      key: "setReadyState",
      value: function setReadyState(newState) {
        this.readyState = newState;
        this.dispatchEvent({
          type: 'readystatechange'
        });

        if (newState === this.DONE) {
          if (this._aborted) {
            this.dispatchEvent({
              type: 'abort'
            });
          } else if (this._hasError) {
            if (this._timedOut) {
              this.dispatchEvent({
                type: 'timeout'
              });
            } else {
              this.dispatchEvent({
                type: 'error'
              });
            }
          } else {
            this.dispatchEvent({
              type: 'load'
            });
          }

          this.dispatchEvent({
            type: 'loadend'
          });
        }
      }
    }, {
      key: "addEventListener",
      value: function addEventListener(type, listener) {
        if (type === 'readystatechange' || type === 'progress') {
          this._incrementalEvents = true;
        }

        babelHelpers.get(XMLHttpRequest.prototype.__proto__ || Object.getPrototypeOf(XMLHttpRequest.prototype), "addEventListener", this).call(this, type, listener);
      }
    }, {
      key: "responseType",
      get: function get() {
        return this._responseType;
      },
      set: function set(responseType) {
        if (this._sent) {
          throw new Error('Failed to set the \'responseType\' property on \'XMLHttpRequest\': The ' + 'response type cannot be set after the request has been sent.');
        }

        if (!SUPPORTED_RESPONSE_TYPES.hasOwnProperty(responseType)) {
          warning(false, "The provided value '" + responseType + "' is not a valid 'responseType'.");
          return;
        }

        invariant(SUPPORTED_RESPONSE_TYPES[responseType] || responseType === 'document', "The provided value '" + responseType + "' is unsupported in this environment.");

        if (responseType === 'blob') {
          invariant(BlobManager.isAvailable, 'Native module BlobModule is required for blob support');
        }

        this._responseType = responseType;
      }
    }, {
      key: "responseText",
      get: function get() {
        if (this._responseType !== '' && this._responseType !== 'text') {
          throw new Error("The 'responseText' property is only available if 'responseType' " + ("is set to '' or 'text', but it is '" + this._responseType + "'."));
        }

        if (this.readyState < LOADING) {
          return '';
        }

        return this._response;
      }
    }, {
      key: "response",
      get: function get() {
        var responseType = this.responseType;

        if (responseType === '' || responseType === 'text') {
          return this.readyState < LOADING || this._hasError ? '' : this._response;
        }

        if (this.readyState !== DONE) {
          return null;
        }

        if (this._cachedResponse !== undefined) {
          return this._cachedResponse;
        }

        switch (responseType) {
          case 'document':
            this._cachedResponse = null;
            break;

          case 'arraybuffer':
            this._cachedResponse = base64.toByteArray(this._response).buffer;
            break;

          case 'blob':
            if (typeof this._response === 'object' && this._response) {
              this._cachedResponse = BlobManager.createFromOptions(this._response);
            } else {
              throw new Error("Invalid response for blob: " + this._response);
            }

            break;

          case 'json':
            try {
              this._cachedResponse = JSON.parse(this._response);
            } catch (_) {
              this._cachedResponse = null;
            }

            break;

          default:
            this._cachedResponse = null;
        }

        return this._cachedResponse;
      }
    }]);
    return XMLHttpRequest;
  }(EventTarget.apply(undefined, babelHelpers.toConsumableArray(XHR_EVENTS)));

  XMLHttpRequest.UNSENT = UNSENT;
  XMLHttpRequest.OPENED = OPENED;
  XMLHttpRequest.HEADERS_RECEIVED = HEADERS_RECEIVED;
  XMLHttpRequest.LOADING = LOADING;
  XMLHttpRequest.DONE = DONE;
  XMLHttpRequest._interceptor = null;
  module.exports = XMLHttpRequest;
},"79c7f38aacaf813c729418c91402b91c",["720d63c49f383f153c021e592c9e6989","a37e4be6be1b0ee1c58e652f0d542442","350961b4c78178ca4358ee1e601c5e2f","8940a4ad43b101ffc23e725363c70f8d","09babf511a081d9520406a63f452d2ef","6432c6a42bdc71908935daf61e70dba8"],"XMLHttpRequest");