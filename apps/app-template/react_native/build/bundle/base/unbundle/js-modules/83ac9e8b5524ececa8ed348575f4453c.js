__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ReactNativeStyleAttributes = _require(_dependencyMap[0], 'ReactNativeStyleAttributes');

  function verifyPropTypes(componentInterface, viewConfig, nativePropsToIgnore) {
    if (!viewConfig) {
      return;
    }

    var componentName = componentInterface.displayName || componentInterface.name || 'unknown';
    var propTypes = componentInterface.__propTypesSecretDontUseThesePlease || componentInterface.propTypes;

    if (!propTypes) {
      return;
    }

    var nativeProps = viewConfig.NativeProps;

    for (var prop in nativeProps) {
      if (!propTypes[prop] && !ReactNativeStyleAttributes[prop] && (!nativePropsToIgnore || !nativePropsToIgnore[prop])) {
        var message;

        if (propTypes.hasOwnProperty(prop)) {
          message = '`' + componentName + '` has incorrectly defined propType for native prop `' + viewConfig.uiViewClassName + '.' + prop + '` of native type `' + nativeProps[prop];
        } else {
          message = '`' + componentName + '` has no propType for native prop `' + viewConfig.uiViewClassName + '.' + prop + '` of native type `' + nativeProps[prop] + '`';
        }

        message += "\nIf you haven't changed this prop yourself, this usually means that " + 'your versions of the native code and JavaScript code are out of sync. Updating both ' + 'should make this error go away.';
        throw new Error(message);
      }
    }
  }

  module.exports = verifyPropTypes;
},"83ac9e8b5524ececa8ed348575f4453c",["48a8d189c373be9b55e02c49ac64e2e8"],"verifyPropTypes");