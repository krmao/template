__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/ReactNative/YellowBox.js";

  var EventEmitter = _require(_dependencyMap[0], 'EventEmitter');

  var Platform = _require(_dependencyMap[1], 'Platform');

  var React = _require(_dependencyMap[2], 'React');

  var SafeAreaView = _require(_dependencyMap[3], 'SafeAreaView');

  var StyleSheet = _require(_dependencyMap[4], 'StyleSheet');

  var RCTLog = _require(_dependencyMap[5], 'RCTLog');

  var infoLog = _require(_dependencyMap[6], 'infoLog');

  var openFileInEditor = _require(_dependencyMap[7], 'openFileInEditor');

  var parseErrorStack = _require(_dependencyMap[8], 'parseErrorStack');

  var stringifySafe = _require(_dependencyMap[9], 'stringifySafe');

  var symbolicateStackTrace = _require(_dependencyMap[10], 'symbolicateStackTrace');

  var _warningEmitter = new EventEmitter();

  var _warningMap = new Map();

  var IGNORED_WARNINGS = [];

  if (__DEV__) {
    var _console = console,
        error = _console.error,
        warn = _console.warn;

    console.error = function () {
      error.apply(console, arguments);

      if (typeof arguments[0] === 'string' && arguments[0].startsWith('Warning: ')) {
        updateWarningMap.apply(null, arguments);
      }
    };

    console.warn = function () {
      warn.apply(console, arguments);
      updateWarningMap.apply(null, arguments);
    };

    if (Platform.isTesting) {
      console.disableYellowBox = true;
    }

    RCTLog.setWarningHandler(function () {
      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      updateWarningMap.apply(null, args);
    });
  }

  function sprintf(format) {
    for (var _len2 = arguments.length, args = Array(_len2 > 1 ? _len2 - 1 : 0), _key2 = 1; _key2 < _len2; _key2++) {
      args[_key2 - 1] = arguments[_key2];
    }

    var index = 0;
    return format.replace(/%s/g, function (match) {
      return args[index++];
    });
  }

  function updateWarningMap() {
    if (console.disableYellowBox) {
      return;
    }

    var warning = void 0;

    for (var _len3 = arguments.length, args = Array(_len3), _key3 = 0; _key3 < _len3; _key3++) {
      args[_key3] = arguments[_key3];
    }

    if (typeof args[0] === 'string') {
      var format = args[0],
          formatArgs = args.slice(1);
      var argCount = (format.match(/%s/g) || []).length;
      warning = [sprintf.apply(undefined, [format].concat(babelHelpers.toConsumableArray(formatArgs.slice(0, argCount).map(stringifySafe))))].concat(babelHelpers.toConsumableArray(formatArgs.slice(argCount).map(stringifySafe))).join(' ');
    } else {
      warning = args.map(stringifySafe).join(' ');
    }

    if (warning.startsWith('(ADVICE)')) {
      return;
    }

    var warningInfo = _warningMap.get(warning);

    if (warningInfo) {
      warningInfo.count += 1;
    } else {
      var _error = new Error();

      _error.framesToPop = 2;

      _warningMap.set(warning, {
        count: 1,
        stacktrace: parseErrorStack(_error),
        symbolicated: false
      });
    }

    _warningEmitter.emit('warning', _warningMap);
  }

  function ensureSymbolicatedWarning(warning) {
    var prevWarningInfo = _warningMap.get(warning);

    if (!prevWarningInfo || prevWarningInfo.symbolicated) {
      return;
    }

    prevWarningInfo.symbolicated = true;
    symbolicateStackTrace(prevWarningInfo.stacktrace).then(function (stack) {
      var nextWarningInfo = _warningMap.get(warning);

      if (nextWarningInfo) {
        nextWarningInfo.stacktrace = stack;

        _warningEmitter.emit('warning', _warningMap);
      }
    }, function (error) {
      var nextWarningInfo = _warningMap.get(warning);

      if (nextWarningInfo) {
        infoLog('Failed to symbolicate warning, "%s":', warning, error);

        _warningEmitter.emit('warning', _warningMap);
      }
    });
  }

  function isWarningIgnored(warning) {
    var isIgnored = IGNORED_WARNINGS.some(function (ignoredWarning) {
      return warning.startsWith(ignoredWarning);
    });

    if (isIgnored) {
      return true;
    }

    return Array.isArray(console.ignoredYellowBox) && console.ignoredYellowBox.some(function (ignorePrefix) {
      return warning.startsWith(String(ignorePrefix));
    });
  }

  var WarningRow = function WarningRow(_ref) {
    var count = _ref.count,
        warning = _ref.warning,
        onPress = _ref.onPress;

    var Text = _require(_dependencyMap[11], 'Text');

    var TouchableHighlight = _require(_dependencyMap[12], 'TouchableHighlight');

    var View = _require(_dependencyMap[13], 'View');

    var countText = count > 1 ? React.createElement(
      Text,
      {
        style: styles.listRowCount,
        __source: {
          fileName: _jsxFileName,
          lineNumber: 195
        }
      },
      '(' + count + ') '
    ) : null;
    return React.createElement(
      View,
      {
        style: styles.listRow,
        __source: {
          fileName: _jsxFileName,
          lineNumber: 199
        }
      },
      React.createElement(
        TouchableHighlight,
        {
          activeOpacity: 0.5,
          onPress: onPress,
          style: styles.listRowContent,
          underlayColor: "transparent",
          __source: {
            fileName: _jsxFileName,
            lineNumber: 200
          }
        },
        React.createElement(
          Text,
          {
            style: styles.listRowText,
            numberOfLines: 2,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 205
            }
          },
          countText,
          warning
        )
      )
    );
  };

  var StackRow = function StackRow(_ref2) {
    var frame = _ref2.frame;

    var Text = _require(_dependencyMap[11], 'Text');

    var TouchableHighlight = _require(_dependencyMap[12], 'TouchableHighlight');

    var file = frame.file,
        lineNumber = frame.lineNumber;
    var fileName = void 0;

    if (file) {
      var fileParts = file.split('/');
      fileName = fileParts[fileParts.length - 1];
    } else {
      fileName = '<unknown file>';
    }

    return React.createElement(
      TouchableHighlight,
      {
        activeOpacity: 0.5,
        style: styles.openInEditorButton,
        underlayColor: "transparent",
        onPress: openFileInEditor.bind(null, file, lineNumber),
        __source: {
          fileName: _jsxFileName,
          lineNumber: 228
        }
      },
      React.createElement(
        Text,
        {
          style: styles.inspectorCountText,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 233
          }
        },
        fileName,
        ":",
        lineNumber
      )
    );
  };

  var WarningInspector = function WarningInspector(_ref3) {
    var warningInfo = _ref3.warningInfo,
        warning = _ref3.warning,
        stacktraceVisible = _ref3.stacktraceVisible,
        onDismiss = _ref3.onDismiss,
        onDismissAll = _ref3.onDismissAll,
        onMinimize = _ref3.onMinimize,
        toggleStacktrace = _ref3.toggleStacktrace;

    var ScrollView = _require(_dependencyMap[14], 'ScrollView');

    var Text = _require(_dependencyMap[11], 'Text');

    var TouchableHighlight = _require(_dependencyMap[12], 'TouchableHighlight');

    var View = _require(_dependencyMap[13], 'View');

    var _ref4 = warningInfo || {},
        count = _ref4.count,
        stacktrace = _ref4.stacktrace;

    var countSentence = 'Warning encountered ' + count + ' time' + (count - 1 ? 's' : '') + '.';
    var stacktraceList = void 0;

    if (stacktraceVisible && stacktrace) {
      stacktraceList = React.createElement(
        View,
        {
          style: styles.stacktraceList,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 261
          }
        },
        stacktrace.map(function (frame, ii) {
          return React.createElement(StackRow, {
            frame: frame,
            key: ii,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 262
            }
          });
        })
      );
    }

    return React.createElement(
      View,
      {
        style: styles.inspector,
        __source: {
          fileName: _jsxFileName,
          lineNumber: 268
        }
      },
      React.createElement(
        SafeAreaView,
        {
          style: styles.safeArea,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 269
          }
        },
        React.createElement(
          View,
          {
            style: styles.inspectorCount,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 270
            }
          },
          React.createElement(
            Text,
            {
              style: styles.inspectorCountText,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 271
              }
            },
            countSentence
          ),
          React.createElement(
            TouchableHighlight,
            {
              onPress: toggleStacktrace,
              underlayColor: "transparent",
              __source: {
                fileName: _jsxFileName,
                lineNumber: 272
              }
            },
            React.createElement(
              Text,
              {
                style: styles.inspectorButtonText,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 275
                }
              },
              stacktraceVisible ? "\u25BC" : "\u25B6",
              " Stacktrace"
            )
          )
        ),
        React.createElement(
          ScrollView,
          {
            style: styles.inspectorWarning,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 280
            }
          },
          stacktraceList,
          React.createElement(
            Text,
            {
              style: styles.inspectorWarningText,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 282
              }
            },
            warning
          )
        ),
        React.createElement(
          View,
          {
            style: styles.inspectorButtons,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 284
            }
          },
          React.createElement(
            TouchableHighlight,
            {
              activeOpacity: 0.5,
              onPress: onMinimize,
              style: styles.inspectorButton,
              underlayColor: "transparent",
              __source: {
                fileName: _jsxFileName,
                lineNumber: 285
              }
            },
            React.createElement(
              Text,
              {
                style: styles.inspectorButtonText,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 290
                }
              },
              "Minimize"
            )
          ),
          React.createElement(
            TouchableHighlight,
            {
              activeOpacity: 0.5,
              onPress: onDismiss,
              style: styles.inspectorButton,
              underlayColor: "transparent",
              __source: {
                fileName: _jsxFileName,
                lineNumber: 292
              }
            },
            React.createElement(
              Text,
              {
                style: styles.inspectorButtonText,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 297
                }
              },
              "Dismiss"
            )
          ),
          React.createElement(
            TouchableHighlight,
            {
              activeOpacity: 0.5,
              onPress: onDismissAll,
              style: styles.inspectorButton,
              underlayColor: "transparent",
              __source: {
                fileName: _jsxFileName,
                lineNumber: 299
              }
            },
            React.createElement(
              Text,
              {
                style: styles.inspectorButtonText,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 304
                }
              },
              "Dismiss All"
            )
          )
        )
      )
    );
  };

  var YellowBox = function (_React$Component) {
    babelHelpers.inherits(YellowBox, _React$Component);

    function YellowBox(props, context) {
      babelHelpers.classCallCheck(this, YellowBox);

      var _this = babelHelpers.possibleConstructorReturn(this, (YellowBox.__proto__ || Object.getPrototypeOf(YellowBox)).call(this, props, context));

      _this.state = {
        inspecting: null,
        stacktraceVisible: false,
        warningMap: _warningMap
      };

      _this.dismissWarning = function (warning) {
        var _this$state = _this.state,
            inspecting = _this$state.inspecting,
            warningMap = _this$state.warningMap;

        if (warning) {
          warningMap.delete(warning);
        } else {
          warningMap.clear();
        }

        _this.setState({
          inspecting: warning && inspecting !== warning ? inspecting : null,
          warningMap: warningMap
        });
      };

      return _this;
    }

    babelHelpers.createClass(YellowBox, [{
      key: "componentDidMount",
      value: function componentDidMount() {
        var _this2 = this;

        var scheduled = null;
        this._listener = _warningEmitter.addListener('warning', function (warningMap) {
          scheduled = scheduled || setImmediate(function () {
            scheduled = null;

            _this2.setState({
              warningMap: warningMap
            });
          });
        });
      }
    }, {
      key: "componentDidUpdate",
      value: function componentDidUpdate() {
        var inspecting = this.state.inspecting;

        if (inspecting != null) {
          ensureSymbolicatedWarning(inspecting);
        }
      }
    }, {
      key: "componentWillUnmount",
      value: function componentWillUnmount() {
        if (this._listener) {
          this._listener.remove();
        }
      }
    }, {
      key: "render",
      value: function render() {
        var _this3 = this;

        if (console.disableYellowBox || this.state.warningMap.size === 0) {
          return null;
        }

        var ScrollView = _require(_dependencyMap[14], 'ScrollView');

        var View = _require(_dependencyMap[13], 'View');

        var _state = this.state,
            inspecting = _state.inspecting,
            stacktraceVisible = _state.stacktraceVisible;
        var inspector = inspecting !== null ? React.createElement(WarningInspector, {
          warningInfo: this.state.warningMap.get(inspecting),
          warning: inspecting,
          stacktraceVisible: stacktraceVisible,
          onDismiss: function onDismiss() {
            return _this3.dismissWarning(inspecting);
          },
          onDismissAll: function onDismissAll() {
            return _this3.dismissWarning(null);
          },
          onMinimize: function onMinimize() {
            return _this3.setState({
              inspecting: null
            });
          },
          toggleStacktrace: function toggleStacktrace() {
            return _this3.setState({
              stacktraceVisible: !stacktraceVisible
            });
          },
          __source: {
            fileName: _jsxFileName,
            lineNumber: 391
          }
        }) : null;
        var rows = [];
        this.state.warningMap.forEach(function (warningInfo, warning) {
          if (!isWarningIgnored(warning)) {
            rows.push(React.createElement(WarningRow, {
              key: warning,
              count: warningInfo.count,
              warning: warning,
              onPress: function onPress() {
                return _this3.setState({
                  inspecting: warning
                });
              },
              onDismiss: function onDismiss() {
                return _this3.dismissWarning(warning);
              },
              __source: {
                fileName: _jsxFileName,
                lineNumber: 408
              }
            }));
          }
        });
        var listStyle = [styles.list, {
          height: Math.min(rows.length, 4.4) * (rowGutter + rowHeight)
        }];
        return React.createElement(
          View,
          {
            style: inspector ? styles.fullScreen : listStyle,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 425
            }
          },
          React.createElement(
            ScrollView,
            {
              style: listStyle,
              scrollsToTop: false,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 426
              }
            },
            rows
          ),
          inspector
        );
      }
    }], [{
      key: "ignoreWarnings",
      value: function ignoreWarnings(warnings) {
        warnings.forEach(function (warning) {
          if (IGNORED_WARNINGS.indexOf(warning) === -1) {
            IGNORED_WARNINGS.push(warning);
          }
        });
      }
    }]);
    return YellowBox;
  }(React.Component);

  var backgroundColor = function backgroundColor(opacity) {
    return 'rgba(250, 186, 48, ' + opacity + ')';
  };

  var textColor = 'white';
  var rowGutter = 1;
  var rowHeight = 46;
  var elevation = Platform.OS === 'android' ? Number.MAX_SAFE_INTEGER : undefined;
  var styles = StyleSheet.create({
    fullScreen: {
      height: '100%',
      width: '100%',
      elevation: elevation,
      position: 'absolute'
    },
    inspector: {
      backgroundColor: backgroundColor(0.95),
      height: '100%',
      paddingTop: 5,
      elevation: elevation
    },
    inspectorButtons: {
      flexDirection: 'row'
    },
    inspectorButton: {
      flex: 1,
      paddingVertical: 22,
      backgroundColor: backgroundColor(1)
    },
    safeArea: {
      flex: 1
    },
    stacktraceList: {
      paddingBottom: 5
    },
    inspectorButtonText: {
      color: textColor,
      fontSize: 14,
      opacity: 0.8,
      textAlign: 'center'
    },
    openInEditorButton: {
      paddingTop: 5,
      paddingBottom: 5
    },
    inspectorCount: {
      padding: 15,
      paddingBottom: 0,
      flexDirection: 'row',
      justifyContent: 'space-between'
    },
    inspectorCountText: {
      color: textColor,
      fontSize: 14
    },
    inspectorWarning: {
      flex: 1,
      paddingHorizontal: 15
    },
    inspectorWarningText: {
      color: textColor,
      fontSize: 16,
      fontWeight: '600'
    },
    list: {
      backgroundColor: 'transparent',
      position: 'absolute',
      left: 0,
      right: 0,
      bottom: 0,
      elevation: elevation
    },
    listRow: {
      backgroundColor: backgroundColor(0.95),
      height: rowHeight,
      marginTop: rowGutter
    },
    listRowContent: {
      flex: 1
    },
    listRowCount: {
      color: 'rgba(255, 255, 255, 0.5)'
    },
    listRowText: {
      color: textColor,
      position: 'absolute',
      left: 0,
      top: Platform.OS === 'android' ? 5 : 7,
      marginLeft: 15,
      marginRight: 15
    }
  });
  module.exports = YellowBox;
},282,[41,32,132,283,171,102,105,277,36,30,60,185,275,173,228],"YellowBox");