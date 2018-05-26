__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/Drawer/DrawerView.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _reactNativeDrawerLayoutPolyfill = _require(_dependencyMap[2], "react-native-drawer-layout-polyfill");

  var _reactNativeDrawerLayoutPolyfill2 = babelHelpers.interopRequireDefault(_reactNativeDrawerLayoutPolyfill);

  var _DrawerSidebar = _require(_dependencyMap[3], "./DrawerSidebar");

  var _DrawerSidebar2 = babelHelpers.interopRequireDefault(_DrawerSidebar);

  var _NavigationActions = _require(_dependencyMap[4], "../../NavigationActions");

  var _NavigationActions2 = babelHelpers.interopRequireDefault(_NavigationActions);

  var _DrawerActions = _require(_dependencyMap[5], "../../routers/DrawerActions");

  var _DrawerActions2 = babelHelpers.interopRequireDefault(_DrawerActions);

  var DrawerView = function (_React$PureComponent) {
    babelHelpers.inherits(DrawerView, _React$PureComponent);

    function DrawerView() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, DrawerView);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = DrawerView.__proto__ || Object.getPrototypeOf(DrawerView)).call.apply(_ref, [this].concat(args))), _this), _this.state = {
        drawerWidth: typeof _this.props.navigationConfig.drawerWidth === 'function' ? _this.props.navigationConfig.drawerWidth() : _this.props.navigationConfig.drawerWidth
      }, _this._handleDrawerOpen = function () {
        var navigation = _this.props.navigation;
        var isDrawerOpen = navigation.state.isDrawerOpen;

        if (!isDrawerOpen) {
          navigation.dispatch({
            type: _DrawerActions2.default.OPEN_DRAWER
          });
        }
      }, _this._handleDrawerClose = function () {
        var navigation = _this.props.navigation;
        var isDrawerOpen = navigation.state.isDrawerOpen;

        if (isDrawerOpen) {
          navigation.dispatch({
            type: _DrawerActions2.default.CLOSE_DRAWER
          });
        }
      }, _this._updateWidth = function () {
        var drawerWidth = typeof _this.props.navigationConfig.drawerWidth === 'function' ? _this.props.navigationConfig.drawerWidth() : _this.props.navigationConfig.drawerWidth;

        if (_this.state.drawerWidth !== drawerWidth) {
          _this.setState({
            drawerWidth: drawerWidth
          });
        }
      }, _this._renderNavigationView = function () {
        return _react2.default.createElement(_DrawerSidebar2.default, babelHelpers.extends({
          screenProps: _this.props.screenProps,
          navigation: _this.props.navigation,
          descriptors: _this.props.descriptors,
          contentComponent: _this.props.navigationConfig.contentComponent,
          contentOptions: _this.props.navigationConfig.contentOptions,
          drawerPosition: _this.props.navigationConfig.drawerPosition,
          style: _this.props.navigationConfig.style
        }, _this.props.navigationConfig, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 68
          }
        }));
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(DrawerView, [{
      key: "componentDidMount",
      value: function componentDidMount() {
        _reactNative.Dimensions.addEventListener('change', this._updateWidth);
      }
    }, {
      key: "componentWillUnmount",
      value: function componentWillUnmount() {
        _reactNative.Dimensions.removeEventListener('change', this._updateWidth);
      }
    }, {
      key: "componentDidUpdate",
      value: function componentDidUpdate(prevProps, prevState) {
        var isDrawerOpen = this.props.navigation.state.isDrawerOpen;
        var wasDrawerOpen = prevProps.navigation.state.isDrawerOpen;

        if (isDrawerOpen && !wasDrawerOpen) {
          this._drawer.openDrawer();
        } else if (wasDrawerOpen && !isDrawerOpen) {
          this._drawer.closeDrawer();
        }
      }
    }, {
      key: "render",
      value: function render() {
        var _this2 = this;

        var state = this.props.navigation.state;
        var activeKey = state.routes[state.index].key;
        var descriptor = this.props.descriptors[activeKey];
        var DrawerScreen = descriptor.getComponent();
        var drawerLockMode = descriptor.options.drawerLockMode;
        return _react2.default.createElement(
          _reactNativeDrawerLayoutPolyfill2.default,
          {
            ref: function ref(c) {
              _this2._drawer = c;
            },
            drawerLockMode: drawerLockMode || this.props.screenProps && this.props.screenProps.drawerLockMode || this.props.navigationConfig.drawerLockMode,
            drawerBackgroundColor: this.props.navigationConfig.drawerBackgroundColor,
            drawerWidth: this.state.drawerWidth,
            onDrawerOpen: this._handleDrawerOpen,
            onDrawerClose: this._handleDrawerClose,
            useNativeAnimations: this.props.navigationConfig.useNativeAnimations,
            renderNavigationView: this._renderNavigationView,
            drawerPosition: this.props.navigationConfig.drawerPosition === 'right' ? _reactNativeDrawerLayoutPolyfill2.default.positions.Right : _reactNativeDrawerLayoutPolyfill2.default.positions.Left,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 91
            }
          },
          _react2.default.createElement(DrawerScreen, {
            screenProps: this.props.screenProps,
            navigation: descriptor.navigation,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 114
            }
          })
        );
      }
    }]);
    return DrawerView;
  }(_react2.default.PureComponent);

  exports.default = DrawerView;
},396,[12,22,397,398,341,395],"node_modules/react-navigation/src/views/Drawer/DrawerView.js");