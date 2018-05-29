__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/ProgressBarAndroid/ProgressBarAndroid.android.js";

  var ColorPropType = _require(_dependencyMap[0], 'ColorPropType');

  var PropTypes = _require(_dependencyMap[1], 'prop-types');

  var React = _require(_dependencyMap[2], 'React');

  var ReactNative = _require(_dependencyMap[3], 'ReactNative');

  var ViewPropTypes = _require(_dependencyMap[4], 'ViewPropTypes');

  var requireNativeComponent = _require(_dependencyMap[5], 'requireNativeComponent');

  var STYLE_ATTRIBUTES = ['Horizontal', 'Normal', 'Small', 'Large', 'Inverse', 'SmallInverse', 'LargeInverse'];

  var indeterminateType = function indeterminateType(props, propName, componentName) {
    var checker = function checker() {
      var indeterminate = props[propName];
      var styleAttr = props.styleAttr;

      if (!indeterminate && styleAttr !== 'Horizontal') {
        return new Error('indeterminate=false is only valid for styleAttr=Horizontal');
      }
    };

    for (var _len = arguments.length, rest = Array(_len > 3 ? _len - 3 : 0), _key = 3; _key < _len; _key++) {
      rest[_key - 3] = arguments[_key];
    }

    return PropTypes.bool.apply(PropTypes, [props, propName, componentName].concat(rest)) || checker();
  };

  var ProgressBarAndroid = function (_ReactNative$NativeCo) {
    babelHelpers.inherits(ProgressBarAndroid, _ReactNative$NativeCo);

    function ProgressBarAndroid() {
      babelHelpers.classCallCheck(this, ProgressBarAndroid);
      return babelHelpers.possibleConstructorReturn(this, (ProgressBarAndroid.__proto__ || Object.getPrototypeOf(ProgressBarAndroid)).apply(this, arguments));
    }

    babelHelpers.createClass(ProgressBarAndroid, [{
      key: "render",
      value: function render() {
        return React.createElement(AndroidProgressBar, babelHelpers.extends({}, this.props, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 110
          }
        }));
      }
    }]);
    return ProgressBarAndroid;
  }(ReactNative.NativeComponent);

  ProgressBarAndroid.propTypes = babelHelpers.extends({}, ViewPropTypes, {
    styleAttr: PropTypes.oneOf(STYLE_ATTRIBUTES),
    animating: PropTypes.bool,
    indeterminate: indeterminateType,
    progress: PropTypes.number,
    color: ColorPropType,
    testID: PropTypes.string
  });
  ProgressBarAndroid.defaultProps = {
    styleAttr: 'Normal',
    indeterminate: true,
    animating: true
  };
  var AndroidProgressBar = requireNativeComponent('AndroidProgressBar', ProgressBarAndroid, {
    nativeOnly: {
      animating: true
    }
  });
  module.exports = ProgressBarAndroid;
},"9fe16a26fb98b4a15283ba3fb0cbadf9",["63c61c7eda525c10d0670d2ef8475012","18eeaf4e01377a466daaccc6ba8ce6f5","e6db4f0efed6b72f641ef0ffed29569f","1102b68d89d7a6aede9677567aa01362","9ff7e107ed674a99182e71b796d889aa","98c1697e1928b0d4ea4ae3837ea09d48"],"ProgressBarAndroid");