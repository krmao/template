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
},"index_js",["node_modules_react_index_js","node_modules_react-native_Libraries_react-native_react-native-implementation_js","node_modules_react-navigation_src_react-navigation_js","src_main_js_base_Global_js","src_main_js_pages_HomeScreen_js","src_main_js_pages_BridgeScreen_js","src_main_js_pages_OrderCommitScreen_js"],"index.js");
__d(function (global, _require, module, exports, _dependencyMap) {
    Object.defineProperty(exports, "__esModule", {
        value: true
    });
    var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/src/main/js/base/Global.js";

    var _react = _require(_dependencyMap[0], "react");

    var _react2 = babelHelpers.interopRequireDefault(_react);

    var _reactNative = _require(_dependencyMap[1], "react-native");

    var STATUS_BAR_HEIGHT = _reactNative.Platform.OS === "android" ? 22 : 0;
    var defaultHeaderTranslucentStyle = {
        backgroundColor: '#20aa80',
        height: 48 + STATUS_BAR_HEIGHT,
        paddingTop: STATUS_BAR_HEIGHT,
        elevation: 2
    };
    var defaultHeaderStyle = {
        backgroundColor: '#fff',
        height: 48,
        elevation: 2
    };
    var defaultHeaderTitleStyle = {
        textAlign: "center",
        fontWeight: "normal",
        color: "#000",
        flex: 1,
        fontSize: 17
    };

    var defaultNavigationOptions = function defaultNavigationOptions(_ref) {
        var navigation = _ref.navigation;
        return {
            headerTitleStyle: global.defaultHeaderTitleStyle,
            headerStyle: global.defaultHeaderStyle,
            headerLeft: _react2.default.createElement(
                _reactNative.TouchableOpacity,
                {
                    onPress: function onPress() {
                        return navigation.goBack();
                    },
                    __source: {
                        fileName: _jsxFileName,
                        lineNumber: 37
                    }
                },
                _react2.default.createElement(_reactNative.Image, {
                    style: {
                        width: 38,
                        height: 38
                    },
                    source: _require(_dependencyMap[2], '../../res/img/back.png'),
                    __source: {
                        fileName: _jsxFileName,
                        lineNumber: 38
                    }
                })
            ),
            headerRight: _react2.default.createElement(_reactNative.View, {
                __source: {
                    fileName: _jsxFileName,
                    lineNumber: 41
                }
            })
        };
    };

    var global = {
        defaultHeaderTranslucentStyle: defaultHeaderTranslucentStyle,
        defaultHeaderStyle: defaultHeaderStyle,
        defaultHeaderTitleStyle: defaultHeaderTitleStyle,
        defaultNavigationOptions: defaultNavigationOptions
    };
    exports.default = global;
},"src_main_js_base_Global_js",["node_modules_react_index_js","node_modules_react-native_Libraries_react-native_react-native-implementation_js","src_main_res_img_back_png"],"src/main/js/base/Global.js");
__d(function (global, _require, module, exports, _dependencyMap) {
   module.exports = _require(_dependencyMap[0], "react-native/Libraries/Image/AssetRegistry").registerAsset({
      "__packager_asset": true,
      "httpServerLocation": "/assets/src/main/res/img",
      "width": 80,
      "height": 80,
      "scales": [1],
      "hash": "cff292c4f48a82c5922c4663e8941249",
      "name": "back",
      "type": "png"
   });
},"src_main_res_img_back_png",["node_modules_react-native_Libraries_Image_AssetRegistry_js"],"src/main/res/img/back.png");
__d(function (global, _require, module, exports, _dependencyMap) {
    Object.defineProperty(exports, "__esModule", {
        value: true
    });
    var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/src/main/js/pages/HomeScreen.js";

    var _react = _require(_dependencyMap[0], "react");

    var _react2 = babelHelpers.interopRequireDefault(_react);

    var _reactNative = _require(_dependencyMap[1], "react-native");

    var HomeScreen = function (_React$Component) {
        babelHelpers.inherits(HomeScreen, _React$Component);

        function HomeScreen() {
            babelHelpers.classCallCheck(this, HomeScreen);
            return babelHelpers.possibleConstructorReturn(this, (HomeScreen.__proto__ || Object.getPrototypeOf(HomeScreen)).apply(this, arguments));
        }

        babelHelpers.createClass(HomeScreen, [{
            key: "render",
            value: function render() {
                var _this2 = this;

                return _react2.default.createElement(
                    _reactNative.View,
                    {
                        style: styles.root,
                        __source: {
                            fileName: _jsxFileName,
                            lineNumber: 20
                        }
                    },
                    _react2.default.createElement(_reactNative.StatusBar, {
                        translucent: false,
                        __source: {
                            fileName: _jsxFileName,
                            lineNumber: 21
                        }
                    }),
                    _react2.default.createElement(
                        _reactNative.View,
                        {
                            style: styles.content,
                            __source: {
                                fileName: _jsxFileName,
                                lineNumber: 23
                            }
                        },
                        _react2.default.createElement(
                            _reactNative.ScrollView,
                            {
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 24
                                }
                            },
                            _react2.default.createElement(
                                _reactNative.TouchableOpacity,
                                {
                                    onPress: function onPress() {
                                        _this2.props.navigation.push('bridge');
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 25
                                    }
                                },
                                _react2.default.createElement(_reactNative.Image, {
                                    style: styles.banner,
                                    resizeMode: "contain",
                                    source: _require(_dependencyMap[2], '../../res/img/banner.png'),
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 29
                                    }
                                })
                            ),
                            _react2.default.createElement(
                                _reactNative.View,
                                {
                                    style: styles.description,
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 35
                                    }
                                },
                                _react2.default.createElement(
                                    _reactNative.Text,
                                    {
                                        style: {
                                            fontSize: 18,
                                            lineHeight: 20,
                                            color: "#27343a"
                                        },
                                        __source: {
                                            fileName: _jsxFileName,
                                            lineNumber: 37
                                        }
                                    },
                                    "\u8F6E\u6BC2\u6E05\u6D01"
                                ),
                                _react2.default.createElement(
                                    _reactNative.Text,
                                    {
                                        style: {
                                            fontSize: 12,
                                            lineHeight: 14,
                                            color: "#646464",
                                            marginTop: 10
                                        },
                                        __source: {
                                            fileName: _jsxFileName,
                                            lineNumber: 38
                                        }
                                    },
                                    "\u8F6E\u6BC2\u53BB\u9664\u94C1\u7C89, \u53BB\u6CB9\u53BB\u6C61"
                                ),
                                _react2.default.createElement(
                                    _reactNative.Text,
                                    {
                                        style: {
                                            fontSize: 19,
                                            lineHeight: 21,
                                            color: "#fc5430",
                                            marginTop: 18
                                        },
                                        __source: {
                                            fileName: _jsxFileName,
                                            lineNumber: 39
                                        }
                                    },
                                    _react2.default.createElement(
                                        _reactNative.Text,
                                        {
                                            style: {
                                                fontSize: 13
                                            },
                                            __source: {
                                                fileName: _jsxFileName,
                                                lineNumber: 40
                                            }
                                        },
                                        "\xA5"
                                    ),
                                    "80.00"
                                ),
                                _react2.default.createElement(_reactNative.View, {
                                    style: {
                                        backgroundColor: "#e8e8e8",
                                        height: 1,
                                        marginTop: 7
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 44
                                    }
                                }),
                                _react2.default.createElement(
                                    _reactNative.Text,
                                    {
                                        style: {
                                            fontSize: 11,
                                            lineHeight: 13,
                                            color: "#929292",
                                            marginTop: 15
                                        },
                                        __source: {
                                            fileName: _jsxFileName,
                                            lineNumber: 46
                                        }
                                    },
                                    "\u53EF\u76F4\u63A5\u5230\u5E97\u670D\u52A1, \u6D88\u8D39\u9AD8\u5CF0\u65F6\u6BB5\u9700\u7B49\u5019, \u656C\u8BF7\u8C05\u89E3."
                                )
                            ),
                            _react2.default.createElement(_reactNative.View, {
                                style: {
                                    backgroundColor: "#f5f5f5",
                                    height: 10
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 50
                                }
                            }),
                            _react2.default.createElement(
                                _reactNative.View,
                                {
                                    style: {
                                        flexDirection: "row",
                                        backgroundColor: "#ffffff",
                                        paddingTop: 15,
                                        paddingBottom: 15,
                                        paddingLeft: 13,
                                        paddingRight: 10,
                                        alignItems: "center"
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 52
                                    }
                                },
                                _react2.default.createElement(
                                    _reactNative.Text,
                                    {
                                        style: {
                                            fontSize: 15,
                                            lineHeight: 17,
                                            color: "#2c353c",
                                            flex: 1
                                        },
                                        __source: {
                                            fileName: _jsxFileName,
                                            lineNumber: 54
                                        }
                                    },
                                    "\u5BA2\u6237\u4F53\u9A8C(8)"
                                ),
                                _react2.default.createElement(
                                    _reactNative.TouchableOpacity,
                                    {
                                        style: {
                                            flexDirection: "row",
                                            alignItems: "center"
                                        },
                                        onPress: function onPress() {
                                            _reactNative.ToastAndroid.show("查看更多", _reactNative.ToastAndroid.SHORT);
                                        },
                                        __source: {
                                            fileName: _jsxFileName,
                                            lineNumber: 56
                                        }
                                    },
                                    _react2.default.createElement(
                                        _reactNative.Text,
                                        {
                                            style: {
                                                fontSize: 13,
                                                color: "#9a9a9a"
                                            },
                                            __source: {
                                                fileName: _jsxFileName,
                                                lineNumber: 61
                                            }
                                        },
                                        "\u67E5\u770B\u66F4\u591A"
                                    ),
                                    _react2.default.createElement(_reactNative.Image, {
                                        style: {
                                            width: 16,
                                            height: 16
                                        },
                                        resizeMode: "contain",
                                        source: _require(_dependencyMap[3], '../../res/img/right_arrow.png'),
                                        __source: {
                                            fileName: _jsxFileName,
                                            lineNumber: 63
                                        }
                                    })
                                )
                            ),
                            _react2.default.createElement(_reactNative.Image, {
                                style: styles.banner,
                                resizeMode: "contain",
                                source: _require(_dependencyMap[2], '../../res/img/banner.png'),
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 67
                                }
                            }),
                            _react2.default.createElement(_reactNative.Image, {
                                style: styles.banner,
                                resizeMode: "contain",
                                source: _require(_dependencyMap[2], '../../res/img/banner.png'),
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 68
                                }
                            }),
                            _react2.default.createElement(_reactNative.Image, {
                                style: styles.banner,
                                resizeMode: "contain",
                                source: _require(_dependencyMap[2], '../../res/img/banner.png'),
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 69
                                }
                            }),
                            _react2.default.createElement(_reactNative.Image, {
                                style: styles.banner,
                                resizeMode: "contain",
                                source: _require(_dependencyMap[2], '../../res/img/banner.png'),
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 70
                                }
                            }),
                            _react2.default.createElement(_reactNative.Image, {
                                style: styles.banner,
                                resizeMode: "contain",
                                source: _require(_dependencyMap[2], '../../res/img/banner.png'),
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 71
                                }
                            }),
                            _react2.default.createElement(_reactNative.Image, {
                                style: styles.banner,
                                resizeMode: "contain",
                                source: _require(_dependencyMap[2], '../../res/img/banner.png'),
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 72
                                }
                            }),
                            _react2.default.createElement(_reactNative.Image, {
                                style: styles.banner,
                                resizeMode: "contain",
                                source: _require(_dependencyMap[2], '../../res/img/banner.png'),
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 73
                                }
                            }),
                            _react2.default.createElement(_reactNative.Image, {
                                style: styles.banner,
                                resizeMode: "contain",
                                source: _require(_dependencyMap[2], '../../res/img/banner.png'),
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 74
                                }
                            }),
                            _react2.default.createElement(_reactNative.Image, {
                                style: styles.banner,
                                resizeMode: "contain",
                                source: _require(_dependencyMap[2], '../../res/img/banner.png'),
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 75
                                }
                            }),
                            _react2.default.createElement(_reactNative.Image, {
                                style: styles.banner,
                                resizeMode: "contain",
                                source: _require(_dependencyMap[2], '../../res/img/banner.png'),
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 76
                                }
                            }),
                            _react2.default.createElement(_reactNative.Image, {
                                style: styles.banner,
                                resizeMode: "contain",
                                source: _require(_dependencyMap[2], '../../res/img/banner.png'),
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 77
                                }
                            })
                        )
                    ),
                    _react2.default.createElement(
                        _reactNative.View,
                        {
                            __source: {
                                fileName: _jsxFileName,
                                lineNumber: 83
                            }
                        },
                        _react2.default.createElement(_reactNative.View, {
                            style: {
                                backgroundColor: "#e8e8e8",
                                height: 1
                            },
                            __source: {
                                fileName: _jsxFileName,
                                lineNumber: 84
                            }
                        }),
                        _react2.default.createElement(
                            _reactNative.View,
                            {
                                style: {
                                    flexDirection: "row",
                                    backgroundColor: "#fff",
                                    padding: 8,
                                    alignItems: "center"
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 86
                                }
                            },
                            _react2.default.createElement(_reactNative.TextInput, {
                                style: {
                                    height: 30,
                                    lineHeight: 15,
                                    fontSize: 15,
                                    color: "#28373e",
                                    textAlignVertical: "center",
                                    backgroundColor: "#f5f5f5",
                                    paddingTop: 0,
                                    paddingBottom: 0,
                                    paddingRight: 0,
                                    paddingLeft: 15,
                                    margin: 0,
                                    flex: 1,
                                    borderRadius: 5,
                                    borderWidth: 0
                                },
                                editable: true,
                                multiline: false,
                                autoFocus: false,
                                underlineColorAndroid: "transparent",
                                placeholderText: "#f5f5f5",
                                placeholder: "\u5DF2\u9009\u95E8\u5E97: \u4E0A\u6D77\u5149\u660E\u8DEF\u5E97",
                                onChangeText: function onChangeText(text) {},
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 87
                                }
                            }),
                            _react2.default.createElement(
                                _reactNative.TouchableOpacity,
                                {
                                    style: {
                                        marginLeft: 25,
                                        flexDirection: "row",
                                        alignItems: "center"
                                    },
                                    onPress: function onPress() {
                                        _reactNative.ToastAndroid.show("切换", _reactNative.ToastAndroid.SHORT);
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 114
                                    }
                                },
                                _react2.default.createElement(
                                    _reactNative.Text,
                                    {
                                        style: {
                                            fontSize: 13,
                                            color: "#2d2e30",
                                            marginRight: 3
                                        },
                                        __source: {
                                            fileName: _jsxFileName,
                                            lineNumber: 119
                                        }
                                    },
                                    "\u5207\u6362"
                                ),
                                _react2.default.createElement(_reactNative.Image, {
                                    style: {
                                        width: 16,
                                        height: 16,
                                        marginRight: 10
                                    },
                                    resizeMode: "contain",
                                    source: _require(_dependencyMap[3], '../../res/img/right_arrow.png'),
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 121
                                    }
                                })
                            )
                        ),
                        _react2.default.createElement(_reactNative.View, {
                            style: {
                                backgroundColor: "#e8e8e8",
                                height: 1
                            },
                            __source: {
                                fileName: _jsxFileName,
                                lineNumber: 124
                            }
                        }),
                        _react2.default.createElement(
                            _reactNative.View,
                            {
                                style: {
                                    flexDirection: "row",
                                    backgroundColor: "#fff",
                                    alignItems: "center",
                                    justifyContent: "center"
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 126
                                }
                            },
                            _react2.default.createElement(
                                _reactNative.TouchableOpacity,
                                {
                                    onPress: function onPress() {
                                        _reactNative.ToastAndroid.show("客服", _reactNative.ToastAndroid.SHORT);
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 127
                                    }
                                },
                                _react2.default.createElement(
                                    _reactNative.View,
                                    {
                                        style: {
                                            flexDirection: "column",
                                            backgroundColor: "#fff",
                                            alignItems: "center",
                                            justifyContent: "center",
                                            marginLeft: 23
                                        },
                                        __source: {
                                            fileName: _jsxFileName,
                                            lineNumber: 131
                                        }
                                    },
                                    _react2.default.createElement(_reactNative.Image, {
                                        style: {
                                            width: 22,
                                            height: 22
                                        },
                                        resizeMode: "contain",
                                        source: _require(_dependencyMap[4], '../../res/img/kefu.png'),
                                        __source: {
                                            fileName: _jsxFileName,
                                            lineNumber: 132
                                        }
                                    }),
                                    _react2.default.createElement(
                                        _reactNative.Text,
                                        {
                                            style: {
                                                fontSize: 12,
                                                color: "#666666"
                                            },
                                            __source: {
                                                fileName: _jsxFileName,
                                                lineNumber: 133
                                            }
                                        },
                                        "\u5BA2\u670D"
                                    )
                                )
                            ),
                            _react2.default.createElement(_reactNative.View, {
                                style: {
                                    backgroundColor: "#e8e8e8",
                                    width: 1,
                                    height: 40,
                                    marginLeft: 15,
                                    marginRight: 7,
                                    marginTop: 9,
                                    marginBottom: 9
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 138
                                }
                            }),
                            _react2.default.createElement(
                                _reactNative.TouchableOpacity,
                                {
                                    style: {
                                        flex: 1,
                                        height: 40,
                                        justifyContent: "center",
                                        alignContent: "center",
                                        margin: 8,
                                        alignItems: "center",
                                        backgroundColor: "#f94246",
                                        borderRadius: 5,
                                        borderWidth: 0
                                    },
                                    onPress: function onPress() {
                                        _this2.props.navigation.navigate('order_commit');
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 140
                                    }
                                },
                                _react2.default.createElement(
                                    _reactNative.Text,
                                    {
                                        style: {
                                            textAlign: "center",
                                            fontSize: 16,
                                            color: "white"
                                        },
                                        __source: {
                                            fileName: _jsxFileName,
                                            lineNumber: 156
                                        }
                                    },
                                    "\u7ACB\u5373\u9884\u7EA6"
                                )
                            )
                        )
                    )
                );
            }
        }]);
        return HomeScreen;
    }(_react2.default.Component);

    HomeScreen.navigationOptions = {
        title: "轮毂清洁",
        headerRight: _react2.default.createElement(
            _reactNative.TouchableOpacity,
            {
                onPress: function onPress() {
                    _reactNative.ToastAndroid.show("分享", _reactNative.ToastAndroid.SHORT);
                },
                __source: {
                    fileName: _jsxFileName,
                    lineNumber: 9
                }
            },
            _react2.default.createElement(_reactNative.Image, {
                style: {
                    width: 38,
                    height: 38
                },
                source: _require(_dependencyMap[5], '../../res/img/share.png'),
                __source: {
                    fileName: _jsxFileName,
                    lineNumber: 13
                }
            })
        )
    };
    exports.default = HomeScreen;

    var styles = _reactNative.StyleSheet.create({
        root: {
            flex: 1,
            flexDirection: "column",
            backgroundColor: '#efefef'
        },
        content: {
            flex: 1
        },
        titleBar: {
            justifyContent: "center",
            alignItems: "center",
            flexDirection: "row",
            height: 48,
            backgroundColor: "#fff"
        },
        title: {
            flex: 1,
            fontSize: 20,
            color: '#333',
            textAlign: 'center'
        },
        banner: {
            width: _reactNative.Dimensions.get('window').width,
            height: _reactNative.Dimensions.get('window').width * 2.0 / 3.0
        },
        description: {
            backgroundColor: "#fff",
            flexDirection: "column",
            paddingLeft: 18,
            paddingTop: 18,
            paddingBottom: 18,
            width: _reactNative.Dimensions.get('window').width
        },
        text: {
            fontSize: 20,
            color: 'black',
            textAlign: 'left'
        }
    });
},"src_main_js_pages_HomeScreen_js",["node_modules_react_index_js","node_modules_react-native_Libraries_react-native_react-native-implementation_js","src_main_res_img_banner_png","src_main_res_img_right_arrow_png","src_main_res_img_kefu_png","src_main_res_img_share_png"],"src/main/js/pages/HomeScreen.js");
__d(function (global, _require, module, exports, _dependencyMap) {
   module.exports = _require(_dependencyMap[0], "react-native/Libraries/Image/AssetRegistry").registerAsset({
      "__packager_asset": true,
      "httpServerLocation": "/assets/src/main/res/img",
      "width": 750,
      "height": 498,
      "scales": [1],
      "hash": "08dac7f263259ee0e6f53298c040d1fc",
      "name": "banner",
      "type": "png"
   });
},"src_main_res_img_banner_png",["node_modules_react-native_Libraries_Image_AssetRegistry_js"],"src/main/res/img/banner.png");
__d(function (global, _require, module, exports, _dependencyMap) {
   module.exports = _require(_dependencyMap[0], "react-native/Libraries/Image/AssetRegistry").registerAsset({
      "__packager_asset": true,
      "httpServerLocation": "/assets/src/main/res/img",
      "width": 29,
      "height": 29,
      "scales": [1],
      "hash": "66ff112faad357eb98de1a967a3a19d4",
      "name": "right_arrow",
      "type": "png"
   });
},"src_main_res_img_right_arrow_png",["node_modules_react-native_Libraries_Image_AssetRegistry_js"],"src/main/res/img/right_arrow.png");
__d(function (global, _require, module, exports, _dependencyMap) {
   module.exports = _require(_dependencyMap[0], "react-native/Libraries/Image/AssetRegistry").registerAsset({
      "__packager_asset": true,
      "httpServerLocation": "/assets/src/main/res/img",
      "width": 44,
      "height": 44,
      "scales": [1],
      "hash": "149c616720f36ea049c52696edc69f3a",
      "name": "kefu",
      "type": "png"
   });
},"src_main_res_img_kefu_png",["node_modules_react-native_Libraries_Image_AssetRegistry_js"],"src/main/res/img/kefu.png");
__d(function (global, _require, module, exports, _dependencyMap) {
   module.exports = _require(_dependencyMap[0], "react-native/Libraries/Image/AssetRegistry").registerAsset({
      "__packager_asset": true,
      "httpServerLocation": "/assets/src/main/res/img",
      "width": 80,
      "height": 80,
      "scales": [1],
      "hash": "366add38db4c0d2fd43bcc3312040b63",
      "name": "share",
      "type": "png"
   });
},"src_main_res_img_share_png",["node_modules_react-native_Libraries_Image_AssetRegistry_js"],"src/main/res/img/share.png");
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
},"src_main_js_pages_BridgeScreen_js",["node_modules_react_index_js","node_modules_react-native_Libraries_react-native_react-native-implementation_js"],"src/main/js/pages/BridgeScreen.js");
__d(function (global, _require, module, exports, _dependencyMap) {
    Object.defineProperty(exports, "__esModule", {
        value: true
    });
    var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/src/main/js/pages/OrderCommitScreen.js";

    var _react = _require(_dependencyMap[0], "react");

    var _react2 = babelHelpers.interopRequireDefault(_react);

    var _reactNative = _require(_dependencyMap[1], "react-native");

    var OrderCommitScreen = function (_React$Component) {
        babelHelpers.inherits(OrderCommitScreen, _React$Component);

        function OrderCommitScreen() {
            babelHelpers.classCallCheck(this, OrderCommitScreen);
            return babelHelpers.possibleConstructorReturn(this, (OrderCommitScreen.__proto__ || Object.getPrototypeOf(OrderCommitScreen)).apply(this, arguments));
        }

        babelHelpers.createClass(OrderCommitScreen, [{
            key: "render",
            value: function render() {
                var _this2 = this;

                return _react2.default.createElement(
                    _reactNative.View,
                    {
                        style: styles.container,
                        __source: {
                            fileName: _jsxFileName,
                            lineNumber: 12
                        }
                    },
                    _react2.default.createElement(
                        _reactNative.ScrollView,
                        {
                            style: {
                                flex: 1
                            },
                            __source: {
                                fileName: _jsxFileName,
                                lineNumber: 13
                            }
                        },
                        _react2.default.createElement(
                            _reactNative.View,
                            {
                                style: {
                                    flexDirection: 'row',
                                    alignItems: 'center',
                                    paddingLeft: 10,
                                    height: 60,
                                    backgroundColor: '#ffffff',
                                    marginTop: 0
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 15
                                }
                            },
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    style: {
                                        fontSize: 24,
                                        fontWeight: 'bold'
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 16
                                    }
                                },
                                "\u4E0A\u6D77\u5149\u660E\u8DEF\u5E97"
                            )
                        ),
                        _react2.default.createElement(
                            _reactNative.View,
                            {
                                style: {
                                    flexDirection: 'row',
                                    alignItems: 'center',
                                    paddingLeft: 10,
                                    paddingRight: 10,
                                    backgroundColor: '#ffffff',
                                    height: 45,
                                    borderBottomWidth: 1,
                                    borderBottomColor: '#e7e7e7'
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 19
                                }
                            },
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    style: {
                                        flex: 1
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 20
                                    }
                                },
                                "\u670D\u52A1\u65F6\u95F4"
                            ),
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    style: {
                                        paddingHorizontal: 10
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 21
                                    }
                                },
                                "2018 -06-11 \u4E0A\u5348 10\uFF1A00"
                            ),
                            _react2.default.createElement(_reactNative.Image, {
                                style: {
                                    width: 16,
                                    height: 16,
                                    marginRight: 10
                                },
                                resizeMode: "contain",
                                source: _require(_dependencyMap[2], '../../res/img/right_arrow.png'),
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 22
                                }
                            })
                        ),
                        _react2.default.createElement(
                            _reactNative.View,
                            {
                                style: {
                                    flexDirection: 'row',
                                    alignItems: 'center',
                                    paddingLeft: 10,
                                    paddingRight: 10,
                                    backgroundColor: '#ffffff',
                                    height: 45,
                                    borderBottomWidth: 1,
                                    borderBottomColor: '#e7e7e7'
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 25
                                }
                            },
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    style: {
                                        width: 80
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 26
                                    }
                                },
                                "\u8054\u7CFB\u7535\u8BDD"
                            ),
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    style: {
                                        color: '#cccccc'
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 27
                                    }
                                },
                                "18980471122"
                            )
                        ),
                        _react2.default.createElement(
                            _reactNative.View,
                            {
                                style: {
                                    flexDirection: 'row',
                                    alignItems: 'center',
                                    paddingLeft: 10,
                                    backgroundColor: '#ffffff',
                                    height: 45,
                                    borderBottomWidth: 1,
                                    borderBottomColor: '#e7e7e7'
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 30
                                }
                            },
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    style: {
                                        width: 80
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 31
                                    }
                                },
                                "\u8054\u7CFB\u4EBA"
                            ),
                            _react2.default.createElement(
                                _reactNative.TextInput,
                                {
                                    style: {
                                        color: '#000000',
                                        fontWeight: 'bold'
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 32
                                    }
                                },
                                "\u80E1\u51AC\u74DC"
                            )
                        ),
                        _react2.default.createElement(
                            _reactNative.View,
                            {
                                style: {
                                    height: 45,
                                    flexDirection: 'row',
                                    alignItems: 'center',
                                    paddingLeft: 10,
                                    backgroundColor: '#f5f5f5',
                                    borderBottomWidth: 1,
                                    borderBottomColor: '#e7e7e7'
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 35
                                }
                            },
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    style: {
                                        color: '#666666'
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 36
                                    }
                                },
                                "\u670D\u52A1\u9879\u76EE"
                            )
                        ),
                        _react2.default.createElement(
                            _reactNative.View,
                            {
                                style: {
                                    flexDirection: 'row',
                                    alignItems: 'center',
                                    paddingLeft: 10,
                                    paddingRight: 10,
                                    backgroundColor: '#ffffff',
                                    height: 45,
                                    borderBottomWidth: 1,
                                    borderBottomColor: '#e7e7e7'
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 39
                                }
                            },
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    style: {
                                        flex: 1
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 40
                                    }
                                },
                                "\u8F6E\u6BC2\u6E05\u6D17"
                            ),
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 41
                                    }
                                },
                                "\uFFE580.00"
                            )
                        ),
                        _react2.default.createElement(
                            _reactNative.View,
                            {
                                style: {
                                    flexDirection: 'row',
                                    alignItems: 'center',
                                    paddingLeft: 10,
                                    paddingRight: 10,
                                    backgroundColor: '#ffffff',
                                    height: 45,
                                    borderBottomWidth: 1,
                                    borderBottomColor: '#e7e7e7'
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 44
                                }
                            },
                            _react2.default.createElement(_reactNative.Text, {
                                style: {
                                    flex: 1
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 45
                                }
                            }),
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    style: {
                                        textAlign: 'right'
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 46
                                    }
                                },
                                "\u5408\u8BA1"
                            ),
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    style: {
                                        textAlign: 'right',
                                        color: 'red'
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 47
                                    }
                                },
                                "\uFFE580.00"
                            )
                        ),
                        _react2.default.createElement(
                            _reactNative.View,
                            {
                                style: {
                                    flexDirection: 'row',
                                    alignItems: 'center',
                                    marginTop: 10,
                                    paddingLeft: 10,
                                    paddingRight: 10,
                                    backgroundColor: '#ffffff',
                                    height: 45,
                                    borderBottomWidth: 1,
                                    borderBottomColor: '#e7e7e7'
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 50
                                }
                            },
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    style: {
                                        flex: 1
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 51
                                    }
                                },
                                "\u4F18\u60E0\u52B5"
                            ),
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    style: {
                                        color: '#cccccc',
                                        paddingHorizontal: 10
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 52
                                    }
                                },
                                "\u65E0\u53EF\u7528\u5238"
                            ),
                            _react2.default.createElement(_reactNative.Image, {
                                style: {
                                    width: 16,
                                    height: 16,
                                    marginRight: 10
                                },
                                resizeMode: "contain",
                                source: _require(_dependencyMap[2], '../../res/img/right_arrow.png'),
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 53
                                }
                            })
                        ),
                        _react2.default.createElement(
                            _reactNative.View,
                            {
                                style: {
                                    flexDirection: 'row',
                                    alignItems: 'center',
                                    paddingLeft: 10,
                                    paddingRight: 10,
                                    backgroundColor: '#ffffff',
                                    height: 45,
                                    borderBottomWidth: 1,
                                    borderBottomColor: '#e7e7e7'
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 56
                                }
                            },
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    style: {
                                        flex: 1
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 57
                                    }
                                },
                                "\u5957\u9910\u5361"
                            ),
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    style: {
                                        color: '#cccccc',
                                        paddingHorizontal: 10
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 58
                                    }
                                },
                                "\u65E0\u53EF\u7528\u5361"
                            ),
                            _react2.default.createElement(_reactNative.Image, {
                                style: {
                                    width: 16,
                                    height: 16,
                                    marginRight: 10
                                },
                                resizeMode: "contain",
                                source: _require(_dependencyMap[2], '../../res/img/right_arrow.png'),
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 59
                                }
                            })
                        )
                    ),
                    _react2.default.createElement(
                        _reactNative.View,
                        {
                            style: {
                                flexDirection: 'row',
                                alignItems: 'center',
                                paddingLeft: 10,
                                backgroundColor: '#f9fbfd',
                                height: 75,
                                borderTopWidth: 1,
                                borderTopColor: '#e7e7e7'
                            },
                            __source: {
                                fileName: _jsxFileName,
                                lineNumber: 63
                            }
                        },
                        _react2.default.createElement(
                            _reactNative.Text,
                            {
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 64
                                }
                            },
                            "\u5728\u7EBF\u652F\u4ED8"
                        ),
                        _react2.default.createElement(
                            _reactNative.Text,
                            {
                                style: {
                                    flex: 1,
                                    color: 'red'
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 65
                                }
                            },
                            "\uFFE580.00"
                        ),
                        _react2.default.createElement(
                            _reactNative.TouchableHighlight,
                            {
                                style: {
                                    alignItems: 'center',
                                    justifyContent: 'center',
                                    borderRadius: 5,
                                    marginRight: 5,
                                    backgroundColor: '#dd3f48',
                                    width: 120,
                                    height: 48
                                },
                                onPress: function onPress() {
                                    _reactNative.Alert.alert("\u63D0\u793A", '跳转到 bridge 测试 ?', [{
                                        text: '以后再说',
                                        onPress: function onPress() {
                                            return console.log('Ask me later pressed');
                                        }
                                    }, {
                                        text: '取消',
                                        onPress: function onPress() {
                                            return console.log('Cancel Pressed');
                                        },
                                        style: 'cancel'
                                    }, {
                                        text: '确定',
                                        onPress: function onPress() {
                                            return _this2.props.navigation.push('bridge');
                                        }
                                    }]);
                                },
                                __source: {
                                    fileName: _jsxFileName,
                                    lineNumber: 66
                                }
                            },
                            _react2.default.createElement(
                                _reactNative.Text,
                                {
                                    style: {
                                        color: 'white',
                                        fontWeight: 'bold',
                                        fontSize: 20
                                    },
                                    __source: {
                                        fileName: _jsxFileName,
                                        lineNumber: 79
                                    }
                                },
                                "\u786E\u8BA4\u63D0\u4EA4"
                            )
                        )
                    )
                );
            }
        }]);
        return OrderCommitScreen;
    }(_react2.default.Component);

    OrderCommitScreen.navigationOptions = {
        title: '提交订单'
    };
    exports.default = OrderCommitScreen;

    var styles = _reactNative.StyleSheet.create({
        container: {
            flex: 1,
            backgroundColor: '#F5F5F5'
        },
        welcome: {
            fontSize: 20,
            textAlign: 'center',
            margin: 10
        },
        instructions: {
            textAlign: 'center',
            color: '#333333',
            marginBottom: 5
        }
    });
},"src_main_js_pages_OrderCommitScreen_js",["node_modules_react_index_js","node_modules_react-native_Libraries_react-native_react-native-implementation_js","src_main_res_img_right_arrow_png"],"src/main/js/pages/OrderCommitScreen.js");
require("index_js");