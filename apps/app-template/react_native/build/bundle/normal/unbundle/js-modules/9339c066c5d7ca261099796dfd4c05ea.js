__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var HeapCapture = {
    captureHeap: function captureHeap(path) {
      var error = null;

      try {
        global.nativeCaptureHeap(path);
        console.log('HeapCapture.captureHeap succeeded: ' + path);
      } catch (e) {
        console.log('HeapCapture.captureHeap error: ' + e.toString());
        error = e.toString();
      }

      _require(_dependencyMap[0], 'NativeModules').JSCHeapCapture.captureComplete(path, error);
    }
  };
  module.exports = HeapCapture;
},"9339c066c5d7ca261099796dfd4c05ea",["ce21807d4d291be64fa852393519f6c8"],"HeapCapture");