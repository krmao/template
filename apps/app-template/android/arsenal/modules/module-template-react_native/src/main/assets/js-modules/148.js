__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Platform = _require(_dependencyMap[0], 'Platform');

  var ReactNativeBridgeEventPlugin = _require(_dependencyMap[1], 'ReactNativeBridgeEventPlugin');

  var ReactNativeStyleAttributes = _require(_dependencyMap[2], 'ReactNativeStyleAttributes');

  var UIManager = _require(_dependencyMap[3], 'UIManager');

  var createReactNativeComponentClass = _require(_dependencyMap[4], 'createReactNativeComponentClass');

  var insetsDiffer = _require(_dependencyMap[5], 'insetsDiffer');

  var matricesDiffer = _require(_dependencyMap[6], 'matricesDiffer');

  var pointsDiffer = _require(_dependencyMap[7], 'pointsDiffer');

  var processColor = _require(_dependencyMap[8], 'processColor');

  var resolveAssetSource = _require(_dependencyMap[9], 'resolveAssetSource');

  var sizesDiffer = _require(_dependencyMap[10], 'sizesDiffer');

  var verifyPropTypes = _require(_dependencyMap[11], 'verifyPropTypes');

  var invariant = _require(_dependencyMap[12], 'fbjs/lib/invariant');

  var warning = _require(_dependencyMap[13], 'fbjs/lib/warning');

  var hasAttachedDefaultEventTypes = false;

  function requireNativeComponent(viewName, componentInterface, extraConfig) {
    function attachDefaultEventTypes(viewConfig) {
      if (Platform.OS === 'android') {
        if (UIManager.ViewManagerNames) {
          viewConfig = merge(viewConfig, UIManager.getDefaultEventTypes());
        } else {
          viewConfig.bubblingEventTypes = merge(viewConfig.bubblingEventTypes, UIManager.genericBubblingEventTypes);
          viewConfig.directEventTypes = merge(viewConfig.directEventTypes, UIManager.genericDirectEventTypes);
        }
      }
    }

    function merge(destination, source) {
      if (!source) {
        return destination;
      }

      if (!destination) {
        return source;
      }

      for (var key in source) {
        if (!source.hasOwnProperty(key)) {
          continue;
        }

        var sourceValue = source[key];

        if (destination.hasOwnProperty(key)) {
          var destinationValue = destination[key];

          if (typeof sourceValue === 'object' && typeof destinationValue === 'object') {
            sourceValue = merge(destinationValue, sourceValue);
          }
        }

        destination[key] = sourceValue;
      }

      return destination;
    }

    function getViewConfig() {
      var viewConfig = UIManager[viewName];
      invariant(viewConfig != null && !viewConfig.NativeProps != null, 'Native component for "%s" does not exist', viewName);
      viewConfig.uiViewClassName = viewName;
      viewConfig.validAttributes = {};

      if (componentInterface) {
        viewConfig.propTypes = typeof componentInterface.__propTypesSecretDontUseThesePlease === 'object' ? componentInterface.__propTypesSecretDontUseThesePlease : componentInterface.propTypes;
      } else {
        viewConfig.propTypes = null;
      }

      var baseModuleName = viewConfig.baseModuleName;
      var bubblingEventTypes = viewConfig.bubblingEventTypes;
      var directEventTypes = viewConfig.directEventTypes;
      var nativeProps = viewConfig.NativeProps;

      while (baseModuleName) {
        var baseModule = UIManager[baseModuleName];

        if (!baseModule) {
          warning(false, 'Base module "%s" does not exist', baseModuleName);
          baseModuleName = null;
        } else {
          bubblingEventTypes = babelHelpers.extends({}, baseModule.bubblingEventTypes, bubblingEventTypes);
          directEventTypes = babelHelpers.extends({}, baseModule.directEventTypes, directEventTypes);
          nativeProps = babelHelpers.extends({}, baseModule.NativeProps, nativeProps);
          baseModuleName = baseModule.baseModuleName;
        }
      }

      viewConfig.bubblingEventTypes = bubblingEventTypes;
      viewConfig.directEventTypes = directEventTypes;

      for (var key in nativeProps) {
        var useAttribute = false;
        var attribute = {};
        var differ = TypeToDifferMap[nativeProps[key]];

        if (differ) {
          attribute.diff = differ;
          useAttribute = true;
        }

        var processor = TypeToProcessorMap[nativeProps[key]];

        if (processor) {
          attribute.process = processor;
          useAttribute = true;
        }

        viewConfig.validAttributes[key] = useAttribute ? attribute : true;
      }

      viewConfig.validAttributes.style = ReactNativeStyleAttributes;

      if (__DEV__) {
        componentInterface && verifyPropTypes(componentInterface, viewConfig, extraConfig && extraConfig.nativeOnly);
      }

      if (!hasAttachedDefaultEventTypes) {
        attachDefaultEventTypes(viewConfig);
        hasAttachedDefaultEventTypes = true;
      }

      ReactNativeBridgeEventPlugin.processEventTypes(viewConfig);
      return viewConfig;
    }

    return createReactNativeComponentClass(viewName, getViewConfig);
  }

  var TypeToDifferMap = {
    CATransform3D: matricesDiffer,
    CGPoint: pointsDiffer,
    CGSize: sizesDiffer,
    UIEdgeInsets: insetsDiffer
  };

  function processColorArray(colors) {
    return colors && colors.map(processColor);
  }

  var TypeToProcessorMap = {
    CGColor: processColor,
    CGColorArray: processColorArray,
    UIColor: processColor,
    UIColorArray: processColorArray,
    CGImage: resolveAssetSource,
    UIImage: resolveAssetSource,
    RCTImageSource: resolveAssetSource,
    Color: processColor,
    ColorArray: processColorArray
  };
  module.exports = requireNativeComponent;
},148,[32,149,150,121,159,160,161,162,155,163,158,170,18,19],"requireNativeComponent");