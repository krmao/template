__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Promise = _require(_dependencyMap[0], './core.js');

  module.exports = Promise;

  Promise.prototype.done = function (onFulfilled, onRejected) {
    var self = arguments.length ? this.then.apply(this, arguments) : this;
    self.then(null, function (err) {
      setTimeout(function () {
        throw err;
      }, 0);
    });
  };
},"468ca029c2f1a246aeab8a13b00bdc2d",["004be1e3b93c14310ddbd367a1c079df"],"node_modules/promise/setimmediate/done.js");