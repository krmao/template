__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/CheckBox/CheckBox.android.js";

  var NativeMethodsMixin = _require(_dependencyMap[0], 'NativeMethodsMixin');

  var PropTypes = _require(_dependencyMap[1], 'prop-types');

  var React = _require(_dependencyMap[2], 'React');

  var StyleSheet = _require(_dependencyMap[3], 'StyleSheet');

  var ViewPropTypes = _require(_dependencyMap[4], 'ViewPropTypes');

  var createReactClass = _require(_dependencyMap[5], 'create-react-class');

  var requireNativeComponent = _require(_dependencyMap[6], 'requireNativeComponent');

  var CheckBox = createReactClass({
    displayName: 'CheckBox',
    propTypes: babelHelpers.extends({}, ViewPropTypes, {
      value: PropTypes.bool,
      disabled: PropTypes.bool,
      onChange: PropTypes.func,
      onValueChange: PropTypes.func,
      testID: PropTypes.string
    }),
    getDefaultProps: function getDefaultProps() {
      return {
        value: false,
        disabled: false
      };
    },
    mixins: [NativeMethodsMixin],
    _rctCheckBox: {},
    _onChange: function _onChange(event) {
      this._rctCheckBox.setNativeProps({
        value: this.props.value
      });

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

      props.enabled = !this.props.disabled;
      props.on = this.props.value;
      props.style = [styles.rctCheckBox, this.props.style];
      return React.createElement(RCTCheckBox, babelHelpers.extends({}, props, {
        ref: function ref(_ref) {
          _this._rctCheckBox = _ref;
        },
        onChange: this._onChange,
        __source: {
          fileName: _jsxFileName,
          lineNumber: 138
        }
      }));
    }
  });
  var styles = StyleSheet.create({
    rctCheckBox: {
      height: 32,
      width: 32
    }
  });
  var RCTCheckBox = requireNativeComponent('AndroidCheckBox', CheckBox, {
    nativeOnly: {
      onChange: true,
      on: true,
      enabled: true
    }
  });
  module.exports = CheckBox;
},"dda771e22d321fcbbdee4c22e34bcbbb",["e2817b4a53aaef19afef34f031e1b9c9","18eeaf4e01377a466daaccc6ba8ce6f5","e6db4f0efed6b72f641ef0ffed29569f","d31e8c1a3f9844becc88973ecddac872","9ff7e107ed674a99182e71b796d889aa","29cb0e104e5fce198008f3e789631772","98c1697e1928b0d4ea4ae3837ea09d48"],"CheckBox");