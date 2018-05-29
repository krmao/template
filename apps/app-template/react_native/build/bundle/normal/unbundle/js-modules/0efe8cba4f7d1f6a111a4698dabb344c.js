__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var NativeAnimatedModule = _require(_dependencyMap[0], 'NativeModules').NativeAnimatedModule;

  var NativeEventEmitter = _require(_dependencyMap[1], 'NativeEventEmitter');

  var invariant = _require(_dependencyMap[2], 'fbjs/lib/invariant');

  var __nativeAnimatedNodeTagCount = 1;
  var __nativeAnimationIdCount = 1;
  var nativeEventEmitter = void 0;
  var API = {
    createAnimatedNode: function createAnimatedNode(tag, config) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.createAnimatedNode(tag, config);
    },
    startListeningToAnimatedNodeValue: function startListeningToAnimatedNodeValue(tag) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.startListeningToAnimatedNodeValue(tag);
    },
    stopListeningToAnimatedNodeValue: function stopListeningToAnimatedNodeValue(tag) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.stopListeningToAnimatedNodeValue(tag);
    },
    connectAnimatedNodes: function connectAnimatedNodes(parentTag, childTag) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.connectAnimatedNodes(parentTag, childTag);
    },
    disconnectAnimatedNodes: function disconnectAnimatedNodes(parentTag, childTag) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.disconnectAnimatedNodes(parentTag, childTag);
    },
    startAnimatingNode: function startAnimatingNode(animationId, nodeTag, config, endCallback) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.startAnimatingNode(animationId, nodeTag, config, endCallback);
    },
    stopAnimation: function stopAnimation(animationId) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.stopAnimation(animationId);
    },
    setAnimatedNodeValue: function setAnimatedNodeValue(nodeTag, value) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.setAnimatedNodeValue(nodeTag, value);
    },
    setAnimatedNodeOffset: function setAnimatedNodeOffset(nodeTag, offset) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.setAnimatedNodeOffset(nodeTag, offset);
    },
    flattenAnimatedNodeOffset: function flattenAnimatedNodeOffset(nodeTag) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.flattenAnimatedNodeOffset(nodeTag);
    },
    extractAnimatedNodeOffset: function extractAnimatedNodeOffset(nodeTag) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.extractAnimatedNodeOffset(nodeTag);
    },
    connectAnimatedNodeToView: function connectAnimatedNodeToView(nodeTag, viewTag) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.connectAnimatedNodeToView(nodeTag, viewTag);
    },
    disconnectAnimatedNodeFromView: function disconnectAnimatedNodeFromView(nodeTag, viewTag) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.disconnectAnimatedNodeFromView(nodeTag, viewTag);
    },
    dropAnimatedNode: function dropAnimatedNode(tag) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.dropAnimatedNode(tag);
    },
    addAnimatedEventToView: function addAnimatedEventToView(viewTag, eventName, eventMapping) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.addAnimatedEventToView(viewTag, eventName, eventMapping);
    },
    removeAnimatedEventFromView: function removeAnimatedEventFromView(viewTag, eventName, animatedNodeTag) {
      assertNativeAnimatedModule();
      NativeAnimatedModule.removeAnimatedEventFromView(viewTag, eventName, animatedNodeTag);
    }
  };
  var STYLES_WHITELIST = {
    opacity: true,
    transform: true,
    shadowOpacity: true,
    shadowRadius: true,
    scaleX: true,
    scaleY: true,
    translateX: true,
    translateY: true
  };
  var TRANSFORM_WHITELIST = {
    translateX: true,
    translateY: true,
    scale: true,
    scaleX: true,
    scaleY: true,
    rotate: true,
    rotateX: true,
    rotateY: true,
    perspective: true
  };
  var SUPPORTED_INTERPOLATION_PARAMS = {
    inputRange: true,
    outputRange: true,
    extrapolate: true,
    extrapolateRight: true,
    extrapolateLeft: true
  };

  function addWhitelistedStyleProp(prop) {
    STYLES_WHITELIST[prop] = true;
  }

  function addWhitelistedTransformProp(prop) {
    TRANSFORM_WHITELIST[prop] = true;
  }

  function addWhitelistedInterpolationParam(param) {
    SUPPORTED_INTERPOLATION_PARAMS[param] = true;
  }

  function validateTransform(configs) {
    configs.forEach(function (config) {
      if (!TRANSFORM_WHITELIST.hasOwnProperty(config.property)) {
        throw new Error("Property '" + config.property + "' is not supported by native animated module");
      }
    });
  }

  function validateStyles(styles) {
    for (var key in styles) {
      if (!STYLES_WHITELIST.hasOwnProperty(key)) {
        throw new Error("Style property '" + key + "' is not supported by native animated module");
      }
    }
  }

  function validateInterpolation(config) {
    for (var key in config) {
      if (!SUPPORTED_INTERPOLATION_PARAMS.hasOwnProperty(key)) {
        throw new Error("Interpolation property '" + key + "' is not supported by native animated module");
      }
    }
  }

  function generateNewNodeTag() {
    return __nativeAnimatedNodeTagCount++;
  }

  function generateNewAnimationId() {
    return __nativeAnimationIdCount++;
  }

  function assertNativeAnimatedModule() {
    invariant(NativeAnimatedModule, 'Native animated module is not available');
  }

  var _warnedMissingNativeAnimated = false;

  function shouldUseNativeDriver(config) {
    if (config.useNativeDriver && !NativeAnimatedModule) {
      if (!_warnedMissingNativeAnimated) {
        console.warn('Animated: `useNativeDriver` is not supported because the native ' + 'animated module is missing. Falling back to JS-based animation. To ' + 'resolve this, add `RCTAnimation` module to this app, or remove ' + '`useNativeDriver`. ' + 'More info: https://github.com/facebook/react-native/issues/11094#issuecomment-263240420');
        _warnedMissingNativeAnimated = true;
      }

      return false;
    }

    return config.useNativeDriver || false;
  }

  module.exports = {
    API: API,
    addWhitelistedStyleProp: addWhitelistedStyleProp,
    addWhitelistedTransformProp: addWhitelistedTransformProp,
    addWhitelistedInterpolationParam: addWhitelistedInterpolationParam,
    validateStyles: validateStyles,
    validateTransform: validateTransform,
    validateInterpolation: validateInterpolation,
    generateNewNodeTag: generateNewNodeTag,
    generateNewAnimationId: generateNewAnimationId,
    assertNativeAnimatedModule: assertNativeAnimatedModule,
    shouldUseNativeDriver: shouldUseNativeDriver,

    get nativeEventEmitter() {
      if (!nativeEventEmitter) {
        nativeEventEmitter = new NativeEventEmitter(NativeAnimatedModule);
      }

      return nativeEventEmitter;
    }

  };
},"0efe8cba4f7d1f6a111a4698dabb344c",["ce21807d4d291be64fa852393519f6c8","522e0292cd937e7e7dc15e8d27ea9246","8940a4ad43b101ffc23e725363c70f8d"],"NativeAnimatedHelper");