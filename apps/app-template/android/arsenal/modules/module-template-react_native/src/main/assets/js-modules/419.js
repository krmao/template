__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/dist/utils/createTabNavigator.js";
  exports.default = createTabNavigator;

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _reactNavigation = _require(_dependencyMap[1], "react-navigation");

  function createTabNavigator(TabView) {
    var NavigationView = function (_React$Component) {
      babelHelpers.inherits(NavigationView, _React$Component);

      function NavigationView() {
        var _ref;

        var _temp, _this, _ret;

        babelHelpers.classCallCheck(this, NavigationView);

        for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
          args[_key] = arguments[_key];
        }

        return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = NavigationView.__proto__ || Object.getPrototypeOf(NavigationView)).call.apply(_ref, [this].concat(args))), _this), _this._renderScene = function (_ref2) {
          var route = _ref2.route;
          var _this$props = _this.props,
              screenProps = _this$props.screenProps,
              descriptors = _this$props.descriptors;
          var descriptor = descriptors[route.key];
          var TabComponent = descriptor.getComponent();
          return React.createElement(_reactNavigation.SceneView, {
            screenProps: screenProps,
            navigation: descriptor.navigation,
            component: TabComponent,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 10
            }
          });
        }, _this._renderIcon = function (_ref3) {
          var route = _ref3.route,
              _ref3$focused = _ref3.focused,
              focused = _ref3$focused === undefined ? true : _ref3$focused,
              tintColor = _ref3.tintColor;
          var descriptors = _this.props.descriptors;
          var descriptor = descriptors[route.key];
          var options = descriptor.options;

          if (options.tabBarIcon) {
            return typeof options.tabBarIcon === 'function' ? options.tabBarIcon({
              focused: focused,
              tintColor: tintColor
            }) : options.tabBarIcon;
          }

          return null;
        }, _this._getLabelText = function (_ref4) {
          var route = _ref4.route;
          var descriptors = _this.props.descriptors;
          var descriptor = descriptors[route.key];
          var options = descriptor.options;

          if (options.tabBarLabel) {
            return options.tabBarLabel;
          }

          if (typeof options.title === 'string') {
            return options.title;
          }

          return route.routeName;
        }, _this._handleTabPress = function (_ref5) {
          var route = _ref5.route;
          _this._isTabPress = true;
          var descriptors = _this.props.descriptors;
          var descriptor = descriptors[route.key];
          var navigation = descriptor.navigation,
              options = descriptor.options;

          var defaultHandler = function defaultHandler() {
            if (navigation.isFocused()) {
              if (route.hasOwnProperty('index') && route.index > 0) {
                navigation.dispatch(_reactNavigation.StackActions.popToTop({
                  key: route.key
                }));
              } else {}
            } else {
              _this._jumpTo(route.routeName);
            }
          };

          if (options.tabBarOnPress) {
            options.tabBarOnPress({
              navigation: navigation,
              defaultHandler: defaultHandler
            });
          } else {
            defaultHandler();
          }
        }, _this._handleIndexChange = function (index) {
          if (_this._isTabPress) {
            _this._isTabPress = false;
            return;
          }

          _this._jumpTo(_this.props.navigation.state.routes[index].routeName);
        }, _this._jumpTo = function (routeName) {
          return _this.props.navigation.dispatch(_reactNavigation.NavigationActions.navigate({
            routeName: routeName
          }));
        }, _this._isTabPress = false, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
      }

      babelHelpers.createClass(NavigationView, [{
        key: "render",
        value: function render() {
          var _props = this.props,
              descriptors = _props.descriptors,
              navigation = _props.navigation,
              screenProps = _props.screenProps;
          var state = navigation.state;
          var route = state.routes[state.index];
          var descriptor = descriptors[route.key];
          var options = babelHelpers.extends({}, this.props.navigationConfig, descriptor.options);
          return React.createElement(TabView, babelHelpers.extends({}, options, {
            getLabelText: this._getLabelText,
            renderIcon: this._renderIcon,
            renderScene: this._renderScene,
            onIndexChange: this._handleIndexChange,
            onTabPress: this._handleTabPress,
            navigation: navigation,
            descriptors: descriptors,
            screenProps: screenProps,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 91
            }
          }));
        }
      }]);
      return NavigationView;
    }(React.Component);

    return function (routes) {
      var config = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
      var router = (0, _reactNavigation.TabRouter)(routes, config);
      var navigator = (0, _reactNavigation.createNavigator)(NavigationView, router, config);
      return (0, _reactNavigation.createNavigationContainer)(navigator);
    };
  }
},419,[12,337],"node_modules/react-navigation/node_modules/react-navigation-tabs/dist/utils/createTabNavigator.js");