__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/navigators/createKeyboardAwareNavigator.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  exports.default = function (Navigator) {
    var _class, _temp2;

    return _temp2 = _class = function (_React$Component) {
      babelHelpers.inherits(KeyboardAwareNavigator, _React$Component);

      function KeyboardAwareNavigator() {
        var _ref;

        var _temp, _this, _ret;

        babelHelpers.classCallCheck(this, KeyboardAwareNavigator);

        for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
          args[_key] = arguments[_key];
        }

        return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = KeyboardAwareNavigator.__proto__ || Object.getPrototypeOf(KeyboardAwareNavigator)).call.apply(_ref, [this].concat(args))), _this), _this._previouslyFocusedTextInput = null, _this._handleGestureBegin = function () {
          _this._previouslyFocusedTextInput = _reactNative.TextInput.State.currentlyFocusedField();

          if (_this._previouslyFocusedTextInput) {
            _reactNative.TextInput.State.blurTextInput(_this._previouslyFocusedTextInput);
          }

          _this.props.onGestureBegin && _this.props.onGestureBegin();
        }, _this._handleGestureCanceled = function () {
          if (_this._previouslyFocusedTextInput) {
            _reactNative.TextInput.State.focusTextInput(_this._previouslyFocusedTextInput);
          }

          _this.props.onGestureFinish && _this.props.onGestureFinish();
        }, _this._handleGestureFinish = function () {
          _this._previouslyFocusedTextInput = null;
          _this.props.onGestureCanceled && _this.props.onGestureCanceled();
        }, _this._handleTransitionStart = function (transitionProps, prevTransitionProps) {
          if (transitionProps.index !== prevTransitionProps.index) {
            var currentField = _reactNative.TextInput.State.currentlyFocusedField();

            if (currentField) {
              _reactNative.TextInput.State.blurTextInput(currentField);
            }
          }

          _this.props.onTransitionStart && _this.props.onTransitionStart(transitionProps, prevTransitionProps);
        }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
      }

      babelHelpers.createClass(KeyboardAwareNavigator, [{
        key: "render",
        value: function render() {
          return _react2.default.createElement(Navigator, babelHelpers.extends({}, this.props, {
            onGestureBegin: this._handleGestureBegin,
            onGestureCanceled: this._handleGestureCanceled,
            onGestureFinish: this._handleGestureFinish,
            onTransitionStart: this._handleTransitionStart,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 11
            }
          }));
        }
      }]);
      return KeyboardAwareNavigator;
    }(_react2.default.Component), _class.router = Navigator.router, _temp2;
  };
},349,[12,22],"node_modules/react-navigation/src/navigators/createKeyboardAwareNavigator.js");