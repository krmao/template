__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var PooledClass = _require(_dependencyMap[0], 'PooledClass');

  var twoArgumentPooler = PooledClass.twoArgumentPooler;

  function Position(left, top) {
    this.left = left;
    this.top = top;
  }

  Position.prototype.destructor = function () {
    this.left = null;
    this.top = null;
  };

  PooledClass.addPoolingTo(Position, twoArgumentPooler);
  module.exports = Position;
},"dd806f6b8b68635a2f6c16fac8b6e51d",["3ed69876e5214ca722938144dcab1ad4"],"Position");