__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var React = _require(_dependencyMap[0], 'React');

  var PropTypes = _require(_dependencyMap[1], 'prop-types');

  var ColorPropType = _require(_dependencyMap[2], 'ColorPropType');

  var Platform = _require(_dependencyMap[3], 'Platform');

  var processColor = _require(_dependencyMap[4], 'processColor');

  var StatusBarManager = _require(_dependencyMap[5], 'NativeModules').StatusBarManager;

  function mergePropsStack(propsStack, defaultValues) {
    return propsStack.reduce(function (prev, cur) {
      for (var prop in cur) {
        if (cur[prop] != null) {
          prev[prop] = cur[prop];
        }
      }

      return prev;
    }, babelHelpers.extends({}, defaultValues));
  }

  function createStackEntry(props) {
    return {
      backgroundColor: props.backgroundColor != null ? {
        value: props.backgroundColor,
        animated: props.animated
      } : null,
      barStyle: props.barStyle != null ? {
        value: props.barStyle,
        animated: props.animated
      } : null,
      translucent: props.translucent,
      hidden: props.hidden != null ? {
        value: props.hidden,
        animated: props.animated,
        transition: props.showHideTransition
      } : null,
      networkActivityIndicatorVisible: props.networkActivityIndicatorVisible
    };
  }

  var StatusBar = function (_React$Component) {
    babelHelpers.inherits(StatusBar, _React$Component);

    function StatusBar() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, StatusBar);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = StatusBar.__proto__ || Object.getPrototypeOf(StatusBar)).call.apply(_ref, [this].concat(args))), _this), _this._stackEntry = null, _this._updatePropsStack = function () {
        clearImmediate(StatusBar._updateImmediate);
        StatusBar._updateImmediate = setImmediate(function () {
          var oldProps = StatusBar._currentValues;
          var mergedProps = mergePropsStack(StatusBar._propsStack, StatusBar._defaultProps);

          if (Platform.OS === 'ios') {
            if (!oldProps || oldProps.barStyle.value !== mergedProps.barStyle.value) {
              StatusBarManager.setStyle(mergedProps.barStyle.value, mergedProps.barStyle.animated);
            }

            if (!oldProps || oldProps.hidden.value !== mergedProps.hidden.value) {
              StatusBarManager.setHidden(mergedProps.hidden.value, mergedProps.hidden.animated ? mergedProps.hidden.transition : 'none');
            }

            if (!oldProps || oldProps.networkActivityIndicatorVisible !== mergedProps.networkActivityIndicatorVisible) {
              StatusBarManager.setNetworkActivityIndicatorVisible(mergedProps.networkActivityIndicatorVisible);
            }
          } else if (Platform.OS === 'android') {
            if (!oldProps || oldProps.barStyle.value !== mergedProps.barStyle.value) {
              StatusBarManager.setStyle(mergedProps.barStyle.value);
            }

            if (!oldProps || oldProps.backgroundColor.value !== mergedProps.backgroundColor.value) {
              StatusBarManager.setColor(processColor(mergedProps.backgroundColor.value), mergedProps.backgroundColor.animated);
            }

            if (!oldProps || oldProps.hidden.value !== mergedProps.hidden.value) {
              StatusBarManager.setHidden(mergedProps.hidden.value);
            }

            if (!oldProps || oldProps.translucent !== mergedProps.translucent) {
              StatusBarManager.setTranslucent(mergedProps.translucent);
            }
          }

          StatusBar._currentValues = mergedProps;
        });
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(StatusBar, [{
      key: "componentDidMount",
      value: function componentDidMount() {
        this._stackEntry = createStackEntry(this.props);

        StatusBar._propsStack.push(this._stackEntry);

        this._updatePropsStack();
      }
    }, {
      key: "componentWillUnmount",
      value: function componentWillUnmount() {
        var index = StatusBar._propsStack.indexOf(this._stackEntry);

        StatusBar._propsStack.splice(index, 1);

        this._updatePropsStack();
      }
    }, {
      key: "componentDidUpdate",
      value: function componentDidUpdate() {
        var index = StatusBar._propsStack.indexOf(this._stackEntry);

        this._stackEntry = createStackEntry(this.props);
        StatusBar._propsStack[index] = this._stackEntry;

        this._updatePropsStack();
      }
    }, {
      key: "render",
      value: function render() {
        return null;
      }
    }], [{
      key: "setHidden",
      value: function setHidden(hidden, animation) {
        animation = animation || 'none';
        StatusBar._defaultProps.hidden.value = hidden;

        if (Platform.OS === 'ios') {
          StatusBarManager.setHidden(hidden, animation);
        } else if (Platform.OS === 'android') {
          StatusBarManager.setHidden(hidden);
        }
      }
    }, {
      key: "setBarStyle",
      value: function setBarStyle(style, animated) {
        animated = animated || false;
        StatusBar._defaultProps.barStyle.value = style;

        if (Platform.OS === 'ios') {
          StatusBarManager.setStyle(style, animated);
        } else if (Platform.OS === 'android') {
          StatusBarManager.setStyle(style);
        }
      }
    }, {
      key: "setNetworkActivityIndicatorVisible",
      value: function setNetworkActivityIndicatorVisible(visible) {
        if (Platform.OS !== 'ios') {
          console.warn('`setNetworkActivityIndicatorVisible` is only available on iOS');
          return;
        }

        StatusBar._defaultProps.networkActivityIndicatorVisible = visible;
        StatusBarManager.setNetworkActivityIndicatorVisible(visible);
      }
    }, {
      key: "setBackgroundColor",
      value: function setBackgroundColor(color, animated) {
        if (Platform.OS !== 'android') {
          console.warn('`setBackgroundColor` is only available on Android');
          return;
        }

        animated = animated || false;
        StatusBar._defaultProps.backgroundColor.value = color;
        StatusBarManager.setColor(processColor(color), animated);
      }
    }, {
      key: "setTranslucent",
      value: function setTranslucent(translucent) {
        if (Platform.OS !== 'android') {
          console.warn('`setTranslucent` is only available on Android');
          return;
        }

        StatusBar._defaultProps.translucent = translucent;
        StatusBarManager.setTranslucent(translucent);
      }
    }]);
    return StatusBar;
  }(React.Component);

  StatusBar._propsStack = [];
  StatusBar._defaultProps = createStackEntry({
    animated: false,
    showHideTransition: 'fade',
    backgroundColor: 'black',
    barStyle: 'default',
    translucent: false,
    hidden: false,
    networkActivityIndicatorVisible: false
  });
  StatusBar._updateImmediate = null;
  StatusBar._currentValues = null;
  StatusBar.currentHeight = StatusBarManager.HEIGHT;
  StatusBar.propTypes = {
    hidden: PropTypes.bool,
    animated: PropTypes.bool,
    backgroundColor: ColorPropType,
    translucent: PropTypes.bool,
    barStyle: PropTypes.oneOf(['default', 'light-content', 'dark-content']),
    networkActivityIndicatorVisible: PropTypes.bool,
    showHideTransition: PropTypes.oneOf(['fade', 'slide'])
  };
  StatusBar.defaultProps = {
    animated: false,
    showHideTransition: 'fade'
  };
  module.exports = StatusBar;
},"3b9e8669111967f90ad28e9ee872e85e",["e6db4f0efed6b72f641ef0ffed29569f","18eeaf4e01377a466daaccc6ba8ce6f5","63c61c7eda525c10d0670d2ef8475012","9493a89f5d95c3a8a47c65cfed9b5542","1b69977972a3b6ad650756d07de7954c","ce21807d4d291be64fa852393519f6c8"],"StatusBar");