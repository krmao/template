__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Promise = _require(_dependencyMap[0], 'promise/setimmediate/es6-extensions');

  _require(_dependencyMap[1], 'promise/setimmediate/done');

  Promise.prototype['finally'] = function (onSettled) {
    return this.then(onSettled, onSettled);
  };

  module.exports = Promise;
},67,[68,70],"node_modules/fbjs/lib/Promise.native.js");