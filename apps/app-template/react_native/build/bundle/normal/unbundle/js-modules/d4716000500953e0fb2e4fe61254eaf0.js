__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var SamplingProfiler = {
    poke: function poke(token) {
      var error = null;
      var result = null;

      try {
        result = global.pokeSamplingProfiler();

        if (result === null) {
          console.log('The JSC Sampling Profiler has started');
        } else {
          console.log('The JSC Sampling Profiler has stopped');
        }
      } catch (e) {
        console.log('Error occurred when restarting Sampling Profiler: ' + e.toString());
        error = e.toString();
      }

      var _require = _require2(_dependencyMap[0], 'NativeModules'),
          JSCSamplingProfiler = _require.JSCSamplingProfiler;

      JSCSamplingProfiler.operationComplete(token, result, error);
    }
  };
  module.exports = SamplingProfiler;
},"d4716000500953e0fb2e4fe61254eaf0",["ce21807d4d291be64fa852393519f6c8"],"SamplingProfiler");