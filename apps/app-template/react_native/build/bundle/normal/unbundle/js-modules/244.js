__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Lists/FlatList.js";

  var MetroListView = _require(_dependencyMap[0], 'MetroListView');

  var React = _require(_dependencyMap[1], 'React');

  var View = _require(_dependencyMap[2], 'View');

  var VirtualizedList = _require(_dependencyMap[3], 'VirtualizedList');

  var ListView = _require(_dependencyMap[4], 'ListView');

  var invariant = _require(_dependencyMap[5], 'fbjs/lib/invariant');

  var defaultProps = babelHelpers.extends({}, VirtualizedList.defaultProps, {
    numColumns: 1
  });

  var FlatList = function (_React$PureComponent) {
    babelHelpers.inherits(FlatList, _React$PureComponent);
    babelHelpers.createClass(FlatList, [{
      key: "scrollToEnd",
      value: function scrollToEnd(params) {
        if (this._listRef) {
          this._listRef.scrollToEnd(params);
        }
      }
    }, {
      key: "scrollToIndex",
      value: function scrollToIndex(params) {
        if (this._listRef) {
          this._listRef.scrollToIndex(params);
        }
      }
    }, {
      key: "scrollToItem",
      value: function scrollToItem(params) {
        if (this._listRef) {
          this._listRef.scrollToItem(params);
        }
      }
    }, {
      key: "scrollToOffset",
      value: function scrollToOffset(params) {
        if (this._listRef) {
          this._listRef.scrollToOffset(params);
        }
      }
    }, {
      key: "recordInteraction",
      value: function recordInteraction() {
        if (this._listRef) {
          this._listRef.recordInteraction();
        }
      }
    }, {
      key: "flashScrollIndicators",
      value: function flashScrollIndicators() {
        if (this._listRef) {
          this._listRef.flashScrollIndicators();
        }
      }
    }, {
      key: "getScrollResponder",
      value: function getScrollResponder() {
        if (this._listRef) {
          return this._listRef.getScrollResponder();
        }
      }
    }, {
      key: "getScrollableNode",
      value: function getScrollableNode() {
        if (this._listRef) {
          return this._listRef.getScrollableNode();
        }
      }
    }, {
      key: "setNativeProps",
      value: function setNativeProps(props) {
        if (this._listRef) {
          this._listRef.setNativeProps(props);
        }
      }
    }, {
      key: "UNSAFE_componentWillMount",
      value: function UNSAFE_componentWillMount() {
        this._checkProps(this.props);
      }
    }, {
      key: "UNSAFE_componentWillReceiveProps",
      value: function UNSAFE_componentWillReceiveProps(nextProps) {
        invariant(nextProps.numColumns === this.props.numColumns, 'Changing numColumns on the fly is not supported. Change the key prop on FlatList when ' + 'changing the number of columns to force a fresh render of the component.');
        invariant(nextProps.onViewableItemsChanged === this.props.onViewableItemsChanged, 'Changing onViewableItemsChanged on the fly is not supported');
        invariant(nextProps.viewabilityConfig === this.props.viewabilityConfig, 'Changing viewabilityConfig on the fly is not supported');
        invariant(nextProps.viewabilityConfigCallbackPairs === this.props.viewabilityConfigCallbackPairs, 'Changing viewabilityConfigCallbackPairs on the fly is not supported');

        this._checkProps(nextProps);
      }
    }]);

    function FlatList(props) {
      babelHelpers.classCallCheck(this, FlatList);

      var _this = babelHelpers.possibleConstructorReturn(this, (FlatList.__proto__ || Object.getPrototypeOf(FlatList)).call(this, props));

      _this._hasWarnedLegacy = false;
      _this._virtualizedListPairs = [];

      _this._captureRef = function (ref) {
        _this._listRef = ref;
      };

      _this._getItem = function (data, index) {
        var numColumns = _this.props.numColumns;

        if (numColumns > 1) {
          var ret = [];

          for (var kk = 0; kk < numColumns; kk++) {
            var _item = data[index * numColumns + kk];
            _item && ret.push(_item);
          }

          return ret;
        } else {
          return data[index];
        }
      };

      _this._getItemCount = function (data) {
        return data ? Math.ceil(data.length / _this.props.numColumns) : 0;
      };

      _this._keyExtractor = function (items, index) {
        var _this$props = _this.props,
            keyExtractor = _this$props.keyExtractor,
            numColumns = _this$props.numColumns;

        if (numColumns > 1) {
          invariant(Array.isArray(items), 'FlatList: Encountered internal consistency error, expected each item to consist of an ' + 'array with 1-%s columns; instead, received a single item.', numColumns);
          return items.map(function (it, kk) {
            return keyExtractor(it, index * numColumns + kk);
          }).join(':');
        } else {
          return keyExtractor(items, index);
        }
      };

      _this._renderItem = function (info) {
        var _this$props2 = _this.props,
            renderItem = _this$props2.renderItem,
            numColumns = _this$props2.numColumns,
            columnWrapperStyle = _this$props2.columnWrapperStyle;

        if (numColumns > 1) {
          var _item2 = info.item,
              _index = info.index;
          invariant(Array.isArray(_item2), 'Expected array of items with numColumns > 1');
          return React.createElement(
            View,
            {
              style: [{
                flexDirection: 'row'
              }, columnWrapperStyle],
              __source: {
                fileName: _jsxFileName,
                lineNumber: 611
              }
            },
            _item2.map(function (it, kk) {
              var element = renderItem({
                item: it,
                index: _index * numColumns + kk,
                separators: info.separators
              });
              return element && React.cloneElement(element, {
                key: kk
              });
            })
          );
        } else {
          return renderItem(info);
        }
      };

      if (_this.props.viewabilityConfigCallbackPairs) {
        _this._virtualizedListPairs = _this.props.viewabilityConfigCallbackPairs.map(function (pair) {
          return {
            viewabilityConfig: pair.viewabilityConfig,
            onViewableItemsChanged: _this._createOnViewableItemsChanged(pair.onViewableItemsChanged)
          };
        });
      } else if (_this.props.onViewableItemsChanged) {
        _this._virtualizedListPairs.push({
          viewabilityConfig: _this.props.viewabilityConfig,
          onViewableItemsChanged: _this._createOnViewableItemsChanged(_this.props.onViewableItemsChanged)
        });
      }

      return _this;
    }

    babelHelpers.createClass(FlatList, [{
      key: "_checkProps",
      value: function _checkProps(props) {
        var getItem = props.getItem,
            getItemCount = props.getItemCount,
            horizontal = props.horizontal,
            legacyImplementation = props.legacyImplementation,
            numColumns = props.numColumns,
            columnWrapperStyle = props.columnWrapperStyle,
            onViewableItemsChanged = props.onViewableItemsChanged,
            viewabilityConfigCallbackPairs = props.viewabilityConfigCallbackPairs;
        invariant(!getItem && !getItemCount, 'FlatList does not support custom data formats.');

        if (numColumns > 1) {
          invariant(!horizontal, 'numColumns does not support horizontal.');
        } else {
          invariant(!columnWrapperStyle, 'columnWrapperStyle not supported for single column lists');
        }

        if (legacyImplementation) {
          invariant(numColumns === 1, 'Legacy list does not support multiple columns.');

          if (!this._hasWarnedLegacy) {
            console.warn('FlatList: Using legacyImplementation - some features not supported and performance ' + 'may suffer');
            this._hasWarnedLegacy = true;
          }
        }

        invariant(!(onViewableItemsChanged && viewabilityConfigCallbackPairs), 'FlatList does not support setting both onViewableItemsChanged and ' + 'viewabilityConfigCallbackPairs.');
      }
    }, {
      key: "_pushMultiColumnViewable",
      value: function _pushMultiColumnViewable(arr, v) {
        var _props = this.props,
            numColumns = _props.numColumns,
            keyExtractor = _props.keyExtractor;
        v.item.forEach(function (item, ii) {
          invariant(v.index != null, 'Missing index!');
          var index = v.index * numColumns + ii;
          arr.push(babelHelpers.extends({}, v, {
            item: item,
            key: keyExtractor(item, index),
            index: index
          }));
        });
      }
    }, {
      key: "_createOnViewableItemsChanged",
      value: function _createOnViewableItemsChanged(onViewableItemsChanged) {
        var _this2 = this;

        return function (info) {
          var numColumns = _this2.props.numColumns;

          if (onViewableItemsChanged) {
            if (numColumns > 1) {
              var _changed = [];
              var _viewableItems = [];
              info.viewableItems.forEach(function (v) {
                return _this2._pushMultiColumnViewable(_viewableItems, v);
              });
              info.changed.forEach(function (v) {
                return _this2._pushMultiColumnViewable(_changed, v);
              });
              onViewableItemsChanged({
                viewableItems: _viewableItems,
                changed: _changed
              });
            } else {
              onViewableItemsChanged(info);
            }
          }
        };
      }
    }, {
      key: "render",
      value: function render() {
        if (this.props.legacyImplementation) {
          return React.createElement(MetroListView, babelHelpers.extends({}, this.props, {
            items: this.props.data,
            ref: this._captureRef,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 633
            }
          }));
        } else {
          return React.createElement(VirtualizedList, babelHelpers.extends({}, this.props, {
            renderItem: this._renderItem,
            getItem: this._getItem,
            getItemCount: this._getItemCount,
            keyExtractor: this._keyExtractor,
            ref: this._captureRef,
            viewabilityConfigCallbackPairs: this._virtualizedListPairs,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 644
            }
          }));
        }
      }
    }]);
    return FlatList;
  }(React.PureComponent);

  FlatList.defaultProps = defaultProps;
  module.exports = FlatList;
},244,[245,132,173,252,246,18],"FlatList");