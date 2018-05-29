__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var NativeModules = _require(_dependencyMap[0], 'NativeModules');

  var invariant = _require(_dependencyMap[1], 'fbjs/lib/invariant');

  var FrameRateLogger = {
    setGlobalOptions: function setGlobalOptions(options) {
      if (options.debug !== undefined) {
        invariant(NativeModules.FrameRateLogger, 'Trying to debug FrameRateLogger without the native module!');
      }

      NativeModules.FrameRateLogger && NativeModules.FrameRateLogger.setGlobalOptions(options);
    },
    setContext: function setContext(context) {
      NativeModules.FrameRateLogger && NativeModules.FrameRateLogger.setContext(context);
    },
    beginScroll: function beginScroll() {
      NativeModules.FrameRateLogger && NativeModules.FrameRateLogger.beginScroll();
    },
    endScroll: function endScroll() {
      NativeModules.FrameRateLogger && NativeModules.FrameRateLogger.endScroll();
    }
  };
  module.exports = FrameRateLogger;
},"7ce68119ca8cc878ecda6c429e543689",["ce21807d4d291be64fa852393519f6c8","8940a4ad43b101ffc23e725363c70f8d"],"FrameRateLogger");