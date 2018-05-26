__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var NativeAnimatedHelper = _require(_dependencyMap[0], '../NativeAnimatedHelper');

  var invariant = _require(_dependencyMap[1], 'fbjs/lib/invariant');

  var AnimatedNode = function () {
    function AnimatedNode() {
      babelHelpers.classCallCheck(this, AnimatedNode);
    }

    babelHelpers.createClass(AnimatedNode, [{
      key: "__attach",
      value: function __attach() {}
    }, {
      key: "__detach",
      value: function __detach() {
        if (this.__isNative && this.__nativeTag != null) {
          NativeAnimatedHelper.API.dropAnimatedNode(this.__nativeTag);
          this.__nativeTag = undefined;
        }
      }
    }, {
      key: "__getValue",
      value: function __getValue() {}
    }, {
      key: "__getAnimatedValue",
      value: function __getAnimatedValue() {
        return this.__getValue();
      }
    }, {
      key: "__addChild",
      value: function __addChild(child) {}
    }, {
      key: "__removeChild",
      value: function __removeChild(child) {}
    }, {
      key: "__getChildren",
      value: function __getChildren() {
        return [];
      }
    }, {
      key: "__makeNative",
      value: function __makeNative() {
        if (!this.__isNative) {
          throw new Error('This node cannot be made a "native" animated node');
        }
      }
    }, {
      key: "__getNativeTag",
      value: function __getNativeTag() {
        NativeAnimatedHelper.assertNativeAnimatedModule();
        invariant(this.__isNative, 'Attempt to get native tag from node not marked as "native"');

        if (this.__nativeTag == null) {
          var nativeTag = NativeAnimatedHelper.generateNewNodeTag();
          NativeAnimatedHelper.API.createAnimatedNode(nativeTag, this.__getNativeConfig());
          this.__nativeTag = nativeTag;
        }

        return this.__nativeTag;
      }
    }, {
      key: "__getNativeConfig",
      value: function __getNativeConfig() {
        throw new Error('This JS animated node type cannot be used as native animated node');
      }
    }, {
      key: "toJSON",
      value: function toJSON() {
        return this.__getValue();
      }
    }]);
    return AnimatedNode;
  }();

  module.exports = AnimatedNode;
},204,[205,18],"AnimatedNode");