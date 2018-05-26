__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-tab-view/src/TabViewPagerAndroid.js";

  var _react = _require(_dependencyMap[0], "react");

  var React = babelHelpers.interopRequireWildcard(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _TabViewPropTypes = _require(_dependencyMap[2], "./TabViewPropTypes");

  var TabViewPagerAndroid = function (_React$Component) {
    babelHelpers.inherits(TabViewPagerAndroid, _React$Component);

    function TabViewPagerAndroid(props) {
      babelHelpers.classCallCheck(this, TabViewPagerAndroid);

      var _this = babelHelpers.possibleConstructorReturn(this, (TabViewPagerAndroid.__proto__ || Object.getPrototypeOf(TabViewPagerAndroid)).call(this, props));

      _this._isIdle = true;
      _this._currentIndex = 0;

      _this._getPageIndex = function (index) {
        return _reactNative.I18nManager.isRTL ? _this.props.navigationState.routes.length - (index + 1) : index;
      };

      _this._setPage = function (index) {
        var animated = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : true;
        var pager = _this._viewPager;

        if (pager) {
          var page = _this._getPageIndex(index);

          if (_this.props.animationEnabled === false || animated === false) {
            pager.setPageWithoutAnimation(page);
          } else {
            pager.setPage(page);
          }
        }
      };

      _this._handlePageChange = function (index, animated) {
        if (_this._isIdle && _this._currentIndex !== index) {
          _this._setPage(index, animated);

          _this._currentIndex = index;
        }
      };

      _this._handlePageScroll = function (e) {
        _this.props.offsetX.setValue(e.nativeEvent.position * _this.props.layout.width * (_reactNative.I18nManager.isRTL ? 1 : -1));

        _this.props.panX.setValue(e.nativeEvent.offset * _this.props.layout.width * (_reactNative.I18nManager.isRTL ? 1 : -1));
      };

      _this._handlePageScrollStateChanged = function (e) {
        _this._isIdle = e === 'idle';
        var nextIndex = _this._currentIndex;
        var nextRoute = _this.props.navigationState.routes[nextIndex];

        if (_this.props.canJumpToTab(nextRoute)) {
          _this.props.jumpTo(nextRoute.key);
        } else {
          _this._setPage(_this.props.navigationState.index);

          _this._currentIndex = _this.props.navigationState.index;
        }

        switch (e) {
          case 'dragging':
            _this.props.onSwipeStart && _this.props.onSwipeStart();
            break;

          case 'settling':
            _this.props.onSwipeEnd && _this.props.onSwipeEnd();
            break;

          case 'idle':
            _this.props.onAnimationEnd && _this.props.onAnimationEnd();
            break;
        }
      };

      _this._handlePageSelected = function (e) {
        var index = _this._getPageIndex(e.nativeEvent.position);

        _this._currentIndex = index;
      };

      _this._setRef = function (el) {
        return _this._viewPager = el;
      };

      _this._currentIndex = _this.props.navigationState.index;
      return _this;
    }

    babelHelpers.createClass(TabViewPagerAndroid, [{
      key: "componentDidUpdate",
      value: function componentDidUpdate(prevProps) {
        if (prevProps.navigationState.routes !== this.props.navigationState.routes || prevProps.layout.width !== this.props.layout.width) {
          this._handlePageChange(this.props.navigationState.index, false);
        } else if (prevProps.navigationState.index !== this.props.navigationState.index) {
          this._handlePageChange(this.props.navigationState.index);
        }
      }
    }, {
      key: "render",
      value: function render() {
        var _props = this.props,
            children = _props.children,
            navigationState = _props.navigationState,
            swipeEnabled = _props.swipeEnabled,
            keyboardDismissMode = _props.keyboardDismissMode;
        var content = React.Children.map(children, function (child, i) {
          return React.createElement(
            _reactNative.View,
            {
              key: navigationState.routes[i].key,
              testID: navigationState.routes[i].testID,
              style: styles.page,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 135
              }
            },
            child
          );
        });

        if (_reactNative.I18nManager.isRTL) {
          content.reverse();
        }

        var initialPage = this._getPageIndex(navigationState.index);

        return React.createElement(
          _reactNative.ViewPagerAndroid,
          {
            key: navigationState.routes.length,
            keyboardDismissMode: keyboardDismissMode,
            initialPage: initialPage,
            scrollEnabled: swipeEnabled !== false,
            onPageScroll: this._handlePageScroll,
            onPageScrollStateChanged: this._handlePageScrollStateChanged,
            onPageSelected: this._handlePageSelected,
            style: styles.container,
            ref: this._setRef,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 151
            }
          },
          content
        );
      }
    }]);
    return TabViewPagerAndroid;
  }(React.Component);

  TabViewPagerAndroid.propTypes = _TabViewPropTypes.PagerRendererPropType;
  TabViewPagerAndroid.defaultProps = {
    canJumpToTab: function canJumpToTab() {
      return true;
    },
    keyboardDismissMode: 'on-drag'
  };
  exports.default = TabViewPagerAndroid;

  var styles = _reactNative.StyleSheet.create({
    container: {
      flexGrow: 1
    },
    page: {
      overflow: 'hidden'
    }
  });
},430,[12,22,429],"node_modules/react-navigation/node_modules/react-navigation-tabs/node_modules/react-native-tab-view/src/TabViewPagerAndroid.js");