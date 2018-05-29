__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/Transitioner.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _invariant = _require(_dependencyMap[2], "../utils/invariant");

  var _invariant2 = babelHelpers.interopRequireDefault(_invariant);

  var _ScenesReducer = _require(_dependencyMap[3], "./ScenesReducer");

  var _ScenesReducer2 = babelHelpers.interopRequireDefault(_ScenesReducer);

  var DefaultTransitionSpec = {
    duration: 250,
    easing: _reactNative.Easing.inOut(_reactNative.Easing.ease),
    timing: _reactNative.Animated.timing
  };

  var Transitioner = function (_React$Component) {
    babelHelpers.inherits(Transitioner, _React$Component);

    function Transitioner(props, context) {
      babelHelpers.classCallCheck(this, Transitioner);

      var _this = babelHelpers.possibleConstructorReturn(this, (Transitioner.__proto__ || Object.getPrototypeOf(Transitioner)).call(this, props, context));

      _initialiseProps.call(_this);

      var layout = {
        height: new _reactNative.Animated.Value(0),
        initHeight: 0,
        initWidth: 0,
        isMeasured: false,
        width: new _reactNative.Animated.Value(0)
      };
      _this.state = {
        layout: layout,
        position: new _reactNative.Animated.Value(_this.props.navigation.state.index),
        progress: new _reactNative.Animated.Value(1),
        scenes: (0, _ScenesReducer2.default)([], _this.props.navigation.state, null, _this.props.descriptors)
      };
      _this._prevTransitionProps = null;
      _this._transitionProps = buildTransitionProps(props, _this.state);
      _this._isMounted = false;
      _this._isTransitionRunning = false;
      _this._queuedTransition = null;
      return _this;
    }

    babelHelpers.createClass(Transitioner, [{
      key: "componentDidMount",
      value: function componentDidMount() {
        this._isMounted = true;
      }
    }, {
      key: "componentWillUnmount",
      value: function componentWillUnmount() {
        this._isMounted = false;
      }
    }, {
      key: "componentWillReceiveProps",
      value: function componentWillReceiveProps(nextProps) {
        var nextScenes = (0, _ScenesReducer2.default)(this.state.scenes, nextProps.navigation.state, this.props.navigation.state, nextProps.descriptors);

        if (!nextProps.navigation.state.isTransitioning) {
          nextScenes = filterStale(nextScenes);
        }

        if (nextScenes === this.state.scenes) {
          return;
        }

        var indexHasChanged = nextProps.navigation.state.index !== this.props.navigation.state.index;

        if (this._isTransitionRunning) {
          this._queuedTransition = {
            nextProps: nextProps,
            nextScenes: nextScenes,
            indexHasChanged: indexHasChanged
          };
          return;
        }

        this._startTransition(nextProps, nextScenes, indexHasChanged);
      }
    }, {
      key: "_startTransition",
      value: function _startTransition(nextProps, nextScenes, indexHasChanged) {
        var _this2 = this;

        var nextState = babelHelpers.extends({}, this.state, {
          scenes: nextScenes
        });
        var position = nextState.position,
            progress = nextState.progress;
        progress.setValue(0);
        this._prevTransitionProps = this._transitionProps;
        this._transitionProps = buildTransitionProps(nextProps, nextState);
        var transitionUserSpec = nextProps.configureTransition ? nextProps.configureTransition(this._transitionProps, this._prevTransitionProps) : null;
        var transitionSpec = babelHelpers.extends({}, DefaultTransitionSpec, transitionUserSpec);
        var timing = transitionSpec.timing;
        delete transitionSpec.timing;
        var toValue = nextProps.navigation.state.index;
        var positionHasChanged = position.__getValue() !== toValue;
        var animations = indexHasChanged && positionHasChanged ? [timing(progress, babelHelpers.extends({}, transitionSpec, {
          toValue: 1
        })), timing(position, babelHelpers.extends({}, transitionSpec, {
          toValue: nextProps.navigation.state.index
        }))] : [];
        this._isTransitionRunning = true;
        this.setState(nextState, function _callee() {
          var result;
          return regeneratorRuntime.async(function _callee$(_context) {
            while (1) {
              switch (_context.prev = _context.next) {
                case 0:
                  if (!nextProps.onTransitionStart) {
                    _context.next = 5;
                    break;
                  }

                  result = nextProps.onTransitionStart(_this2._transitionProps, _this2._prevTransitionProps);

                  if (!(result instanceof Promise)) {
                    _context.next = 5;
                    break;
                  }

                  _context.next = 5;
                  return regeneratorRuntime.awrap(result);

                case 5:
                  _reactNative.Animated.parallel(animations).start(_this2._onTransitionEnd);

                case 6:
                case "end":
                  return _context.stop();
              }
            }
          }, null, _this2);
        });
      }
    }, {
      key: "render",
      value: function render() {
        return _react2.default.createElement(
          _reactNative.View,
          {
            onLayout: this._onLayout,
            style: [styles.main],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 146
            }
          },
          this.props.render(this._transitionProps, this._prevTransitionProps)
        );
      }
    }]);
    return Transitioner;
  }(_react2.default.Component);

  var _initialiseProps = function _initialiseProps() {
    var _this3 = this;

    this._onLayout = function (event) {
      var _event$nativeEvent$la = event.nativeEvent.layout,
          height = _event$nativeEvent$la.height,
          width = _event$nativeEvent$la.width;

      if (_this3.state.layout.initWidth === width && _this3.state.layout.initHeight === height) {
        return;
      }

      var layout = babelHelpers.extends({}, _this3.state.layout, {
        initHeight: height,
        initWidth: width,
        isMeasured: true
      });
      layout.height.setValue(height);
      layout.width.setValue(width);
      var nextState = babelHelpers.extends({}, _this3.state, {
        layout: layout
      });
      _this3._transitionProps = buildTransitionProps(_this3.props, nextState);

      _this3.setState(nextState);
    };

    this._onTransitionEnd = function () {
      if (!_this3._isMounted) {
        return;
      }

      var prevTransitionProps = _this3._prevTransitionProps;
      _this3._prevTransitionProps = null;
      var scenes = filterStale(_this3.state.scenes);
      var nextState = babelHelpers.extends({}, _this3.state, {
        scenes: scenes
      });
      _this3._transitionProps = buildTransitionProps(_this3.props, nextState);

      _this3.setState(nextState, function _callee2() {
        var result;
        return regeneratorRuntime.async(function _callee2$(_context2) {
          while (1) {
            switch (_context2.prev = _context2.next) {
              case 0:
                if (!_this3.props.onTransitionEnd) {
                  _context2.next = 5;
                  break;
                }

                result = _this3.props.onTransitionEnd(_this3._transitionProps, prevTransitionProps);

                if (!(result instanceof Promise)) {
                  _context2.next = 5;
                  break;
                }

                _context2.next = 5;
                return regeneratorRuntime.awrap(result);

              case 5:
                if (_this3._queuedTransition) {
                  _this3._startTransition(_this3._queuedTransition.nextProps, _this3._queuedTransition.nextScenes, _this3._queuedTransition.indexHasChanged);

                  _this3._queuedTransition = null;
                } else {
                  _this3._isTransitionRunning = false;
                }

              case 6:
              case "end":
                return _context2.stop();
            }
          }
        }, null, _this3);
      });
    };
  };

  function buildTransitionProps(props, state) {
    var navigation = props.navigation;
    var layout = state.layout,
        position = state.position,
        progress = state.progress,
        scenes = state.scenes;
    var scene = scenes.find(isSceneActive);
    (0, _invariant2.default)(scene, 'Could not find active scene');
    return {
      layout: layout,
      navigation: navigation,
      position: position,
      progress: progress,
      scenes: scenes,
      scene: scene,
      index: scene.index
    };
  }

  function isSceneNotStale(scene) {
    return !scene.isStale;
  }

  function filterStale(scenes) {
    var filtered = scenes.filter(isSceneNotStale);

    if (filtered.length === scenes.length) {
      return scenes;
    }

    return filtered;
  }

  function isSceneActive(scene) {
    return scene.isActive;
  }

  var styles = _reactNative.StyleSheet.create({
    main: {
      flex: 1
    }
  });

  exports.default = Transitioner;
},"07f0214b33d7d02821e9126871ddee24",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04","09df40ab147e7353903f31659d93ee58","f056d3e7dd42bd17dd233d2158d31ff3"],"node_modules/react-navigation/src/views/Transitioner.js");