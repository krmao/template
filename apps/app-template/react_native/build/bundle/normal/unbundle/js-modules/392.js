__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/navigators/createDrawerNavigator.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _reactNativeSafeAreaView = _require(_dependencyMap[2], "react-native-safe-area-view");

  var _reactNativeSafeAreaView2 = babelHelpers.interopRequireDefault(_reactNativeSafeAreaView);

  var _createNavigator = _require(_dependencyMap[3], "./createNavigator");

  var _createNavigator2 = babelHelpers.interopRequireDefault(_createNavigator);

  var _createNavigationContainer = _require(_dependencyMap[4], "../createNavigationContainer");

  var _createNavigationContainer2 = babelHelpers.interopRequireDefault(_createNavigationContainer);

  var _DrawerRouter = _require(_dependencyMap[5], "../routers/DrawerRouter");

  var _DrawerRouter2 = babelHelpers.interopRequireDefault(_DrawerRouter);

  var _DrawerView = _require(_dependencyMap[6], "../views/Drawer/DrawerView");

  var _DrawerView2 = babelHelpers.interopRequireDefault(_DrawerView);

  var _DrawerNavigatorItems = _require(_dependencyMap[7], "../views/Drawer/DrawerNavigatorItems");

  var _DrawerNavigatorItems2 = babelHelpers.interopRequireDefault(_DrawerNavigatorItems);

  var defaultContentComponent = function defaultContentComponent(props) {
    return _react2.default.createElement(
      _reactNative.ScrollView,
      {
        alwaysBounceVertical: false,
        __source: {
          fileName: _jsxFileName,
          lineNumber: 16
        }
      },
      _react2.default.createElement(
        _reactNativeSafeAreaView2.default,
        {
          forceInset: {
            top: 'always',
            horizontal: 'never'
          },
          __source: {
            fileName: _jsxFileName,
            lineNumber: 17
          }
        },
        _react2.default.createElement(_DrawerNavigatorItems2.default, babelHelpers.extends({}, props, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 18
          }
        }))
      )
    );
  };

  var DefaultDrawerConfig = {
    drawerWidth: function drawerWidth() {
      var _Dimensions$get = _reactNative.Dimensions.get('window'),
          height = _Dimensions$get.height,
          width = _Dimensions$get.width;

      var smallerAxisSize = Math.min(height, width);
      var isLandscape = width > height;
      var isTablet = smallerAxisSize >= 600;
      var appBarHeight = _reactNative.Platform.OS === 'ios' ? isLandscape ? 32 : 44 : 56;
      var maxWidth = isTablet ? 320 : 280;
      return Math.min(smallerAxisSize - appBarHeight, maxWidth);
    },
    contentComponent: defaultContentComponent,
    drawerPosition: 'left',
    drawerBackgroundColor: 'white',
    useNativeAnimations: true
  };

  var DrawerNavigator = function DrawerNavigator(routeConfigs) {
    var config = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
    var mergedConfig = babelHelpers.extends({}, DefaultDrawerConfig, config);
    var order = mergedConfig.order,
        paths = mergedConfig.paths,
        initialRouteName = mergedConfig.initialRouteName,
        backBehavior = mergedConfig.backBehavior,
        drawerConfig = babelHelpers.objectWithoutProperties(mergedConfig, ["order", "paths", "initialRouteName", "backBehavior"]);
    var routerConfig = {
      order: order,
      paths: paths,
      initialRouteName: initialRouteName,
      backBehavior: backBehavior
    };
    var drawerRouter = (0, _DrawerRouter2.default)(routeConfigs, routerConfig);
    var navigator = (0, _createNavigator2.default)(_DrawerView2.default, drawerRouter, drawerConfig);
    return (0, _createNavigationContainer2.default)(navigator);
  };

  exports.default = DrawerNavigator;
},392,[12,22,357,346,338,393,396,399],"node_modules/react-navigation/src/navigators/createDrawerNavigator.js");