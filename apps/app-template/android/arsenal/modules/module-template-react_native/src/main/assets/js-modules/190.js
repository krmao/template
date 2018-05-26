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
},190,[189],"Position");