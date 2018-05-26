__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/StackView/createPointerEventsContainer.js";
  exports.default = createPointerEventsContainer;

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _invariant = _require(_dependencyMap[1], "../../utils/invariant");

  var _invariant2 = babelHelpers.interopRequireDefault(_invariant);

  var _AnimatedValueSubscription = _require(_dependencyMap[2], "../AnimatedValueSubscription");

  var _AnimatedValueSubscription2 = babelHelpers.interopRequireDefault(_AnimatedValueSubscription);

  var MIN_POSITION_OFFSET = 0.01;

  function createPointerEventsContainer(Component) {
    var Container = function (_React$Component) {
      babelHelpers.inherits(Container, _React$Component);

      function Container(props, context) {
        babelHelpers.classCallCheck(this, Container);

        var _this = babelHelpers.possibleConstructorReturn(this, (Container.__proto__ || Object.getPrototypeOf(Container)).call(this, props, context));

        _this._onComponentRef = function (component) {
          _this._component = component;

          if (component) {
            (0, _invariant2.default)(typeof component.setNativeProps === 'function', 'component must implement method `setNativeProps`');
          }
        };

        _this._onPositionChange = function () {
          if (_this._component) {
            var pointerEvents = _this._computePointerEvents();

            if (_this._pointerEvents !== pointerEvents) {
              _this._pointerEvents = pointerEvents;

              _this._component.setNativeProps({
                pointerEvents: pointerEvents
              });
            }
          }
        };

        _this._pointerEvents = _this._computePointerEvents();
        return _this;
      }

      babelHelpers.createClass(Container, [{
        key: "componentWillUnmount",
        value: function componentWillUnmount() {
          this._positionListener && this._positionListener.remove();
        }
      }, {
        key: "render",
        value: function render() {
          this._bindPosition();

          this._pointerEvents = this._computePointerEvents();
          return _react2.default.createElement(Component, babelHelpers.extends({}, this.props, {
            pointerEvents: this._pointerEvents,
            onComponentRef: this._onComponentRef,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 28
            }
          }));
        }
      }, {
        key: "_bindPosition",
        value: function _bindPosition() {
          this._positionListener && this._positionListener.remove();
          this._positionListener = new _AnimatedValueSubscription2.default(this.props.position, this._onPositionChange);
        }
      }, {
        key: "_computePointerEvents",
        value: function _computePointerEvents() {
          var _props = this.props,
              navigation = _props.navigation,
              position = _props.position,
              scene = _props.scene;

          if (scene.isStale || navigation.state.index !== scene.index) {
            return scene.index > navigation.state.index ? 'box-only' : 'none';
          }

          var offset = position.__getAnimatedValue() - navigation.state.index;

          if (Math.abs(offset) > MIN_POSITION_OFFSET) {
            return 'box-only';
          }

          return 'auto';
        }
      }]);
      return Container;
    }(_react2.default.Component);

    return Container;
  }
},354,[12,342,355],"node_modules/react-navigation/src/views/StackView/createPointerEventsContainer.js");