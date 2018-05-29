__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _SwitchRouter = _require(_dependencyMap[0], "./SwitchRouter");

  var _SwitchRouter2 = babelHelpers.interopRequireDefault(_SwitchRouter);

  var _NavigationActions = _require(_dependencyMap[1], "../NavigationActions");

  var _NavigationActions2 = babelHelpers.interopRequireDefault(_NavigationActions);

  var _invariant = _require(_dependencyMap[2], "../utils/invariant");

  var _invariant2 = babelHelpers.interopRequireDefault(_invariant);

  var _withDefaultValue = _require(_dependencyMap[3], "../utils/withDefaultValue");

  var _withDefaultValue2 = babelHelpers.interopRequireDefault(_withDefaultValue);

  var _DrawerActions = _require(_dependencyMap[4], "./DrawerActions");

  var _DrawerActions2 = babelHelpers.interopRequireDefault(_DrawerActions);

  exports.default = function (routeConfigs) {
    var config = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    config = babelHelpers.extends({}, config);
    config = (0, _withDefaultValue2.default)(config, 'resetOnBlur', false);
    config = (0, _withDefaultValue2.default)(config, 'backBehavior', 'initialRoute');
    var switchRouter = (0, _SwitchRouter2.default)(routeConfigs, config);
    return babelHelpers.extends({}, switchRouter, {
      getActionCreators: function getActionCreators(route, navStateKey) {
        return babelHelpers.extends({
          openDrawer: function openDrawer() {
            return _DrawerActions2.default.openDrawer({
              key: navStateKey
            });
          },
          closeDrawer: function closeDrawer() {
            return _DrawerActions2.default.closeDrawer({
              key: navStateKey
            });
          },
          toggleDrawer: function toggleDrawer() {
            return _DrawerActions2.default.toggleDrawer({
              key: navStateKey
            });
          }
        }, switchRouter.getActionCreators(route, navStateKey));
      },
      getStateForAction: function getStateForAction(action, state) {
        if (!state) {
          return babelHelpers.extends({}, switchRouter.getStateForAction(action, undefined), {
            isDrawerOpen: false
          });
        }

        var isRouterTargeted = action.key == null || action.key === state.key;

        if (isRouterTargeted) {
          if (action.type === _DrawerActions2.default.CLOSE_DRAWER && state.isDrawerOpen) {
            return babelHelpers.extends({}, state, {
              isDrawerOpen: false
            });
          }

          if (action.type === _DrawerActions2.default.OPEN_DRAWER && !state.isDrawerOpen) {
            return babelHelpers.extends({}, state, {
              isDrawerOpen: true
            });
          }

          if (action.type === _DrawerActions2.default.TOGGLE_DRAWER) {
            return babelHelpers.extends({}, state, {
              isDrawerOpen: !state.isDrawerOpen
            });
          }
        }

        var switchedState = switchRouter.getStateForAction(action, state);

        if (switchedState === null) {
          return null;
        }

        if (switchedState !== state) {
          if (switchedState.index !== state.index) {
            return babelHelpers.extends({}, switchedState, {
              isDrawerOpen: false
            });
          }

          return switchedState;
        }

        return state;
      }
    });
  };
},"8002f3a49b3e2f1b4db38c0522a53ac6",["9f5d5448f569850fd5db4a2e7fe03ee2","e91a423170a2d063c973900187b02b24","09df40ab147e7353903f31659d93ee58","cf4d0ac993e09ede88fc6b8df3b19ab0","0585470f8233b750e0e3e018473efb42"],"node_modules/react-navigation/src/routers/DrawerRouter.js");