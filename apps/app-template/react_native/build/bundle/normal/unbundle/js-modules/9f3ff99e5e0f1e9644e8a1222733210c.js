__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/RefreshControl/RefreshControl.js";

  var ColorPropType = _require(_dependencyMap[0], 'ColorPropType');

  var NativeMethodsMixin = _require(_dependencyMap[1], 'NativeMethodsMixin');

  var Platform = _require(_dependencyMap[2], 'Platform');

  var React = _require(_dependencyMap[3], 'React');

  var PropTypes = _require(_dependencyMap[4], 'prop-types');

  var ViewPropTypes = _require(_dependencyMap[5], 'ViewPropTypes');

  var createReactClass = _require(_dependencyMap[6], 'create-react-class');

  var requireNativeComponent = _require(_dependencyMap[7], 'requireNativeComponent');

  if (Platform.OS === 'android') {
    var AndroidSwipeRefreshLayout = _require(_dependencyMap[8], 'UIManager').AndroidSwipeRefreshLayout;

    var RefreshLayoutConsts = AndroidSwipeRefreshLayout ? AndroidSwipeRefreshLayout.Constants : {
      SIZE: {}
    };
  } else {
    var RefreshLayoutConsts = {
      SIZE: {}
    };
  }

  var RefreshControl = createReactClass({
    displayName: 'RefreshControl',
    statics: {
      SIZE: RefreshLayoutConsts.SIZE
    },
    mixins: [NativeMethodsMixin],
    propTypes: babelHelpers.extends({}, ViewPropTypes, {
      onRefresh: PropTypes.func,
      refreshing: PropTypes.bool.isRequired,
      tintColor: ColorPropType,
      titleColor: ColorPropType,
      title: PropTypes.string,
      enabled: PropTypes.bool,
      colors: PropTypes.arrayOf(ColorPropType),
      progressBackgroundColor: ColorPropType,
      size: PropTypes.oneOf([RefreshLayoutConsts.SIZE.DEFAULT, RefreshLayoutConsts.SIZE.LARGE]),
      progressViewOffset: PropTypes.number
    }),
    _nativeRef: null,
    _lastNativeRefreshing: false,
    componentDidMount: function componentDidMount() {
      this._lastNativeRefreshing = this.props.refreshing;
    },
    componentDidUpdate: function componentDidUpdate(prevProps) {
      if (this.props.refreshing !== prevProps.refreshing) {
        this._lastNativeRefreshing = this.props.refreshing;
      } else if (this.props.refreshing !== this._lastNativeRefreshing) {
        this._nativeRef.setNativeProps({
          refreshing: this.props.refreshing
        });

        this._lastNativeRefreshing = this.props.refreshing;
      }
    },
    render: function render() {
      var _this = this;

      return React.createElement(NativeRefreshControl, babelHelpers.extends({}, this.props, {
        ref: function ref(_ref) {
          _this._nativeRef = _ref;
        },
        onRefresh: this._onRefresh,
        __source: {
          fileName: _jsxFileName,
          lineNumber: 158
        }
      }));
    },
    _onRefresh: function _onRefresh() {
      this._lastNativeRefreshing = true;
      this.props.onRefresh && this.props.onRefresh();
      this.forceUpdate();
    }
  });

  if (Platform.OS === 'ios') {
    var NativeRefreshControl = requireNativeComponent('RCTRefreshControl', RefreshControl);
  } else if (Platform.OS === 'android') {
    var NativeRefreshControl = requireNativeComponent('AndroidSwipeRefreshLayout', RefreshControl);
  }

  module.exports = RefreshControl;
},"9f3ff99e5e0f1e9644e8a1222733210c",["63c61c7eda525c10d0670d2ef8475012","e2817b4a53aaef19afef34f031e1b9c9","9493a89f5d95c3a8a47c65cfed9b5542","e6db4f0efed6b72f641ef0ffed29569f","18eeaf4e01377a466daaccc6ba8ce6f5","9ff7e107ed674a99182e71b796d889aa","29cb0e104e5fce198008f3e789631772","98c1697e1928b0d4ea4ae3837ea09d48","467cd3365342d9aaa2e941fe7ace641c"],"RefreshControl");