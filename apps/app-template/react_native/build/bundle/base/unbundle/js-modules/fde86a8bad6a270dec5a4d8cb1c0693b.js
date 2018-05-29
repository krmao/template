__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-tab-view/src/TabViewPagerScroll.js";

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _TabViewPropTypes = _require(_dependencyMap[2], "./TabViewPropTypes");

  var TabViewPagerScroll = function (_React$Component) {
    babelHelpers.inherits(TabViewPagerScroll, _React$Component);

    function TabViewPagerScroll(props) {
      babelHelpers.classCallCheck(this, TabViewPagerScroll);

      var _this = babelHelpers.possibleConstructorReturn(this, (TabViewPagerScroll.__proto__ || Object.getPrototypeOf(TabViewPagerScroll)).call(this, props));

      _initialiseProps.call(_this);

      var _this$props = _this.props,
          navigationState = _this$props.navigationState,
          layout = _this$props.layout;
      _this.state = {
        initialOffset: {
          x: navigationState.index * layout.width,
          y: 0
        }
      };
      return _this;
    }

    babelHelpers.createClass(TabViewPagerScroll, [{
      key: "componentDidMount",
      value: function componentDidMount() {
        this._setInitialPage();
      }
    }, {
      key: "componentDidUpdate",
      value: function componentDidUpdate(prevProps) {
        var amount = this.props.navigationState.index * this.props.layout.width;

        if (prevProps.navigationState.routes !== this.props.navigationState.routes || prevProps.layout.width !== this.props.layout.width) {
          this._scrollTo(amount, false);
        } else if (prevProps.navigationState.index !== this.props.navigationState.index) {
          this._scrollTo(amount);
        }
      }
    }, {
      key: "render",
      value: function render() {
        var _this2 = this;

        var _props = this.props,
            children = _props.children,
            layout = _props.layout,
            navigationState = _props.navigationState,
            onSwipeStart = _props.onSwipeStart,
            onSwipeEnd = _props.onSwipeEnd;
        return React.createElement(
          _reactNative.ScrollView,
          {
            horizontal: true,
            pagingEnabled: true,
            directionalLockEnabled: true,
            keyboardDismissMode: "on-drag",
            keyboardShouldPersistTaps: "always",
            overScrollMode: "never",
            scrollEnabled: this.props.swipeEnabled,
            automaticallyAdjustContentInsets: false,
            bounces: false,
            alwaysBounceHorizontal: false,
            scrollsToTop: false,
            showsHorizontalScrollIndicator: false,
            scrollEventThrottle: 1,
            onScroll: this._handleScroll,
            onScrollBeginDrag: onSwipeStart,
            onScrollEndDrag: onSwipeEnd,
            onMomentumScrollEnd: this._handleMomentumScrollEnd,
            contentOffset: this.state.initialOffset,
            style: styles.container,
            contentContainerStyle: layout.width ? null : styles.container,
            ref: function ref(el) {
              return _this2._scrollView = el;
            },
            __source: {
              fileName: _jsxFileName,
              lineNumber: 145
            }
          },
          React.Children.map(children, function (child, i) {
            return React.createElement(
              _reactNative.View,
              {
                key: navigationState.routes[i].key,
                testID: navigationState.routes[i].testID,
                style: layout.width ? {
                  width: layout.width,
                  overflow: 'hidden'
                } : i === navigationState.index ? styles.page : null,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 169
                }
              },
              i === navigationState.index || layout.width ? child : null
            );
          })
        );
      }
    }]);
    return TabViewPagerScroll;
  }(React.Component);

  TabViewPagerScroll.propTypes = _TabViewPropTypes.PagerRendererPropType;
  TabViewPagerScroll.defaultProps = {
    canJumpToTab: function canJumpToTab() {
      return true;
    }
  };

  var _initialiseProps = function _initialiseProps() {
    var _this3 = this;

    this._isIdle = true;
    this._isInitial = true;

    this._setInitialPage = function () {
      if (_this3.props.layout.width) {
        _this3._isInitial = true;

        _this3._scrollTo(_this3.props.navigationState.index * _this3.props.layout.width, false);
      }

      setTimeout(function () {
        _this3._isInitial = false;
      }, 50);
    };

    this._scrollTo = function (x) {
      var animated = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : true;

      if (_this3._isIdle && _this3._scrollView) {
        _this3._scrollView.scrollTo({
          x: x,
          animated: animated && _this3.props.animationEnabled !== false
        });
      }
    };

    this._handleMomentumScrollEnd = function (e) {
      var nextIndex = Math.round(e.nativeEvent.contentOffset.x / _this3.props.layout.width);
      var nextRoute = _this3.props.navigationState.routes[nextIndex];

      if (_this3.props.canJumpToTab(nextRoute)) {
        _this3.props.jumpTo(nextRoute.key);

        _this3.props.onAnimationEnd && _this3.props.onAnimationEnd();
      } else {
        global.requestAnimationFrame(function () {
          _this3._scrollTo(_this3.props.navigationState.index * _this3.props.layout.width);
        });
      }
    };

    this._handleScroll = function (e) {
      if (_this3._isInitial || e.nativeEvent.contentSize.width === 0) {
        return;
      }

      var _props2 = _this3.props,
          navigationState = _props2.navigationState,
          layout = _props2.layout;
      var offset = navigationState.index * layout.width;

      _this3.props.offsetX.setValue(-offset);

      _this3.props.panX.setValue(offset - e.nativeEvent.contentOffset.x);

      global.cancelAnimationFrame(_this3._idleCallback);
      _this3._isIdle = false;
      _this3._idleCallback = global.requestAnimationFrame(function () {
        _this3._isIdle = true;
      });
    };
  };

  exports.default = TabViewPagerScroll;

  var styles = _reactNative.StyleSheet.create({
    container: {
      flex: 1
    },
    page: {
      flex: 1,
      overflow: 'hidden'
    }
  });
},"fde86a8bad6a270dec5a4d8cb1c0693b",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04","b1e48d98aae16da0d57958d903d17014"],"node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-tab-view/src/TabViewPagerScroll.js");