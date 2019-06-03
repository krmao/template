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
},"aa3441b497f0a727ed708d5a9e65b326",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04","6b30517a7f2a94e8009abc053da3142b"],"src/main/js/pages/OrderCommitScreen.js");