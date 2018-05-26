__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/ReactNative/AppContainer.js";

  var EmitterSubscription = _require(_dependencyMap[0], 'EmitterSubscription');

  var PropTypes = _require(_dependencyMap[1], 'prop-types');

  var RCTDeviceEventEmitter = _require(_dependencyMap[2], 'RCTDeviceEventEmitter');

  var React = _require(_dependencyMap[3], 'React');

  var ReactNative = _require(_dependencyMap[4], 'ReactNative');

  var StyleSheet = _require(_dependencyMap[5], 'StyleSheet');

  var View = _require(_dependencyMap[6], 'View');

  var AppContainer = function (_React$Component) {
    babelHelpers.inherits(AppContainer, _React$Component);

    function AppContainer() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, AppContainer);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = AppContainer.__proto__ || Object.getPrototypeOf(AppContainer)).call.apply(_ref, [this].concat(args))), _this), _this.state = {
        inspector: null,
        mainKey: 1
      }, _this._subscription = null, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(AppContainer, [{
      key: "getChildContext",
      value: function getChildContext() {
        return {
          rootTag: this.props.rootTag
        };
      }
    }, {
      key: "componentDidMount",
      value: function componentDidMount() {
        var _this2 = this;

        if (__DEV__) {
          if (!global.__RCTProfileIsProfiling) {
            this._subscription = RCTDeviceEventEmitter.addListener('toggleElementInspector', function () {
              var Inspector = _require(_dependencyMap[7], 'Inspector');

              var inspector = _this2.state.inspector ? null : React.createElement(Inspector, {
                inspectedViewTag: ReactNative.findNodeHandle(_this2._mainRef),
                onRequestRerenderApp: function onRequestRerenderApp(updateInspectedViewTag) {
                  _this2.setState(function (s) {
                    return {
                      mainKey: s.mainKey + 1
                    };
                  }, function () {
                    return updateInspectedViewTag(ReactNative.findNodeHandle(_this2._mainRef));
                  });
                },
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 65
                }
              });

              _this2.setState({
                inspector: inspector
              });
            });
          }
        }
      }
    }, {
      key: "componentWillUnmount",
      value: function componentWillUnmount() {
        if (this._subscription) {
          this._subscription.remove();
        }
      }
    }, {
      key: "render",
      value: function render() {
        var _this3 = this;

        var yellowBox = null;

        if (__DEV__) {
          if (!global.__RCTProfileIsProfiling && !this.props.fabric) {
            var YellowBox = _require(_dependencyMap[8], 'YellowBox');

            yellowBox = React.createElement(YellowBox, {
              __source: {
                fileName: _jsxFileName,
                lineNumber: 97
              }
            });
          }
        }

        var innerView = React.createElement(
          View,
          {
            collapsable: !this.state.inspector,
            key: this.state.mainKey,
            pointerEvents: "box-none",
            style: styles.appContainer,
            ref: function ref(_ref2) {
              _this3._mainRef = _ref2;
            },
            __source: {
              fileName: _jsxFileName,
              lineNumber: 102
            }
          },
          this.props.children
        );
        var Wrapper = this.props.WrapperComponent;

        if (Wrapper) {
          innerView = React.createElement(
            Wrapper,
            {
              __source: {
                fileName: _jsxFileName,
                lineNumber: 119
              }
            },
            innerView
          );
        }

        return React.createElement(
          View,
          {
            style: styles.appContainer,
            pointerEvents: "box-none",
            __source: {
              fileName: _jsxFileName,
              lineNumber: 122
            }
          },
          innerView,
          yellowBox,
          this.state.inspector
        );
      }
    }]);
    return AppContainer;
  }(React.Component);

  AppContainer.childContextTypes = {
    rootTag: PropTypes.number
  };
  var styles = StyleSheet.create({
    appContainer: {
      flex: 1
    }
  });
  module.exports = AppContainer;
},265,[42,129,40,132,49,171,173,266,282],"AppContainer");