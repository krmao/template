__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/views/ResourceSavingSceneView.js";

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _propTypes = _require(_dependencyMap[2], "prop-types");

  var _propTypes2 = babelHelpers.interopRequireDefault(_propTypes);

  var _reactLifecyclesCompat = _require(_dependencyMap[3], "react-lifecycles-compat");

  var _SceneView = _require(_dependencyMap[4], "./SceneView");

  var _SceneView2 = babelHelpers.interopRequireDefault(_SceneView);

  var FAR_FAR_AWAY = 3000;

  var ResourceSavingSceneView = function (_React$PureComponent) {
    babelHelpers.inherits(ResourceSavingSceneView, _React$PureComponent);
    babelHelpers.createClass(ResourceSavingSceneView, null, [{
      key: "getDerivedStateFromProps",
      value: function getDerivedStateFromProps(nextProps, prevState) {
        if (nextProps.isFocused && !prevState.awake) {
          return {
            awake: true
          };
        } else {
          return null;
        }
      }
    }]);

    function ResourceSavingSceneView(props) {
      babelHelpers.classCallCheck(this, ResourceSavingSceneView);

      var _this = babelHelpers.possibleConstructorReturn(this, (ResourceSavingSceneView.__proto__ || Object.getPrototypeOf(ResourceSavingSceneView)).call(this));

      _this._mustAlwaysBeVisible = function () {
        return _this.props.animationEnabled || _this.props.swipeEnabled;
      };

      _this.state = {
        awake: props.lazy ? props.isFocused : true
      };
      return _this;
    }

    babelHelpers.createClass(ResourceSavingSceneView, [{
      key: "render",
      value: function render() {
        var awake = this.state.awake;
        var _props = this.props,
            isFocused = _props.isFocused,
            childNavigation = _props.childNavigation,
            navigation = _props.navigation,
            removeClippedSubviews = _props.removeClippedSubviews,
            lazy = _props.lazy,
            rest = babelHelpers.objectWithoutProperties(_props, ["isFocused", "childNavigation", "navigation", "removeClippedSubviews", "lazy"]);
        return _react2.default.createElement(
          _reactNative.View,
          {
            style: styles.container,
            collapsable: false,
            removeClippedSubviews: _reactNative.Platform.OS === 'android' ? removeClippedSubviews : !isFocused && removeClippedSubviews,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 39
            }
          },
          _react2.default.createElement(
            _reactNative.View,
            {
              style: this._mustAlwaysBeVisible() || isFocused ? styles.innerAttached : styles.innerDetached,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 48
              }
            },
            awake ? _react2.default.createElement(_SceneView2.default, babelHelpers.extends({}, rest, {
              navigation: childNavigation,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 55
              }
            })) : null
          )
        );
      }
    }]);
    return ResourceSavingSceneView;
  }(_react2.default.PureComponent);

  var styles = _reactNative.StyleSheet.create({
    container: {
      flex: 1,
      overflow: 'hidden'
    },
    innerAttached: {
      flex: 1
    },
    innerDetached: {
      flex: 1,
      top: FAR_FAR_AWAY
    }
  });

  exports.default = (0, _reactLifecyclesCompat.polyfill)(ResourceSavingSceneView);
},"c3be15fe301c34ea6c6abea8e37ffde4",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04","18eeaf4e01377a466daaccc6ba8ce6f5","9b11863b437a9ee794a44444ae115a3b","642023f04391babe6960216152f851fe"],"node_modules/react-navigation/src/views/ResourceSavingSceneView.js");