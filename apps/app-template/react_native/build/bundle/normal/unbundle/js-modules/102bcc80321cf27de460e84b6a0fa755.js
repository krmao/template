__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedValue = _require(_dependencyMap[0], './AnimatedValue');

  var AnimatedWithChildren = _require(_dependencyMap[1], './AnimatedWithChildren');

  var invariant = _require(_dependencyMap[2], 'fbjs/lib/invariant');

  var _uniqueId = 1;

  var AnimatedValueXY = function (_AnimatedWithChildren) {
    babelHelpers.inherits(AnimatedValueXY, _AnimatedWithChildren);

    function AnimatedValueXY(valueIn) {
      babelHelpers.classCallCheck(this, AnimatedValueXY);

      var _this = babelHelpers.possibleConstructorReturn(this, (AnimatedValueXY.__proto__ || Object.getPrototypeOf(AnimatedValueXY)).call(this));

      var value = valueIn || {
        x: 0,
        y: 0
      };

      if (typeof value.x === 'number' && typeof value.y === 'number') {
        _this.x = new AnimatedValue(value.x);
        _this.y = new AnimatedValue(value.y);
      } else {
        invariant(value.x instanceof AnimatedValue && value.y instanceof AnimatedValue, 'AnimatedValueXY must be initialized with an object of numbers or ' + 'AnimatedValues.');
        _this.x = value.x;
        _this.y = value.y;
      }

      _this._listeners = {};
      return _this;
    }

    babelHelpers.createClass(AnimatedValueXY, [{
      key: "setValue",
      value: function setValue(value) {
        this.x.setValue(value.x);
        this.y.setValue(value.y);
      }
    }, {
      key: "setOffset",
      value: function setOffset(offset) {
        this.x.setOffset(offset.x);
        this.y.setOffset(offset.y);
      }
    }, {
      key: "flattenOffset",
      value: function flattenOffset() {
        this.x.flattenOffset();
        this.y.flattenOffset();
      }
    }, {
      key: "extractOffset",
      value: function extractOffset() {
        this.x.extractOffset();
        this.y.extractOffset();
      }
    }, {
      key: "__getValue",
      value: function __getValue() {
        return {
          x: this.x.__getValue(),
          y: this.y.__getValue()
        };
      }
    }, {
      key: "resetAnimation",
      value: function resetAnimation(callback) {
        this.x.resetAnimation();
        this.y.resetAnimation();
        callback && callback(this.__getValue());
      }
    }, {
      key: "stopAnimation",
      value: function stopAnimation(callback) {
        this.x.stopAnimation();
        this.y.stopAnimation();
        callback && callback(this.__getValue());
      }
    }, {
      key: "addListener",
      value: function addListener(callback) {
        var _this2 = this;

        var id = String(_uniqueId++);

        var jointCallback = function jointCallback(_ref) {
          var number = _ref.value;
          callback(_this2.__getValue());
        };

        this._listeners[id] = {
          x: this.x.addListener(jointCallback),
          y: this.y.addListener(jointCallback)
        };
        return id;
      }
    }, {
      key: "removeListener",
      value: function removeListener(id) {
        this.x.removeListener(this._listeners[id].x);
        this.y.removeListener(this._listeners[id].y);
        delete this._listeners[id];
      }
    }, {
      key: "removeAllListeners",
      value: function removeAllListeners() {
        this.x.removeAllListeners();
        this.y.removeAllListeners();
        this._listeners = {};
      }
    }, {
      key: "getLayout",
      value: function getLayout() {
        return {
          left: this.x,
          top: this.y
        };
      }
    }, {
      key: "getTranslateTransform",
      value: function getTranslateTransform() {
        return [{
          translateX: this.x
        }, {
          translateY: this.y
        }];
      }
    }]);
    return AnimatedValueXY;
  }(AnimatedWithChildren);

  module.exports = AnimatedValueXY;
},"102bcc80321cf27de460e84b6a0fa755",["9afc2783fb30bfb04619192598012491","432262d84c52a4f1a29f541ab0bb0c48","8940a4ad43b101ffc23e725363c70f8d"],"AnimatedValueXY");