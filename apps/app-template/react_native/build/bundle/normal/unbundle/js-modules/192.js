__d(function (global, _require, module, exports, _dependencyMap) {
  "use strict";

  var TouchEventUtils = {
    extractSingleTouch: function extractSingleTouch(nativeEvent) {
      var touches = nativeEvent.touches;
      var changedTouches = nativeEvent.changedTouches;
      var hasTouches = touches && touches.length > 0;
      var hasChangedTouches = changedTouches && changedTouches.length > 0;
      return !hasTouches && hasChangedTouches ? changedTouches[0] : hasTouches ? touches[0] : nativeEvent;
    }
  };
  module.exports = TouchEventUtils;
},192,[],"node_modules/fbjs/lib/TouchEventUtils.js");