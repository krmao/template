__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/navigators/createNavigator.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _getChildEventSubscriber = _require(_dependencyMap[1], "../getChildEventSubscriber");

  var _getChildEventSubscriber2 = babelHelpers.interopRequireDefault(_getChildEventSubscriber);

  function createNavigator(NavigatorView, router, navigationConfig) {
    var Navigator = function (_React$Component) {
      babelHelpers.inherits(Navigator, _React$Component);

      function Navigator() {
        var _ref;

        var _temp, _this, _ret;

        babelHelpers.classCallCheck(this, Navigator);

        for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
          args[_key] = arguments[_key];
        }

        return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = Navigator.__proto__ || Object.getPrototypeOf(Navigator)).call.apply(_ref, [this].concat(args))), _this), _this.childEventSubscribers = {}, _this._isRouteFocused = function (route) {
          var state = _this.props.navigation.state;
          var focusedRoute = state.routes[state.index];
          return route === focusedRoute;
        }, _this._dangerouslyGetParent = function () {
          return _this.props.navigation;
        }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
      }

      babelHelpers.createClass(Navigator, [{
        key: "componentDidUpdate",
        value: function componentDidUpdate() {
          var _this2 = this;

          var activeKeys = this.props.navigation.state.routes.map(function (r) {
            return r.key;
          });
          Object.keys(this.childEventSubscribers).forEach(function (key) {
            if (!activeKeys.includes(key)) {
              delete _this2.childEventSubscribers[key];
            }
          });
        }
      }, {
        key: "componentWillUnmount",
        value: function componentWillUnmount() {
          this.childEventSubscribers = {};
        }
      }, {
        key: "render",
        value: function render() {
          var _this3 = this;

          var _props = this.props,
              navigation = _props.navigation,
              screenProps = _props.screenProps;
          var dispatch = navigation.dispatch,
              state = navigation.state,
              addListener = navigation.addListener;
          var routes = state.routes;
          var descriptors = {};
          routes.forEach(function (route) {
            var getComponent = function getComponent() {
              return router.getComponentForRouteName(route.routeName);
            };

            if (!_this3.childEventSubscribers[route.key]) {
              _this3.childEventSubscribers[route.key] = (0, _getChildEventSubscriber2.default)(addListener, route.key);
            }

            var actionCreators = babelHelpers.extends({}, navigation.actions, router.getActionCreators(route, state.key));
            var actionHelpers = {};
            Object.keys(actionCreators).forEach(function (actionName) {
              actionHelpers[actionName] = function () {
                var actionCreator = actionCreators[actionName];
                var action = actionCreator.apply(undefined, arguments);
                dispatch(action);
              };
            });
            var childNavigation = babelHelpers.extends({}, actionHelpers, {
              actions: actionCreators,
              dispatch: dispatch,
              state: route,
              isFocused: function isFocused() {
                return _this3._isRouteFocused(route);
              },
              dangerouslyGetParent: _this3._dangerouslyGetParent,
              addListener: _this3.childEventSubscribers[route.key].addListener,
              getParam: function getParam(paramName, defaultValue) {
                var params = route.params;

                if (params && paramName in params) {
                  return params[paramName];
                }

                return defaultValue;
              }
            });
            var options = router.getScreenOptions(childNavigation, screenProps);
            descriptors[route.key] = {
              key: route.key,
              getComponent: getComponent,
              options: options,
              state: route,
              navigation: childNavigation
            };
          });
          return _react2.default.createElement(NavigatorView, babelHelpers.extends({}, this.props, {
            screenProps: screenProps,
            navigation: navigation,
            navigationConfig: navigationConfig,
            descriptors: descriptors,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 96
            }
          }));
        }
      }]);
      return Navigator;
    }(_react2.default.Component);

    Navigator.router = router;
    Navigator.navigationOptions = null;
    return Navigator;
  }

  exports.default = createNavigator;
},"80bc0b30071534809c7bc11f7396fe43",["c42a5e17831e80ed1e1c8cf91f5ddb40","b00f70989b181b053baf833c33435a78"],"node_modules/react-navigation/src/navigators/createNavigator.js");