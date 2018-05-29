__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  function _shouldActuallyPolyfillES6Collection(collectionName) {
    var Collection = global[collectionName];

    if (Collection == null) {
      return true;
    }

    if (typeof global.Symbol !== 'function') {
      return true;
    }

    var proto = Collection.prototype;
    return Collection == null || typeof Collection !== 'function' || typeof proto.clear !== 'function' || new Collection().size !== 0 || typeof proto.keys !== 'function' || typeof proto.forEach !== 'function';
  }

  var cache = {};

  function _shouldPolyfillES6Collection(collectionName) {
    var result = cache[collectionName];

    if (result !== undefined) {
      return result;
    }

    result = _shouldActuallyPolyfillES6Collection(collectionName);
    cache[collectionName] = result;
    return result;
  }

  module.exports = _shouldPolyfillES6Collection;
},"ee4b9bb0852a41afb06d2c68334f0f19",[],"_shouldPolyfillES6Collection");