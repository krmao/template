__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/Button.js";

  var ColorPropType = _require(_dependencyMap[0], 'ColorPropType');

  var Platform = _require(_dependencyMap[1], 'Platform');

  var React = _require(_dependencyMap[2], 'React');

  var PropTypes = _require(_dependencyMap[3], 'prop-types');

  var StyleSheet = _require(_dependencyMap[4], 'StyleSheet');

  var Text = _require(_dependencyMap[5], 'Text');

  var TouchableNativeFeedback = _require(_dependencyMap[6], 'TouchableNativeFeedback');

  var TouchableOpacity = _require(_dependencyMap[7], 'TouchableOpacity');

  var View = _require(_dependencyMap[8], 'View');

  var invariant = _require(_dependencyMap[9], 'fbjs/lib/invariant');

  var Button = function (_React$Component) {
    babelHelpers.inherits(Button, _React$Component);

    function Button() {
      babelHelpers.classCallCheck(this, Button);
      return babelHelpers.possibleConstructorReturn(this, (Button.__proto__ || Object.getPrototypeOf(Button)).apply(this, arguments));
    }

    babelHelpers.createClass(Button, [{
      key: "render",
      value: function render() {
        var _props = this.props,
            accessibilityLabel = _props.accessibilityLabel,
            color = _props.color,
            onPress = _props.onPress,
            title = _props.title,
            hasTVPreferredFocus = _props.hasTVPreferredFocus,
            disabled = _props.disabled,
            testID = _props.testID;
        var buttonStyles = [styles.button];
        var textStyles = [styles.text];

        if (color) {
          if (Platform.OS === 'ios') {
            textStyles.push({
              color: color
            });
          } else {
            buttonStyles.push({
              backgroundColor: color
            });
          }
        }

        var accessibilityTraits = ['button'];

        if (disabled) {
          buttonStyles.push(styles.buttonDisabled);
          textStyles.push(styles.textDisabled);
          accessibilityTraits.push('disabled');
        }

        invariant(typeof title === 'string', 'The title prop of a Button must be a string');
        var formattedTitle = Platform.OS === 'android' ? title.toUpperCase() : title;
        var Touchable = Platform.OS === 'android' ? TouchableNativeFeedback : TouchableOpacity;
        return React.createElement(
          Touchable,
          {
            accessibilityComponentType: "button",
            accessibilityLabel: accessibilityLabel,
            accessibilityTraits: accessibilityTraits,
            hasTVPreferredFocus: hasTVPreferredFocus,
            testID: testID,
            disabled: disabled,
            onPress: onPress,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 125
            }
          },
          React.createElement(
            View,
            {
              style: buttonStyles,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 133
              }
            },
            React.createElement(
              Text,
              {
                style: textStyles,
                disabled: disabled,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 134
                }
              },
              formattedTitle
            )
          )
        );
      }
    }]);
    return Button;
  }(React.Component);

  Button.propTypes = {
    title: PropTypes.string.isRequired,
    accessibilityLabel: PropTypes.string,
    color: ColorPropType,
    disabled: PropTypes.bool,
    hasTVPreferredFocus: PropTypes.bool,
    onPress: PropTypes.func.isRequired,
    testID: PropTypes.string
  };
  var styles = StyleSheet.create({
    button: Platform.select({
      ios: {},
      android: {
        elevation: 4,
        backgroundColor: '#2196F3',
        borderRadius: 2
      }
    }),
    text: Platform.select({
      ios: {
        color: '#007AFF',
        textAlign: 'center',
        padding: 8,
        fontSize: 18
      },
      android: {
        color: 'white',
        textAlign: 'center',
        padding: 8,
        fontWeight: '500'
      }
    }),
    buttonDisabled: Platform.select({
      ios: {},
      android: {
        elevation: 0,
        backgroundColor: '#dfdfdf'
      }
    }),
    textDisabled: Platform.select({
      ios: {
        color: '#cdcdcd'
      },
      android: {
        color: '#a1a1a1'
      }
    })
  });
  module.exports = Button;
},"2bfe2f7207f7306e0bd391bde54155b1",["63c61c7eda525c10d0670d2ef8475012","9493a89f5d95c3a8a47c65cfed9b5542","e6db4f0efed6b72f641ef0ffed29569f","18eeaf4e01377a466daaccc6ba8ce6f5","d31e8c1a3f9844becc88973ecddac872","c03ca8878a60b3cdaf32e10931ff258d","c0d6127359adee60e42e0d2a170972b5","5df1ad9630614dcb5c10b152b20df075","30a3b04291b6e1f01b778ff31271ccc5","8940a4ad43b101ffc23e725363c70f8d"],"Button");