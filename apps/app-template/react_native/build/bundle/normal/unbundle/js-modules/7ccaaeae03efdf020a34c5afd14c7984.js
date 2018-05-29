__d(function (global, _require, module, exports, _dependencyMap) {
    Object.defineProperty(exports, "__esModule", {
        value: true
    });
    var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/src/main/js/pages/BridgeScreen.js";

    var _react = _require(_dependencyMap[0], "react");

    var _react2 = babelHelpers.interopRequireDefault(_react);

    var _reactNative = _require(_dependencyMap[1], "react-native");

    var BridgeScreen = function (_React$Component) {
        babelHelpers.inherits(BridgeScreen, _React$Component);

        function BridgeScreen() {
            babelHelpers.classCallCheck(this, BridgeScreen);

            var _this = babelHelpers.possibleConstructorReturn(this, (BridgeScreen.__proto__ || Object.getPrototypeOf(BridgeScreen)).call(this));

            _this.state = {
                "dataToNative": 0,
                "resultFromNative": "null"
            };
            return _this;
        }

        babelHelpers.createClass(BridgeScreen, [{
            key: "componentWillMount",
            value: function componentWillMount() {
                console.debug("componentWillMount -> " + this.props.native_params);
            }
        }, {
            key: "componentDidMount",
            value: function componentDidMount() {
                console.debug("componentDidMount -> " + this.props.native_params);
                console.debug(this.props);
                this.reactBridge = _reactNative.NativeModules.ReactBridge;
                var CXToastUtil = _reactNative.NativeModules.CXToastUtil;
                this.native_listener = _reactNative.DeviceEventEmitter.addListener('native_event', function (event) {
                    console.warn("监听到有数据从native传递过来(这里不会重新渲染界面) -> " + event);
                });
            }
        }, {
            key: "componentWillReceiveProps",
            value: function componentWillReceiveProps() {
                console.debug("componentWillReceiveProps -> " + this.props.native_params);
            }
        }, {
            key: "shouldComponentUpdate",
            value: function shouldComponentUpdate() {
                var shouldComponentUpdate = this.props.native_params % 2 !== 0;
                console.debug("shouldComponentUpdate(" + shouldComponentUpdate + ") -> " + this.props.native_params);
                return shouldComponentUpdate;
            }
        }, {
            key: "componentDidUpdate",
            value: function componentDidUpdate() {
                console.debug("componentDidUpdate -> " + this.props.native_params);
            }
        }, {
            key: "componentWillUnmount",
            value: function componentWillUnmount() {
                console.debug("componentWillUnmount -> " + this.props.native_params);
                this.native_listener.remove();
            }
        }, {
            key: "render",
            value: function render() {
                var _this2 = this;

                console.debug("render -> " + this.props.native_params);
                return _react2.default.createElement(
                    _reactNative.View,
                    {
                        style: styles.container,
                        __source: {
                            fileName: _jsxFileName,
                            lineNumber: 67
                        }
                    },
                    _react2.default.createElement(_reactNative.StatusBar, {
                        translucent: false,
                        __source: {
                            fileName: _jsxFileName,
                            lineNumber: 69
                        }
                    }),
                    _react2.default.createElement(
                        _reactNative.TouchableOpacity,
                        {
                            onPress: function onPress() {
                                _this2.reactBridge.callNative((_this2.state.dataToNative * (_this2.state.dataToNative > 0 ? 1 : -1) + 1).toString()).then(function (successResult) {
                                    console.debug("successResult -> " + successResult);

                                    _this2.setState({
                                        "dataToNative": Number(successResult),
                                        "resultFromNative": successResult
                                    });
                                }, function (errorCode, errorMsg, error) {
                                    console.debug("errorCode -> " + errorCode);
                                    console.debug("errorMsg -> " + errorMsg);
                                    console.debug("error -> ");
                                    console.warn(error);
                                });
                            },
                            __source: {
                                fileName: _jsxFileName,
                                lineNumber: 71
                            }
                        },
                        _react2.default.createElement(
                            _reactNative.Text,
                            {
                                style: styles.button,
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 89
                                }
                            },
                            "点击调用 native 方法并回调(每次肯定是正数):" + (this.state.dataToNative * (this.state.dataToNative > 0 ? 1 : -1) + 1)
                        )
                    ),
                    _react2.default.createElement(
                        _reactNative.TouchableOpacity,
                        {
                            onPress: function onPress() {
                                _this2.reactBridge.callNative((_this2.state.dataToNative * (_this2.state.dataToNative > 0 ? -1 : 1) - 100).toString()).then(function (successResult) {
                                    console.debug("successResult -> " + successResult);

                                    _this2.setState({
                                        "dataToNative": Number(successResult),
                                        "resultFromNative": successResult
                                    });
                                }, function (errorCode, errorMsg, error) {
                                    console.debug("errorCode -> " + errorCode);
                                    console.debug("errorMsg -> " + errorMsg);
                                    console.debug("error -> ");
                                    console.warn(error);
                                });
                            },
                            __source: {
                                fileName: _jsxFileName,
                                lineNumber: 92
                            }
                        },
                        _react2.default.createElement(
                            _reactNative.Text,
                            {
                                style: styles.button,
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 110
                                }
                            },
                            "点击调用 native 方法 , 然后通过 native 调用 RN 方法(每次肯定是负数):" + (this.state.dataToNative * (this.state.dataToNative > 0 ? -1 : 1) - 100)
                        )
                    ),
                    _react2.default.createElement(
                        _reactNative.Text,
                        {
                            style: styles.content,
                            __source: {
                                fileName: _jsxFileName,
                                lineNumber: 114
                            }
                        },
                        "\u8C03\u7528 native \u540E\u7684\u8FD4\u56DE\u7ED3\u679C: ",
                        this.state.resultFromNative
                    ),
                    _react2.default.createElement(
                        _reactNative.Text,
                        {
                            style: styles.content,
                            __source: {
                                fileName: _jsxFileName,
                                lineNumber: 115
                            }
                        },
                        "\u5F53\u524D REACT-NATIVE \u542F\u52A8\u53C2\u6570: ",
                        this.props.native_params
                    ),
                    _react2.default.createElement(
                        _reactNative.Text,
                        {
                            style: styles.desc,
                            __source: {
                                fileName: _jsxFileName,
                                lineNumber: 116
                            }
                        },
                        "(\u53EA\u6709\u53CC\u6570\u624D\u4F1A\u91CD\u65B0\u6E32\u67D3\u754C\u9762)"
                    ),
                    _react2.default.createElement(
                        _reactNative.Text,
                        {
                            style: styles.desc,
                            __source: {
                                fileName: _jsxFileName,
                                lineNumber: 117
                            }
                        },
                        "\u901A\u8FC7\u5728 native \u91CD\u65B0\u8BBE\u7F6E react_root_view?.appProperties \u4FEE\u6539 REACT-NATIVE \u542F\u52A8\u53C2\u6570"
                    ),
                    _react2.default.createElement(
                        _reactNative.Text,
                        {
                            style: styles.desc,
                            __source: {
                                fileName: _jsxFileName,
                                lineNumber: 118
                            }
                        },
                        "\u901A\u8FC7 shouldComponentUpdate \u5224\u65AD\u662F\u5426\u9700\u8981\u91CD\u65B0\u6E32\u67D3\u754C\u9762"
                    )
                );
            }
        }]);
        return BridgeScreen;
    }(_react2.default.Component);

    BridgeScreen.navigationOptions = function (_ref) {
        var navigation = _ref.navigation;
        return {
            title: "双向交互测试",
            headerRight: _react2.default.createElement(_reactNative.View, {
                __source: {
                    fileName: _jsxFileName,
                    lineNumber: 8
                }
            })
        };
    };

    exports.default = BridgeScreen;

    var styles = _reactNative.StyleSheet.create({
        container: {
            flex: 1,
            justifyContent: 'center',
            backgroundColor: '#D3D3D3'
        },
        content: {
            fontSize: 20,
            fontWeight: 'bold',
            color: 'red',
            textAlign: 'center',
            margin: 10
        },
        desc: {
            fontSize: 12,
            textAlign: 'center'
        },
        button: {
            fontSize: 20,
            fontWeight: 'bold',
            color: 'white',
            textAlign: 'center',
            padding: 10,
            backgroundColor: 'blue'
        }
    });
},"7ccaaeae03efdf020a34c5afd14c7984",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04"],"src/main/js/pages/BridgeScreen.js");