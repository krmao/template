__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var NativeEventEmitter = _require(_dependencyMap[0], 'NativeEventEmitter');

  var RCTLocationObserver = _require(_dependencyMap[1], 'NativeModules').LocationObserver;

  var invariant = _require(_dependencyMap[2], 'fbjs/lib/invariant');

  var logError = _require(_dependencyMap[3], 'logError');

  var warning = _require(_dependencyMap[4], 'fbjs/lib/warning');

  var LocationEventEmitter = new NativeEventEmitter(RCTLocationObserver);

  var Platform = _require(_dependencyMap[5], 'Platform');

  var PermissionsAndroid = _require(_dependencyMap[6], 'PermissionsAndroid');

  var subscriptions = [];
  var updatesEnabled = false;
  var Geolocation = {
    setRNConfiguration: function setRNConfiguration(config) {
      if (RCTLocationObserver.setConfiguration) {
        RCTLocationObserver.setConfiguration(config);
      }
    },
    requestAuthorization: function requestAuthorization() {
      RCTLocationObserver.requestAuthorization();
    },
    getCurrentPosition: function getCurrentPosition(geo_success, geo_error, geo_options) {
      var hasPermission, status;
      return regeneratorRuntime.async(function getCurrentPosition$(_context) {
        while (1) {
          switch (_context.prev = _context.next) {
            case 0:
              invariant(typeof geo_success === 'function', 'Must provide a valid geo_success callback.');
              hasPermission = true;

              if (!(Platform.OS === 'android' && Platform.Version >= 23)) {
                _context.next = 11;
                break;
              }

              _context.next = 5;
              return regeneratorRuntime.awrap(PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION));

            case 5:
              hasPermission = _context.sent;

              if (hasPermission) {
                _context.next = 11;
                break;
              }

              _context.next = 9;
              return regeneratorRuntime.awrap(PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION));

            case 9:
              status = _context.sent;
              hasPermission = status === PermissionsAndroid.RESULTS.GRANTED;

            case 11:
              if (hasPermission) {
                RCTLocationObserver.getCurrentPosition(geo_options || {}, geo_success, geo_error || logError);
              }

            case 12:
            case "end":
              return _context.stop();
          }
        }
      }, null, this);
    },
    watchPosition: function watchPosition(success, error, options) {
      if (!updatesEnabled) {
        RCTLocationObserver.startObserving(options || {});
        updatesEnabled = true;
      }

      var watchID = subscriptions.length;
      subscriptions.push([LocationEventEmitter.addListener('geolocationDidChange', success), error ? LocationEventEmitter.addListener('geolocationError', error) : null]);
      return watchID;
    },
    clearWatch: function clearWatch(watchID) {
      var sub = subscriptions[watchID];

      if (!sub) {
        return;
      }

      sub[0].remove();
      var sub1 = sub[1];
      sub1 && sub1.remove();
      subscriptions[watchID] = undefined;
      var noWatchers = true;

      for (var ii = 0; ii < subscriptions.length; ii++) {
        if (subscriptions[ii]) {
          noWatchers = false;
        }
      }

      if (noWatchers) {
        Geolocation.stopObserving();
      }
    },
    stopObserving: function stopObserving() {
      if (updatesEnabled) {
        RCTLocationObserver.stopObserving();
        updatesEnabled = false;

        for (var ii = 0; ii < subscriptions.length; ii++) {
          var sub = subscriptions[ii];

          if (sub) {
            warning(false, 'Called stopObserving with existing subscriptions.');
            sub[0].remove();
            var sub1 = sub[1];
            sub1 && sub1.remove();
          }
        }

        subscriptions = [];
      }
    }
  };
  module.exports = Geolocation;
},"f01475cdda1d6eb3c4ae0a2b485aaba1",["522e0292cd937e7e7dc15e8d27ea9246","ce21807d4d291be64fa852393519f6c8","8940a4ad43b101ffc23e725363c70f8d","a714f18e0e5e29fe585e1b62a28844f0","09babf511a081d9520406a63f452d2ef","9493a89f5d95c3a8a47c65cfed9b5542","be963805cb6d0666a620727bacebd6d6"],"Geolocation");