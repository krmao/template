__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/ActivityIndicator/ActivityIndicator.js";

  var ColorPropType = _require(_dependencyMap[0], 'ColorPropType');

  var NativeMethodsMixin = _require(_dependencyMap[1], 'NativeMethodsMixin');

  var Platform = _require(_dependencyMap[2], 'Platform');

  var ProgressBarAndroid = _require(_dependencyMap[3], 'ProgressBarAndroid');

  var PropTypes = _require(_dependencyMap[4], 'prop-types');

  var React = _require(_dependencyMap[5], 'React');

  var StyleSheet = _require(_dependencyMap[6], 'StyleSheet');

  var View = _require(_dependencyMap[7], 'View');

  var ViewPropTypes = _require(_dependencyMap[8], 'ViewPropTypes');

  var createReactClass = _require(_dependencyMap[9], 'create-react-class');

  var requireNativeComponent = _require(_dependencyMap[10], 'requireNativeComponent');

  var RCTActivityIndicator = void 0;
  var GRAY = '#999999';
  var ActivityIndicator = createReactClass({
    displayName: 'ActivityIndicator',
    mixins: [NativeMethodsMixin],
    propTypes: babelHelpers.extends({}, ViewPropTypes, {
      animating: PropTypes.bool,
      color: ColorPropType,
      size: PropTypes.oneOfType([PropTypes.oneOf(['small', 'large']), PropTypes.number]),
      hidesWhenStopped: PropTypes.bool
    }),
    getDefaultProps: function getDefaultProps() {
      return {
        animating: true,
        color: Platform.OS === 'ios' ? GRAY : undefined,
        hidesWhenStopped: true,
        size: 'small'
      };
    },
    render: function render() {
      var _props = this.props,
          onLayout = _props.onLayout,
          style = _props.style,
          props = babelHelpers.objectWithoutProperties(_props, ["onLayout", "style"]);
      var sizeStyle = void 0;

      switch (props.size) {
        case 'small':
          sizeStyle = styles.sizeSmall;
          break;

        case 'large':
          sizeStyle = styles.sizeLarge;
          break;

        default:
          sizeStyle = {
            height: props.size,
            width: props.size
          };
          break;
      }

      var nativeProps = babelHelpers.extends({}, props, {
        style: sizeStyle,
        styleAttr: 'Normal',
        indeterminate: true
      });
      return React.createElement(
        View,
        {
          onLayout: onLayout,
          style: [styles.container, style],
          __source: {
            fileName: _jsxFileName,
            lineNumber: 113
          }
        },
        Platform.OS === 'ios' ? React.createElement(RCTActivityIndicator, babelHelpers.extends({}, nativeProps, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 115
          }
        })) : React.createElement(ProgressBarAndroid, babelHelpers.extends({}, nativeProps, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 117
          }
        }))
      );
    }
  });

  if (Platform.OS === 'ios') {
    RCTActivityIndicator = requireNativeComponent('RCTActivityIndicatorView', ActivityIndicator, {
      nativeOnly: {
        activityIndicatorViewStyle: true
      }
    });
  }

  var styles = StyleSheet.create({
    container: {
      alignItems: 'center',
      justifyContent: 'center'
    },
    sizeSmall: {
      width: 20,
      height: 20
    },
    sizeLarge: {
      width: 36,
      height: 36
    }
  });
  module.exports = ActivityIndicator;
},"446c314de88c8d6e75f5fb760a2cc27f",["63c61c7eda525c10d0670d2ef8475012","e2817b4a53aaef19afef34f031e1b9c9","9493a89f5d95c3a8a47c65cfed9b5542","9fe16a26fb98b4a15283ba3fb0cbadf9","18eeaf4e01377a466daaccc6ba8ce6f5","e6db4f0efed6b72f641ef0ffed29569f","d31e8c1a3f9844becc88973ecddac872","30a3b04291b6e1f01b778ff31271ccc5","9ff7e107ed674a99182e71b796d889aa","29cb0e104e5fce198008f3e789631772","98c1697e1928b0d4ea4ae3837ea09d48"],"ActivityIndicator");