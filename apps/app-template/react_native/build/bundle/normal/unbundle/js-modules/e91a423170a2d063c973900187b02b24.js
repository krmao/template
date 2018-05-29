__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var BACK = 'Navigation/BACK';
  var INIT = 'Navigation/INIT';
  var NAVIGATE = 'Navigation/NAVIGATE';
  var SET_PARAMS = 'Navigation/SET_PARAMS';

  var back = function back() {
    var payload = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
    return {
      type: BACK,
      key: payload.key,
      immediate: payload.immediate
    };
  };

  var init = function init() {
    var payload = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
    var action = {
      type: INIT
    };

    if (payload.params) {
      action.params = payload.params;
    }

    return action;
  };

  var navigate = function navigate(payload) {
    var action = {
      type: NAVIGATE,
      routeName: payload.routeName
    };

    if (payload.params) {
      action.params = payload.params;
    }

    if (payload.action) {
      action.action = payload.action;
    }

    if (payload.key) {
      action.key = payload.key;
    }

    return action;
  };

  var setParams = function setParams(payload) {
    return {
      type: SET_PARAMS,
      key: payload.key,
      params: payload.params
    };
  };

  exports.default = {
    BACK: BACK,
    INIT: INIT,
    NAVIGATE: NAVIGATE,
    SET_PARAMS: SET_PARAMS,
    back: back,
    init: init,
    navigate: navigate,
    setParams: setParams
  };
},"e91a423170a2d063c973900187b02b24",[],"node_modules/react-navigation/src/NavigationActions.js");