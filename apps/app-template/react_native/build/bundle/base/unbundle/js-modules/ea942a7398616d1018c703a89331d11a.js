__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/Slider/Slider.js";

  var Image = _require(_dependencyMap[0], 'Image');

  var ColorPropType = _require(_dependencyMap[1], 'ColorPropType');

  var NativeMethodsMixin = _require(_dependencyMap[2], 'NativeMethodsMixin');

  var ReactNativeViewAttributes = _require(_dependencyMap[3], 'ReactNativeViewAttributes');

  var Platform = _require(_dependencyMap[4], 'Platform');

  var React = _require(_dependencyMap[5], 'React');

  var PropTypes = _require(_dependencyMap[6], 'prop-types');

  var StyleSheet = _require(_dependencyMap[7], 'StyleSheet');

  var ViewPropTypes = _require(_dependencyMap[8], 'ViewPropTypes');

  var createReactClass = _require(_dependencyMap[9], 'create-react-class');

  var requireNativeComponent = _require(_dependencyMap[10], 'requireNativeComponent');

  var Slider = createReactClass({
    displayName: 'Slider',
    mixins: [NativeMethodsMixin],
    propTypes: babelHelpers.extends({}, ViewPropTypes, {
      style: ViewPropTypes.style,
      value: PropTypes.number,
      step: PropTypes.number,
      minimumValue: PropTypes.number,
      maximumValue: PropTypes.number,
      minimumTrackTintColor: ColorPropType,
      maximumTrackTintColor: ColorPropType,
      disabled: PropTypes.bool,
      trackImage: Image.propTypes.source,
      minimumTrackImage: Image.propTypes.source,
      maximumTrackImage: Image.propTypes.source,
      thumbImage: Image.propTypes.source,
      thumbTintColor: ColorPropType,
      onValueChange: PropTypes.func,
      onSlidingComplete: PropTypes.func,
      testID: PropTypes.string
    }),
    getDefaultProps: function getDefaultProps() {
      return {
        disabled: false,
        value: 0,
        minimumValue: 0,
        maximumValue: 1,
        step: 0
      };
    },
    viewConfig: {
      uiViewClassName: 'RCTSlider',
      validAttributes: babelHelpers.extends({}, ReactNativeViewAttributes.RCTView, {
        value: true
      })
    },
    render: function render() {
      var _props = this.props,
          style = _props.style,
          onValueChange = _props.onValueChange,
          onSlidingComplete = _props.onSlidingComplete,
          props = babelHelpers.objectWithoutProperties(_props, ["style", "onValueChange", "onSlidingComplete"]);
      props.style = [styles.slider, style];

      props.onValueChange = onValueChange && function (event) {
        var userEvent = true;

        if (Platform.OS === 'android') {
          userEvent = event.nativeEvent.fromUser;
        }

        onValueChange && userEvent && onValueChange(event.nativeEvent.value);
      };

      props.onChange = props.onValueChange;

      props.onSlidingComplete = onSlidingComplete && function (event) {
        onSlidingComplete && onSlidingComplete(event.nativeEvent.value);
      };

      return React.createElement(RCTSlider, babelHelpers.extends({}, props, {
        enabled: !this.props.disabled,
        onStartShouldSetResponder: function onStartShouldSetResponder() {
          return true;
        },
        onResponderTerminationRequest: function onResponderTerminationRequest() {
          return false;
        },
        __source: {
          fileName: _jsxFileName,
          lineNumber: 246
        }
      }));
    }
  });
  var styles = void 0;

  if (Platform.OS === 'ios') {
    styles = StyleSheet.create({
      slider: {
        height: 40
      }
    });
  } else {
    styles = StyleSheet.create({
      slider: {}
    });
  }

  var options = {};

  if (Platform.OS === 'android') {
    options = {
      nativeOnly: {
        enabled: true
      }
    };
  }

  var RCTSlider = requireNativeComponent('RCTSlider', Slider, options);
  module.exports = Slider;
},"ea942a7398616d1018c703a89331d11a",["717234c0b5cb768e5677b97d7b48fff8","63c61c7eda525c10d0670d2ef8475012","e2817b4a53aaef19afef34f031e1b9c9","6477887be0d285a967d42967386335cd","9493a89f5d95c3a8a47c65cfed9b5542","e6db4f0efed6b72f641ef0ffed29569f","18eeaf4e01377a466daaccc6ba8ce6f5","d31e8c1a3f9844becc88973ecddac872","9ff7e107ed674a99182e71b796d889aa","29cb0e104e5fce198008f3e789631772","98c1697e1928b0d4ea4ae3837ea09d48"],"Slider");