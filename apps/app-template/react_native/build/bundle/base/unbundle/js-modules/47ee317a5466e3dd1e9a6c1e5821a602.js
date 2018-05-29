__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/ViewPager/ViewPagerAndroid.android.js";

  var React = _require(_dependencyMap[0], 'React');

  var PropTypes = _require(_dependencyMap[1], 'prop-types');

  var ReactNative = _require(_dependencyMap[2], 'ReactNative');

  var UIManager = _require(_dependencyMap[3], 'UIManager');

  var ViewPropTypes = _require(_dependencyMap[4], 'ViewPropTypes');

  var dismissKeyboard = _require(_dependencyMap[5], 'dismissKeyboard');

  var requireNativeComponent = _require(_dependencyMap[6], 'requireNativeComponent');

  var VIEWPAGER_REF = 'viewPager';

  var ViewPagerAndroid = function (_React$Component) {
    babelHelpers.inherits(ViewPagerAndroid, _React$Component);

    function ViewPagerAndroid() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, ViewPagerAndroid);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = ViewPagerAndroid.__proto__ || Object.getPrototypeOf(ViewPagerAndroid)).call.apply(_ref, [this].concat(args))), _this), _this.getInnerViewNode = function () {
        return _this.refs[VIEWPAGER_REF].getInnerViewNode();
      }, _this._childrenWithOverridenStyle = function () {
        return React.Children.map(_this.props.children, function (child) {
          if (!child) {
            return null;
          }

          var newProps = babelHelpers.extends({}, child.props, {
            style: [child.props.style, {
              position: 'absolute',
              left: 0,
              top: 0,
              right: 0,
              bottom: 0,
              width: undefined,
              height: undefined
            }],
            collapsable: false
          });

          if (child.type && child.type.displayName && child.type.displayName !== 'RCTView' && child.type.displayName !== 'View') {
            console.warn('Each ViewPager child must be a <View>. Was ' + child.type.displayName);
          }

          return React.createElement(child.type, newProps);
        });
      }, _this._onPageScroll = function (e) {
        if (_this.props.onPageScroll) {
          _this.props.onPageScroll(e);
        }

        if (_this.props.keyboardDismissMode === 'on-drag') {
          dismissKeyboard();
        }
      }, _this._onPageScrollStateChanged = function (e) {
        if (_this.props.onPageScrollStateChanged) {
          _this.props.onPageScrollStateChanged(e.nativeEvent.pageScrollState);
        }
      }, _this._onPageSelected = function (e) {
        if (_this.props.onPageSelected) {
          _this.props.onPageSelected(e);
        }
      }, _this.setPage = function (selectedPage) {
        UIManager.dispatchViewManagerCommand(ReactNative.findNodeHandle(_this), UIManager.AndroidViewPager.Commands.setPage, [selectedPage]);
      }, _this.setPageWithoutAnimation = function (selectedPage) {
        UIManager.dispatchViewManagerCommand(ReactNative.findNodeHandle(_this), UIManager.AndroidViewPager.Commands.setPageWithoutAnimation, [selectedPage]);
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(ViewPagerAndroid, [{
      key: "componentDidMount",
      value: function componentDidMount() {
        if (this.props.initialPage != null) {
          this.setPageWithoutAnimation(this.props.initialPage);
        }
      }
    }, {
      key: "render",
      value: function render() {
        return React.createElement(NativeAndroidViewPager, babelHelpers.extends({}, this.props, {
          ref: VIEWPAGER_REF,
          style: this.props.style,
          onPageScroll: this._onPageScroll,
          onPageScrollStateChanged: this._onPageScrollStateChanged,
          onPageSelected: this._onPageSelected,
          children: this._childrenWithOverridenStyle(),
          __source: {
            fileName: _jsxFileName,
            lineNumber: 236
          }
        }));
      }
    }]);
    return ViewPagerAndroid;
  }(React.Component);

  ViewPagerAndroid.propTypes = babelHelpers.extends({}, ViewPropTypes, {
    initialPage: PropTypes.number,
    onPageScroll: PropTypes.func,
    onPageScrollStateChanged: PropTypes.func,
    onPageSelected: PropTypes.func,
    pageMargin: PropTypes.number,
    keyboardDismissMode: PropTypes.oneOf(['none', 'on-drag']),
    scrollEnabled: PropTypes.bool,
    peekEnabled: PropTypes.bool
  });
  var NativeAndroidViewPager = requireNativeComponent('AndroidViewPager', ViewPagerAndroid);
  module.exports = ViewPagerAndroid;
},"47ee317a5466e3dd1e9a6c1e5821a602",["e6db4f0efed6b72f641ef0ffed29569f","18eeaf4e01377a466daaccc6ba8ce6f5","1102b68d89d7a6aede9677567aa01362","467cd3365342d9aaa2e941fe7ace641c","9ff7e107ed674a99182e71b796d889aa","9bc80013596b455d6a897518595d41ba","98c1697e1928b0d4ea4ae3837ea09d48"],"ViewPagerAndroid");