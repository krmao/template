__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var NativeAnimatedHelper = _require(_dependencyMap[0], 'NativeAnimatedHelper');

  var Animation = function () {
    function Animation() {
      babelHelpers.classCallCheck(this, Animation);
    }

    babelHelpers.createClass(Animation, [{
      key: "start",
      value: function start(fromValue, onUpdate, onEnd, previousAnimation, animatedValue) {}
    }, {
      key: "stop",
      value: function stop() {
        if (this.__nativeId) {
          NativeAnimatedHelper.API.stopAnimation(this.__nativeId);
        }
      }
    }, {
      key: "__getNativeAnimationConfig",
      value: function __getNativeAnimationConfig() {
        throw new Error('This animation type cannot be offloaded to native');
      }
    }, {
      key: "__debouncedOnEnd",
      value: function __debouncedOnEnd(result) {
        var onEnd = this.__onEnd;
        this.__onEnd = null;
        onEnd && onEnd(result);
      }
    }, {
      key: "__startNativeAnimation",
      value: function __startNativeAnimation(animatedValue) {
        animatedValue.__makeNative();

        this.__nativeId = NativeAnimatedHelper.generateNewAnimationId();
        NativeAnimatedHelper.API.startAnimatingNode(this.__nativeId, animatedValue.__getNativeTag(), this.__getNativeAnimationConfig(), this.__debouncedOnEnd.bind(this));
      }
    }]);
    return Animation;
  }();

  module.exports = Animation;
},"53a6c9e0c45aaaee15cb89af1a206518",["0efe8cba4f7d1f6a111a4698dabb344c"],"Animation");