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
},444,[12,22,445,446,447,448],"src/main/js/pages/HomeScreen.js");