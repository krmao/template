__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var MissingNativeEventEmitterShim = _require(_dependencyMap[0], 'MissingNativeEventEmitterShim');

  var NativeEventEmitter = _require(_dependencyMap[1], 'NativeEventEmitter');

  var NativeModules = _require(_dependencyMap[2], 'NativeModules');

  var RCTAppState = NativeModules.AppState;

  var logError = _require(_dependencyMap[3], 'logError');

  var invariant = _require(_dependencyMap[4], 'fbjs/lib/invariant');

  var AppState = function (_NativeEventEmitter) {
    babelHelpers.inherits(AppState, _NativeEventEmitter);

    function AppState() {
      babelHelpers.classCallCheck(this, AppState);

      var _this = babelHelpers.possibleConstructorReturn(this, (AppState.__proto__ || Object.getPrototypeOf(AppState)).call(this, RCTAppState));

      _this.isAvailable = true;
      _this.isAvailable = true;
      _this._eventHandlers = {
        change: new Map(),
        memoryWarning: new Map()
      };
      _this.currentState = RCTAppState.initialAppState || 'active';
      var eventUpdated = false;

      _this.addListener('appStateDidChange', function (appStateData) {
        eventUpdated = true;
        _this.currentState = appStateData.app_state;
      });

      RCTAppState.getCurrentAppState(function (appStateData) {
        if (!eventUpdated) {
          _this.currentState = appStateData.app_state;
        }
      }, logError);
      return _this;
    }

    babelHelpers.createClass(AppState, [{
      key: "addEventListener",
      value: function addEventListener(type, handler) {
        invariant(['change', 'memoryWarning'].indexOf(type) !== -1, 'Trying to subscribe to unknown event: "%s"', type);

        if (type === 'change') {
          this._eventHandlers[type].set(handler, this.addListener('appStateDidChange', function (appStateData) {
            handler(appStateData.app_state);
          }));
        } else if (type === 'memoryWarning') {
          this._eventHandlers[type].set(handler, this.addListener('memoryWarning', handler));
        }
      }
    }, {
      key: "removeEventListener",
      value: function removeEventListener(type, handler) {
        invariant(['change', 'memoryWarning'].indexOf(type) !== -1, 'Trying to remove listener for unknown event: "%s"', type);

        if (!this._eventHandlers[type].has(handler)) {
          return;
        }

        this._eventHandlers[type].get(handler).remove();

        this._eventHandlers[type].delete(handler);
      }
    }]);
    return AppState;
  }(NativeEventEmitter);

  if (__DEV__ && !RCTAppState) {
    var MissingNativeAppStateShim = function (_MissingNativeEventEm) {
      babelHelpers.inherits(MissingNativeAppStateShim, _MissingNativeEventEm);

      function MissingNativeAppStateShim() {
        babelHelpers.classCallCheck(this, MissingNativeAppStateShim);
        return babelHelpers.possibleConstructorReturn(this, (MissingNativeAppStateShim.__proto__ || Object.getPrototypeOf(MissingNativeAppStateShim)).call(this, 'RCTAppState', 'AppState'));
      }

      babelHelpers.createClass(MissingNativeAppStateShim, [{
        key: "addEventListener",
        value: function addEventListener() {
          this.throwMissingNativeModule();
        }
      }, {
        key: "removeEventListener",
        value: function removeEventListener() {
          this.throwMissingNativeModule();
        }
      }, {
        key: "currentState",
        get: function get() {
          this.throwMissingNativeModule();
        }
      }]);
      return MissingNativeAppStateShim;
    }(MissingNativeEventEmitterShim);

    AppState = new MissingNativeAppStateShim();
  } else {
    AppState = new AppState();
  }

  module.exports = AppState;
},114,[81,82,24,98,18],"AppState");