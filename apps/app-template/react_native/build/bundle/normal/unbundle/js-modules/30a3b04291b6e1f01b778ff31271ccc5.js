__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/View/View.js";

  var Platform = _require2(_dependencyMap[0], 'Platform');

  var React = _require2(_dependencyMap[1], 'React');

  var ReactNative = _require2(_dependencyMap[2], 'ReactNative');

  var ReactNativeStyleAttributes = _require2(_dependencyMap[3], 'ReactNativeStyleAttributes');

  var ReactNativeViewAttributes = _require2(_dependencyMap[4], 'ReactNativeViewAttributes');

  var ViewPropTypes = _require2(_dependencyMap[5], 'ViewPropTypes');

  var _require = _require2(_dependencyMap[6], 'ViewContext'),
      ViewContextTypes = _require.ViewContextTypes;

  var invariant = _require2(_dependencyMap[7], 'fbjs/lib/invariant');

  var requireNativeComponent = _require2(_dependencyMap[8], 'requireNativeComponent');

  var View = function (_ReactNative$NativeCo) {
    babelHelpers.inherits(View, _ReactNative$NativeCo);

    function View() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, View);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = View.__proto__ || Object.getPrototypeOf(View)).call.apply(_ref, [this].concat(args))), _this), _this.viewConfig = {
        uiViewClassName: 'RCTView',
        validAttributes: ReactNativeViewAttributes.RCTView
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(View, [{
      key: "getChildContext",
      value: function getChildContext() {
        return {
          isInAParentText: false
        };
      }
    }, {
      key: "render",
      value: function render() {
        invariant(!(this.context.isInAParentText && Platform.OS === 'android'), 'Nesting of <View> within <Text> is not supported on Android.');
        return React.createElement(RCTView, babelHelpers.extends({}, this.props, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 60
          }
        }));
      }
    }]);
    return View;
  }(ReactNative.NativeComponent);

  View.propTypes = ViewPropTypes;
  View.childContextTypes = ViewContextTypes;
  var RCTView = requireNativeComponent('RCTView', View, {
    nativeOnly: {
      nativeBackgroundAndroid: true,
      nativeForegroundAndroid: true
    }
  });

  if (__DEV__) {
    var UIManager = _require2(_dependencyMap[9], 'UIManager');

    var viewConfig = UIManager.viewConfigs && UIManager.viewConfigs.RCTView || {};

    for (var prop in viewConfig.nativeProps) {
      var viewAny = View;

      if (!viewAny.propTypes[prop] && !ReactNativeStyleAttributes[prop]) {
        throw new Error('View is missing propType for native prop `' + prop + '`');
      }
    }
  }

  var ViewToExport = RCTView;

  if (__DEV__) {
    ViewToExport = View;
  }

  module.exports = ViewToExport;
},"30a3b04291b6e1f01b778ff31271ccc5",["9493a89f5d95c3a8a47c65cfed9b5542","e6db4f0efed6b72f641ef0ffed29569f","1102b68d89d7a6aede9677567aa01362","48a8d189c373be9b55e02c49ac64e2e8","6477887be0d285a967d42967386335cd","9ff7e107ed674a99182e71b796d889aa","a0a67b647dff8a7e11698d04fd60772b","8940a4ad43b101ffc23e725363c70f8d","98c1697e1928b0d4ea4ae3837ea09d48","467cd3365342d9aaa2e941fe7ace641c"],"View");