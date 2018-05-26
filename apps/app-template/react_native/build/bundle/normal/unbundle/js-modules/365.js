__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _reactNative = _require(_dependencyMap[0], "react-native");

  var _getSceneIndicesForInterpolationInputRange = _require(_dependencyMap[1], "../../utils/getSceneIndicesForInterpolationInputRange");

  var _getSceneIndicesForInterpolationInputRange2 = babelHelpers.interopRequireDefault(_getSceneIndicesForInterpolationInputRange);

  function hasHeader(scene) {
    if (!scene) {
      return true;
    }

    var descriptor = scene.descriptor;
    return descriptor.options.header !== null;
  }

  var crossFadeInterpolation = function crossFadeInterpolation(scenes, first, index, last) {
    return {
      inputRange: [first, first + 0.001, index - 0.9, index - 0.2, index, last - 0.001, last],
      outputRange: [0, hasHeader(scenes[first]) ? 0 : 1, hasHeader(scenes[first]) ? 0 : 1, hasHeader(scenes[first]) ? 0.3 : 1, hasHeader(scenes[index]) ? 1 : 0, hasHeader(scenes[last]) ? 0 : 1, 0]
    };
  };

  function isGoingBack(scenes) {
    var lastSceneIndexInScenes = scenes.length - 1;
    return !scenes[lastSceneIndexInScenes].isActive;
  }

  function forLayout(props) {
    var layout = props.layout,
        position = props.position,
        scene = props.scene,
        scenes = props.scenes,
        mode = props.mode;

    if (mode !== 'float') {
      return {};
    }

    var isBack = isGoingBack(scenes);
    var interpolate = (0, _getSceneIndicesForInterpolationInputRange2.default)(props);
    if (!interpolate) return {};
    var first = interpolate.first,
        last = interpolate.last;
    var index = scene.index;
    var width = layout.initWidth;

    if (isBack && !hasHeader(scenes[index]) && !hasHeader(scenes[last]) || !isBack && !hasHeader(scenes[first]) && !hasHeader(scenes[index])) {
      return {
        transform: [{
          translateX: width
        }]
      };
    }

    var rtlMult = _reactNative.I18nManager.isRTL ? -1 : 1;
    var translateX = position.interpolate({
      inputRange: [first, index, last],
      outputRange: [rtlMult * (hasHeader(scenes[first]) ? 0 : width), rtlMult * (hasHeader(scenes[index]) ? 0 : isBack ? width : -width), rtlMult * (hasHeader(scenes[last]) ? 0 : -width)]
    });
    return {
      transform: [{
        translateX: translateX
      }]
    };
  }

  function forLeft(props) {
    var position = props.position,
        scene = props.scene,
        scenes = props.scenes;
    var interpolate = (0, _getSceneIndicesForInterpolationInputRange2.default)(props);
    if (!interpolate) return {
      opacity: 0
    };
    var first = interpolate.first,
        last = interpolate.last;
    var index = scene.index;
    return {
      opacity: position.interpolate(crossFadeInterpolation(scenes, first, index, last))
    };
  }

  function forCenter(props) {
    var position = props.position,
        scene = props.scene,
        scenes = props.scenes;
    var interpolate = (0, _getSceneIndicesForInterpolationInputRange2.default)(props);
    if (!interpolate) return {
      opacity: 0
    };
    var first = interpolate.first,
        last = interpolate.last;
    var index = scene.index;
    return {
      opacity: position.interpolate(crossFadeInterpolation(scenes, first, index, last))
    };
  }

  function forRight(props) {
    var position = props.position,
        scene = props.scene,
        scenes = props.scenes;
    var interpolate = (0, _getSceneIndicesForInterpolationInputRange2.default)(props);
    if (!interpolate) return {
      opacity: 0
    };
    var first = interpolate.first,
        last = interpolate.last;
    var index = scene.index;
    return {
      opacity: position.interpolate(crossFadeInterpolation(scenes, first, index, last))
    };
  }

  function forLeftButton(props) {
    var position = props.position,
        scene = props.scene,
        scenes = props.scenes;
    var interpolate = (0, _getSceneIndicesForInterpolationInputRange2.default)(props);
    if (!interpolate) return {
      opacity: 0
    };
    var first = interpolate.first,
        last = interpolate.last;
    var index = scene.index;
    var inputRange = [first, first + 0.001, first + Math.abs(index - first) / 2, index, last - Math.abs(last - index) / 2, last - 0.001, last];
    var outputRange = [0, hasHeader(scenes[first]) ? 0 : 1, hasHeader(scenes[first]) ? 0.1 : 1, hasHeader(scenes[index]) ? 1 : 0, hasHeader(scenes[last]) ? 0.1 : 1, hasHeader(scenes[last]) ? 0 : 1, 0];
    return {
      opacity: position.interpolate({
        inputRange: inputRange,
        outputRange: outputRange
      })
    };
  }

  var LEFT_LABEL_OFFSET = _reactNative.Dimensions.get('window').width / 2 - 70 - 25;

  function forLeftLabel(props) {
    var position = props.position,
        scene = props.scene,
        scenes = props.scenes;
    var interpolate = (0, _getSceneIndicesForInterpolationInputRange2.default)(props);
    if (!interpolate) return {
      opacity: 0
    };
    var first = interpolate.first,
        last = interpolate.last;
    var index = scene.index;
    var offset = LEFT_LABEL_OFFSET;
    return {
      opacity: position.interpolate({
        inputRange: [first, first + 0.001, index - 0.35, index, index + 0.5, last - 0.001, last],
        outputRange: [0, hasHeader(scenes[first]) ? 0 : 1, hasHeader(scenes[first]) ? 0 : 1, hasHeader(scenes[index]) ? 1 : 0, hasHeader(scenes[last]) ? 0.5 : 1, hasHeader(scenes[last]) ? 0 : 1, 0]
      }),
      transform: [{
        translateX: position.interpolate({
          inputRange: [first, first + 0.001, index, last - 0.001, last],
          outputRange: _reactNative.I18nManager.isRTL ? [-offset * 1.5, hasHeader(scenes[first]) ? -offset * 1.5 : 0, 0, hasHeader(scenes[last]) ? offset : 0, offset] : [offset, hasHeader(scenes[first]) ? offset : 0, 0, hasHeader(scenes[last]) ? -offset * 1.5 : 0, -offset * 1.5]
        })
      }]
    };
  }

  var TITLE_OFFSET_IOS = _reactNative.Dimensions.get('window').width / 2 - 70 + 25;

  function forCenterFromLeft(props) {
    var position = props.position,
        scene = props.scene,
        scenes = props.scenes;
    var interpolate = (0, _getSceneIndicesForInterpolationInputRange2.default)(props);
    if (!interpolate) return {
      opacity: 0
    };
    var first = interpolate.first,
        last = interpolate.last;
    var index = scene.index;
    var inputRange = [first, index - 0.5, index, index + 0.5, last];
    var offset = TITLE_OFFSET_IOS;
    return {
      opacity: position.interpolate({
        inputRange: [first, first + 0.001, index - 0.5, index, index + 0.7, last - 0.001, last],
        outputRange: [0, hasHeader(scenes[first]) ? 0 : 1, hasHeader(scenes[first]) ? 0 : 1, hasHeader(scenes[index]) ? 1 : 0, hasHeader(scenes[last]) ? 0 : 1, hasHeader(scenes[last]) ? 0 : 1, 0]
      }),
      transform: [{
        translateX: position.interpolate({
          inputRange: [first, first + 0.001, index, last - 0.001, last],
          outputRange: _reactNative.I18nManager.isRTL ? [-offset, hasHeader(scenes[first]) ? -offset : 0, 0, hasHeader(scenes[last]) ? offset : 0, offset] : [offset, hasHeader(scenes[first]) ? offset : 0, 0, hasHeader(scenes[last]) ? -offset : 0, -offset]
        })
      }]
    };
  }

  exports.default = {
    forLayout: forLayout,
    forLeft: forLeft,
    forLeftButton: forLeftButton,
    forLeftLabel: forLeftLabel,
    forCenterFromLeft: forCenterFromLeft,
    forCenter: forCenter,
    forRight: forRight
  };
},365,[22,366],"node_modules/react-navigation/src/views/Header/HeaderStyleInterpolator.js");