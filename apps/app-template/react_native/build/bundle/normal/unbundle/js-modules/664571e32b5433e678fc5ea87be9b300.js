__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Inspector/InspectorOverlay.js";

  var Dimensions = _require(_dependencyMap[0], 'Dimensions');

  var ElementBox = _require(_dependencyMap[1], 'ElementBox');

  var PropTypes = _require(_dependencyMap[2], 'prop-types');

  var React = _require(_dependencyMap[3], 'React');

  var StyleSheet = _require(_dependencyMap[4], 'StyleSheet');

  var UIManager = _require(_dependencyMap[5], 'UIManager');

  var View = _require(_dependencyMap[6], 'View');

  var InspectorOverlay = function (_React$Component) {
    babelHelpers.inherits(InspectorOverlay, _React$Component);

    function InspectorOverlay() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, InspectorOverlay);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = InspectorOverlay.__proto__ || Object.getPrototypeOf(InspectorOverlay)).call.apply(_ref, [this].concat(args))), _this), _this.findViewForTouchEvent = function (e) {
        var _e$nativeEvent$touche = e.nativeEvent.touches[0],
            locationX = _e$nativeEvent$touche.locationX,
            locationY = _e$nativeEvent$touche.locationY;
        UIManager.findSubviewIn(_this.props.inspectedViewTag, [locationX, locationY], function (nativeViewTag, left, top, width, height) {
          _this.props.onTouchViewTag(nativeViewTag, {
            left: left,
            top: top,
            width: width,
            height: height
          }, locationY);
        });
      }, _this.shouldSetResponser = function (e) {
        _this.findViewForTouchEvent(e);

        return true;
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(InspectorOverlay, [{
      key: "render",
      value: function render() {
        var content = null;

        if (this.props.inspected) {
          content = React.createElement(ElementBox, {
            frame: this.props.inspected.frame,
            style: this.props.inspected.style,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 60
            }
          });
        }

        return React.createElement(
          View,
          {
            onStartShouldSetResponder: this.shouldSetResponser,
            onResponderMove: this.findViewForTouchEvent,
            style: [styles.inspector, {
              height: Dimensions.get('window').height
            }],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 64
            }
          },
          content
        );
      }
    }]);
    return InspectorOverlay;
  }(React.Component);

  InspectorOverlay.propTypes = {
    inspected: PropTypes.shape({
      frame: PropTypes.object,
      style: PropTypes.any
    }),
    inspectedViewTag: PropTypes.number,
    onTouchViewTag: PropTypes.func.isRequired
  };
  var styles = StyleSheet.create({
    inspector: {
      backgroundColor: 'transparent',
      position: 'absolute',
      left: 0,
      top: 0,
      right: 0
    }
  });
  module.exports = InspectorOverlay;
},"664571e32b5433e678fc5ea87be9b300",["cbac9baa189a2fa69f7f5fdd76e9bc71","bc1d1fc40bc039c47736d7a7862dfab2","18eeaf4e01377a466daaccc6ba8ce6f5","e6db4f0efed6b72f641ef0ffed29569f","d31e8c1a3f9844becc88973ecddac872","467cd3365342d9aaa2e941fe7ace641c","30a3b04291b6e1f01b778ff31271ccc5"],"InspectorOverlay");