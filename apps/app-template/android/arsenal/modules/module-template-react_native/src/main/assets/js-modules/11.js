__d(function (global, _require, module, exports, _dependencyMap) {
    Object.defineProperty(exports, "__esModule", {
        value: true
    });
    var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/index.js";

    var _react = _require(_dependencyMap[0], "react");

    var _react2 = babelHelpers.interopRequireDefault(_react);

    var _reactNative = _require(_dependencyMap[1], "react-native");

    var _reactNavigation = _require(_dependencyMap[2], "react-navigation");

    var _Global = _require(_dependencyMap[3], "./src/main/js/base/Global");

    var _Global2 = babelHelpers.interopRequireDefault(_Global);

    var _HomeScreen = _require(_dependencyMap[4], "./src/main/js/pages/HomeScreen");

    var _HomeScreen2 = babelHelpers.interopRequireDefault(_HomeScreen);

    var _BridgeScreen = _require(_dependencyMap[5], "./src/main/js/pages/BridgeScreen");

    var _BridgeScreen2 = babelHelpers.interopRequireDefault(_BridgeScreen);

    var _OrderCommitScreen = _require(_dependencyMap[6], "./src/main/js/pages/OrderCommitScreen");

    var _OrderCommitScreen2 = babelHelpers.interopRequireDefault(_OrderCommitScreen);

    console.log("OS:" + _reactNative.Platform.OS);
    console.log("STATUS_BAR_HEIGHT:" + _Global2.default.STATUS_BAR_HEIGHT);

    _reactNative.YellowBox.ignoreWarnings(['Warning: isMounted(...) is deprecated', 'Module RCTImageLoader']);

    var RootStack = (0, _reactNavigation.createStackNavigator)({
        home: {
            screen: _HomeScreen2.default,
            navigationOptions: function navigationOptions(navigation) {
                return _Global2.default.defaultNavigationOptions(navigation);
            }
        },
        bridge: {
            screen: _BridgeScreen2.default,
            navigationOptions: function navigationOptions(navigation) {
                return _Global2.default.defaultNavigationOptions(navigation);
            }
        },
        order_commit: {
            screen: _OrderCommitScreen2.default,
            navigationOptions: function navigationOptions(navigation) {
                return _Global2.default.defaultNavigationOptions(navigation);
            }
        }
    }, {
        initialRouteName: "home",
        navigationOptions: {
            gesturesEnabled: true
        },
        mode: "card",
        headerMode: "screen",
        headerTransitionPreset: "uikit",
        transitionConfig: function transitionConfig() {
            return {
                transitionSpec: {
                    duration: 300,
                    easing: _reactNative.Easing.out(_reactNative.Easing.poly(4)),
                    timing: _reactNative.Animated.timing
                },
                screenInterpolator: function screenInterpolator(sceneProps) {
                    var layout = sceneProps.layout,
                        position = sceneProps.position,
                        scene = sceneProps.scene;
                    var index = scene.index;
                    var width = layout.initWidth;
                    var translateX = position.interpolate({
                        inputRange: [index - 1, index, index + 1],
                        outputRange: [width, 0, 0]
                    });
                    var opacity = position.interpolate({
                        inputRange: [index - 1, index - 0.99, index],
                        outputRange: [0, 1, 1]
                    });
                    return {
                        opacity: opacity,
                        transform: [{
                            translateX: translateX
                        }]
                    };
                }
            };
        }
    });

    var App = function (_React$Component) {
        babelHelpers.inherits(App, _React$Component);

        function App() {
            var _ref;

            var _temp, _this, _ret;

            babelHelpers.classCallCheck(this, App);

            for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
                args[_key] = arguments[_key];
            }

            return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = App.__proto__ || Object.getPrototypeOf(App)).call.apply(_ref, [this].concat(args))), _this), _this.onBackPressed = function () {
                return false;
            }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
        }

        babelHelpers.createClass(App, [{
            key: "componentWillMount",
            value: function componentWillMount() {
                _reactNative.BackHandler.addEventListener("hardwareBackPress", this.onBackPressed);
            }
        }, {
            key: "componentWillUnmount",
            value: function componentWillUnmount() {
                _reactNative.BackHandler.removeEventListener("hardwareBackPress", this.onBackPressed);
            }
        }, {
            key: "render",
            value: function render() {
                return _react2.default.createElement(RootStack, {
                    __source: {
                        fileName: _jsxFileName,
                        lineNumber: 75
                    }
                });
            }
        }]);
        return App;
    }(_react2.default.Component);

    exports.default = App;

    _reactNative.AppRegistry.registerComponent("react-module-home", function () {
        return App;
    });
},11,[12,22,337,442,444,449,450],"index.js");