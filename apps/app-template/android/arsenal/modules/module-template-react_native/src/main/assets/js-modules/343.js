__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _NavigationActions = _require(_dependencyMap[0], "../NavigationActions");

  var _NavigationActions2 = babelHelpers.interopRequireDefault(_NavigationActions);

  var _invariant = _require(_dependencyMap[1], "../utils/invariant");

  var _invariant2 = babelHelpers.interopRequireDefault(_invariant);

  var getNavigationActionCreators = function getNavigationActionCreators(route) {
    return {
      goBack: function goBack(key) {
        var actualizedKey = key;

        if (key === undefined && route.key) {
          (0, _invariant2.default)(typeof route.key === 'string', 'key should be a string');
          actualizedKey = route.key;
        }

        return _NavigationActions2.default.back({
          key: actualizedKey
        });
      },
      navigate: function navigate(navigateTo, params, action) {
        if (typeof navigateTo === 'string') {
          return _NavigationActions2.default.navigate({
            routeName: navigateTo,
            params: params,
            action: action
          });
        }

        (0, _invariant2.default)(typeof navigateTo === 'object', 'Must navigateTo an object or a string');
        (0, _invariant2.default)(params == null, 'Params must not be provided to .navigate() when specifying an object');
        (0, _invariant2.default)(action == null, 'Child action must not be provided to .navigate() when specifying an object');
        return _NavigationActions2.default.navigate(navigateTo);
      },
      setParams: function setParams(params) {
        (0, _invariant2.default)(route.key && typeof route.key === 'string', 'setParams cannot be called by root navigator');
        return _NavigationActions2.default.setParams({
          params: params,
          key: route.key
        });
      }
    };
  };

  exports.default = getNavigationActionCreators;
},343,[341,342],"node_modules/react-navigation/src/routers/getNavigationActionCreators.js");