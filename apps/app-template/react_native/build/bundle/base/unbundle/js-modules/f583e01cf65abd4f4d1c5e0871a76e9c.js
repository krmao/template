__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  function mapWithSeparator(items, itemRenderer, spacerRenderer) {
    var mapped = [];

    if (items.length > 0) {
      mapped.push(itemRenderer(items[0], 0, items));

      for (var ii = 1; ii < items.length; ii++) {
        mapped.push(spacerRenderer(ii - 1), itemRenderer(items[ii], ii, items));
      }
    }

    return mapped;
  }

  module.exports = mapWithSeparator;
},"f583e01cf65abd4f4d1c5e0871a76e9c",[],"mapWithSeparator");