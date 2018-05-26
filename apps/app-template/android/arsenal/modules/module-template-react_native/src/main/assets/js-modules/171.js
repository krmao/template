__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var PixelRatio = _require(_dependencyMap[0], 'PixelRatio');

  var ReactNativePropRegistry = _require(_dependencyMap[1], 'ReactNativePropRegistry');

  var ReactNativeStyleAttributes = _require(_dependencyMap[2], 'ReactNativeStyleAttributes');

  var StyleSheetValidation = _require(_dependencyMap[3], 'StyleSheetValidation');

  var flatten = _require(_dependencyMap[4], 'flattenStyle');

  var hairlineWidth = PixelRatio.roundToNearestPixel(0.4);

  if (hairlineWidth === 0) {
    hairlineWidth = 1 / PixelRatio.get();
  }

  var absoluteFillObject = {
    position: 'absolute',
    left: 0,
    right: 0,
    top: 0,
    bottom: 0
  };
  var absoluteFill = ReactNativePropRegistry.register(absoluteFillObject);
  module.exports = {
    hairlineWidth: hairlineWidth,
    absoluteFill: absoluteFill,
    absoluteFillObject: absoluteFillObject,
    compose: function compose(style1, style2) {
      if (style1 != null && style2 != null) {
        return [style1, style2];
      } else {
        return style1 != null ? style1 : style2;
      }
    },
    flatten: flatten,
    setStyleAttributePreprocessor: function setStyleAttributePreprocessor(property, process) {
      var value = void 0;

      if (typeof ReactNativeStyleAttributes[property] === 'string') {
        value = {};
      } else if (typeof ReactNativeStyleAttributes[property] === 'object') {
        value = ReactNativeStyleAttributes[property];
      } else {
        console.error(property + " is not a valid style attribute");
        return;
      }

      if (__DEV__ && typeof value.process === 'function') {
        console.warn("Overwriting " + property + " style attribute preprocessor");
      }

      ReactNativeStyleAttributes[property] = babelHelpers.extends({}, value, {
        process: process
      });
    },
    create: function create(obj) {
      var result = {};

      for (var key in obj) {
        StyleSheetValidation.validateStyle(key, obj);
        result[key] = obj[key] && ReactNativePropRegistry.register(obj[key]);
      }

      return result;
    }
  };
},171,[166,117,150,172,116],"StyleSheet");