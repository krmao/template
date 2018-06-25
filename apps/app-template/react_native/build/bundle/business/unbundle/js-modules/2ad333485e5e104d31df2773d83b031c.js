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
},"2ad333485e5e104d31df2773d83b031c",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04","59d2a6488cbb4cc29f803db028219cac"],"src/main/js/base/Global.js");