__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _invariant = _require(_dependencyMap[0], "../utils/invariant");

  var _invariant2 = babelHelpers.interopRequireDefault(_invariant);

  var _getScreenForRouteName = _require(_dependencyMap[1], "./getScreenForRouteName");

  var _getScreenForRouteName2 = babelHelpers.interopRequireDefault(_getScreenForRouteName);

  var _createConfigGetter = _require(_dependencyMap[2], "./createConfigGetter");

  var _createConfigGetter2 = babelHelpers.interopRequireDefault(_createConfigGetter);

  var _NavigationActions = _require(_dependencyMap[3], "../NavigationActions");

  var _NavigationActions2 = babelHelpers.interopRequireDefault(_NavigationActions);

  var _StackActions = _require(_dependencyMap[4], "./StackActions");

  var _StackActions2 = babelHelpers.interopRequireDefault(_StackActions);

  var _validateRouteConfigMap = _require(_dependencyMap[5], "./validateRouteConfigMap");

  var _validateRouteConfigMap2 = babelHelpers.interopRequireDefault(_validateRouteConfigMap);

  var _getNavigationActionCreators = _require(_dependencyMap[6], "./getNavigationActionCreators");

  var _getNavigationActionCreators2 = babelHelpers.interopRequireDefault(_getNavigationActionCreators);

  var defaultActionCreators = function defaultActionCreators(route, navStateKey) {
    return {};
  };

  function childrenUpdateWithoutSwitchingIndex(actionType) {
    return [_NavigationActions2.default.SET_PARAMS, _StackActions2.default.COMPLETE_TRANSITION].includes(actionType);
  }

  exports.default = function (routeConfigs) {
    var config = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    (0, _validateRouteConfigMap2.default)(routeConfigs);
    var order = config.order || Object.keys(routeConfigs);
    var paths = config.paths || {};
    var getCustomActionCreators = config.getCustomActionCreators || defaultActionCreators;
    var initialRouteParams = config.initialRouteParams;
    var initialRouteName = config.initialRouteName || order[0];
    var backBehavior = config.backBehavior || 'none';
    var shouldBackNavigateToInitialRoute = backBehavior === 'initialRoute';
    var resetOnBlur = config.hasOwnProperty('resetOnBlur') ? config.resetOnBlur : true;
    var initialRouteIndex = order.indexOf(initialRouteName);
    var childRouters = {};
    order.forEach(function (routeName) {
      var routeConfig = routeConfigs[routeName];

      if (!paths[routeName]) {
        paths[routeName] = typeof routeConfig.path === 'string' ? routeConfig.path : routeName;
      }

      childRouters[routeName] = null;
      var screen = (0, _getScreenForRouteName2.default)(routeConfigs, routeName);

      if (screen.router) {
        childRouters[routeName] = screen.router;
      }
    });

    if (initialRouteIndex === -1) {
      throw new Error("Invalid initialRouteName '" + initialRouteName + "'." + ("Should be one of " + order.map(function (n) {
        return "\"" + n + "\"";
      }).join(', ')));
    }

    function resetChildRoute(routeName) {
      var params = routeName === initialRouteName ? initialRouteParams : undefined;
      var childRouter = childRouters[routeName];

      if (childRouter) {
        var childAction = _NavigationActions2.default.init();

        return babelHelpers.extends({}, childRouter.getStateForAction(childAction), {
          key: routeName,
          routeName: routeName,
          params: params
        });
      }

      return {
        key: routeName,
        routeName: routeName,
        params: params
      };
    }

    return {
      getInitialState: function getInitialState() {
        var routes = order.map(resetChildRoute);
        return {
          routes: routes,
          index: initialRouteIndex,
          isTransitioning: false
        };
      },
      getNextState: function getNextState(prevState, possibleNextState) {
        if (!prevState) {
          return possibleNextState;
        }

        var nextState = void 0;

        if (prevState.index !== possibleNextState.index && resetOnBlur) {
          var prevRouteName = prevState.routes[prevState.index].routeName;
          var nextRoutes = [].concat(babelHelpers.toConsumableArray(possibleNextState.routes));
          nextRoutes[prevState.index] = resetChildRoute(prevRouteName);
          return babelHelpers.extends({}, possibleNextState, {
            routes: nextRoutes
          });
        } else {
          nextState = possibleNextState;
        }

        return nextState;
      },
      getActionCreators: function getActionCreators(route, stateKey) {
        return babelHelpers.extends({}, (0, _getNavigationActionCreators2.default)(route), getCustomActionCreators(route, stateKey));
      },
      getStateForAction: function getStateForAction(action, inputState) {
        var prevState = inputState ? babelHelpers.extends({}, inputState) : inputState;
        var state = inputState || this.getInitialState();
        var activeChildIndex = state.index;

        if (action.type === _NavigationActions2.default.INIT) {
          var params = action.params;

          if (params) {
            state.routes = state.routes.map(function (route) {
              return babelHelpers.extends({}, route, {
                params: babelHelpers.extends({}, route.params, params, route.routeName === initialRouteName ? initialRouteParams : null)
              });
            });
          }
        }

        var activeChildLastState = state.routes[state.index];
        var activeChildRouter = childRouters[order[state.index]];

        if (activeChildRouter) {
          var activeChildState = activeChildRouter.getStateForAction(action, activeChildLastState);

          if (!activeChildState && inputState) {
            return null;
          }

          if (activeChildState && activeChildState !== activeChildLastState) {
            var _routes = [].concat(babelHelpers.toConsumableArray(state.routes));

            _routes[state.index] = activeChildState;
            return this.getNextState(prevState, babelHelpers.extends({}, state, {
              routes: _routes
            }));
          }
        }

        var isBackEligible = action.key == null || action.key === activeChildLastState.key;

        if (action.type === _NavigationActions2.default.BACK) {
          if (isBackEligible && shouldBackNavigateToInitialRoute) {
            activeChildIndex = initialRouteIndex;
          } else {
            return state;
          }
        }

        var didNavigate = false;

        if (action.type === _NavigationActions2.default.NAVIGATE) {
          didNavigate = !!order.find(function (childId, i) {
            if (childId === action.routeName) {
              activeChildIndex = i;
              return true;
            }

            return false;
          });

          if (didNavigate) {
            var childState = state.routes[activeChildIndex];
            var childRouter = childRouters[action.routeName];
            var newChildState = void 0;

            if (action.action) {
              newChildState = childRouter ? childRouter.getStateForAction(action.action, childState) : null;
            } else if (!action.action && !childRouter && action.params) {
              newChildState = babelHelpers.extends({}, childState, {
                params: babelHelpers.extends({}, childState.params || {}, action.params)
              });
            }

            if (newChildState && newChildState !== childState) {
              var _routes2 = [].concat(babelHelpers.toConsumableArray(state.routes));

              _routes2[activeChildIndex] = newChildState;
              return this.getNextState(prevState, babelHelpers.extends({}, state, {
                routes: _routes2,
                index: activeChildIndex
              }));
            } else if (!newChildState && state.index === activeChildIndex && prevState) {
              return null;
            }
          }
        }

        if (action.type === _NavigationActions2.default.SET_PARAMS) {
          var key = action.key;
          var lastRoute = state.routes.find(function (route) {
            return route.key === key;
          });

          if (lastRoute) {
            var _params = babelHelpers.extends({}, lastRoute.params, action.params);

            var _routes3 = [].concat(babelHelpers.toConsumableArray(state.routes));

            _routes3[state.routes.indexOf(lastRoute)] = babelHelpers.extends({}, lastRoute, {
              params: _params
            });
            return this.getNextState(prevState, babelHelpers.extends({}, state, {
              routes: _routes3
            }));
          }
        }

        if (activeChildIndex !== state.index) {
          return this.getNextState(prevState, babelHelpers.extends({}, state, {
            index: activeChildIndex
          }));
        } else if (didNavigate && !inputState) {
          return state;
        } else if (didNavigate) {
          return babelHelpers.extends({}, state);
        }

        var index = state.index;
        var routes = state.routes;
        order.find(function (childId, i) {
          var childRouter = childRouters[childId];

          if (i === index) {
            return false;
          }

          var childState = routes[i];

          if (childRouter) {
            childState = childRouter.getStateForAction(action, childState);
          }

          if (!childState) {
            index = i;
            return true;
          }

          if (childState !== routes[i]) {
            routes = [].concat(babelHelpers.toConsumableArray(routes));
            routes[i] = childState;
            index = i;
            return true;
          }

          return false;
        });

        if (childrenUpdateWithoutSwitchingIndex(action.type)) {
          index = state.index;
        }

        if (index !== state.index || routes !== state.routes) {
          return this.getNextState(prevState, babelHelpers.extends({}, state, {
            index: index,
            routes: routes
          }));
        }

        return state;
      },
      getComponentForState: function getComponentForState(state) {
        var routeName = state.routes[state.index].routeName;
        (0, _invariant2.default)(routeName, "There is no route defined for index " + state.index + ". Check that\n        that you passed in a navigation state with a valid tab/screen index.");
        var childRouter = childRouters[routeName];

        if (childRouter) {
          return childRouter.getComponentForState(state.routes[state.index]);
        }

        return (0, _getScreenForRouteName2.default)(routeConfigs, routeName);
      },
      getComponentForRouteName: function getComponentForRouteName(routeName) {
        return (0, _getScreenForRouteName2.default)(routeConfigs, routeName);
      },
      getPathAndParamsForState: function getPathAndParamsForState(state) {
        var route = state.routes[state.index];
        var routeName = order[state.index];
        var subPath = paths[routeName];
        var screen = (0, _getScreenForRouteName2.default)(routeConfigs, routeName);
        var path = subPath;
        var params = route.params;

        if (screen && screen.router) {
          var stateRoute = route;
          var child = screen.router.getPathAndParamsForState(stateRoute);
          path = subPath ? subPath + "/" + child.path : child.path;
          params = child.params ? babelHelpers.extends({}, params, child.params) : params;
        }

        return {
          path: path,
          params: params
        };
      },
      getActionForPathAndParams: function getActionForPathAndParams(path, params) {
        if (!path) {
          return _NavigationActions2.default.navigate({
            routeName: initialRouteName,
            params: params
          });
        }

        return order.map(function (childId) {
          var parts = path.split('/');
          var pathToTest = paths[childId];
          var partsInTestPath = pathToTest.split('/').length;
          var pathPartsToTest = parts.slice(0, partsInTestPath).join('/');

          if (pathPartsToTest === pathToTest) {
            var childRouter = childRouters[childId];

            var action = _NavigationActions2.default.navigate({
              routeName: childId
            });

            if (childRouter && childRouter.getActionForPathAndParams) {
              action.action = childRouter.getActionForPathAndParams(parts.slice(partsInTestPath).join('/'), params);
            }

            if (params) {
              action.params = params;
            }

            return action;
          }

          return null;
        }).find(function (action) {
          return !!action;
        }) || order.map(function (childId) {
          var childRouter = childRouters[childId];
          return childRouter && childRouter.getActionForPathAndParams(path, params);
        }).find(function (action) {
          return !!action;
        }) || null;
      },
      getScreenOptions: (0, _createConfigGetter2.default)(routeConfigs, config.navigationOptions)
    };
  };
},"9f5d5448f569850fd5db4a2e7fe03ee2",["09df40ab147e7353903f31659d93ee58","6405153adc2f639554606506cfaae361","b4f5a74289f659543a354c90625eb19f","e91a423170a2d063c973900187b02b24","6bd004bef2f53ff343e677f2e121d275","af70b47f27a9c71070efb68d037eb634","1c61d573044491492645aa9a9dd9fc34"],"node_modules/react-navigation/src/routers/SwitchRouter.js");