__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var invariant = _require(_dependencyMap[0], 'fbjs/lib/invariant');

  var MAX_MERGE_DEPTH = 36;

  var isTerminal = function isTerminal(o) {
    return typeof o !== 'object' || o instanceof Date || o === null;
  };

  var mergeHelpers = {
    MAX_MERGE_DEPTH: MAX_MERGE_DEPTH,
    isTerminal: isTerminal,
    normalizeMergeArg: function normalizeMergeArg(arg) {
      return arg === undefined || arg === null ? {} : arg;
    },
    checkMergeArrayArgs: function checkMergeArrayArgs(one, two) {
      invariant(Array.isArray(one) && Array.isArray(two), 'Tried to merge arrays, instead got %s and %s.', one, two);
    },
    checkMergeObjectArgs: function checkMergeObjectArgs(one, two) {
      mergeHelpers.checkMergeObjectArg(one);
      mergeHelpers.checkMergeObjectArg(two);
    },
    checkMergeObjectArg: function checkMergeObjectArg(arg) {
      invariant(!isTerminal(arg) && !Array.isArray(arg), 'Tried to merge an object, instead got %s.', arg);
    },
    checkMergeIntoObjectArg: function checkMergeIntoObjectArg(arg) {
      invariant((!isTerminal(arg) || typeof arg === 'function') && !Array.isArray(arg), 'Tried to merge into an object, instead got %s.', arg);
    },
    checkMergeLevel: function checkMergeLevel(level) {
      invariant(level < MAX_MERGE_DEPTH, 'Maximum deep merge depth exceeded. You may be attempting to merge ' + 'circular structures in an unsupported way.');
    },
    checkArrayStrategy: function checkArrayStrategy(strategy) {
      invariant(strategy === undefined || strategy in mergeHelpers.ArrayStrategies, 'You must provide an array strategy to deep merge functions to ' + 'instruct the deep merge how to resolve merging two arrays.');
    },
    ArrayStrategies: {
      Clobber: 'Clobber',
      Concat: 'Concat',
      IndexByIndex: 'IndexByIndex'
    }
  };
  module.exports = mergeHelpers;
},"2bd83cc7b22b1a52dd73bd77e69e815b",["8940a4ad43b101ffc23e725363c70f8d"],"mergeHelpers");