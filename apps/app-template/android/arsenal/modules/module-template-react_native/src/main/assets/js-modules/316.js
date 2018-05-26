__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var RCTDeviceEventEmitter = _require(_dependencyMap[0], 'RCTDeviceEventEmitter');

  var Map = _require(_dependencyMap[1], 'Map');

  var infoLog = _require(_dependencyMap[2], 'infoLog');

  function defaultExtras() {
    BugReporting.addFileSource('react_hierarchy.txt', function () {
      return _require(_dependencyMap[3], 'dumpReactTree')();
    });
  }

  var BugReporting = function () {
    function BugReporting() {
      babelHelpers.classCallCheck(this, BugReporting);
    }

    babelHelpers.createClass(BugReporting, null, [{
      key: "_maybeInit",
      value: function _maybeInit() {
        if (!BugReporting._subscription) {
          BugReporting._subscription = RCTDeviceEventEmitter.addListener('collectBugExtraData', BugReporting.collectExtraData, null);
          defaultExtras();
        }

        if (!BugReporting._redboxSubscription) {
          BugReporting._redboxSubscription = RCTDeviceEventEmitter.addListener('collectRedBoxExtraData', BugReporting.collectExtraData, null);
        }
      }
    }, {
      key: "addSource",
      value: function addSource(key, callback) {
        return this._addSource(key, callback, BugReporting._extraSources);
      }
    }, {
      key: "addFileSource",
      value: function addFileSource(key, callback) {
        return this._addSource(key, callback, BugReporting._fileSources);
      }
    }, {
      key: "_addSource",
      value: function _addSource(key, callback, source) {
        BugReporting._maybeInit();

        if (source.has(key)) {
          console.warn("BugReporting.add* called multiple times for same key '" + key + "'");
        }

        source.set(key, callback);
        return {
          remove: function remove() {
            source.delete(key);
          }
        };
      }
    }, {
      key: "collectExtraData",
      value: function collectExtraData() {
        var extraData = {};

        for (var _iterator = BugReporting._extraSources, _isArray = Array.isArray(_iterator), _i = 0, _iterator = _isArray ? _iterator : _iterator[typeof Symbol === "function" ? Symbol.iterator : "@@iterator"]();;) {
          var _ref3;

          if (_isArray) {
            if (_i >= _iterator.length) break;
            _ref3 = _iterator[_i++];
          } else {
            _i = _iterator.next();
            if (_i.done) break;
            _ref3 = _i.value;
          }

          var _ref = _ref3;

          var _ref2 = babelHelpers.slicedToArray(_ref, 2);

          var _key = _ref2[0];
          var callback = _ref2[1];
          extraData[_key] = callback();
        }

        var fileData = {};

        for (var _iterator2 = BugReporting._fileSources, _isArray2 = Array.isArray(_iterator2), _i2 = 0, _iterator2 = _isArray2 ? _iterator2 : _iterator2[typeof Symbol === "function" ? Symbol.iterator : "@@iterator"]();;) {
          var _ref6;

          if (_isArray2) {
            if (_i2 >= _iterator2.length) break;
            _ref6 = _iterator2[_i2++];
          } else {
            _i2 = _iterator2.next();
            if (_i2.done) break;
            _ref6 = _i2.value;
          }

          var _ref4 = _ref6;

          var _ref5 = babelHelpers.slicedToArray(_ref4, 2);

          var _key2 = _ref5[0];
          var _callback = _ref5[1];
          fileData[_key2] = _callback();
        }

        infoLog('BugReporting extraData:', extraData);

        var BugReportingNativeModule = _require(_dependencyMap[4], 'NativeModules').BugReporting;

        BugReportingNativeModule && BugReportingNativeModule.setExtraData && BugReportingNativeModule.setExtraData(extraData, fileData);

        var RedBoxNativeModule = _require(_dependencyMap[4], 'NativeModules').RedBox;

        RedBoxNativeModule && RedBoxNativeModule.setExtraData && RedBoxNativeModule.setExtraData(extraData, 'From BugReporting.js');
        return {
          extras: extraData,
          files: fileData
        };
      }
    }]);
    return BugReporting;
  }();

  BugReporting._extraSources = new Map();
  BugReporting._fileSources = new Map();
  BugReporting._subscription = null;
  BugReporting._redboxSubscription = null;
  module.exports = BugReporting;
},316,[40,54,105,317,24],"BugReporting");