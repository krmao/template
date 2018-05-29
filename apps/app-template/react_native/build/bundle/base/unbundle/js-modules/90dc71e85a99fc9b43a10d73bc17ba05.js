__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var dummyInsets = {
    top: undefined,
    left: undefined,
    right: undefined,
    bottom: undefined
  };

  var insetsDiffer = function insetsDiffer(one, two) {
    one = one || dummyInsets;
    two = two || dummyInsets;
    return one !== two && (one.top !== two.top || one.left !== two.left || one.right !== two.right || one.bottom !== two.bottom);
  };

  module.exports = insetsDiffer;
},"90dc71e85a99fc9b43a10d73bc17ba05",[],"insetsDiffer");