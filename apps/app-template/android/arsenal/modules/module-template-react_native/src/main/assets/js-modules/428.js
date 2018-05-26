__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-tab-view/src/TabViewAnimated.js";

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _propTypes = _require(_dependencyMap[1], "prop-types");

  var _propTypes2 = babelHelpers.interopRequireDefault(_propTypes);

  var _reactNative = _require(_dependencyMap[2], "react-native");

  var _TabViewPropTypes = _require(_dependencyMap[3], "./TabViewPropTypes");

  var TabViewPager = void 0;

  switch (_reactNative.Platform.OS) {
    case 'android':
      TabViewPager = _require(_dependencyMap[4], './TabViewPagerAndroid').default;
      break;

    case 'ios':
      TabViewPager = _require(_dependencyMap[5], './TabViewPagerScroll').default;
      break;

    default:
      TabViewPager = _require(_dependencyMap[6], './TabViewPagerPan').default;
      break;
  }

  var TabViewAnimated = function (_React$Component) {
    babelHelpers.inherits(TabViewAnimated, _React$Component);

    function TabViewAnimated(props) {
      babelHelpers.classCallCheck(this, TabViewAnimated);

      var _this = babelHelpers.possibleConstructorReturn(this, (TabViewAnimated.__proto__ || Object.getPrototypeOf(TabViewAnimated)).call(this, props));

      _initialiseProps.call(_this);

      var navigationState = _this.props.navigationState;
      var layout = babelHelpers.extends({}, _this.props.initialLayout, {
        measured: false
      });
      var panX = new _reactNative.Animated.Value(0);
      var offsetX = new _reactNative.Animated.Value(-navigationState.index * layout.width);
      var layoutXY = new _reactNative.Animated.ValueXY({
        x: layout.width || 0.001,
        y: layout.height || 0.001
      });

      var position = _reactNative.Animated.multiply(_reactNative.Animated.divide(_reactNative.Animated.add(panX, offsetX), layoutXY.x), -1);

      _this.state = {
        layout: layout,
        layoutXY: layoutXY,
        panX: panX,
        offsetX: offsetX,
        position: position
      };
      return _this;
    }

    babelHelpers.createClass(TabViewAnimated, [{
      key: "componentDidMount",
      value: function componentDidMount() {
        this._mounted = true;
      }
    }, {
      key: "componentWillUnmount",
      value: function componentWillUnmount() {
        this._mounted = false;
      }
    }, {
      key: "render",
      value: function render() {
        var _this2 = this;

        var _props = this.props,
            navigationState = _props.navigationState,
            onIndexChange = _props.onIndexChange,
            initialLayout = _props.initialLayout,
            renderScene = _props.renderScene,
            renderPager = _props.renderPager,
            renderHeader = _props.renderHeader,
            renderFooter = _props.renderFooter,
            rest = babelHelpers.objectWithoutProperties(_props, ["navigationState", "onIndexChange", "initialLayout", "renderScene", "renderPager", "renderHeader", "renderFooter"]);

        var props = this._buildSceneRendererProps();

        return React.createElement(
          _reactNative.View,
          {
            collapsable: false,
            style: [styles.container, this.props.style],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 209
            }
          },
          renderHeader && renderHeader(props),
          React.createElement(
            _reactNative.View,
            {
              onLayout: this._handleLayout,
              style: styles.pager,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 211
              }
            },
            renderPager(babelHelpers.extends({}, props, rest, {
              panX: this.state.panX,
              offsetX: this.state.offsetX,
              children: navigationState.routes.map(function (route, index) {
                var scene = _this2._renderScene(babelHelpers.extends({}, props, {
                  route: route,
                  index: index,
                  focused: index === navigationState.index
                }));

                if (scene) {
                  return React.cloneElement(scene, {
                    key: route.key
                  });
                }

                return scene;
              })
            }))
          ),
          renderFooter && renderFooter(props)
        );
      }
    }]);
    return TabViewAnimated;
  }(React.Component);

  TabViewAnimated.propTypes = {
    navigationState: _TabViewPropTypes.NavigationStatePropType.isRequired,
    onIndexChange: _propTypes2.default.func.isRequired,
    initialLayout: _propTypes2.default.shape({
      height: _propTypes2.default.number.isRequired,
      width: _propTypes2.default.number.isRequired
    }),
    canJumpToTab: _propTypes2.default.func.isRequired,
    renderPager: _propTypes2.default.func.isRequired,
    renderScene: _propTypes2.default.func.isRequired,
    renderHeader: _propTypes2.default.func,
    renderFooter: _propTypes2.default.func
  };
  TabViewAnimated.defaultProps = {
    canJumpToTab: function canJumpToTab() {
      return true;
    },
    renderPager: function renderPager(props) {
      return React.createElement(TabViewPager, babelHelpers.extends({}, props, {
        __source: {
          fileName: _jsxFileName,
          lineNumber: 74
        }
      }));
    },
    initialLayout: {
      height: 0,
      width: 0
    },
    useNativeDriver: false
  };

  var _initialiseProps = function _initialiseProps() {
    var _this3 = this;

    this._mounted = false;

    this._renderScene = function (props) {
      return _this3.props.renderScene(props);
    };

    this._handleLayout = function (e) {
      var _e$nativeEvent$layout = e.nativeEvent.layout,
          height = _e$nativeEvent$layout.height,
          width = _e$nativeEvent$layout.width;

      if (_this3.state.layout.width === width && _this3.state.layout.height === height) {
        return;
      }

      _this3.state.offsetX.setValue(-_this3.props.navigationState.index * width);

      _this3.state.layoutXY.setValue({
        x: width || 0.001,
        y: height || 0.001
      });

      _this3.setState({
        layout: {
          measured: true,
          height: height,
          width: width
        }
      });
    };

    this._buildSceneRendererProps = function () {
      return {
        panX: _this3.state.panX,
        offsetX: _this3.state.offsetX,
        position: _this3.state.position,
        layout: _this3.state.layout,
        navigationState: _this3.props.navigationState,
        jumpTo: _this3._jumpTo,
        jumpToIndex: _this3._jumpToIndex,
        useNativeDriver: _this3.props.useNativeDriver === true
      };
    };

    this._jumpToIndex = function (index) {
      var key = _this3.props.navigationState.routes[index].key;
      console.warn('Method `jumpToIndex` is deprecated. Please upgrade your code to use `jumpTo` instead.', "Change your code from `jumpToIndex(" + index + ")` to `jumpTo('" + key + "').`");

      _this3._jumpTo(key);
    };

    this._jumpTo = function (key) {
      if (!_this3._mounted) {
        return;
      }

      var _props2 = _this3.props,
          canJumpToTab = _props2.canJumpToTab,
          navigationState = _props2.navigationState;
      var index = navigationState.routes.findIndex(function (route) {
        return route.key === key;
      });

      if (!canJumpToTab(navigationState.routes[index])) {
        return;
      }

      if (index !== navigationState.index) {
        _this3.props.onIndexChange(index);
      }
    };
  };

  exports.default = TabViewAnimated;

  var styles = _reactNative.StyleSheet.create({
    container: {
      flex: 1,
      overflow: 'hidden'
    },
    pager: {
      flex: 1
    }
  });
},428,[12,129,22,429,430,431,432],"node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-tab-view/src/TabViewAnimated.js");