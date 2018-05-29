__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/Switch/Switch.js";

  var ColorPropType = _require(_dependencyMap[0], 'ColorPropType');

  var NativeMethodsMixin = _require(_dependencyMap[1], 'NativeMethodsMixin');

  var Platform = _require(_dependencyMap[2], 'Platform');

  var React = _require(_dependencyMap[3], 'React');

  var PropTypes = _require(_dependencyMap[4], 'prop-types');

  var StyleSheet = _require(_dependencyMap[5], 'StyleSheet');

  var ViewPropTypes = _require(_dependencyMap[6], 'ViewPropTypes');

  var createReactClass = _require(_dependencyMap[7], 'create-react-class');

  var requireNativeComponent = _require(_dependencyMap[8], 'requireNativeComponent');

  var Switch = createReactClass({
    displayName: 'Switch',
    propTypes: babelHelpers.extends({}, ViewPropTypes, {
      value: PropTypes.bool,
      disabled: PropTypes.bool,
      onValueChange: PropTypes.func,
      testID: PropTypes.string,
      tintColor: ColorPropType,
      onTintColor: ColorPropType,
      thumbTintColor: ColorPropType
    }),
    getDefaultProps: function getDefaultProps() {
      return {
        value: false,
        disabled: false
      };
    },
    mixins: [NativeMethodsMixin],
    _rctSwitch: {},
    _onChange: function _onChange(event) {
      if (Platform.OS === 'android') {
        this._rctSwitch.setNativeProps({
          on: this.props.value
        });
      } else {
        this._rctSwitch.setNativeProps({
          value: this.props.value
        });
      }

      this.props.onChange && this.props.onChange(event);
      this.props.onValueChange && this.props.onValueChange(event.nativeEvent.value);
    },
    render: function render() {
      var _this = this;

      var props = babelHelpers.extends({}, this.props);

      props.onStartShouldSetResponder = function () {
        return true;
      };

      props.onResponderTerminationRequest = function () {
        return false;
      };

      if (Platform.OS === 'android') {
        props.enabled = !this.props.disabled;
        props.on = this.props.value;
        props.style = this.props.style;
        props.trackTintColor = this.props.value ? this.props.onTintColor : this.props.tintColor;
      } else if (Platform.OS === 'ios') {
        props.style = [styles.rctSwitchIOS, this.props.style];
      }

      return React.createElement(RCTSwitch, babelHelpers.extends({}, props, {
        ref: function ref(_ref) {
          _this._rctSwitch = _ref;
        },
        onChange: this._onChange,
        __source: {
          fileName: _jsxFileName,
          lineNumber: 113
        }
      }));
    }
  });
  var styles = StyleSheet.create({
    rctSwitchIOS: {
      height: 31,
      width: 51
    }
  });

  if (Platform.OS === 'android') {
    var RCTSwitch = requireNativeComponent('AndroidSwitch', Switch, {
      nativeOnly: {
        onChange: true,
        on: true,
        enabled: true,
        trackTintColor: true
      }
    });
  } else {
    var RCTSwitch = requireNativeComponent('RCTSwitch', Switch, {
      nativeOnly: {
        onChange: true
      }
    });
  }

  module.exports = Switch;
},"c2cbe6678768638ce187b2abe5fef874",["63c61c7eda525c10d0670d2ef8475012","e2817b4a53aaef19afef34f031e1b9c9","9493a89f5d95c3a8a47c65cfed9b5542","e6db4f0efed6b72f641ef0ffed29569f","18eeaf4e01377a466daaccc6ba8ce6f5","d31e8c1a3f9844becc88973ecddac872","9ff7e107ed674a99182e71b796d889aa","29cb0e104e5fce198008f3e789631772","98c1697e1928b0d4ea4ae3837ea09d48"],"Switch");