__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports.PagerRendererPropType = exports.SceneRendererPropType = exports.NavigationStatePropType = exports.NavigationRoutePropType = undefined;

  var _propTypes = _require(_dependencyMap[0], "prop-types");

  var _propTypes2 = babelHelpers.interopRequireDefault(_propTypes);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var NavigationRoutePropType = exports.NavigationRoutePropType = _propTypes2.default.shape({
    title: _propTypes2.default.string,
    key: _propTypes2.default.string.isRequired
  });

  var NavigationStatePropType = exports.NavigationStatePropType = _propTypes2.default.shape({
    routes: _propTypes2.default.arrayOf(NavigationRoutePropType).isRequired,
    index: _propTypes2.default.number.isRequired
  });

  var SceneRendererPropType = exports.SceneRendererPropType = {
    panX: _propTypes2.default.object.isRequired,
    offsetX: _propTypes2.default.object.isRequired,
    layout: _propTypes2.default.shape({
      measured: _propTypes2.default.bool.isRequired,
      height: _propTypes2.default.number.isRequired,
      width: _propTypes2.default.number.isRequired
    }).isRequired,
    navigationState: NavigationStatePropType.isRequired,
    position: _propTypes2.default.object.isRequired,
    jumpTo: _propTypes2.default.func.isRequired,
    jumpToIndex: _propTypes2.default.func.isRequired,
    useNativeDriver: _propTypes2.default.bool
  };
  var PagerRendererPropType = exports.PagerRendererPropType = {
    layout: _propTypes2.default.shape({
      measured: _propTypes2.default.bool.isRequired,
      height: _propTypes2.default.number.isRequired,
      width: _propTypes2.default.number.isRequired
    }).isRequired,
    navigationState: NavigationStatePropType.isRequired,
    panX: _propTypes2.default.instanceOf(_reactNative.Animated.Value).isRequired,
    offsetX: _propTypes2.default.instanceOf(_reactNative.Animated.Value).isRequired,
    canJumpToTab: _propTypes2.default.func.isRequired,
    jumpTo: _propTypes2.default.func.isRequired,
    animationEnabled: _propTypes2.default.bool,
    swipeEnabled: _propTypes2.default.bool,
    useNativeDriver: _propTypes2.default.bool,
    onSwipeStart: _propTypes2.default.func,
    onSwipeEnd: _propTypes2.default.func,
    onAnimationEnd: _propTypes2.default.func,
    children: _propTypes2.default.node.isRequired
  };
},405,[129,22],"node_modules/react-navigation/node_modules/react-native-tab-view/src/TabViewPropTypes.js");