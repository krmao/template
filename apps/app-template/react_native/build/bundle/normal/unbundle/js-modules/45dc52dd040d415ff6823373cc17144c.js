__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/Drawer/DrawerNavigatorItems.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _reactNativeSafeAreaView = _require(_dependencyMap[2], "react-native-safe-area-view");

  var _reactNativeSafeAreaView2 = babelHelpers.interopRequireDefault(_reactNativeSafeAreaView);

  var _TouchableItem = _require(_dependencyMap[3], "../TouchableItem");

  var _TouchableItem2 = babelHelpers.interopRequireDefault(_TouchableItem);

  var DrawerNavigatorItems = function DrawerNavigatorItems(_ref) {
    var _ref$navigation = _ref.navigation,
        state = _ref$navigation.state,
        navigate = _ref$navigation.navigate,
        items = _ref.items,
        activeItemKey = _ref.activeItemKey,
        activeTintColor = _ref.activeTintColor,
        activeBackgroundColor = _ref.activeBackgroundColor,
        inactiveTintColor = _ref.inactiveTintColor,
        inactiveBackgroundColor = _ref.inactiveBackgroundColor,
        getLabel = _ref.getLabel,
        renderIcon = _ref.renderIcon,
        onItemPress = _ref.onItemPress,
        itemsContainerStyle = _ref.itemsContainerStyle,
        itemStyle = _ref.itemStyle,
        labelStyle = _ref.labelStyle,
        activeLabelStyle = _ref.activeLabelStyle,
        inactiveLabelStyle = _ref.inactiveLabelStyle,
        iconContainerStyle = _ref.iconContainerStyle,
        drawerPosition = _ref.drawerPosition;
    return _react2.default.createElement(
      _reactNative.View,
      {
        style: [styles.container, itemsContainerStyle],
        __source: {
          fileName: _jsxFileName,
          lineNumber: 29
        }
      },
      items.map(function (route, index) {
        var _ref2;

        var focused = activeItemKey === route.key;
        var color = focused ? activeTintColor : inactiveTintColor;
        var backgroundColor = focused ? activeBackgroundColor : inactiveBackgroundColor;
        var scene = {
          route: route,
          index: index,
          focused: focused,
          tintColor: color
        };
        var icon = renderIcon(scene);
        var label = getLabel(scene);
        var extraLabelStyle = focused ? activeLabelStyle : inactiveLabelStyle;
        return _react2.default.createElement(
          _TouchableItem2.default,
          {
            key: route.key,
            onPress: function onPress() {
              onItemPress({
                route: route,
                focused: focused
              });
            },
            delayPressIn: 0,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 41
            }
          },
          _react2.default.createElement(
            _reactNativeSafeAreaView2.default,
            {
              style: {
                backgroundColor: backgroundColor
              },
              forceInset: (_ref2 = {}, babelHelpers.defineProperty(_ref2, drawerPosition, 'always'), babelHelpers.defineProperty(_ref2, drawerPosition === 'left' ? 'right' : 'left', 'never'), babelHelpers.defineProperty(_ref2, "vertical", 'never'), _ref2),
              __source: {
                fileName: _jsxFileName,
                lineNumber: 48
              }
            },
            _react2.default.createElement(
              _reactNative.View,
              {
                style: [styles.item, itemStyle],
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 56
                }
              },
              icon ? _react2.default.createElement(
                _reactNative.View,
                {
                  style: [styles.icon, focused ? null : styles.inactiveIcon, iconContainerStyle],
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 58
                  }
                },
                icon
              ) : null,
              typeof label === 'string' ? _react2.default.createElement(
                _reactNative.Text,
                {
                  style: [styles.label, {
                    color: color
                  }, labelStyle, extraLabelStyle],
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 69
                  }
                },
                label
              ) : label
            )
          )
        );
      })
    );
  };

  DrawerNavigatorItems.defaultProps = {
    activeTintColor: '#2196f3',
    activeBackgroundColor: 'rgba(0, 0, 0, .04)',
    inactiveTintColor: 'rgba(0, 0, 0, .87)',
    inactiveBackgroundColor: 'transparent'
  };

  var styles = _reactNative.StyleSheet.create({
    container: {
      paddingVertical: 4
    },
    item: {
      flexDirection: 'row',
      alignItems: 'center'
    },
    icon: {
      marginHorizontal: 16,
      width: 24,
      alignItems: 'center'
    },
    inactiveIcon: {
      opacity: 0.62
    },
    label: {
      margin: 16,
      fontWeight: 'bold'
    }
  });

  exports.default = DrawerNavigatorItems;
},"45dc52dd040d415ff6823373cc17144c",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04","3a4dc8c54e97a0cf03c38b0fb8563a28","e56e3b4f6f7f1318e519a227d65a0b65"],"node_modules/react-navigation/src/views/Drawer/DrawerNavigatorItems.js");