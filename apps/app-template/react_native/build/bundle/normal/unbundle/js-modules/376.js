__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _reactNative = _require(_dependencyMap[0], "react-native");

  var _getSceneIndicesForInterpolationInputRange = _require(_dependencyMap[1], "../../utils/getSceneIndicesForInterpolationInputRange");

  var _getSceneIndicesForInterpolationInputRange2 = babelHelpers.interopRequireDefault(_getSceneIndicesForInterpolationInputRange);

  function forInitial(props) {
    var navigation = props.navigation,
        scene = props.scene;
    var focused = navigation.state.index === scene.index;
    var opacity = focused ? 1 : 0;
    var translate = focused ? 0 : 1000000;
    return {
      opacity: opacity,
      transform: [{
        translateX: translate
      }, {
        translateY: translate
      }]
    };
  }

  function forHorizontal(props) {
    var layout = props.layout,
        position = props.position,
        scene = props.scene;

    if (!layout.isMeasured) {
      return forInitial(props);
    }

    var interpolate = (0, _getSceneIndicesForInterpolationInputRange2.default)(props);
    if (!interpolate) return {
      opacity: 0
    };
    var first = interpolate.first,
        last = interpolate.last;
    var index = scene.index;
    var opacity = position.interpolate({
      inputRange: [first, first + 0.01, index, last - 0.01, last],
      outputRange: [0, 1, 1, 0.85, 0]
    });
    var width = layout.initWidth;
    var translateX = position.interpolate({
      inputRange: [first, index, last],
      outputRange: _reactNative.I18nManager.isRTL ? [-width, 0, width * 0.3] : [width, 0, width * -0.3]
    });
    var translateY = 0;
    return {
      opacity: opacity,
      transform: [{
        translateX: translateX
      }, {
        translateY: translateY
      }]
    };
  }

  function forVertical(props) {
    var layout = props.layout,
        position = props.position,
        scene = props.scene;

    if (!layout.isMeasured) {
      return forInitial(props);
    }

    var interpolate = (0, _getSceneIndicesForInterpolationInputRange2.default)(props);
    if (!interpolate) return {
      opacity: 0
    };
    var first = interpolate.first,
        last = interpolate.last;
    var index = scene.index;
    var opacity = position.interpolate({
      inputRange: [first, first + 0.01, index, last - 0.01, last],
      outputRange: [0, 1, 1, 0.85, 0]
    });
    var height = layout.initHeight;
    var translateY = position.interpolate({
      inputRange: [first, index, last],
      outputRange: [height, 0, 0]
    });
    var translateX = 0;
    return {
      opacity: opacity,
      transform: [{
        translateX: translateX
      }, {
        translateY: translateY
      }]
    };
  }

  function forFadeFromBottomAndroid(props) {
    var layout = props.layout,
        position = props.position,
        scene = props.scene;

    if (!layout.isMeasured) {
      return forInitial(props);
    }

    var interpolate = (0, _getSceneIndicesForInterpolationInputRange2.default)(props);
    if (!interpolate) return {
      opacity: 0
    };
    var first = interpolate.first,
        last = interpolate.last;
    var index = scene.index;
    var inputRange = [first, index, last - 0.01, last];
    var opacity = position.interpolate({
      inputRange: inputRange,
      outputRange: [0, 1, 1, 0]
    });
    var translateY = position.interpolate({
      inputRange: inputRange,
      outputRange: [50, 0, 0, 0]
    });
    var translateX = 0;
    return {
      opacity: opacity,
      transform: [{
        translateX: translateX
      }, {
        translateY: translateY
      }]
    };
  }

  function forFade(props) {
    var layout = props.layout,
        position = props.position,
        scene = props.scene;

    if (!layout.isMeasured) {
      return forInitial(props);
    }

    var interpolate = (0, _getSceneIndicesForInterpolationInputRange2.default)(props);
    if (!interpolate) return {
      opacity: 0
    };
    var first = interpolate.first,
        last = interpolate.last;
    var index = scene.index;
    var opacity = position.interpolate({
      inputRange: [first, index, last],
      outputRange: [0, 1, 1]
    });
    return {
      opacity: opacity
    };
  }

  exports.default = {
    forHorizontal: forHorizontal,
    forVertical: forVertical,
    forFadeFromBottomAndroid: forFadeFromBottomAndroid,
    forFade: forFade
  };
},376,[22,366],"node_modules/react-navigation/src/views/StackView/StackViewStyleInterpolator.js");