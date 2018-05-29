__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Inspector/NetworkOverlay.js";

  var ListView = _require(_dependencyMap[0], 'ListView');

  var React = _require(_dependencyMap[1], 'React');

  var ScrollView = _require(_dependencyMap[2], 'ScrollView');

  var StyleSheet = _require(_dependencyMap[3], 'StyleSheet');

  var Text = _require(_dependencyMap[4], 'Text');

  var TouchableHighlight = _require(_dependencyMap[5], 'TouchableHighlight');

  var View = _require(_dependencyMap[6], 'View');

  var WebSocketInterceptor = _require(_dependencyMap[7], 'WebSocketInterceptor');

  var XHRInterceptor = _require(_dependencyMap[8], 'XHRInterceptor');

  var LISTVIEW_CELL_HEIGHT = 15;
  var SEPARATOR_THICKNESS = 2;
  var nextXHRId = 0;

  var NetworkOverlay = function (_React$Component) {
    babelHelpers.inherits(NetworkOverlay, _React$Component);

    function NetworkOverlay(props) {
      babelHelpers.classCallCheck(this, NetworkOverlay);

      var _this = babelHelpers.possibleConstructorReturn(this, (NetworkOverlay.__proto__ || Object.getPrototypeOf(NetworkOverlay)).call(this, props));

      _this._requests = [];
      _this._detailViewItems = [];
      _this._listViewDataSource = new ListView.DataSource({
        rowHasChanged: function rowHasChanged(r1, r2) {
          return r1 !== r2;
        }
      });
      _this.state = {
        dataSource: _this._listViewDataSource.cloneWithRows([]),
        newDetailInfo: false,
        detailRowID: null
      };
      _this._listViewHighlighted = false;
      _this._listViewHeight = 0;
      _this._captureRequestListView = _this._captureRequestListView.bind(_this);
      _this._captureDetailScrollView = _this._captureDetailScrollView.bind(_this);
      _this._listViewOnLayout = _this._listViewOnLayout.bind(_this);
      _this._renderRow = _this._renderRow.bind(_this);
      _this._closeButtonClicked = _this._closeButtonClicked.bind(_this);
      _this._socketIdMap = {};
      _this._xhrIdMap = {};
      return _this;
    }

    babelHelpers.createClass(NetworkOverlay, [{
      key: "_enableXHRInterception",
      value: function _enableXHRInterception() {
        var _this2 = this;

        if (XHRInterceptor.isInterceptorEnabled()) {
          return;
        }

        XHRInterceptor.setOpenCallback(function (method, url, xhr) {
          xhr._index = nextXHRId++;
          var xhrIndex = _this2._requests.length;
          _this2._xhrIdMap[xhr._index] = xhrIndex;
          var _xhr = {
            'type': 'XMLHttpRequest',
            'method': method,
            'url': url
          };

          _this2._requests.push(_xhr);

          _this2._detailViewItems.push([]);

          _this2._genDetailViewItem(xhrIndex);

          _this2.setState({
            dataSource: _this2._listViewDataSource.cloneWithRows(_this2._requests)
          }, _this2._scrollToBottom());
        });
        XHRInterceptor.setRequestHeaderCallback(function (header, value, xhr) {
          var xhrIndex = _this2._getRequestIndexByXHRID(xhr._index);

          if (xhrIndex === -1) {
            return;
          }

          var networkInfo = _this2._requests[xhrIndex];

          if (!networkInfo.requestHeaders) {
            networkInfo.requestHeaders = {};
          }

          networkInfo.requestHeaders[header] = value;

          _this2._genDetailViewItem(xhrIndex);
        });
        XHRInterceptor.setSendCallback(function (data, xhr) {
          var xhrIndex = _this2._getRequestIndexByXHRID(xhr._index);

          if (xhrIndex === -1) {
            return;
          }

          _this2._requests[xhrIndex].dataSent = data;

          _this2._genDetailViewItem(xhrIndex);
        });
        XHRInterceptor.setHeaderReceivedCallback(function (type, size, responseHeaders, xhr) {
          var xhrIndex = _this2._getRequestIndexByXHRID(xhr._index);

          if (xhrIndex === -1) {
            return;
          }

          var networkInfo = _this2._requests[xhrIndex];
          networkInfo.responseContentType = type;
          networkInfo.responseSize = size;
          networkInfo.responseHeaders = responseHeaders;

          _this2._genDetailViewItem(xhrIndex);
        });
        XHRInterceptor.setResponseCallback(function (status, timeout, response, responseURL, responseType, xhr) {
          var xhrIndex = _this2._getRequestIndexByXHRID(xhr._index);

          if (xhrIndex === -1) {
            return;
          }

          var networkInfo = _this2._requests[xhrIndex];
          networkInfo.status = status;
          networkInfo.timeout = timeout;
          networkInfo.response = response;
          networkInfo.responseURL = responseURL;
          networkInfo.responseType = responseType;

          _this2._genDetailViewItem(xhrIndex);
        });
        XHRInterceptor.enableInterception();
      }
    }, {
      key: "_enableWebSocketInterception",
      value: function _enableWebSocketInterception() {
        var _this3 = this;

        if (WebSocketInterceptor.isInterceptorEnabled()) {
          return;
        }

        WebSocketInterceptor.setConnectCallback(function (url, protocols, options, socketId) {
          var socketIndex = _this3._requests.length;
          _this3._socketIdMap[socketId] = socketIndex;
          var _webSocket = {
            'type': 'WebSocket',
            'url': url,
            'protocols': protocols
          };

          _this3._requests.push(_webSocket);

          _this3._detailViewItems.push([]);

          _this3._genDetailViewItem(socketIndex);

          _this3.setState({
            dataSource: _this3._listViewDataSource.cloneWithRows(_this3._requests)
          }, _this3._scrollToBottom());
        });
        WebSocketInterceptor.setCloseCallback(function (statusCode, closeReason, socketId) {
          var socketIndex = _this3._socketIdMap[socketId];

          if (socketIndex === undefined) {
            return;
          }

          if (statusCode !== null && closeReason !== null) {
            _this3._requests[socketIndex].status = statusCode;
            _this3._requests[socketIndex].closeReason = closeReason;
          }

          _this3._genDetailViewItem(socketIndex);
        });
        WebSocketInterceptor.setSendCallback(function (data, socketId) {
          var socketIndex = _this3._socketIdMap[socketId];

          if (socketIndex === undefined) {
            return;
          }

          if (!_this3._requests[socketIndex].messages) {
            _this3._requests[socketIndex].messages = '';
          }

          _this3._requests[socketIndex].messages += 'Sent: ' + JSON.stringify(data) + '\n';

          _this3._genDetailViewItem(socketIndex);
        });
        WebSocketInterceptor.setOnMessageCallback(function (socketId, message) {
          var socketIndex = _this3._socketIdMap[socketId];

          if (socketIndex === undefined) {
            return;
          }

          if (!_this3._requests[socketIndex].messages) {
            _this3._requests[socketIndex].messages = '';
          }

          _this3._requests[socketIndex].messages += 'Received: ' + JSON.stringify(message) + '\n';

          _this3._genDetailViewItem(socketIndex);
        });
        WebSocketInterceptor.setOnCloseCallback(function (socketId, message) {
          var socketIndex = _this3._socketIdMap[socketId];

          if (socketIndex === undefined) {
            return;
          }

          _this3._requests[socketIndex].serverClose = message;

          _this3._genDetailViewItem(socketIndex);
        });
        WebSocketInterceptor.setOnErrorCallback(function (socketId, message) {
          var socketIndex = _this3._socketIdMap[socketId];

          if (socketIndex === undefined) {
            return;
          }

          _this3._requests[socketIndex].serverError = message;

          _this3._genDetailViewItem(socketIndex);
        });
        WebSocketInterceptor.enableInterception();
      }
    }, {
      key: "componentDidMount",
      value: function componentDidMount() {
        this._enableXHRInterception();

        this._enableWebSocketInterception();
      }
    }, {
      key: "componentWillUnmount",
      value: function componentWillUnmount() {
        XHRInterceptor.disableInterception();
        WebSocketInterceptor.disableInterception();
      }
    }, {
      key: "_renderRow",
      value: function _renderRow(rowData, sectionID, rowID, highlightRow) {
        var _this4 = this;

        var urlCellViewStyle = styles.urlEvenCellView;
        var methodCellViewStyle = styles.methodEvenCellView;

        if (rowID % 2 === 1) {
          urlCellViewStyle = styles.urlOddCellView;
          methodCellViewStyle = styles.methodOddCellView;
        }

        return React.createElement(
          TouchableHighlight,
          {
            onPress: function onPress() {
              _this4._pressRow(rowID);

              highlightRow(sectionID, rowID);
            },
            __source: {
              fileName: _jsxFileName,
              lineNumber: 298
            }
          },
          React.createElement(
            View,
            {
              __source: {
                fileName: _jsxFileName,
                lineNumber: 302
              }
            },
            React.createElement(
              View,
              {
                style: styles.tableRow,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 303
                }
              },
              React.createElement(
                View,
                {
                  style: urlCellViewStyle,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 304
                  }
                },
                React.createElement(
                  Text,
                  {
                    style: styles.cellText,
                    numberOfLines: 1,
                    __source: {
                      fileName: _jsxFileName,
                      lineNumber: 305
                    }
                  },
                  rowData.url
                )
              ),
              React.createElement(
                View,
                {
                  style: methodCellViewStyle,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 309
                  }
                },
                React.createElement(
                  Text,
                  {
                    style: styles.cellText,
                    numberOfLines: 1,
                    __source: {
                      fileName: _jsxFileName,
                      lineNumber: 310
                    }
                  },
                  this._getTypeShortName(rowData.type)
                )
              )
            )
          )
        );
      }
    }, {
      key: "_renderSeperator",
      value: function _renderSeperator(sectionID, rowID, adjacentRowHighlighted) {
        return React.createElement(View, {
          key: sectionID + "-" + rowID,
          style: {
            height: adjacentRowHighlighted ? SEPARATOR_THICKNESS : 0,
            backgroundColor: adjacentRowHighlighted ? '#3B5998' : '#CCCCCC'
          },
          __source: {
            fileName: _jsxFileName,
            lineNumber: 325
          }
        });
      }
    }, {
      key: "_scrollToBottom",
      value: function _scrollToBottom() {
        if (this._listView) {
          var scrollResponder = this._listView.getScrollResponder();

          if (scrollResponder) {
            var scrollY = Math.max(this._requests.length * LISTVIEW_CELL_HEIGHT + (this._listViewHighlighted ? 2 * SEPARATOR_THICKNESS : 0) - this._listViewHeight, 0);
            scrollResponder.scrollResponderScrollTo({
              x: 0,
              y: scrollY,
              animated: true
            });
          }
        }
      }
    }, {
      key: "_captureRequestListView",
      value: function _captureRequestListView(listRef) {
        this._listView = listRef;
      }
    }, {
      key: "_listViewOnLayout",
      value: function _listViewOnLayout(event) {
        var height = event.nativeEvent.layout.height;
        this._listViewHeight = height;
      }
    }, {
      key: "_pressRow",
      value: function _pressRow(rowID) {
        this._listViewHighlighted = true;
        this.setState({
          detailRowID: rowID
        }, this._scrollToTop());
      }
    }, {
      key: "_scrollToTop",
      value: function _scrollToTop() {
        if (this._scrollView) {
          this._scrollView.scrollTo({
            y: 0,
            animated: false
          });
        }
      }
    }, {
      key: "_captureDetailScrollView",
      value: function _captureDetailScrollView(scrollRef) {
        this._scrollView = scrollRef;
      }
    }, {
      key: "_closeButtonClicked",
      value: function _closeButtonClicked() {
        this.setState({
          detailRowID: null
        });
      }
    }, {
      key: "_getStringByValue",
      value: function _getStringByValue(value) {
        if (value === undefined) {
          return 'undefined';
        }

        if (typeof value === 'object') {
          return JSON.stringify(value);
        }

        if (typeof value === 'string' && value.length > 500) {
          return String(value).substr(0, 500).concat('\n***TRUNCATED TO 500 CHARACTERS***');
        }

        return value;
      }
    }, {
      key: "_getRequestIndexByXHRID",
      value: function _getRequestIndexByXHRID(index) {
        if (index === undefined) {
          return -1;
        }

        var xhrIndex = this._xhrIdMap[index];

        if (xhrIndex === undefined) {
          return -1;
        } else {
          return xhrIndex;
        }
      }
    }, {
      key: "_getTypeShortName",
      value: function _getTypeShortName(type) {
        if (type === 'XMLHttpRequest') {
          return 'XHR';
        } else if (type === 'WebSocket') {
          return 'WS';
        }

        return '';
      }
    }, {
      key: "_genDetailViewItem",
      value: function _genDetailViewItem(index) {
        this._detailViewItems[index] = [];
        var detailViewItem = this._detailViewItems[index];
        var requestItem = this._requests[index];

        for (var _key in requestItem) {
          detailViewItem.push(React.createElement(
            View,
            {
              style: styles.detailViewRow,
              key: _key,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 440
              }
            },
            React.createElement(
              Text,
              {
                style: [styles.detailViewText, styles.detailKeyCellView],
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 441
                }
              },
              _key
            ),
            React.createElement(
              Text,
              {
                style: [styles.detailViewText, styles.detailValueCellView],
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 444
                }
              },
              this._getStringByValue(requestItem[_key])
            )
          ));
        }

        if (this.state.detailRowID != null && Number(this.state.detailRowID) === index) {
          this.setState({
            newDetailInfo: true
          });
        }
      }
    }, {
      key: "render",
      value: function render() {
        return React.createElement(
          View,
          {
            style: styles.container,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 459
            }
          },
          this.state.detailRowID != null && React.createElement(
            TouchableHighlight,
            {
              style: styles.closeButton,
              onPress: this._closeButtonClicked,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 461
              }
            },
            React.createElement(
              View,
              {
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 464
                }
              },
              React.createElement(
                Text,
                {
                  style: styles.clostButtonText,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 465
                  }
                },
                "v"
              )
            )
          ),
          this.state.detailRowID != null && React.createElement(
            ScrollView,
            {
              style: styles.detailScrollView,
              ref: this._captureDetailScrollView,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 469
              }
            },
            this._detailViewItems[this.state.detailRowID]
          ),
          React.createElement(
            View,
            {
              style: styles.listViewTitle,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 474
              }
            },
            this._requests.length > 0 && React.createElement(
              View,
              {
                style: styles.tableRow,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 476
                }
              },
              React.createElement(
                View,
                {
                  style: styles.urlTitleCellView,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 477
                  }
                },
                React.createElement(
                  Text,
                  {
                    style: styles.cellText,
                    numberOfLines: 1,
                    __source: {
                      fileName: _jsxFileName,
                      lineNumber: 478
                    }
                  },
                  "URL"
                )
              ),
              React.createElement(
                View,
                {
                  style: styles.methodTitleCellView,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 480
                  }
                },
                React.createElement(
                  Text,
                  {
                    style: styles.cellText,
                    numberOfLines: 1,
                    __source: {
                      fileName: _jsxFileName,
                      lineNumber: 481
                    }
                  },
                  "Type"
                )
              )
            )
          ),
          React.createElement(ListView, {
            style: styles.listView,
            ref: this._captureRequestListView,
            dataSource: this.state.dataSource,
            renderRow: this._renderRow,
            enableEmptySections: true,
            renderSeparator: this._renderSeperator,
            onLayout: this._listViewOnLayout,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 485
            }
          })
        );
      }
    }]);
    return NetworkOverlay;
  }(React.Component);

  var styles = StyleSheet.create({
    container: {
      paddingTop: 10,
      paddingBottom: 10,
      paddingLeft: 5,
      paddingRight: 5
    },
    listViewTitle: {
      height: 20
    },
    listView: {
      flex: 1,
      height: 60
    },
    tableRow: {
      flexDirection: 'row',
      flex: 1
    },
    cellText: {
      color: 'white',
      fontSize: 12
    },
    methodTitleCellView: {
      height: 18,
      borderColor: '#DCD7CD',
      borderTopWidth: 1,
      borderBottomWidth: 1,
      borderRightWidth: 1,
      alignItems: 'center',
      justifyContent: 'center',
      backgroundColor: '#444',
      flex: 1
    },
    urlTitleCellView: {
      height: 18,
      borderColor: '#DCD7CD',
      borderTopWidth: 1,
      borderBottomWidth: 1,
      borderLeftWidth: 1,
      borderRightWidth: 1,
      justifyContent: 'center',
      backgroundColor: '#444',
      flex: 5,
      paddingLeft: 3
    },
    methodOddCellView: {
      height: 15,
      borderColor: '#DCD7CD',
      borderRightWidth: 1,
      alignItems: 'center',
      justifyContent: 'center',
      backgroundColor: '#000',
      flex: 1
    },
    urlOddCellView: {
      height: 15,
      borderColor: '#DCD7CD',
      borderLeftWidth: 1,
      borderRightWidth: 1,
      justifyContent: 'center',
      backgroundColor: '#000',
      flex: 5,
      paddingLeft: 3
    },
    methodEvenCellView: {
      height: 15,
      borderColor: '#DCD7CD',
      borderRightWidth: 1,
      alignItems: 'center',
      justifyContent: 'center',
      backgroundColor: '#888',
      flex: 1
    },
    urlEvenCellView: {
      height: 15,
      borderColor: '#DCD7CD',
      borderLeftWidth: 1,
      borderRightWidth: 1,
      justifyContent: 'center',
      backgroundColor: '#888',
      flex: 5,
      paddingLeft: 3
    },
    detailScrollView: {
      flex: 1,
      height: 180,
      marginTop: 5,
      marginBottom: 5
    },
    detailKeyCellView: {
      flex: 1.3
    },
    detailValueCellView: {
      flex: 2
    },
    detailViewRow: {
      flexDirection: 'row',
      paddingHorizontal: 3
    },
    detailViewText: {
      color: 'white',
      fontSize: 11
    },
    clostButtonText: {
      color: 'white',
      fontSize: 10
    },
    closeButton: {
      marginTop: 5,
      backgroundColor: '#888',
      justifyContent: 'center',
      alignItems: 'center'
    }
  });
  module.exports = NetworkOverlay;
},"d22216a4351a4224118d463bcb0726f1",["f7953c54cdefedbfed49ad61cce46031","e6db4f0efed6b72f641ef0ffed29569f","aa8514022050149acc8c46c0b18dc75a","d31e8c1a3f9844becc88973ecddac872","c03ca8878a60b3cdaf32e10931ff258d","0d3968e3413084f66ce651afdba962c2","30a3b04291b6e1f01b778ff31271ccc5","7361c13d8fc22924c7b3ce34396a013e","2bd8a70ec5d8820effbbea25d4a80ad1"],"NetworkOverlay");