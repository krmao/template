__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  function dumpReactTree() {
    try {
      return getReactTree();
    } catch (e) {
      return 'Failed to dump react tree: ' + e;
    }
  }

  function getReactTree() {
    return 'React tree dumps have been temporarily disabled while React is ' + 'upgraded to Fiber.';
  }

  module.exports = dumpReactTree;
},"81df05e5dc9f8543292c0f7ef334c5d3",[],"dumpReactTree");