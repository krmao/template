__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Dimensions = _require(_dependencyMap[0], 'Dimensions');

  var PixelRatio = function () {
    function PixelRatio() {
      babelHelpers.classCallCheck(this, PixelRatio);
    }

    babelHelpers.createClass(PixelRatio, null, [{
      key: "get",
      value: function get() {
        return Dimensions.get('window').scale;
      }
    }, {
      key: "getFontScale",
      value: function getFontScale() {
        return Dimensions.get('window').fontScale || PixelRatio.get();
      }
    }, {
      key: "getPixelSizeForLayoutSize",
      value: function getPixelSizeForLayoutSize(layoutSize) {
        return Math.round(layoutSize * PixelRatio.get());
      }
    }, {
      key: "roundToNearestPixel",
      value: function roundToNearestPixel(layoutSize) {
        var ratio = PixelRatio.get();
        return Math.round(layoutSize * ratio) / ratio;
      }
    }, {
      key: "startDetecting",
      value: function startDetecting() {}
    }]);
    return PixelRatio;
  }();

  module.exports = PixelRatio;
},166,[167],"PixelRatio");