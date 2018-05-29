__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Promise = _require(_dependencyMap[0], 'fbjs/lib/Promise.native');

  if (__DEV__) {
    _require(_dependencyMap[1], 'promise/setimmediate/rejection-tracking').enable({
      allRejections: true,
      onUnhandled: function onUnhandled(id) {
        var error = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};
        var message = void 0;
        var stack = void 0;
        var stringValue = Object.prototype.toString.call(error);

        if (stringValue === '[object Error]') {
          message = Error.prototype.toString.call(error);
          stack = error.stack;
        } else {
          message = _require(_dependencyMap[2], 'pretty-format')(error);
        }

        var warning = "Possible Unhandled Promise Rejection (id: " + id + "):\n" + (message + "\n") + (stack == null ? '' : stack);
        console.warn(warning);
      },
      onHandled: function onHandled(id) {
        var warning = "Promise Rejection Handled (id: " + id + ")\n" + 'This means you can ignore any previous messages of the form ' + ("\"Possible Unhandled Promise Rejection (id: " + id + "):\"");
        console.warn(warning);
      }
    });
  }

  module.exports = Promise;
},"671e0d3f7a033da96d1450e1878fc16f",["1955ab0229a3b0d990b6cc4b263f6681","a746aeac0009f0e5dc560b0d81b2ff44","c38fe993638abcd0f2fb0fa93884d321"],"Promise");