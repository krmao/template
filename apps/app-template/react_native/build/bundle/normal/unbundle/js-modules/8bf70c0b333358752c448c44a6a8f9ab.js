__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _invariant = _require(_dependencyMap[0], "./utils/invariant");

  var _invariant2 = babelHelpers.interopRequireDefault(_invariant);

  var StateUtils = {
    get: function get(state, key) {
      return state.routes.find(function (route) {
        return route.key === key;
      }) || null;
    },
    indexOf: function indexOf(state, key) {
      return state.routes.findIndex(function (route) {
        return route.key === key;
      });
    },
    has: function has(state, key) {
      return !!state.routes.some(function (route) {
        return route.key === key;
      });
    },
    push: function push(state, route) {
      (0, _invariant2.default)(StateUtils.indexOf(state, route.key) === -1, 'should not push route with duplicated key %s', route.key);
      var routes = state.routes.slice();
      routes.push(route);
      return babelHelpers.extends({}, state, {
        index: routes.length - 1,
        routes: routes
      });
    },
    pop: function pop(state) {
      if (state.index <= 0) {
        return state;
      }

      var routes = state.routes.slice(0, -1);
      return babelHelpers.extends({}, state, {
        index: routes.length - 1,
        routes: routes
      });
    },
    jumpToIndex: function jumpToIndex(state, index) {
      if (index === state.index) {
        return state;
      }

      (0, _invariant2.default)(!!state.routes[index], 'invalid index %s to jump to', index);
      return babelHelpers.extends({}, state, {
        index: index
      });
    },
    jumpTo: function jumpTo(state, key) {
      var index = StateUtils.indexOf(state, key);
      return StateUtils.jumpToIndex(state, index);
    },
    back: function back(state) {
      var index = state.index - 1;
      var route = state.routes[index];
      return route ? StateUtils.jumpToIndex(state, index) : state;
    },
    forward: function forward(state) {
      var index = state.index + 1;
      var route = state.routes[index];
      return route ? StateUtils.jumpToIndex(state, index) : state;
    },
    replaceAndPrune: function replaceAndPrune(state, key, route) {
      var index = StateUtils.indexOf(state, key);
      var replaced = StateUtils.replaceAtIndex(state, index, route);
      return babelHelpers.extends({}, replaced, {
        routes: replaced.routes.slice(0, index + 1)
      });
    },
    replaceAt: function replaceAt(state, key, route) {
      var index = StateUtils.indexOf(state, key);
      return StateUtils.replaceAtIndex(state, index, route);
    },
    replaceAtIndex: function replaceAtIndex(state, index, route) {
      (0, _invariant2.default)(!!state.routes[index], 'invalid index %s for replacing route %s', index, route.key);

      if (state.routes[index] === route && index === state.index) {
        return state;
      }

      var routes = state.routes.slice();
      routes[index] = route;
      return babelHelpers.extends({}, state, {
        index: index,
        routes: routes
      });
    },
    reset: function reset(state, routes, index) {
      (0, _invariant2.default)(routes.length && Array.isArray(routes), 'invalid routes to replace');
      var nextIndex = index === undefined ? routes.length - 1 : index;

      if (state.routes.length === routes.length && state.index === nextIndex) {
        var compare = function compare(route, ii) {
          return routes[ii] === route;
        };

        if (state.routes.every(compare)) {
          return state;
        }
      }

      (0, _invariant2.default)(!!routes[nextIndex], 'invalid index %s to reset', nextIndex);
      return babelHelpers.extends({}, state, {
        index: nextIndex,
        routes: routes
      });
    }
  };
  exports.default = StateUtils;
},"8bf70c0b333358752c448c44a6a8f9ab",["09df40ab147e7353903f31659d93ee58"],"node_modules/react-navigation/src/StateUtils.js");