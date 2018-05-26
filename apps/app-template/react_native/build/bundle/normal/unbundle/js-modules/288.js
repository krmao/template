__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/Picker/PickerAndroid.android.js";

  var ColorPropType = _require(_dependencyMap[0], 'ColorPropType');

  var React = _require(_dependencyMap[1], 'React');

  var ReactPropTypes = _require(_dependencyMap[2], 'prop-types');

  var StyleSheet = _require(_dependencyMap[3], 'StyleSheet');

  var StyleSheetPropType = _require(_dependencyMap[4], 'StyleSheetPropType');

  var ViewPropTypes = _require(_dependencyMap[5], 'ViewPropTypes');

  var ViewStylePropTypes = _require(_dependencyMap[6], 'ViewStylePropTypes');

  var processColor = _require(_dependencyMap[7], 'processColor');

  var requireNativeComponent = _require(_dependencyMap[8], 'requireNativeComponent');

  var REF_PICKER = 'picker';
  var MODE_DROPDOWN = 'dropdown';
  var pickerStyleType = StyleSheetPropType(babelHelpers.extends({}, ViewStylePropTypes, {
    color: ColorPropType
  }));

  var PickerAndroid = function (_React$Component) {
    babelHelpers.inherits(PickerAndroid, _React$Component);

    function PickerAndroid(props, context) {
      babelHelpers.classCallCheck(this, PickerAndroid);

      var _this = babelHelpers.possibleConstructorReturn(this, (PickerAndroid.__proto__ || Object.getPrototypeOf(PickerAndroid)).call(this, props, context));

      _initialiseProps.call(_this);

      var state = _this._stateFromProps(props);

      _this.state = babelHelpers.extends({}, state, {
        initialSelectedIndex: state.selectedIndex
      });
      return _this;
    }

    babelHelpers.createClass(PickerAndroid, [{
      key: "UNSAFE_componentWillReceiveProps",
      value: function UNSAFE_componentWillReceiveProps(nextProps) {
        this.setState(this._stateFromProps(nextProps));
      }
    }, {
      key: "render",
      value: function render() {
        var Picker = this.props.mode === MODE_DROPDOWN ? DropdownPicker : DialogPicker;
        var nativeProps = {
          enabled: this.props.enabled,
          items: this.state.items,
          mode: this.props.mode,
          onSelect: this._onChange,
          prompt: this.props.prompt,
          selected: this.state.initialSelectedIndex,
          testID: this.props.testID,
          style: [styles.pickerAndroid, this.props.style],
          accessibilityLabel: this.props.accessibilityLabel
        };
        return React.createElement(Picker, babelHelpers.extends({
          ref: REF_PICKER
        }, nativeProps, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 105
          }
        }));
      }
    }, {
      key: "componentDidMount",
      value: function componentDidMount() {
        this._lastNativePosition = this.state.initialSelectedIndex;
      }
    }, {
      key: "componentDidUpdate",
      value: function componentDidUpdate() {
        if (this.refs[REF_PICKER] && this.state.selectedIndex !== this._lastNativePosition) {
          this.refs[REF_PICKER].setNativeProps({
            selected: this.state.selectedIndex
          });
          this._lastNativePosition = this.state.selectedIndex;
        }
      }
    }]);
    return PickerAndroid;
  }(React.Component);

  PickerAndroid.propTypes = babelHelpers.extends({}, ViewPropTypes, {
    style: pickerStyleType,
    selectedValue: ReactPropTypes.any,
    enabled: ReactPropTypes.bool,
    mode: ReactPropTypes.oneOf(['dialog', 'dropdown']),
    onValueChange: ReactPropTypes.func,
    prompt: ReactPropTypes.string,
    testID: ReactPropTypes.string
  });

  var _initialiseProps = function _initialiseProps() {
    var _this2 = this;

    this._stateFromProps = function (props) {
      var selectedIndex = 0;
      var items = React.Children.map(props.children, function (child, index) {
        if (child.props.value === props.selectedValue) {
          selectedIndex = index;
        }

        var childProps = {
          value: child.props.value,
          label: child.props.label
        };

        if (child.props.color) {
          childProps.color = processColor(child.props.color);
        }

        return childProps;
      });
      return {
        selectedIndex: selectedIndex,
        items: items
      };
    };

    this._onChange = function (event) {
      if (_this2.props.onValueChange) {
        var position = event.nativeEvent.position;

        if (position >= 0) {
          var children = React.Children.toArray(_this2.props.children);
          var value = children[position].props.value;

          _this2.props.onValueChange(value, position);
        } else {
          _this2.props.onValueChange(null, position);
        }
      }

      _this2._lastNativePosition = event.nativeEvent.position;

      _this2.forceUpdate();
    };
  };

  var styles = StyleSheet.create({
    pickerAndroid: {
      height: 50
    }
  });
  var cfg = {
    nativeOnly: {
      items: true,
      selected: true
    }
  };
  var DropdownPicker = requireNativeComponent('AndroidDropdownPicker', PickerAndroid, cfg);
  var DialogPicker = requireNativeComponent('AndroidDialogPicker', PickerAndroid, cfg);
  module.exports = PickerAndroid;
},288,[46,132,129,171,141,133,142,155,148],"PickerAndroid");