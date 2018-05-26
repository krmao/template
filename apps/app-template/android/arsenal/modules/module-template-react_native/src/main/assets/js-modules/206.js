__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedNode = _require(_dependencyMap[0], './AnimatedNode');

  var NativeAnimatedHelper = _require(_dependencyMap[1], '../NativeAnimatedHelper');

  var AnimatedWithChildren = function (_AnimatedNode) {
    babelHelpers.inherits(AnimatedWithChildren, _AnimatedNode);

    function AnimatedWithChildren() {
      babelHelpers.classCallCheck(this, AnimatedWithChildren);

      var _this = babelHelpers.possibleConstructorReturn(this, (AnimatedWithChildren.__proto__ || Object.getPrototypeOf(AnimatedWithChildren)).call(this));

      _this._children = [];
      return _this;
    }

    babelHelpers.createClass(AnimatedWithChildren, [{
      key: "__makeNative",
      value: function __makeNative() {
        if (!this.__isNative) {
          this.__isNative = true;

          for (var _iterator = this._children, _isArray = Array.isArray(_iterator), _i = 0, _iterator = _isArray ? _iterator : _iterator[typeof Symbol === "function" ? Symbol.iterator : "@@iterator"]();;) {
            var _ref;

            if (_isArray) {
              if (_i >= _iterator.length) break;
              _ref = _iterator[_i++];
            } else {
              _i = _iterator.next();
              if (_i.done) break;
              _ref = _i.value;
            }

            var child = _ref;

            child.__makeNative();

            NativeAnimatedHelper.API.connectAnimatedNodes(this.__getNativeTag(), child.__getNativeTag());
          }
        }
      }
    }, {
      key: "__addChild",
      value: function __addChild(child) {
        if (this._children.length === 0) {
          this.__attach();
        }

        this._children.push(child);

        if (this.__isNative) {
          child.__makeNative();

          NativeAnimatedHelper.API.connectAnimatedNodes(this.__getNativeTag(), child.__getNativeTag());
        }
      }
    }, {
      key: "__removeChild",
      value: function __removeChild(child) {
        var index = this._children.indexOf(child);

        if (index === -1) {
          console.warn("Trying to remove a child that doesn't exist");
          return;
        }

        if (this.__isNative && child.__isNative) {
          NativeAnimatedHelper.API.disconnectAnimatedNodes(this.__getNativeTag(), child.__getNativeTag());
        }

        this._children.splice(index, 1);

        if (this._children.length === 0) {
          this.__detach();
        }
      }
    }, {
      key: "__getChildren",
      value: function __getChildren() {
        return this._children;
      }
    }]);
    return AnimatedWithChildren;
  }(AnimatedNode);

  module.exports = AnimatedWithChildren;
},206,[204,205],"AnimatedWithChildren");