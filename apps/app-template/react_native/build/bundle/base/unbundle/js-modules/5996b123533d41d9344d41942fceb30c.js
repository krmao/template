__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _reactNative = _require(_dependencyMap[0], "react-native");

  var AnimatedValueSubscription = function () {
    function AnimatedValueSubscription(value, callback) {
      babelHelpers.classCallCheck(this, AnimatedValueSubscription);
      this._value = value;
      this._token = value.addListener(callback);
    }

    babelHelpers.createClass(AnimatedValueSubscription, [{
      key: "remove",
      value: function remove() {
        this._value.removeListener(this._token);
      }
    }]);
    return AnimatedValueSubscription;
  }();

  exports.default = AnimatedValueSubscription;
},"5996b123533d41d9344d41942fceb30c",["cc757a791ecb3cd320f65c256a791c04"],"node_modules/react-navigation/src/views/AnimatedValueSubscription.js");