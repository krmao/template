__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/TextInput/TextInput.js";

  var ColorPropType = _require2(_dependencyMap[0], 'ColorPropType');

  var DocumentSelectionState = _require2(_dependencyMap[1], 'DocumentSelectionState');

  var EventEmitter = _require2(_dependencyMap[2], 'EventEmitter');

  var NativeMethodsMixin = _require2(_dependencyMap[3], 'NativeMethodsMixin');

  var Platform = _require2(_dependencyMap[4], 'Platform');

  var React = _require2(_dependencyMap[5], 'React');

  var createReactClass = _require2(_dependencyMap[6], 'create-react-class');

  var PropTypes = _require2(_dependencyMap[7], 'prop-types');

  var ReactNative = _require2(_dependencyMap[8], 'ReactNative');

  var StyleSheet = _require2(_dependencyMap[9], 'StyleSheet');

  var Text = _require2(_dependencyMap[10], 'Text');

  var TextInputState = _require2(_dependencyMap[11], 'TextInputState');

  var TimerMixin = _require2(_dependencyMap[12], 'react-timer-mixin');

  var TouchableWithoutFeedback = _require2(_dependencyMap[13], 'TouchableWithoutFeedback');

  var UIManager = _require2(_dependencyMap[14], 'UIManager');

  var ViewPropTypes = _require2(_dependencyMap[15], 'ViewPropTypes');

  var _require = _require2(_dependencyMap[16], 'ViewContext'),
      ViewContextTypes = _require.ViewContextTypes;

  var emptyFunction = _require2(_dependencyMap[17], 'fbjs/lib/emptyFunction');

  var invariant = _require2(_dependencyMap[18], 'fbjs/lib/invariant');

  var requireNativeComponent = _require2(_dependencyMap[19], 'requireNativeComponent');

  var warning = _require2(_dependencyMap[20], 'fbjs/lib/warning');

  var AndroidTextInput = void 0;
  var RCTMultilineTextInputView = void 0;
  var RCTSinglelineTextInputView = void 0;
  var onlyMultiline = {
    onTextInput: true,
    children: true
  };

  if (Platform.OS === 'android') {
    AndroidTextInput = requireNativeComponent('AndroidTextInput', null);
  } else if (Platform.OS === 'ios') {
    RCTMultilineTextInputView = requireNativeComponent('RCTMultilineTextInputView', null);
    RCTSinglelineTextInputView = requireNativeComponent('RCTSinglelineTextInputView', null);
  }

  var DataDetectorTypes = ['phoneNumber', 'link', 'address', 'calendarEvent', 'none', 'all'];
  var TextInput = createReactClass({
    displayName: 'TextInput',
    statics: {
      State: TextInputState
    },
    propTypes: babelHelpers.extends({}, ViewPropTypes, {
      autoCapitalize: PropTypes.oneOf(['none', 'sentences', 'words', 'characters']),
      autoCorrect: PropTypes.bool,
      spellCheck: PropTypes.bool,
      autoFocus: PropTypes.bool,
      allowFontScaling: PropTypes.bool,
      editable: PropTypes.bool,
      keyboardType: PropTypes.oneOf(['default', 'email-address', 'numeric', 'phone-pad', 'ascii-capable', 'numbers-and-punctuation', 'url', 'number-pad', 'name-phone-pad', 'decimal-pad', 'twitter', 'web-search', 'visible-password']),
      keyboardAppearance: PropTypes.oneOf(['default', 'light', 'dark']),
      returnKeyType: PropTypes.oneOf(['done', 'go', 'next', 'search', 'send', 'none', 'previous', 'default', 'emergency-call', 'google', 'join', 'route', 'yahoo']),
      returnKeyLabel: PropTypes.string,
      maxLength: PropTypes.number,
      numberOfLines: PropTypes.number,
      disableFullscreenUI: PropTypes.bool,
      enablesReturnKeyAutomatically: PropTypes.bool,
      multiline: PropTypes.bool,
      textBreakStrategy: PropTypes.oneOf(['simple', 'highQuality', 'balanced']),
      onBlur: PropTypes.func,
      onFocus: PropTypes.func,
      onChange: PropTypes.func,
      onChangeText: PropTypes.func,
      onContentSizeChange: PropTypes.func,
      onEndEditing: PropTypes.func,
      onSelectionChange: PropTypes.func,
      onSubmitEditing: PropTypes.func,
      onKeyPress: PropTypes.func,
      onLayout: PropTypes.func,
      onScroll: PropTypes.func,
      placeholder: PropTypes.string,
      placeholderTextColor: ColorPropType,
      secureTextEntry: PropTypes.bool,
      selectionColor: ColorPropType,
      selectionState: PropTypes.instanceOf(DocumentSelectionState),
      selection: PropTypes.shape({
        start: PropTypes.number.isRequired,
        end: PropTypes.number
      }),
      value: PropTypes.string,
      defaultValue: PropTypes.string,
      clearButtonMode: PropTypes.oneOf(['never', 'while-editing', 'unless-editing', 'always']),
      clearTextOnFocus: PropTypes.bool,
      selectTextOnFocus: PropTypes.bool,
      blurOnSubmit: PropTypes.bool,
      style: Text.propTypes.style,
      underlineColorAndroid: ColorPropType,
      inlineImageLeft: PropTypes.string,
      inlineImagePadding: PropTypes.number,
      dataDetectorTypes: PropTypes.oneOfType([PropTypes.oneOf(DataDetectorTypes), PropTypes.arrayOf(PropTypes.oneOf(DataDetectorTypes))]),
      caretHidden: PropTypes.bool,
      contextMenuHidden: PropTypes.bool,
      inputAccessoryViewID: PropTypes.string
    }),
    getDefaultProps: function getDefaultProps() {
      return {
        allowFontScaling: true
      };
    },
    mixins: [NativeMethodsMixin, TimerMixin],
    isFocused: function isFocused() {
      return TextInputState.currentlyFocusedField() === ReactNative.findNodeHandle(this._inputRef);
    },
    _inputRef: undefined,
    _focusSubscription: undefined,
    _lastNativeText: undefined,
    _lastNativeSelection: undefined,
    componentDidMount: function componentDidMount() {
      var _this = this;

      this._lastNativeText = this.props.value;

      if (!this.context.focusEmitter) {
        if (this.props.autoFocus) {
          this.requestAnimationFrame(this.focus);
        }

        return;
      }

      this._focusSubscription = this.context.focusEmitter.addListener('focus', function (el) {
        if (_this === el) {
          _this.requestAnimationFrame(_this.focus);
        } else if (_this.isFocused()) {
          _this.blur();
        }
      });

      if (this.props.autoFocus) {
        this.context.onFocusRequested(this);
      }
    },
    componentWillUnmount: function componentWillUnmount() {
      this._focusSubscription && this._focusSubscription.remove();

      if (this.isFocused()) {
        this.blur();
      }
    },
    getChildContext: function getChildContext() {
      return {
        isInAParentText: true
      };
    },
    childContextTypes: ViewContextTypes,
    contextTypes: babelHelpers.extends({}, ViewContextTypes, {
      onFocusRequested: PropTypes.func,
      focusEmitter: PropTypes.instanceOf(EventEmitter)
    }),
    clear: function clear() {
      this.setNativeProps({
        text: ''
      });
    },
    render: function render() {
      if (Platform.OS === 'ios') {
        return UIManager.RCTVirtualText ? this._renderIOS() : this._renderIOSLegacy();
      } else if (Platform.OS === 'android') {
        return this._renderAndroid();
      }
    },
    _getText: function _getText() {
      return typeof this.props.value === 'string' ? this.props.value : typeof this.props.defaultValue === 'string' ? this.props.defaultValue : '';
    },
    _setNativeRef: function _setNativeRef(ref) {
      this._inputRef = ref;
    },
    _renderIOSLegacy: function _renderIOSLegacy() {
      var textContainer = void 0;
      var props = babelHelpers.extends({}, this.props);
      props.style = [this.props.style];

      if (props.selection && props.selection.end == null) {
        props.selection = {
          start: props.selection.start,
          end: props.selection.start
        };
      }

      if (!props.multiline) {
        if (__DEV__) {
          for (var propKey in onlyMultiline) {
            if (props[propKey]) {
              var error = new Error('TextInput prop `' + propKey + '` is only supported with multiline.');
              warning(false, '%s', error.stack);
            }
          }
        }

        textContainer = React.createElement(RCTSinglelineTextInputView, babelHelpers.extends({
          ref: this._setNativeRef
        }, props, {
          onFocus: this._onFocus,
          onBlur: this._onBlur,
          onChange: this._onChange,
          onSelectionChange: this._onSelectionChange,
          onSelectionChangeShouldSetResponder: emptyFunction.thatReturnsTrue,
          text: this._getText(),
          __source: {
            fileName: _jsxFileName,
            lineNumber: 735
          }
        }));
      } else {
        var children = props.children;
        var childCount = 0;
        React.Children.forEach(children, function () {
          return ++childCount;
        });
        invariant(!(props.value && childCount), 'Cannot specify both value and children.');

        if (childCount >= 1) {
          children = React.createElement(
            Text,
            {
              style: props.style,
              allowFontScaling: props.allowFontScaling,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 756
              }
            },
            children
          );
        }

        if (props.inputView) {
          children = [children, props.inputView];
        }

        props.style.unshift(styles.multilineInput);
        textContainer = React.createElement(RCTMultilineTextInputView, babelHelpers.extends({
          ref: this._setNativeRef
        }, props, {
          children: children,
          onFocus: this._onFocus,
          onBlur: this._onBlur,
          onChange: this._onChange,
          onContentSizeChange: this.props.onContentSizeChange,
          onSelectionChange: this._onSelectionChange,
          onTextInput: this._onTextInput,
          onSelectionChangeShouldSetResponder: emptyFunction.thatReturnsTrue,
          text: this._getText(),
          dataDetectorTypes: this.props.dataDetectorTypes,
          onScroll: this._onScroll,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 766
          }
        }));
      }

      return React.createElement(
        TouchableWithoutFeedback,
        {
          onLayout: props.onLayout,
          onPress: this._onPress,
          rejectResponderTermination: true,
          accessible: props.accessible,
          accessibilityLabel: props.accessibilityLabel,
          accessibilityTraits: props.accessibilityTraits,
          nativeID: this.props.nativeID,
          testID: props.testID,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 785
          }
        },
        textContainer
      );
    },
    _renderIOS: function _renderIOS() {
      var props = babelHelpers.extends({}, this.props);
      props.style = [this.props.style];

      if (props.selection && props.selection.end == null) {
        props.selection = {
          start: props.selection.start,
          end: props.selection.start
        };
      }

      var RCTTextInputView = props.multiline ? RCTMultilineTextInputView : RCTSinglelineTextInputView;

      if (props.multiline) {
        props.style.unshift(styles.multilineInput);
      }

      var textContainer = React.createElement(RCTTextInputView, babelHelpers.extends({
        ref: this._setNativeRef
      }, props, {
        onFocus: this._onFocus,
        onBlur: this._onBlur,
        onChange: this._onChange,
        onContentSizeChange: this.props.onContentSizeChange,
        onSelectionChange: this._onSelectionChange,
        onTextInput: this._onTextInput,
        onSelectionChangeShouldSetResponder: emptyFunction.thatReturnsTrue,
        text: this._getText(),
        dataDetectorTypes: this.props.dataDetectorTypes,
        onScroll: this._onScroll,
        __source: {
          fileName: _jsxFileName,
          lineNumber: 819
        }
      }));
      return React.createElement(
        TouchableWithoutFeedback,
        {
          onLayout: props.onLayout,
          onPress: this._onPress,
          rejectResponderTermination: true,
          accessible: props.accessible,
          accessibilityLabel: props.accessibilityLabel,
          accessibilityTraits: props.accessibilityTraits,
          nativeID: this.props.nativeID,
          testID: props.testID,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 836
          }
        },
        textContainer
      );
    },
    _renderAndroid: function _renderAndroid() {
      var props = babelHelpers.extends({}, this.props);
      props.style = [this.props.style];
      props.autoCapitalize = UIManager.AndroidTextInput.Constants.AutoCapitalizationType[props.autoCapitalize || 'sentences'];
      var children = this.props.children;
      var childCount = 0;
      React.Children.forEach(children, function () {
        return ++childCount;
      });
      invariant(!(this.props.value && childCount), 'Cannot specify both value and children.');

      if (childCount > 1) {
        children = React.createElement(
          Text,
          {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 868
            }
          },
          children
        );
      }

      if (props.selection && props.selection.end == null) {
        props.selection = {
          start: props.selection.start,
          end: props.selection.start
        };
      }

      var textContainer = React.createElement(AndroidTextInput, babelHelpers.extends({
        ref: this._setNativeRef
      }, props, {
        mostRecentEventCount: 0,
        onFocus: this._onFocus,
        onBlur: this._onBlur,
        onChange: this._onChange,
        onSelectionChange: this._onSelectionChange,
        onTextInput: this._onTextInput,
        text: this._getText(),
        children: children,
        disableFullscreenUI: this.props.disableFullscreenUI,
        textBreakStrategy: this.props.textBreakStrategy,
        onScroll: this._onScroll,
        __source: {
          fileName: _jsxFileName,
          lineNumber: 879
        }
      }));
      return React.createElement(
        TouchableWithoutFeedback,
        {
          onLayout: props.onLayout,
          onPress: this._onPress,
          accessible: this.props.accessible,
          accessibilityLabel: this.props.accessibilityLabel,
          accessibilityComponentType: this.props.accessibilityComponentType,
          nativeID: this.props.nativeID,
          testID: this.props.testID,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 897
          }
        },
        textContainer
      );
    },
    _onFocus: function _onFocus(event) {
      if (this.props.onFocus) {
        this.props.onFocus(event);
      }

      if (this.props.selectionState) {
        this.props.selectionState.focus();
      }
    },
    _onPress: function _onPress(event) {
      if (this.props.editable || this.props.editable === undefined) {
        this.focus();
      }
    },
    _onChange: function _onChange(event) {
      if (this._inputRef) {
        this._inputRef.setNativeProps({
          mostRecentEventCount: event.nativeEvent.eventCount
        });
      }

      var text = event.nativeEvent.text;
      this.props.onChange && this.props.onChange(event);
      this.props.onChangeText && this.props.onChangeText(text);

      if (!this._inputRef) {
        return;
      }

      this._lastNativeText = text;
      this.forceUpdate();
    },
    _onSelectionChange: function _onSelectionChange(event) {
      this.props.onSelectionChange && this.props.onSelectionChange(event);

      if (!this._inputRef) {
        return;
      }

      this._lastNativeSelection = event.nativeEvent.selection;

      if (this.props.selection || this.props.selectionState) {
        this.forceUpdate();
      }
    },
    componentDidUpdate: function componentDidUpdate() {
      var nativeProps = {};

      if (this._lastNativeText !== this.props.value && typeof this.props.value === 'string') {
        nativeProps.text = this.props.value;
      }

      var selection = this.props.selection;

      if (this._lastNativeSelection && selection && (this._lastNativeSelection.start !== selection.start || this._lastNativeSelection.end !== selection.end)) {
        nativeProps.selection = this.props.selection;
      }

      if (Object.keys(nativeProps).length > 0 && this._inputRef) {
        this._inputRef.setNativeProps(nativeProps);
      }

      if (this.props.selectionState && selection) {
        this.props.selectionState.update(selection.start, selection.end);
      }
    },
    _onBlur: function _onBlur(event) {
      this.blur();

      if (this.props.onBlur) {
        this.props.onBlur(event);
      }

      if (this.props.selectionState) {
        this.props.selectionState.blur();
      }
    },
    _onTextInput: function _onTextInput(event) {
      this.props.onTextInput && this.props.onTextInput(event);
    },
    _onScroll: function _onScroll(event) {
      this.props.onScroll && this.props.onScroll(event);
    }
  });
  var styles = StyleSheet.create({
    multilineInput: {
      paddingTop: 5
    }
  });
  module.exports = TextInput;
},"e80660ff6e98e7a3974b80923bcdfd85",["63c61c7eda525c10d0670d2ef8475012","cc8beba56f5bc4be4647cd4a6f1d8996","6ab4a8e3cdc2c08a793730abd92b6eff","e2817b4a53aaef19afef34f031e1b9c9","9493a89f5d95c3a8a47c65cfed9b5542","e6db4f0efed6b72f641ef0ffed29569f","29cb0e104e5fce198008f3e789631772","18eeaf4e01377a466daaccc6ba8ce6f5","1102b68d89d7a6aede9677567aa01362","d31e8c1a3f9844becc88973ecddac872","c03ca8878a60b3cdaf32e10931ff258d","a54fbb2d9dfc450c68d1d302d42f00e1","5c0ec7f0bbb23ef3f86807b9f50421dc","9e4c8667cb3e1e5fa7d33d9679f26159","467cd3365342d9aaa2e941fe7ace641c","9ff7e107ed674a99182e71b796d889aa","a0a67b647dff8a7e11698d04fd60772b","7be5aa3f60ced36f3bf5972d0a12f299","8940a4ad43b101ffc23e725363c70f8d","98c1697e1928b0d4ea4ae3837ea09d48","09babf511a081d9520406a63f452d2ef"],"TextInput");