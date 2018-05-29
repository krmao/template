__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var PooledClass = _require(_dependencyMap[0], 'PooledClass');

  var twoArgumentPooler = PooledClass.twoArgumentPooler;

  function BoundingDimensions(width, height) {
    this.width = width;
    this.height = height;
  }

  BoundingDimensions.prototype.destructor = function () {
    this.width = null;
    this.height = null;
  };

  BoundingDimensions.getPooledFromElement = function (element) {
    return BoundingDimensions.getPooled(element.offsetWidth, element.offsetHeight);
  };

  PooledClass.addPoolingTo(BoundingDimensions, twoArgumentPooler);
  module.exports = BoundingDimensions;
},"a065931349324c289e730ba053baa086",["3ed69876e5214ca722938144dcab1ad4"],"BoundingDimensions");