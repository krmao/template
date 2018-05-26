__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var NativeModules = _require(_dependencyMap[0], 'NativeModules');

  var RCTAsyncStorage = NativeModules.AsyncRocksDBStorage || NativeModules.AsyncSQLiteDBStorage || NativeModules.AsyncLocalStorage;
  var AsyncStorage = {
    _getRequests: [],
    _getKeys: [],
    _immediate: null,
    getItem: function getItem(key, callback) {
      return new Promise(function (resolve, reject) {
        RCTAsyncStorage.multiGet([key], function (errors, result) {
          var value = result && result[0] && result[0][1] ? result[0][1] : null;
          var errs = convertErrors(errors);
          callback && callback(errs && errs[0], value);

          if (errs) {
            reject(errs[0]);
          } else {
            resolve(value);
          }
        });
      });
    },
    setItem: function setItem(key, value, callback) {
      return new Promise(function (resolve, reject) {
        RCTAsyncStorage.multiSet([[key, value]], function (errors) {
          var errs = convertErrors(errors);
          callback && callback(errs && errs[0]);

          if (errs) {
            reject(errs[0]);
          } else {
            resolve(null);
          }
        });
      });
    },
    removeItem: function removeItem(key, callback) {
      return new Promise(function (resolve, reject) {
        RCTAsyncStorage.multiRemove([key], function (errors) {
          var errs = convertErrors(errors);
          callback && callback(errs && errs[0]);

          if (errs) {
            reject(errs[0]);
          } else {
            resolve(null);
          }
        });
      });
    },
    mergeItem: function mergeItem(key, value, callback) {
      return new Promise(function (resolve, reject) {
        RCTAsyncStorage.multiMerge([[key, value]], function (errors) {
          var errs = convertErrors(errors);
          callback && callback(errs && errs[0]);

          if (errs) {
            reject(errs[0]);
          } else {
            resolve(null);
          }
        });
      });
    },
    clear: function clear(callback) {
      return new Promise(function (resolve, reject) {
        RCTAsyncStorage.clear(function (error) {
          callback && callback(convertError(error));

          if (error && convertError(error)) {
            reject(convertError(error));
          } else {
            resolve(null);
          }
        });
      });
    },
    getAllKeys: function getAllKeys(callback) {
      return new Promise(function (resolve, reject) {
        RCTAsyncStorage.getAllKeys(function (error, keys) {
          callback && callback(convertError(error), keys);

          if (error) {
            reject(convertError(error));
          } else {
            resolve(keys);
          }
        });
      });
    },
    flushGetRequests: function flushGetRequests() {
      var getRequests = this._getRequests;
      var getKeys = this._getKeys;
      this._getRequests = [];
      this._getKeys = [];
      RCTAsyncStorage.multiGet(getKeys, function (errors, result) {
        var map = {};
        result && result.forEach(function (_ref) {
          var _ref2 = babelHelpers.slicedToArray(_ref, 2),
              key = _ref2[0],
              value = _ref2[1];

          map[key] = value;
          return value;
        });
        var reqLength = getRequests.length;

        for (var i = 0; i < reqLength; i++) {
          var request = getRequests[i];
          var requestKeys = request.keys;
          var requestResult = requestKeys.map(function (key) {
            return [key, map[key]];
          });
          request.callback && request.callback(null, requestResult);
          request.resolve && request.resolve(requestResult);
        }
      });
    },
    multiGet: function multiGet(keys, callback) {
      var _this = this;

      if (!this._immediate) {
        this._immediate = setImmediate(function () {
          _this._immediate = null;

          _this.flushGetRequests();
        });
      }

      var getRequest = {
        keys: keys,
        callback: callback,
        keyIndex: this._getKeys.length,
        resolve: null,
        reject: null
      };
      var promiseResult = new Promise(function (resolve, reject) {
        getRequest.resolve = resolve;
        getRequest.reject = reject;
      });

      this._getRequests.push(getRequest);

      keys.forEach(function (key) {
        if (_this._getKeys.indexOf(key) === -1) {
          _this._getKeys.push(key);
        }
      });
      return promiseResult;
    },
    multiSet: function multiSet(keyValuePairs, callback) {
      return new Promise(function (resolve, reject) {
        RCTAsyncStorage.multiSet(keyValuePairs, function (errors) {
          var error = convertErrors(errors);
          callback && callback(error);

          if (error) {
            reject(error);
          } else {
            resolve(null);
          }
        });
      });
    },
    multiRemove: function multiRemove(keys, callback) {
      return new Promise(function (resolve, reject) {
        RCTAsyncStorage.multiRemove(keys, function (errors) {
          var error = convertErrors(errors);
          callback && callback(error);

          if (error) {
            reject(error);
          } else {
            resolve(null);
          }
        });
      });
    },
    multiMerge: function multiMerge(keyValuePairs, callback) {
      return new Promise(function (resolve, reject) {
        RCTAsyncStorage.multiMerge(keyValuePairs, function (errors) {
          var error = convertErrors(errors);
          callback && callback(error);

          if (error) {
            reject(error);
          } else {
            resolve(null);
          }
        });
      });
    }
  };

  if (!RCTAsyncStorage.multiMerge) {
    delete AsyncStorage.mergeItem;
    delete AsyncStorage.multiMerge;
  }

  function convertErrors(errs) {
    if (!errs) {
      return null;
    }

    return (Array.isArray(errs) ? errs : [errs]).map(function (e) {
      return convertError(e);
    });
  }

  function convertError(error) {
    if (!error) {
      return null;
    }

    var out = new Error(error.message);
    out.key = error.key;
    return out;
  }

  module.exports = AsyncStorage;
},321,[24],"AsyncStorage");