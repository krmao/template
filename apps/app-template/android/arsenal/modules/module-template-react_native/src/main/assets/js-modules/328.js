__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Map = _require(_dependencyMap[0], 'Map');

  var NativeEventEmitter = _require(_dependencyMap[1], 'NativeEventEmitter');

  var NativeModules = _require(_dependencyMap[2], 'NativeModules');

  var Platform = _require(_dependencyMap[3], 'Platform');

  var RCTNetInfo = NativeModules.NetInfo;
  var NetInfoEventEmitter = new NativeEventEmitter(RCTNetInfo);
  var DEVICE_CONNECTIVITY_EVENT = 'networkStatusDidChange';

  var _subscriptions = new Map();

  var _isConnectedDeprecated = void 0;

  if (Platform.OS === 'ios') {
    _isConnectedDeprecated = function _isConnectedDeprecated(reachability) {
      return reachability !== 'none' && reachability !== 'unknown';
    };
  } else if (Platform.OS === 'android') {
    _isConnectedDeprecated = function _isConnectedDeprecated(connectionType) {
      return connectionType !== 'NONE' && connectionType !== 'UNKNOWN';
    };
  }

  function _isConnected(connection) {
    return connection.type !== 'none' && connection.type !== 'unknown';
  }

  var _isConnectedSubscriptions = new Map();

  var NetInfo = {
    addEventListener: function addEventListener(eventName, handler) {
      var listener = void 0;

      if (eventName === 'connectionChange') {
        listener = NetInfoEventEmitter.addListener(DEVICE_CONNECTIVITY_EVENT, function (appStateData) {
          handler({
            type: appStateData.connectionType,
            effectiveType: appStateData.effectiveConnectionType
          });
        });
      } else if (eventName === 'change') {
        console.warn('NetInfo\'s "change" event is deprecated. Listen to the "connectionChange" event instead.');
        listener = NetInfoEventEmitter.addListener(DEVICE_CONNECTIVITY_EVENT, function (appStateData) {
          handler(appStateData.network_info);
        });
      } else {
        console.warn('Trying to subscribe to unknown event: "' + eventName + '"');
        return {
          remove: function remove() {}
        };
      }

      _subscriptions.set(handler, listener);

      return {
        remove: function remove() {
          return NetInfo.removeEventListener(eventName, handler);
        }
      };
    },
    removeEventListener: function removeEventListener(eventName, handler) {
      var listener = _subscriptions.get(handler);

      if (!listener) {
        return;
      }

      listener.remove();

      _subscriptions.delete(handler);
    },
    fetch: function fetch() {
      console.warn('NetInfo.fetch() is deprecated. Use NetInfo.getConnectionInfo() instead.');
      return RCTNetInfo.getCurrentConnectivity().then(function (resp) {
        return resp.network_info;
      });
    },
    getConnectionInfo: function getConnectionInfo() {
      return RCTNetInfo.getCurrentConnectivity().then(function (resp) {
        return {
          type: resp.connectionType,
          effectiveType: resp.effectiveConnectionType
        };
      });
    },
    isConnected: {
      addEventListener: function addEventListener(eventName, handler) {
        var listener = function listener(connection) {
          if (eventName === 'change') {
            handler(_isConnectedDeprecated(connection));
          } else if (eventName === 'connectionChange') {
            handler(_isConnected(connection));
          }
        };

        _isConnectedSubscriptions.set(handler, listener);

        NetInfo.addEventListener(eventName, listener);
        return {
          remove: function remove() {
            return NetInfo.isConnected.removeEventListener(eventName, handler);
          }
        };
      },
      removeEventListener: function removeEventListener(eventName, handler) {
        var listener = _isConnectedSubscriptions.get(handler);

        NetInfo.removeEventListener(eventName, listener);

        _isConnectedSubscriptions.delete(handler);
      },
      fetch: function fetch() {
        return NetInfo.getConnectionInfo().then(_isConnected);
      }
    },
    isConnectionExpensive: function isConnectionExpensive() {
      return Platform.OS === 'android' ? RCTNetInfo.isConnectionMetered() : Promise.reject(new Error('Currently not supported on iOS'));
    }
  };
  module.exports = NetInfo;
},328,[54,82,24,32],"NetInfo");