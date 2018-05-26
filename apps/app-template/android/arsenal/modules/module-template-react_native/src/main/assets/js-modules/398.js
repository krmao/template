__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/Drawer/DrawerSidebar.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _NavigationActions = _require(_dependencyMap[2], "../../NavigationActions");

  var _NavigationActions2 = babelHelpers.interopRequireDefault(_NavigationActions);

  var _StackActions = _require(_dependencyMap[3], "../../routers/StackActions");

  var _StackActions2 = babelHelpers.interopRequireDefault(_StackActions);

  var _invariant = _require(_dependencyMap[4], "../../utils/invariant");

  var _invariant2 = babelHelpers.interopRequireDefault(_invariant);

  var DrawerSidebar = function (_React$PureComponent) {
    babelHelpers.inherits(DrawerSidebar, _React$PureComponent);

    function DrawerSidebar() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, DrawerSidebar);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = DrawerSidebar.__proto__ || Object.getPrototypeOf(DrawerSidebar)).call.apply(_ref, [this].concat(args))), _this), _this._getScreenOptions = function (routeKey) {
        var descriptor = _this.props.descriptors[routeKey];
        (0, _invariant2.default)(descriptor.options, 'Cannot access screen descriptor options from drawer sidebar');
        return descriptor.options;
      }, _this._getLabel = function (_ref2) {
        var focused = _ref2.focused,
            tintColor = _ref2.tintColor,
            route = _ref2.route;

        var _this$_getScreenOptio = _this._getScreenOptions(route.key),
            drawerLabel = _this$_getScreenOptio.drawerLabel,
            title = _this$_getScreenOptio.title;

        if (drawerLabel) {
          return typeof drawerLabel === 'function' ? drawerLabel({
            tintColor: tintColor,
            focused: focused
          }) : drawerLabel;
        }

        if (typeof title === 'string') {
          return title;
        }

        return route.routeName;
      }, _this._renderIcon = function (_ref3) {
        var focused = _ref3.focused,
            tintColor = _ref3.tintColor,
            route = _ref3.route;

        var _this$_getScreenOptio2 = _this._getScreenOptions(route.key),
            drawerIcon = _this$_getScreenOptio2.drawerIcon;

        if (drawerIcon) {
          return typeof drawerIcon === 'function' ? drawerIcon({
            tintColor: tintColor,
            focused: focused
          }) : drawerIcon;
        }

        return null;
      }, _this._onItemPress = function (_ref4) {
        var route = _ref4.route,
            focused = _ref4.focused;

        if (!focused) {
          var subAction = void 0;

          if (route.index != null && route.index !== 0) {
            subAction = _StackActions2.default.reset({
              index: 0,
              actions: [_NavigationActions2.default.navigate({
                routeName: route.routes[0].routeName
              })]
            });
          }

          _this.props.navigation.dispatch(_NavigationActions2.default.navigate({
            routeName: route.routeName,
            action: subAction
          }));
        }
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(DrawerSidebar, [{
      key: "render",
      value: function render() {
        var ContentComponent = this.props.contentComponent;

        if (!ContentComponent) {
          return null;
        }

        var state = this.props.navigation.state;
        (0, _invariant2.default)(typeof state.index === 'number', 'should be set');
        return _react2.default.createElement(
          _reactNative.View,
          {
            style: [styles.container, this.props.style],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 79
            }
          },
          _react2.default.createElement(ContentComponent, babelHelpers.extends({}, this.props.contentOptions, {
            navigation: this.props.navigation,
            descriptors: this.props.descriptors,
            items: state.routes,
            activeItemKey: state.routes[state.index] ? state.routes[state.index].key : null,
            screenProps: this.props.screenProps,
            getLabel: this._getLabel,
            renderIcon: this._renderIcon,
            onItemPress: this._onItemPress,
            drawerPosition: this.props.drawerPosition,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 80
            }
          }))
        );
      }
    }]);
    return DrawerSidebar;
  }(_react2.default.PureComponent);

  exports.default = DrawerSidebar;

  var styles = _reactNative.StyleSheet.create({
    container: {
      flex: 1
    }
  });
},398,[12,22,341,369,342],"node_modules/react-navigation/src/views/Drawer/DrawerSidebar.js");