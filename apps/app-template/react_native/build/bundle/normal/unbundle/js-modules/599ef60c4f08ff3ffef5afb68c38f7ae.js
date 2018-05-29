__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/Picker/Picker.js";

  var ColorPropType = _require(_dependencyMap[0], 'ColorPropType');

  var PickerIOS = _require(_dependencyMap[1], 'PickerIOS');

  var PickerAndroid = _require(_dependencyMap[2], 'PickerAndroid');

  var Platform = _require(_dependencyMap[3], 'Platform');

  var React = _require(_dependencyMap[4], 'React');

  var PropTypes = _require(_dependencyMap[5], 'prop-types');

  var StyleSheetPropType = _require(_dependencyMap[6], 'StyleSheetPropType');

  var TextStylePropTypes = _require(_dependencyMap[7], 'TextStylePropTypes');

  var UnimplementedView = _require(_dependencyMap[8], 'UnimplementedView');

  var ViewPropTypes = _require(_dependencyMap[9], 'ViewPropTypes');

  var ViewStylePropTypes = _require(_dependencyMap[10], 'ViewStylePropTypes');

  var itemStylePropType = StyleSheetPropType(TextStylePropTypes);
  var pickerStyleType = StyleSheetPropType(babelHelpers.extends({}, ViewStylePropTypes, {
    color: ColorPropType
  }));
  var MODE_DIALOG = 'dialog';
  var MODE_DROPDOWN = 'dropdown';

  var PickerItem = function (_React$Component) {
    babelHelpers.inherits(PickerItem, _React$Component);

    function PickerItem() {
      babelHelpers.classCallCheck(this, PickerItem);
      return babelHelpers.possibleConstructorReturn(this, (PickerItem.__proto__ || Object.getPrototypeOf(PickerItem)).apply(this, arguments));
    }

    babelHelpers.createClass(PickerItem, [{
      key: "render",
      value: function render() {
        throw null;
      }
    }]);
    return PickerItem;
  }(React.Component);

  PickerItem.propTypes = {
    label: PropTypes.string.isRequired,
    value: PropTypes.any,
    color: ColorPropType,
    testID: PropTypes.string
  };

  var Picker = function (_React$Component2) {
    babelHelpers.inherits(Picker, _React$Component2);

    function Picker() {
      babelHelpers.classCallCheck(this, Picker);
      return babelHelpers.possibleConstructorReturn(this, (Picker.__proto__ || Object.getPrototypeOf(Picker)).apply(this, arguments));
    }

    babelHelpers.createClass(Picker, [{
      key: "render",
      value: function render() {
        if (Platform.OS === 'ios') {
          return React.createElement(
            PickerIOS,
            babelHelpers.extends({}, this.props, {
              __source: {
                fileName: _jsxFileName,
                lineNumber: 155
              }
            }),
            this.props.children
          );
        } else if (Platform.OS === 'android') {
          return React.createElement(
            PickerAndroid,
            babelHelpers.extends({}, this.props, {
              __source: {
                fileName: _jsxFileName,
                lineNumber: 158
              }
            }),
            this.props.children
          );
        } else {
          return React.createElement(UnimplementedView, {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 160
            }
          });
        }
      }
    }]);
    return Picker;
  }(React.Component);

  Picker.MODE_DIALOG = MODE_DIALOG;
  Picker.MODE_DROPDOWN = MODE_DROPDOWN;
  Picker.Item = PickerItem;
  Picker.defaultProps = {
    mode: MODE_DIALOG
  };
  Picker.propTypes = babelHelpers.extends({}, ViewPropTypes, {
    style: pickerStyleType,
    selectedValue: PropTypes.any,
    onValueChange: PropTypes.func,
    enabled: PropTypes.bool,
    mode: PropTypes.oneOf(['dialog', 'dropdown']),
    itemStyle: itemStylePropType,
    prompt: PropTypes.string,
    testID: PropTypes.string
  });
  module.exports = Picker;
},"599ef60c4f08ff3ffef5afb68c38f7ae",["63c61c7eda525c10d0670d2ef8475012","44043422e24050ffa2c9428763aa54dd","f62affb2eab7b3b63d7fb70c61e91808","9493a89f5d95c3a8a47c65cfed9b5542","e6db4f0efed6b72f641ef0ffed29569f","18eeaf4e01377a466daaccc6ba8ce6f5","60dc775dcc40daa6b8d0b23f322ce91f","45e1de962d0658d8cb6946a3c4531cbf","2048fde248982a55be3eceb4c402bf60","9ff7e107ed674a99182e71b796d889aa","007a69250301e98c7330c9706693fadc"],"Picker");