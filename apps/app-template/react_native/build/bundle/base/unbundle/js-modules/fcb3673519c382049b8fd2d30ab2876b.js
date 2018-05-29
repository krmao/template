__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Image/ImageBackground.js";

  var Image = _require(_dependencyMap[0], 'Image');

  var React = _require(_dependencyMap[1], 'React');

  var StyleSheet = _require(_dependencyMap[2], 'StyleSheet');

  var View = _require(_dependencyMap[3], 'View');

  var ensureComponentIsNative = _require(_dependencyMap[4], 'ensureComponentIsNative');

  var ImageBackground = function (_React$Component) {
    babelHelpers.inherits(ImageBackground, _React$Component);

    function ImageBackground() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, ImageBackground);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = ImageBackground.__proto__ || Object.getPrototypeOf(ImageBackground)).call.apply(_ref, [this].concat(args))), _this), _this._viewRef = null, _this._captureRef = function (ref) {
        _this._viewRef = ref;
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(ImageBackground, [{
      key: "setNativeProps",
      value: function setNativeProps(props) {
        var viewRef = this._viewRef;

        if (viewRef) {
          ensureComponentIsNative(viewRef);
          viewRef.setNativeProps(props);
        }
      }
    }, {
      key: "render",
      value: function render() {
        var _props = this.props,
            children = _props.children,
            style = _props.style,
            imageStyle = _props.imageStyle,
            imageRef = _props.imageRef,
            props = babelHelpers.objectWithoutProperties(_props, ["children", "style", "imageStyle", "imageRef"]);
        return React.createElement(
          View,
          {
            style: style,
            ref: this._captureRef,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 64
            }
          },
          React.createElement(Image, babelHelpers.extends({}, props, {
            style: [StyleSheet.absoluteFill, {
              width: style.width,
              height: style.height
            }, imageStyle],
            ref: imageRef,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 65
            }
          })),
          children
        );
      }
    }]);
    return ImageBackground;
  }(React.Component);

  module.exports = ImageBackground;
},"fcb3673519c382049b8fd2d30ab2876b",["717234c0b5cb768e5677b97d7b48fff8","e6db4f0efed6b72f641ef0ffed29569f","d31e8c1a3f9844becc88973ecddac872","30a3b04291b6e1f01b778ff31271ccc5","1422daa580d530201ccfee48c0551a95"],"ImageBackground");