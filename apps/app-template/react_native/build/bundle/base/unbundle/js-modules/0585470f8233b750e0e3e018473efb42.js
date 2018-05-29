__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var OPEN_DRAWER = 'Navigation/OPEN_DRAWER';
  var CLOSE_DRAWER = 'Navigation/CLOSE_DRAWER';
  var TOGGLE_DRAWER = 'Navigation/TOGGLE_DRAWER';

  var openDrawer = function openDrawer(payload) {
    return babelHelpers.extends({
      type: OPEN_DRAWER
    }, payload);
  };

  var closeDrawer = function closeDrawer(payload) {
    return babelHelpers.extends({
      type: CLOSE_DRAWER
    }, payload);
  };

  var toggleDrawer = function toggleDrawer(payload) {
    return babelHelpers.extends({
      type: TOGGLE_DRAWER
    }, payload);
  };

  exports.default = {
    OPEN_DRAWER: OPEN_DRAWER,
    CLOSE_DRAWER: CLOSE_DRAWER,
    TOGGLE_DRAWER: TOGGLE_DRAWER,
    openDrawer: openDrawer,
    closeDrawer: closeDrawer,
    toggleDrawer: toggleDrawer
  };
},"0585470f8233b750e0e3e018473efb42",[],"node_modules/react-navigation/src/routers/DrawerActions.js");