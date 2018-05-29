__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _reactNative = _require(_dependencyMap[0], "react-native");

  var _StackViewStyleInterpolator = _require(_dependencyMap[1], "./StackViewStyleInterpolator");

  var _StackViewStyleInterpolator2 = babelHelpers.interopRequireDefault(_StackViewStyleInterpolator);

  var _ReactNativeFeatures = _require(_dependencyMap[2], "../../utils/ReactNativeFeatures");

  var ReactNativeFeatures = babelHelpers.interopRequireWildcard(_ReactNativeFeatures);
  var IOSTransitionSpec = void 0;

  if (ReactNativeFeatures.supportsImprovedSpringAnimation()) {
    IOSTransitionSpec = {
      timing: _reactNative.Animated.spring,
      stiffness: 1000,
      damping: 500,
      mass: 3
    };
  } else {
    IOSTransitionSpec = {
      duration: 500,
      easing: _reactNative.Easing.bezier(0.2833, 0.99, 0.31833, 0.99),
      timing: _reactNative.Animated.timing
    };
  }

  var SlideFromRightIOS = {
    transitionSpec: IOSTransitionSpec,
    screenInterpolator: _StackViewStyleInterpolator2.default.forHorizontal,
    containerStyle: {
      backgroundColor: '#000'
    }
  };
  var ModalSlideFromBottomIOS = {
    transitionSpec: IOSTransitionSpec,
    screenInterpolator: _StackViewStyleInterpolator2.default.forVertical,
    containerStyle: {
      backgroundColor: '#000'
    }
  };
  var FadeInFromBottomAndroid = {
    transitionSpec: {
      duration: 350,
      easing: _reactNative.Easing.out(_reactNative.Easing.poly(5)),
      timing: _reactNative.Animated.timing
    },
    screenInterpolator: _StackViewStyleInterpolator2.default.forFadeFromBottomAndroid
  };
  var FadeOutToBottomAndroid = {
    transitionSpec: {
      duration: 230,
      easing: _reactNative.Easing.in(_reactNative.Easing.poly(4)),
      timing: _reactNative.Animated.timing
    },
    screenInterpolator: _StackViewStyleInterpolator2.default.forFadeFromBottomAndroid
  };

  function defaultTransitionConfig(transitionProps, prevTransitionProps, isModal) {
    if (_reactNative.Platform.OS === 'android') {
      if (prevTransitionProps && transitionProps.index < prevTransitionProps.index) {
        return FadeOutToBottomAndroid;
      }

      return FadeInFromBottomAndroid;
    }

    if (isModal) {
      return ModalSlideFromBottomIOS;
    }

    return SlideFromRightIOS;
  }

  function getTransitionConfig(transitionConfigurer, transitionProps, prevTransitionProps, isModal) {
    var defaultConfig = defaultTransitionConfig(transitionProps, prevTransitionProps, isModal);

    if (transitionConfigurer) {
      return babelHelpers.extends({}, defaultConfig, transitionConfigurer(transitionProps, prevTransitionProps, isModal));
    }

    return defaultConfig;
  }

  exports.default = {
    defaultTransitionConfig: defaultTransitionConfig,
    getTransitionConfig: getTransitionConfig
  };
},"79b022a09e7e5daf4ec8b2b903c645dc",["cc757a791ecb3cd320f65c256a791c04","deea33604847a0b888ebc73068625bba","b886806cc3f4ad8d13158780908d3098"],"node_modules/react-navigation/src/views/StackView/StackViewTransitionConfigs.js");