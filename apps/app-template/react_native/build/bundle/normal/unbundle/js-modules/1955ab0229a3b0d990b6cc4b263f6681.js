__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Promise = _require(_dependencyMap[0], 'promise/setimmediate/es6-extensions');

  _require(_dependencyMap[1], 'promise/setimmediate/done');

  Promise.prototype['finally'] = function (onSettled) {
    return this.then(onSettled, onSettled);
  };

  module.exports = Promise;
},"1955ab0229a3b0d990b6cc4b263f6681",["6d59fa2d58547d22bb0d53d94e07a807","468ca029c2f1a246aeab8a13b00bdc2d"],"node_modules/fbjs/lib/Promise.native.js");