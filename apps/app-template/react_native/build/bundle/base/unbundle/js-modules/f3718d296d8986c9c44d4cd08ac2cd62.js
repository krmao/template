__d(function (global, _require, module, exports, _dependencyMap) {
  "use strict";

  var mergeHelpers = _require(_dependencyMap[0], 'mergeHelpers');

  var checkMergeObjectArg = mergeHelpers.checkMergeObjectArg;
  var checkMergeIntoObjectArg = mergeHelpers.checkMergeIntoObjectArg;

  function mergeInto(one, two) {
    checkMergeIntoObjectArg(one);

    if (two != null) {
      checkMergeObjectArg(two);

      for (var key in two) {
        if (!two.hasOwnProperty(key)) {
          continue;
        }

        one[key] = two[key];
      }
    }
  }

  module.exports = mergeInto;
},"f3718d296d8986c9c44d4cd08ac2cd62",["2bd83cc7b22b1a52dd73bd77e69e815b"],"mergeInto");