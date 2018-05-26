__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _pathToRegexp = _require(_dependencyMap[0], "path-to-regexp");

  var _pathToRegexp2 = babelHelpers.interopRequireDefault(_pathToRegexp);

  var _NavigationActions = _require(_dependencyMap[1], "../NavigationActions");

  var _NavigationActions2 = babelHelpers.interopRequireDefault(_NavigationActions);

  var _StackActions = _require(_dependencyMap[2], "./StackActions");

  var _StackActions2 = babelHelpers.interopRequireDefault(_StackActions);

  var _createConfigGetter = _require(_dependencyMap[3], "./createConfigGetter");

  var _createConfigGetter2 = babelHelpers.interopRequireDefault(_createConfigGetter);

  var _getScreenForRouteName = _require(_dependencyMap[4], "./getScreenForRouteName");

  var _getScreenForRouteName2 = babelHelpers.interopRequireDefault(_getScreenForRouteName);

  var _StateUtils = _require(_dependencyMap[5], "../StateUtils");

  var _StateUtils2 = babelHelpers.interopRequireDefault(_StateUtils);

  var _validateRouteConfigMap = _require(_dependencyMap[6], "./validateRouteConfigMap");

  var _validateRouteConfigMap2 = babelHelpers.interopRequireDefault(_validateRouteConfigMap);

  var _invariant = _require(_dependencyMap[7], "../utils/invariant");

  var _invariant2 = babelHelpers.interopRequireDefault(_invariant);

  var _KeyGenerator = _require(_dependencyMap[8], "./KeyGenerator");

  var _getNavigationActionCreators = _require(_dependencyMap[9], "./getNavigationActionCreators");

  var _getNavigationActionCreators2 = babelHelpers.interopRequireDefault(_getNavigationActionCreators);

  function isEmpty(obj) {
    if (!obj) return true;

    for (var key in obj) {
      return false;
    }

    return true;
  }

  function behavesLikePushAction(action) {
    return action.type === _NavigationActions2.default.NAVIGATE || action.type === _StackActions2.default.PUSH;
  }

  var defaultActionCreators = function defaultActionCreators(route, navStateKey) {
    return {};
  };

  function isResetToRootStack(action) {
    return action.type === _StackActions2.default.RESET && action.key === null;
  }

  exports.default = function (routeConfigs) {
    var stackConfig = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    (0, _validateRouteConfigMap2.default)(routeConfigs);
    var childRouters = {};
    var routeNames = Object.keys(routeConfigs);
    routeNames.forEach(function (routeName) {
      var screen = (0, _getScreenForRouteName2.default)(routeConfigs, routeName);

      if (screen && screen.router) {
        childRouters[routeName] = screen.router;
      } else {
        childRouters[routeName] = null;
      }
    });
    var initialRouteParams = stackConfig.initialRouteParams;
    var getCustomActionCreators = stackConfig.getCustomActionCreators || defaultActionCreators;
    var initialRouteName = stackConfig.initialRouteName || routeNames[0];
    var initialChildRouter = childRouters[initialRouteName];
    var pathsByRouteNames = babelHelpers.extends({}, stackConfig.paths) || {};
    var paths = [];

    function getInitialState(action) {
      var route = {};
      var childRouter = childRouters[action.routeName];

      if (behavesLikePushAction(action) && childRouter !== undefined) {
        var childState = {};

        if (childRouter !== null) {
          var childAction = action.action || _NavigationActions2.default.init({
            params: action.params
          });

          childState = childRouter.getStateForAction(childAction);
        }

        return {
          key: 'StackRouterRoot',
          isTransitioning: false,
          index: 0,
          routes: [babelHelpers.extends({
            params: action.params
          }, childState, {
            key: action.key || (0, _KeyGenerator.generateKey)(),
            routeName: action.routeName
          })]
        };
      }

      if (initialChildRouter) {
        route = initialChildRouter.getStateForAction(_NavigationActions2.default.navigate({
          routeName: initialRouteName,
          params: initialRouteParams
        }));
      }

      var params = (route.params || action.params || initialRouteParams) && babelHelpers.extends({}, route.params || {}, action.params || {}, initialRouteParams || {});
      var initialRouteKey = stackConfig.initialRouteKey;
      route = babelHelpers.extends({}, route, params ? {
        params: params
      } : {}, {
        routeName: initialRouteName,
        key: action.key || initialRouteKey || (0, _KeyGenerator.generateKey)()
      });
      return {
        key: 'StackRouterRoot',
        isTransitioning: false,
        index: 0,
        routes: [route]
      };
    }

    routeNames.forEach(function (routeName) {
      var pathPattern = pathsByRouteNames[routeName] || routeConfigs[routeName].path;
      var matchExact = !!pathPattern && !childRouters[routeName];

      if (pathPattern === undefined) {
        pathPattern = routeName;
      }

      var keys = [];
      var re = void 0,
          toPath = void 0,
          priority = void 0;

      if (typeof pathPattern === 'string') {
        re = (0, _pathToRegexp2.default)(pathPattern, keys);
        toPath = _pathToRegexp2.default.compile(pathPattern);
        priority = 0;
      } else {
        re = (0, _pathToRegexp2.default)('*', keys);

        toPath = function toPath() {
          return '';
        };

        matchExact = true;
        priority = -1;
      }

      if (!matchExact) {
        var wildcardRe = (0, _pathToRegexp2.default)(pathPattern + "/*", keys);
        re = new RegExp("(?:" + re.source + ")|(?:" + wildcardRe.source + ")");
      }

      pathsByRouteNames[routeName] = {
        re: re,
        keys: keys,
        toPath: toPath,
        priority: priority
      };
    });
    paths = Object.entries(pathsByRouteNames);
    paths.sort(function (a, b) {
      return b[1].priority - a[1].priority;
    });
    return {
      getComponentForState: function getComponentForState(state) {
        var activeChildRoute = state.routes[state.index];
        var routeName = activeChildRoute.routeName;

        if (childRouters[routeName]) {
          return childRouters[routeName].getComponentForState(activeChildRoute);
        }

        return (0, _getScreenForRouteName2.default)(routeConfigs, routeName);
      },
      getComponentForRouteName: function getComponentForRouteName(routeName) {
        return (0, _getScreenForRouteName2.default)(routeConfigs, routeName);
      },
      getActionCreators: function getActionCreators(route, navStateKey) {
        return babelHelpers.extends({}, (0, _getNavigationActionCreators2.default)(route), getCustomActionCreators(route, navStateKey), {
          pop: function pop(n, params) {
            return _StackActions2.default.pop(babelHelpers.extends({
              n: n
            }, params));
          },
          popToTop: function popToTop(params) {
            return _StackActions2.default.popToTop(params);
          },
          push: function push(routeName, params, action) {
            return _StackActions2.default.push({
              routeName: routeName,
              params: params,
              action: action
            });
          },
          replace: function replace(replaceWith, params, action, newKey) {
            if (typeof replaceWith === 'string') {
              return _StackActions2.default.replace({
                routeName: replaceWith,
                params: params,
                action: action,
                key: route.key,
                newKey: newKey
              });
            }

            (0, _invariant2.default)(typeof replaceWith === 'object', 'Must replaceWith an object or a string');
            (0, _invariant2.default)(params == null, 'Params must not be provided to .replace() when specifying an object');
            (0, _invariant2.default)(action == null, 'Child action must not be provided to .replace() when specifying an object');
            (0, _invariant2.default)(newKey == null, 'Child action must not be provided to .replace() when specifying an object');
            return _StackActions2.default.replace(replaceWith);
          },
          reset: function reset(actions, index) {
            return _StackActions2.default.reset({
              actions: actions,
              index: index == null ? actions.length - 1 : index,
              key: navStateKey
            });
          },
          dismiss: function dismiss() {
            return _NavigationActions2.default.back({
              key: navStateKey
            });
          }
        });
      },
      getStateForAction: function getStateForAction(action, state) {
        if (!state) {
          return getInitialState(action);
        }

        if (!isResetToRootStack(action) && action.type !== _NavigationActions2.default.NAVIGATE) {
          var keyIndex = action.key ? _StateUtils2.default.indexOf(state, action.key) : -1;
          var childIndex = keyIndex >= 0 ? keyIndex : state.index;
          var childRoute = state.routes[childIndex];
          (0, _invariant2.default)(childRoute, "StateUtils erroneously thought index " + childIndex + " exists");
          var childRouter = childRouters[childRoute.routeName];

          if (childRouter) {
            var route = childRouter.getStateForAction(action, childRoute);

            if (route === null) {
              return state;
            }

            if (route && route !== childRoute) {
              return _StateUtils2.default.replaceAt(state, childRoute.key, route);
            }
          }
        } else if (action.type === _NavigationActions2.default.NAVIGATE) {
          for (var _iterator = state.routes.slice().reverse(), _isArray = Array.isArray(_iterator), _i = 0, _iterator = _isArray ? _iterator : _iterator[typeof Symbol === "function" ? Symbol.iterator : "@@iterator"]();;) {
            var _ref;

            if (_isArray) {
              if (_i >= _iterator.length) break;
              _ref = _iterator[_i++];
            } else {
              _i = _iterator.next();
              if (_i.done) break;
              _ref = _i.value;
            }

            var _childRoute = _ref;
            var _childRouter = childRouters[_childRoute.routeName];
            var childAction = action.routeName === _childRoute.routeName && action.action ? action.action : action;

            if (_childRouter) {
              var nextRouteState = _childRouter.getStateForAction(childAction, _childRoute);

              if (nextRouteState === null || nextRouteState !== _childRoute) {
                return _StateUtils2.default.replaceAndPrune(state, nextRouteState ? nextRouteState.key : _childRoute.key, nextRouteState ? nextRouteState : _childRoute);
              }
            }
          }
        }

        if (behavesLikePushAction(action) && childRouters[action.routeName] !== undefined) {
          var _childRouter2 = childRouters[action.routeName];

          var _route = void 0;

          (0, _invariant2.default)(action.type !== _StackActions2.default.PUSH || action.key == null, 'StackRouter does not support key on the push action');
          var lastRouteIndex = state.routes.findIndex(function (r) {
            if (action.key) {
              return r.key === action.key;
            } else {
              return r.routeName === action.routeName;
            }
          });

          if (action.type !== _StackActions2.default.PUSH && lastRouteIndex !== -1) {
            if (state.index === lastRouteIndex && !action.params) {
              return null;
            }

            var routes = state.routes.slice(0, lastRouteIndex + 1);

            if (action.params) {
              var _route2 = state.routes[lastRouteIndex];
              routes[lastRouteIndex] = babelHelpers.extends({}, _route2, {
                params: babelHelpers.extends({}, _route2.params, action.params)
              });
            }

            return babelHelpers.extends({}, state, {
              isTransitioning: state.index !== lastRouteIndex ? action.immediate !== true : state.isTransitioning,
              index: lastRouteIndex,
              routes: routes
            });
          }

          if (_childRouter2) {
            var _childAction = action.action || _NavigationActions2.default.init({
              params: action.params
            });

            _route = babelHelpers.extends({
              params: action.params
            }, _childRouter2.getStateForAction(_childAction), {
              routeName: action.routeName,
              key: action.key || (0, _KeyGenerator.generateKey)()
            });
          } else {
            _route = {
              params: action.params,
              routeName: action.routeName,
              key: action.key || (0, _KeyGenerator.generateKey)()
            };
          }

          return babelHelpers.extends({}, _StateUtils2.default.push(state, _route), {
            isTransitioning: action.immediate !== true
          });
        } else if (action.type === _StackActions2.default.PUSH && childRouters[action.routeName] === undefined) {
          return state;
        }

        if (behavesLikePushAction(action)) {
          var childRouterNames = Object.keys(childRouters);

          for (var i = 0; i < childRouterNames.length; i++) {
            var childRouterName = childRouterNames[i];
            var _childRouter3 = childRouters[childRouterName];

            if (_childRouter3) {
              var initChildRoute = _childRouter3.getStateForAction(_NavigationActions2.default.init());

              var navigatedChildRoute = _childRouter3.getStateForAction(action, initChildRoute);

              var routeToPush = null;

              if (navigatedChildRoute === null) {
                routeToPush = initChildRoute;
              } else if (navigatedChildRoute !== initChildRoute) {
                routeToPush = navigatedChildRoute;
              }

              if (routeToPush) {
                var _route3 = babelHelpers.extends({}, routeToPush, {
                  routeName: childRouterName,
                  key: action.key || (0, _KeyGenerator.generateKey)()
                });

                return _StateUtils2.default.push(state, _route3);
              }
            }
          }
        }

        if (action.type === _StackActions2.default.POP_TO_TOP) {
          if (action.key && state.key !== action.key) {
            return state;
          }

          if (state.index > 0) {
            return babelHelpers.extends({}, state, {
              isTransitioning: action.immediate !== true,
              index: 0,
              routes: [state.routes[0]]
            });
          }

          return state;
        }

        if (action.type === _StackActions2.default.REPLACE) {
          var routeIndex = state.routes.findIndex(function (r) {
            return r.key === action.key;
          });

          if (routeIndex !== -1) {
            var _childRouter4 = childRouters[action.routeName];
            var childState = {};

            if (_childRouter4) {
              var _childAction2 = action.action || _NavigationActions2.default.init({
                params: action.params
              });

              childState = _childRouter4.getStateForAction(_childAction2);
            }

            var _routes = [].concat(babelHelpers.toConsumableArray(state.routes));

            _routes[routeIndex] = babelHelpers.extends({
              params: action.params
            }, childState, {
              routeName: action.routeName,
              key: action.newKey || (0, _KeyGenerator.generateKey)()
            });
            return babelHelpers.extends({}, state, {
              routes: _routes
            });
          }
        }

        if (action.type === _StackActions2.default.COMPLETE_TRANSITION && (action.key == null || action.key === state.key) && state.isTransitioning) {
          return babelHelpers.extends({}, state, {
            isTransitioning: false
          });
        }

        if (action.type === _NavigationActions2.default.SET_PARAMS) {
          var key = action.key;
          var lastRoute = state.routes.find(function (route) {
            return route.key === key;
          });

          if (lastRoute) {
            var params = babelHelpers.extends({}, lastRoute.params, action.params);

            var _routes2 = [].concat(babelHelpers.toConsumableArray(state.routes));

            _routes2[state.routes.indexOf(lastRoute)] = babelHelpers.extends({}, lastRoute, {
              params: params
            });
            return babelHelpers.extends({}, state, {
              routes: _routes2
            });
          }
        }

        if (action.type === _StackActions2.default.RESET) {
          if (action.key != null && action.key != state.key) {
            return state;
          }

          var newStackActions = action.actions;
          return babelHelpers.extends({}, state, {
            routes: newStackActions.map(function (newStackAction) {
              var router = childRouters[newStackAction.routeName];
              var childState = {};

              if (router) {
                var _childAction3 = newStackAction.action || _NavigationActions2.default.init({
                  params: newStackAction.params
                });

                childState = router.getStateForAction(_childAction3);
              }

              return babelHelpers.extends({
                params: newStackAction.params
              }, childState, {
                routeName: newStackAction.routeName,
                key: newStackAction.key || (0, _KeyGenerator.generateKey)()
              });
            }),
            index: action.index
          });
        }

        if (action.type === _NavigationActions2.default.BACK || action.type === _StackActions2.default.POP) {
          var _key = action.key,
              n = action.n,
              immediate = action.immediate;
          var backRouteIndex = state.index;

          if (action.type === _StackActions2.default.POP && n != null) {
            backRouteIndex = Math.max(1, state.index - n + 1);
          } else if (_key) {
            var backRoute = state.routes.find(function (route) {
              return route.key === _key;
            });
            backRouteIndex = state.routes.indexOf(backRoute);
          }

          if (backRouteIndex > 0) {
            return babelHelpers.extends({}, state, {
              routes: state.routes.slice(0, backRouteIndex),
              index: backRouteIndex - 1,
              isTransitioning: immediate !== true
            });
          }
        }

        return state;
      },
      getPathAndParamsForState: function getPathAndParamsForState(state) {
        var route = state.routes[state.index];
        var routeName = route.routeName;
        var screen = (0, _getScreenForRouteName2.default)(routeConfigs, routeName);
        var subPath = pathsByRouteNames[routeName].toPath(route.params);
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
      getActionForPathAndParams: function getActionForPathAndParams(pathToResolve, inputParams) {
        if (!pathToResolve) {
          return _NavigationActions2.default.navigate({
            routeName: initialRouteName,
            params: inputParams
          });
        }

        var _pathToResolve$split = pathToResolve.split('?'),
            _pathToResolve$split2 = babelHelpers.slicedToArray(_pathToResolve$split, 2),
            pathNameToResolve = _pathToResolve$split2[0],
            queryString = _pathToResolve$split2[1];

        var matchedRouteName = void 0;
        var pathMatch = void 0;
        var pathMatchKeys = void 0;

        for (var _iterator2 = paths, _isArray2 = Array.isArray(_iterator2), _i2 = 0, _iterator2 = _isArray2 ? _iterator2 : _iterator2[typeof Symbol === "function" ? Symbol.iterator : "@@iterator"]();;) {
          var _ref4;

          if (_isArray2) {
            if (_i2 >= _iterator2.length) break;
            _ref4 = _iterator2[_i2++];
          } else {
            _i2 = _iterator2.next();
            if (_i2.done) break;
            _ref4 = _i2.value;
          }

          var _ref2 = _ref4;

          var _ref3 = babelHelpers.slicedToArray(_ref2, 2);

          var routeName = _ref3[0];
          var path = _ref3[1];
          var re = path.re,
              keys = path.keys;
          pathMatch = re.exec(pathNameToResolve);

          if (pathMatch && pathMatch.length) {
            pathMatchKeys = keys;
            matchedRouteName = routeName;
            break;
          }
        }

        if (!matchedRouteName) {
          if (!pathToResolve) {
            return _NavigationActions2.default.navigate({
              routeName: initialRouteName
            });
          }

          return null;
        }

        var nestedAction = void 0;
        var nestedQueryString = queryString ? '?' + queryString : '';

        if (childRouters[matchedRouteName]) {
          nestedAction = childRouters[matchedRouteName].getActionForPathAndParams(pathMatch.slice(pathMatchKeys.length).join('/') + nestedQueryString);

          if (!nestedAction) {
            return null;
          }
        }

        var queryParams = !isEmpty(inputParams) ? inputParams : (queryString || '').split('&').reduce(function (result, item) {
          if (item !== '') {
            var nextResult = result || {};

            var _item$split = item.split('='),
                _item$split2 = babelHelpers.slicedToArray(_item$split, 2),
                key = _item$split2[0],
                value = _item$split2[1];

            nextResult[key] = value;
            return nextResult;
          }

          return result;
        }, null);
        var params = pathMatch.slice(1).reduce(function (result, matchResult, i) {
          var key = pathMatchKeys[i];

          if (key.asterisk || !key) {
            return result;
          }

          var nextResult = result || inputParams || {};
          var paramName = key.name;
          var decodedMatchResult = void 0;

          try {
            decodedMatchResult = decodeURIComponent(matchResult);
          } catch (e) {}

          nextResult[paramName] = decodedMatchResult || matchResult;
          return nextResult;
        }, queryParams);
        return _NavigationActions2.default.navigate(babelHelpers.extends({
          routeName: matchedRouteName
        }, params ? {
          params: params
        } : {}, nestedAction ? {
          action: nestedAction
        } : {}));
      },
      getScreenOptions: (0, _createConfigGetter2.default)(routeConfigs, stackConfig.navigationOptions)
    };
  };
},381,[382,341,369,384,385,345,387,342,388,343],"node_modules/react-navigation/src/routers/StackRouter.js");