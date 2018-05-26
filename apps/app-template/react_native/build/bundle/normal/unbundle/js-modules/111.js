__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ToastAndroid = _require(_dependencyMap[0], 'ToastAndroid');

  var TOAST_SHORT_DELAY = 2000;

  var HMRLoadingView = function () {
    function HMRLoadingView() {
      babelHelpers.classCallCheck(this, HMRLoadingView);
    }

    babelHelpers.createClass(HMRLoadingView, null, [{
      key: "showMessage",
      value: function showMessage(message) {
        if (HMRLoadingView._showing) {
          return;
        }

        ToastAndroid.show(message, ToastAndroid.SHORT);
        HMRLoadingView._showing = true;
        setTimeout(function () {
          HMRLoadingView._showing = false;
        }, TOAST_SHORT_DELAY);
      }
    }, {
      key: "hide",
      value: function hide() {}
    }]);
    return HMRLoadingView;
  }();

  module.exports = HMRLoadingView;
},111,[112],"HMRLoadingView");