__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Lists/VirtualizedList.js";

  var Batchinator = _require2(_dependencyMap[0], 'Batchinator');

  var FillRateHelper = _require2(_dependencyMap[1], 'FillRateHelper');

  var PropTypes = _require2(_dependencyMap[2], 'prop-types');

  var React = _require2(_dependencyMap[3], 'React');

  var ReactNative = _require2(_dependencyMap[4], 'ReactNative');

  var RefreshControl = _require2(_dependencyMap[5], 'RefreshControl');

  var ScrollView = _require2(_dependencyMap[6], 'ScrollView');

  var StyleSheet = _require2(_dependencyMap[7], 'StyleSheet');

  var UIManager = _require2(_dependencyMap[8], 'UIManager');

  var View = _require2(_dependencyMap[9], 'View');

  var ViewabilityHelper = _require2(_dependencyMap[10], 'ViewabilityHelper');

  var flattenStyle = _require2(_dependencyMap[11], 'flattenStyle');

  var infoLog = _require2(_dependencyMap[12], 'infoLog');

  var invariant = _require2(_dependencyMap[13], 'fbjs/lib/invariant');

  var warning = _require2(_dependencyMap[14], 'fbjs/lib/warning');

  var _require = _require2(_dependencyMap[15], 'VirtualizeUtils'),
      computeWindowedRenderLimits = _require.computeWindowedRenderLimits;

  var _usedIndexForKey = false;

  var VirtualizedList = function (_React$PureComponent) {
    babelHelpers.inherits(VirtualizedList, _React$PureComponent);
    babelHelpers.createClass(VirtualizedList, [{
      key: "scrollToEnd",
      value: function scrollToEnd(params) {
        var animated = params ? params.animated : true;
        var veryLast = this.props.getItemCount(this.props.data) - 1;

        var frame = this._getFrameMetricsApprox(veryLast);

        var offset = Math.max(0, frame.offset + frame.length + this._footerLength - this._scrollMetrics.visibleLength);

        this._scrollRef.scrollTo(this.props.horizontal ? {
          x: offset,
          animated: animated
        } : {
          y: offset,
          animated: animated
        });
      }
    }, {
      key: "scrollToIndex",
      value: function scrollToIndex(params) {
        var _props = this.props,
            data = _props.data,
            horizontal = _props.horizontal,
            getItemCount = _props.getItemCount,
            getItemLayout = _props.getItemLayout,
            onScrollToIndexFailed = _props.onScrollToIndexFailed;
        var animated = params.animated,
            index = params.index,
            viewOffset = params.viewOffset,
            viewPosition = params.viewPosition;
        invariant(index >= 0 && index < getItemCount(data), "scrollToIndex out of range: " + index + " vs " + (getItemCount(data) - 1));

        if (!getItemLayout && index > this._highestMeasuredFrameIndex) {
          invariant(!!onScrollToIndexFailed, 'scrollToIndex should be used in conjunction with getItemLayout or onScrollToIndexFailed, ' + 'otherwise there is no way to know the location of offscreen indices or handle failures.');
          onScrollToIndexFailed({
            averageItemLength: this._averageCellLength,
            highestMeasuredFrameIndex: this._highestMeasuredFrameIndex,
            index: index
          });
          return;
        }

        var frame = this._getFrameMetricsApprox(index);

        var offset = Math.max(0, frame.offset - (viewPosition || 0) * (this._scrollMetrics.visibleLength - frame.length)) - (viewOffset || 0);

        this._scrollRef.scrollTo(horizontal ? {
          x: offset,
          animated: animated
        } : {
          y: offset,
          animated: animated
        });
      }
    }, {
      key: "scrollToItem",
      value: function scrollToItem(params) {
        var item = params.item;
        var _props2 = this.props,
            data = _props2.data,
            getItem = _props2.getItem,
            getItemCount = _props2.getItemCount;
        var itemCount = getItemCount(data);

        for (var _index = 0; _index < itemCount; _index++) {
          if (getItem(data, _index) === item) {
            this.scrollToIndex(babelHelpers.extends({}, params, {
              index: _index
            }));
            break;
          }
        }
      }
    }, {
      key: "scrollToOffset",
      value: function scrollToOffset(params) {
        var animated = params.animated,
            offset = params.offset;

        this._scrollRef.scrollTo(this.props.horizontal ? {
          x: offset,
          animated: animated
        } : {
          y: offset,
          animated: animated
        });
      }
    }, {
      key: "recordInteraction",
      value: function recordInteraction() {
        this._nestedChildLists.forEach(function (childList) {
          childList.ref && childList.ref.recordInteraction();
        });

        this._viewabilityTuples.forEach(function (t) {
          t.viewabilityHelper.recordInteraction();
        });

        this._updateViewableItems(this.props.data);
      }
    }, {
      key: "flashScrollIndicators",
      value: function flashScrollIndicators() {
        this._scrollRef.flashScrollIndicators();
      }
    }, {
      key: "getScrollResponder",
      value: function getScrollResponder() {
        if (this._scrollRef && this._scrollRef.getScrollResponder) {
          return this._scrollRef.getScrollResponder();
        }
      }
    }, {
      key: "getScrollableNode",
      value: function getScrollableNode() {
        if (this._scrollRef && this._scrollRef.getScrollableNode) {
          return this._scrollRef.getScrollableNode();
        } else {
          return ReactNative.findNodeHandle(this._scrollRef);
        }
      }
    }, {
      key: "setNativeProps",
      value: function setNativeProps(props) {
        if (this._scrollRef) {
          this._scrollRef.setNativeProps(props);
        }
      }
    }, {
      key: "getChildContext",
      value: function getChildContext() {
        return {
          virtualizedList: {
            getScrollMetrics: this._getScrollMetrics,
            horizontal: this.props.horizontal,
            getOutermostParentListRef: this._getOutermostParentListRef,
            getNestedChildState: this._getNestedChildState,
            registerAsNestedChild: this._registerAsNestedChild,
            unregisterAsNestedChild: this._unregisterAsNestedChild
          }
        };
      }
    }, {
      key: "_getCellKey",
      value: function _getCellKey() {
        return this.context.virtualizedCell && this.context.virtualizedCell.cellKey || 'rootList';
      }
    }, {
      key: "hasMore",
      value: function hasMore() {
        return this._hasMore;
      }
    }]);

    function VirtualizedList(props, context) {
      babelHelpers.classCallCheck(this, VirtualizedList);

      var _this = babelHelpers.possibleConstructorReturn(this, (VirtualizedList.__proto__ || Object.getPrototypeOf(VirtualizedList)).call(this, props, context));

      _initialiseProps.call(_this);

      invariant(!props.onScroll || !props.onScroll.__isNative, 'Components based on VirtualizedList must be wrapped with Animated.createAnimatedComponent ' + 'to support native onScroll events with useNativeDriver');
      invariant(props.windowSize > 0, 'VirtualizedList: The windowSize prop must be present and set to a value greater than 0.');
      _this._fillRateHelper = new FillRateHelper(_this._getFrameMetrics);
      _this._updateCellsToRenderBatcher = new Batchinator(_this._updateCellsToRender, _this.props.updateCellsBatchingPeriod);

      if (_this.props.viewabilityConfigCallbackPairs) {
        _this._viewabilityTuples = _this.props.viewabilityConfigCallbackPairs.map(function (pair) {
          return {
            viewabilityHelper: new ViewabilityHelper(pair.viewabilityConfig),
            onViewableItemsChanged: pair.onViewableItemsChanged
          };
        });
      } else if (_this.props.onViewableItemsChanged) {
        _this._viewabilityTuples.push({
          viewabilityHelper: new ViewabilityHelper(_this.props.viewabilityConfig),
          onViewableItemsChanged: _this.props.onViewableItemsChanged
        });
      }

      var initialState = {
        first: _this.props.initialScrollIndex || 0,
        last: Math.min(_this.props.getItemCount(_this.props.data), (_this.props.initialScrollIndex || 0) + _this.props.initialNumToRender) - 1
      };

      if (_this._isNestedWithSameOrientation()) {
        var storedState = _this.context.virtualizedList.getNestedChildState(_this.props.listKey || _this._getCellKey());

        if (storedState) {
          initialState = storedState;
          _this.state = storedState;
          _this._frames = storedState.frames;
        }
      }

      _this.state = initialState;
      return _this;
    }

    babelHelpers.createClass(VirtualizedList, [{
      key: "componentDidMount",
      value: function componentDidMount() {
        if (this._isNestedWithSameOrientation()) {
          this.context.virtualizedList.registerAsNestedChild({
            cellKey: this._getCellKey(),
            key: this.props.listKey || this._getCellKey(),
            ref: this
          });
        }
      }
    }, {
      key: "componentWillUnmount",
      value: function componentWillUnmount() {
        if (this._isNestedWithSameOrientation()) {
          this.context.virtualizedList.unregisterAsNestedChild({
            key: this.props.listKey || this._getCellKey(),
            state: {
              first: this.state.first,
              last: this.state.last,
              frames: this._frames
            }
          });
        }

        this._updateViewableItems(null);

        this._updateCellsToRenderBatcher.dispose({
          abort: true
        });

        this._viewabilityTuples.forEach(function (tuple) {
          tuple.viewabilityHelper.dispose();
        });

        this._fillRateHelper.deactivateAndFlush();
      }
    }, {
      key: "_pushCells",
      value: function _pushCells(cells, stickyHeaderIndices, stickyIndicesFromProps, first, last, inversionStyle) {
        var _this2 = this;

        var _props3 = this.props,
            CellRendererComponent = _props3.CellRendererComponent,
            ItemSeparatorComponent = _props3.ItemSeparatorComponent,
            data = _props3.data,
            getItem = _props3.getItem,
            getItemCount = _props3.getItemCount,
            horizontal = _props3.horizontal,
            keyExtractor = _props3.keyExtractor;
        var stickyOffset = this.props.ListHeaderComponent ? 1 : 0;
        var end = getItemCount(data) - 1;
        var prevCellKey = void 0;
        last = Math.min(end, last);

        var _loop = function _loop(ii) {
          var item = getItem(data, ii);
          var key = keyExtractor(item, ii);

          _this2._indicesToKeys.set(ii, key);

          if (stickyIndicesFromProps.has(ii + stickyOffset)) {
            stickyHeaderIndices.push(cells.length);
          }

          cells.push(React.createElement(CellRenderer, {
            CellRendererComponent: CellRendererComponent,
            ItemSeparatorComponent: ii < end ? ItemSeparatorComponent : undefined,
            cellKey: key,
            fillRateHelper: _this2._fillRateHelper,
            horizontal: horizontal,
            index: ii,
            inversionStyle: inversionStyle,
            item: item,
            key: key,
            prevCellKey: prevCellKey,
            onUpdateSeparators: _this2._onUpdateSeparators,
            onLayout: function onLayout(e) {
              return _this2._onCellLayout(e, key, ii);
            },
            onUnmount: _this2._onCellUnmount,
            parentProps: _this2.props,
            ref: function (_ref) {
              function ref(_x) {
                return _ref.apply(this, arguments);
              }

              ref.toString = function () {
                return _ref.toString();
              };

              return ref;
            }(function (ref) {
              _this2._cellRefs[key] = ref;
            }),
            __source: {
              fileName: _jsxFileName,
              lineNumber: 670
            }
          }));
          prevCellKey = key;
        };

        for (var ii = first; ii <= last; ii++) {
          _loop(ii);
        }
      }
    }, {
      key: "_isVirtualizationDisabled",
      value: function _isVirtualizationDisabled() {
        return this.props.disableVirtualization;
      }
    }, {
      key: "_isNestedWithSameOrientation",
      value: function _isNestedWithSameOrientation() {
        var nestedContext = this.context.virtualizedList;
        return !!(nestedContext && !!nestedContext.horizontal === !!this.props.horizontal);
      }
    }, {
      key: "render",
      value: function render() {
        if (__DEV__) {
          var flatStyles = flattenStyle(this.props.contentContainerStyle);
          warning(flatStyles == null || flatStyles.flexWrap !== 'wrap', '`flexWrap: `wrap`` is not supported with the `VirtualizedList` components.' + 'Consider using `numColumns` with `FlatList` instead.');
        }

        var _props4 = this.props,
            ListEmptyComponent = _props4.ListEmptyComponent,
            ListFooterComponent = _props4.ListFooterComponent,
            ListHeaderComponent = _props4.ListHeaderComponent;
        var _props5 = this.props,
            data = _props5.data,
            horizontal = _props5.horizontal;

        var isVirtualizationDisabled = this._isVirtualizationDisabled();

        var inversionStyle = this.props.inverted ? this.props.horizontal ? styles.horizontallyInverted : styles.verticallyInverted : null;
        var cells = [];
        var stickyIndicesFromProps = new Set(this.props.stickyHeaderIndices);
        var stickyHeaderIndices = [];

        if (ListHeaderComponent) {
          if (stickyIndicesFromProps.has(0)) {
            stickyHeaderIndices.push(0);
          }

          var element = React.isValidElement(ListHeaderComponent) ? ListHeaderComponent : React.createElement(ListHeaderComponent, {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 744
            }
          });
          cells.push(React.createElement(
            VirtualizedCellWrapper,
            {
              cellKey: this._getCellKey() + '-header',
              key: "$header",
              __source: {
                fileName: _jsxFileName,
                lineNumber: 747
              }
            },
            React.createElement(
              View,
              {
                onLayout: this._onLayoutHeader,
                style: inversionStyle,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 750
                }
              },
              element
            )
          ));
        }

        var itemCount = this.props.getItemCount(data);

        if (itemCount > 0) {
          _usedIndexForKey = false;
          var spacerKey = !horizontal ? 'height' : 'width';
          var lastInitialIndex = this.props.initialScrollIndex ? -1 : this.props.initialNumToRender - 1;
          var _state = this.state,
              _first = _state.first,
              _last = _state.last;

          this._pushCells(cells, stickyHeaderIndices, stickyIndicesFromProps, 0, lastInitialIndex, inversionStyle);

          var firstAfterInitial = Math.max(lastInitialIndex + 1, _first);

          if (!isVirtualizationDisabled && _first > lastInitialIndex + 1) {
            var insertedStickySpacer = false;

            if (stickyIndicesFromProps.size > 0) {
              var stickyOffset = ListHeaderComponent ? 1 : 0;

              for (var ii = firstAfterInitial - 1; ii > lastInitialIndex; ii--) {
                if (stickyIndicesFromProps.has(ii + stickyOffset)) {
                  var initBlock = this._getFrameMetricsApprox(lastInitialIndex);

                  var stickyBlock = this._getFrameMetricsApprox(ii);

                  var leadSpace = stickyBlock.offset - (initBlock.offset + initBlock.length);
                  cells.push(React.createElement(View, {
                    key: "$sticky_lead",
                    style: babelHelpers.defineProperty({}, spacerKey, leadSpace),
                    __source: {
                      fileName: _jsxFileName,
                      lineNumber: 789
                    }
                  }));

                  this._pushCells(cells, stickyHeaderIndices, stickyIndicesFromProps, ii, ii, inversionStyle);

                  var trailSpace = this._getFrameMetricsApprox(_first).offset - (stickyBlock.offset + stickyBlock.length);
                  cells.push(React.createElement(View, {
                    key: "$sticky_trail",
                    style: babelHelpers.defineProperty({}, spacerKey, trailSpace),
                    __source: {
                      fileName: _jsxFileName,
                      lineNumber: 803
                    }
                  }));
                  insertedStickySpacer = true;
                  break;
                }
              }
            }

            if (!insertedStickySpacer) {
              var _initBlock = this._getFrameMetricsApprox(lastInitialIndex);

              var firstSpace = this._getFrameMetricsApprox(_first).offset - (_initBlock.offset + _initBlock.length);

              cells.push(React.createElement(View, {
                key: "$lead_spacer",
                style: babelHelpers.defineProperty({}, spacerKey, firstSpace),
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 816
                }
              }));
            }
          }

          this._pushCells(cells, stickyHeaderIndices, stickyIndicesFromProps, firstAfterInitial, _last, inversionStyle);

          if (!this._hasWarned.keys && _usedIndexForKey) {
            console.warn('VirtualizedList: missing keys for items, make sure to specify a key property on each ' + 'item or provide a custom keyExtractor.');
            this._hasWarned.keys = true;
          }

          if (!isVirtualizationDisabled && _last < itemCount - 1) {
            var lastFrame = this._getFrameMetricsApprox(_last);

            var end = this.props.getItemLayout ? itemCount - 1 : Math.min(itemCount - 1, this._highestMeasuredFrameIndex);

            var endFrame = this._getFrameMetricsApprox(end);

            var tailSpacerLength = endFrame.offset + endFrame.length - (lastFrame.offset + lastFrame.length);
            cells.push(React.createElement(View, {
              key: "$tail_spacer",
              style: babelHelpers.defineProperty({}, spacerKey, tailSpacerLength),
              __source: {
                fileName: _jsxFileName,
                lineNumber: 849
              }
            }));
          }
        } else if (ListEmptyComponent) {
          var _element = React.isValidElement(ListEmptyComponent) ? ListEmptyComponent : React.createElement(ListEmptyComponent, {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 857
            }
          });

          cells.push(React.createElement(
            View,
            {
              key: "$empty",
              onLayout: this._onLayoutEmpty,
              style: inversionStyle,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 860
              }
            },
            _element
          ));
        }

        if (ListFooterComponent) {
          var _element2 = React.isValidElement(ListFooterComponent) ? ListFooterComponent : React.createElement(ListFooterComponent, {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 877
            }
          });

          cells.push(React.createElement(
            VirtualizedCellWrapper,
            {
              cellKey: this._getCellKey() + '-footer',
              key: "$footer",
              __source: {
                fileName: _jsxFileName,
                lineNumber: 880
              }
            },
            React.createElement(
              View,
              {
                onLayout: this._onLayoutFooter,
                style: inversionStyle,
                __source: {
                  fileName: _jsxFileName,
                  lineNumber: 883
                }
              },
              _element2
            )
          ));
        }

        var scrollProps = babelHelpers.extends({}, this.props, {
          onContentSizeChange: this._onContentSizeChange,
          onLayout: this._onLayout,
          onScroll: this._onScroll,
          onScrollBeginDrag: this._onScrollBeginDrag,
          onScrollEndDrag: this._onScrollEndDrag,
          onMomentumScrollEnd: this._onMomentumScrollEnd,
          scrollEventThrottle: this.props.scrollEventThrottle,
          invertStickyHeaders: this.props.invertStickyHeaders !== undefined ? this.props.invertStickyHeaders : this.props.inverted,
          stickyHeaderIndices: stickyHeaderIndices
        });

        if (inversionStyle) {
          scrollProps.style = [inversionStyle, this.props.style];
        }

        this._hasMore = this.state.last < this.props.getItemCount(this.props.data) - 1;
        var ret = React.cloneElement((this.props.renderScrollComponent || this._defaultRenderScrollComponent)(scrollProps), {
          ref: this._captureScrollRef
        }, cells);

        if (this.props.debug) {
          return React.createElement(
            View,
            {
              style: {
                flex: 1
              },
              __source: {
                fileName: _jsxFileName,
                lineNumber: 925
              }
            },
            ret,
            this._renderDebugOverlay()
          );
        } else {
          return ret;
        }
      }
    }, {
      key: "componentDidUpdate",
      value: function componentDidUpdate(prevProps) {
        var _props6 = this.props,
            data = _props6.data,
            extraData = _props6.extraData;

        if (data !== prevProps.data || extraData !== prevProps.extraData) {
          this._hasDataChangedSinceEndReached = true;

          this._viewabilityTuples.forEach(function (tuple) {
            tuple.viewabilityHelper.resetViewableIndices();
          });
        }

        this._scheduleCellsToRenderUpdate();
      }
    }, {
      key: "_computeBlankness",
      value: function _computeBlankness() {
        this._fillRateHelper.computeBlankness(this.props, this.state, this._scrollMetrics);
      }
    }, {
      key: "_onCellLayout",
      value: function _onCellLayout(e, cellKey, index) {
        var layout = e.nativeEvent.layout;
        var next = {
          offset: this._selectOffset(layout),
          length: this._selectLength(layout),
          index: index,
          inLayout: true
        };
        var curr = this._frames[cellKey];

        if (!curr || next.offset !== curr.offset || next.length !== curr.length || index !== curr.index) {
          this._totalCellLength += next.length - (curr ? curr.length : 0);
          this._totalCellsMeasured += curr ? 0 : 1;
          this._averageCellLength = this._totalCellLength / this._totalCellsMeasured;
          this._frames[cellKey] = next;
          this._highestMeasuredFrameIndex = Math.max(this._highestMeasuredFrameIndex, index);

          this._scheduleCellsToRenderUpdate();
        } else {
          this._frames[cellKey].inLayout = true;
        }

        this._computeBlankness();
      }
    }, {
      key: "_measureLayoutRelativeToContainingList",
      value: function _measureLayoutRelativeToContainingList() {
        var _this3 = this;

        UIManager.measureLayout(ReactNative.findNodeHandle(this), ReactNative.findNodeHandle(this.context.virtualizedList.getOutermostParentListRef()), function (error) {
          console.warn("VirtualizedList: Encountered an error while measuring a list's" + ' offset from its containing VirtualizedList.');
        }, function (x, y, width, height) {
          _this3._offsetFromParentVirtualizedList = _this3._selectOffset({
            x: x,
            y: y
          });
          _this3._scrollMetrics.contentLength = _this3._selectLength({
            width: width,
            height: height
          });

          var scrollMetrics = _this3._convertParentScrollMetrics(_this3.context.virtualizedList.getScrollMetrics());

          _this3._scrollMetrics.visibleLength = scrollMetrics.visibleLength;
          _this3._scrollMetrics.offset = scrollMetrics.offset;
        });
      }
    }, {
      key: "_renderDebugOverlay",
      value: function _renderDebugOverlay() {
        var normalize = this._scrollMetrics.visibleLength / this._scrollMetrics.contentLength;
        var framesInLayout = [];
        var itemCount = this.props.getItemCount(this.props.data);

        for (var ii = 0; ii < itemCount; ii++) {
          var frame = this._getFrameMetricsApprox(ii);

          if (frame.inLayout) {
            framesInLayout.push(frame);
          }
        }

        var windowTop = this._getFrameMetricsApprox(this.state.first).offset;

        var frameLast = this._getFrameMetricsApprox(this.state.last);

        var windowLen = frameLast.offset + frameLast.length - windowTop;
        var visTop = this._scrollMetrics.offset;
        var visLen = this._scrollMetrics.visibleLength;
        var baseStyle = {
          position: 'absolute',
          top: 0,
          right: 0
        };
        return React.createElement(
          View,
          {
            style: babelHelpers.extends({}, baseStyle, {
              bottom: 0,
              width: 20,
              borderColor: 'blue',
              borderWidth: 1
            }),
            __source: {
              fileName: _jsxFileName,
              lineNumber: 1136
            }
          },
          framesInLayout.map(function (f, ii) {
            return React.createElement(View, {
              key: 'f' + ii,
              style: babelHelpers.extends({}, baseStyle, {
                left: 0,
                top: f.offset * normalize,
                height: f.length * normalize,
                backgroundColor: 'orange'
              }),
              __source: {
                fileName: _jsxFileName,
                lineNumber: 1145
              }
            });
          }),
          React.createElement(View, {
            style: babelHelpers.extends({}, baseStyle, {
              left: 0,
              top: windowTop * normalize,
              height: windowLen * normalize,
              borderColor: 'green',
              borderWidth: 2
            }),
            __source: {
              fileName: _jsxFileName,
              lineNumber: 1156
            }
          }),
          React.createElement(View, {
            style: babelHelpers.extends({}, baseStyle, {
              left: 0,
              top: visTop * normalize,
              height: visLen * normalize,
              borderColor: 'red',
              borderWidth: 2
            }),
            __source: {
              fileName: _jsxFileName,
              lineNumber: 1166
            }
          })
        );
      }
    }, {
      key: "_selectLength",
      value: function _selectLength(metrics) {
        return !this.props.horizontal ? metrics.height : metrics.width;
      }
    }, {
      key: "_selectOffset",
      value: function _selectOffset(metrics) {
        return !this.props.horizontal ? metrics.y : metrics.x;
      }
    }, {
      key: "_maybeCallOnEndReached",
      value: function _maybeCallOnEndReached() {
        var _props7 = this.props,
            data = _props7.data,
            getItemCount = _props7.getItemCount,
            onEndReached = _props7.onEndReached,
            onEndReachedThreshold = _props7.onEndReachedThreshold;
        var _scrollMetrics = this._scrollMetrics,
            contentLength = _scrollMetrics.contentLength,
            visibleLength = _scrollMetrics.visibleLength,
            offset = _scrollMetrics.offset;
        var distanceFromEnd = contentLength - visibleLength - offset;

        if (onEndReached && this.state.last === getItemCount(data) - 1 && distanceFromEnd < onEndReachedThreshold * visibleLength && (this._hasDataChangedSinceEndReached || this._scrollMetrics.contentLength !== this._sentEndForContentLength)) {
          this._hasDataChangedSinceEndReached = false;
          this._sentEndForContentLength = this._scrollMetrics.contentLength;
          onEndReached({
            distanceFromEnd: distanceFromEnd
          });
        }
      }
    }, {
      key: "_scheduleCellsToRenderUpdate",
      value: function _scheduleCellsToRenderUpdate() {
        var _state2 = this.state,
            first = _state2.first,
            last = _state2.last;
        var _scrollMetrics2 = this._scrollMetrics,
            offset = _scrollMetrics2.offset,
            visibleLength = _scrollMetrics2.visibleLength,
            velocity = _scrollMetrics2.velocity;
        var itemCount = this.props.getItemCount(this.props.data);
        var hiPri = false;

        if (first > 0 || last < itemCount - 1) {
          var distTop = offset - this._getFrameMetricsApprox(first).offset;

          var distBottom = this._getFrameMetricsApprox(last).offset - (offset + visibleLength);
          var scrollingThreshold = this.props.onEndReachedThreshold * visibleLength / 2;
          hiPri = Math.min(distTop, distBottom) < 0 || velocity < -2 && distTop < scrollingThreshold || velocity > 2 && distBottom < scrollingThreshold;
        }

        if (hiPri && this._averageCellLength) {
          this._updateCellsToRenderBatcher.dispose({
            abort: true
          });

          this._updateCellsToRender();

          return;
        } else {
          this._updateCellsToRenderBatcher.schedule();
        }
      }
    }, {
      key: "_updateViewableItems",
      value: function _updateViewableItems(data) {
        var _this4 = this;

        var getItemCount = this.props.getItemCount;

        this._viewabilityTuples.forEach(function (tuple) {
          tuple.viewabilityHelper.onUpdate(getItemCount(data), _this4._scrollMetrics.offset, _this4._scrollMetrics.visibleLength, _this4._getFrameMetrics, _this4._createViewToken, tuple.onViewableItemsChanged, _this4.state);
        });
      }
    }], [{
      key: "getDerivedStateFromProps",
      value: function getDerivedStateFromProps(newProps, prevState) {
        var data = newProps.data,
            extraData = newProps.extraData,
            getItemCount = newProps.getItemCount,
            maxToRenderPerBatch = newProps.maxToRenderPerBatch;
        return {
          first: Math.max(0, Math.min(prevState.first, getItemCount(data) - 1 - maxToRenderPerBatch)),
          last: Math.max(0, Math.min(prevState.last, getItemCount(data) - 1))
        };
      }
    }]);
    return VirtualizedList;
  }(React.PureComponent);

  VirtualizedList.defaultProps = {
    disableVirtualization: false,
    horizontal: false,
    initialNumToRender: 10,
    keyExtractor: function keyExtractor(item, index) {
      if (item.key != null) {
        return item.key;
      }

      _usedIndexForKey = true;
      return String(index);
    },
    maxToRenderPerBatch: 10,
    onEndReachedThreshold: 2,
    scrollEventThrottle: 50,
    updateCellsBatchingPeriod: 50,
    windowSize: 21
  };
  VirtualizedList.contextTypes = {
    virtualizedCell: PropTypes.shape({
      cellKey: PropTypes.string
    }),
    virtualizedList: PropTypes.shape({
      getScrollMetrics: PropTypes.func,
      horizontal: PropTypes.bool,
      getOutermostParentListRef: PropTypes.func,
      getNestedChildState: PropTypes.func,
      registerAsNestedChild: PropTypes.func,
      unregisterAsNestedChild: PropTypes.func
    })
  };
  VirtualizedList.childContextTypes = {
    virtualizedList: PropTypes.shape({
      getScrollMetrics: PropTypes.func,
      horizontal: PropTypes.bool,
      getOutermostParentListRef: PropTypes.func,
      getNestedChildState: PropTypes.func,
      registerAsNestedChild: PropTypes.func,
      unregisterAsNestedChild: PropTypes.func
    })
  };

  var _initialiseProps = function _initialiseProps() {
    var _this7 = this;

    this._getScrollMetrics = function () {
      return _this7._scrollMetrics;
    };

    this._getOutermostParentListRef = function () {
      if (_this7._isNestedWithSameOrientation()) {
        return _this7.context.virtualizedList.getOutermostParentListRef();
      } else {
        return _this7;
      }
    };

    this._getNestedChildState = function (key) {
      var existingChildData = _this7._nestedChildLists.get(key);

      return existingChildData && existingChildData.state;
    };

    this._registerAsNestedChild = function (childList) {
      var childListsInCell = _this7._cellKeysToChildListKeys.get(childList.cellKey) || new Set();
      childListsInCell.add(childList.key);

      _this7._cellKeysToChildListKeys.set(childList.cellKey, childListsInCell);

      var existingChildData = _this7._nestedChildLists.get(childList.key);

      invariant(!(existingChildData && existingChildData.ref !== null), 'A VirtualizedList contains a cell which itself contains ' + 'more than one VirtualizedList of the same orientation as the parent ' + 'list. You must pass a unique listKey prop to each sibling list.');

      _this7._nestedChildLists.set(childList.key, {
        ref: childList.ref,
        state: null
      });

      if (_this7._hasInteracted) {
        childList.ref.recordInteraction();
      }
    };

    this._unregisterAsNestedChild = function (childList) {
      _this7._nestedChildLists.set(childList.key, {
        ref: null,
        state: childList.state
      });
    };

    this._onUpdateSeparators = function (keys, newProps) {
      keys.forEach(function (key) {
        var ref = key != null && _this7._cellRefs[key];
        ref && ref.updateSeparatorProps(newProps);
      });
    };

    this._averageCellLength = 0;
    this._cellKeysToChildListKeys = new Map();
    this._cellRefs = {};
    this._frames = {};
    this._footerLength = 0;
    this._hasDataChangedSinceEndReached = true;
    this._hasInteracted = false;
    this._hasMore = false;
    this._hasWarned = {};
    this._highestMeasuredFrameIndex = 0;
    this._headerLength = 0;
    this._indicesToKeys = new Map();
    this._hasDoneInitialScroll = false;
    this._nestedChildLists = new Map();
    this._offsetFromParentVirtualizedList = 0;
    this._prevParentOffset = 0;
    this._scrollMetrics = {
      contentLength: 0,
      dOffset: 0,
      dt: 10,
      offset: 0,
      timestamp: 0,
      velocity: 0,
      visibleLength: 0
    };
    this._scrollRef = null;
    this._sentEndForContentLength = 0;
    this._totalCellLength = 0;
    this._totalCellsMeasured = 0;
    this._viewabilityTuples = [];

    this._captureScrollRef = function (ref) {
      _this7._scrollRef = ref;
    };

    this._defaultRenderScrollComponent = function (props) {
      if (_this7._isNestedWithSameOrientation()) {
        return React.createElement(View, babelHelpers.extends({}, props, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 1000
          }
        }));
      } else if (props.onRefresh) {
        invariant(typeof props.refreshing === 'boolean', '`refreshing` prop must be set as a boolean in order to use `onRefresh`, but got `' + JSON.stringify(props.refreshing) + '`');
        return React.createElement(ScrollView, babelHelpers.extends({}, props, {
          refreshControl: React.createElement(RefreshControl, {
            refreshing: props.refreshing,
            onRefresh: props.onRefresh,
            progressViewOffset: props.progressViewOffset,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 1015
            }
          }),
          __source: {
            fileName: _jsxFileName,
            lineNumber: 1009
          }
        }));
      } else {
        return React.createElement(ScrollView, babelHelpers.extends({}, props, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 1024
          }
        }));
      }
    };

    this._onCellUnmount = function (cellKey) {
      var curr = _this7._frames[cellKey];

      if (curr) {
        _this7._frames[cellKey] = babelHelpers.extends({}, curr, {
          inLayout: false
        });
      }
    };

    this._onLayout = function (e) {
      if (_this7._isNestedWithSameOrientation()) {
        _this7._measureLayoutRelativeToContainingList();
      } else {
        _this7._scrollMetrics.visibleLength = _this7._selectLength(e.nativeEvent.layout);
      }

      _this7.props.onLayout && _this7.props.onLayout(e);

      _this7._scheduleCellsToRenderUpdate();

      _this7._maybeCallOnEndReached();
    };

    this._onLayoutEmpty = function (e) {
      _this7.props.onLayout && _this7.props.onLayout(e);
    };

    this._onLayoutFooter = function (e) {
      _this7._footerLength = _this7._selectLength(e.nativeEvent.layout);
    };

    this._onLayoutHeader = function (e) {
      _this7._headerLength = _this7._selectLength(e.nativeEvent.layout);
    };

    this._onContentSizeChange = function (width, height) {
      if (width > 0 && height > 0 && _this7.props.initialScrollIndex != null && _this7.props.initialScrollIndex > 0 && !_this7._hasDoneInitialScroll) {
        _this7.scrollToIndex({
          animated: false,
          index: _this7.props.initialScrollIndex
        });

        _this7._hasDoneInitialScroll = true;
      }

      if (_this7.props.onContentSizeChange) {
        _this7.props.onContentSizeChange(width, height);
      }

      _this7._scrollMetrics.contentLength = _this7._selectLength({
        height: height,
        width: width
      });

      _this7._scheduleCellsToRenderUpdate();

      _this7._maybeCallOnEndReached();
    };

    this._convertParentScrollMetrics = function (metrics) {
      var offset = metrics.offset - _this7._offsetFromParentVirtualizedList;
      var visibleLength = metrics.visibleLength;
      var dOffset = offset - _this7._scrollMetrics.offset;
      var contentLength = _this7._scrollMetrics.contentLength;
      return {
        visibleLength: visibleLength,
        contentLength: contentLength,
        offset: offset,
        dOffset: dOffset
      };
    };

    this._onScroll = function (e) {
      _this7._nestedChildLists.forEach(function (childList) {
        childList.ref && childList.ref._onScroll(e);
      });

      if (_this7.props.onScroll) {
        _this7.props.onScroll(e);
      }

      var timestamp = e.timeStamp;

      var visibleLength = _this7._selectLength(e.nativeEvent.layoutMeasurement);

      var contentLength = _this7._selectLength(e.nativeEvent.contentSize);

      var offset = _this7._selectOffset(e.nativeEvent.contentOffset);

      var dOffset = offset - _this7._scrollMetrics.offset;

      if (_this7._isNestedWithSameOrientation()) {
        if (_this7._scrollMetrics.contentLength === 0) {
          return;
        }

        var _convertParentScrollM = _this7._convertParentScrollMetrics({
          visibleLength: visibleLength,
          offset: offset
        });

        visibleLength = _convertParentScrollM.visibleLength;
        contentLength = _convertParentScrollM.contentLength;
        offset = _convertParentScrollM.offset;
        dOffset = _convertParentScrollM.dOffset;
      }

      var dt = _this7._scrollMetrics.timestamp ? Math.max(1, timestamp - _this7._scrollMetrics.timestamp) : 1;
      var velocity = dOffset / dt;

      if (dt > 500 && _this7._scrollMetrics.dt > 500 && contentLength > 5 * visibleLength && !_this7._hasWarned.perf) {
        infoLog('VirtualizedList: You have a large list that is slow to update - make sure your ' + 'renderItem function renders components that follow React performance best practices ' + 'like PureComponent, shouldComponentUpdate, etc.', {
          dt: dt,
          prevDt: _this7._scrollMetrics.dt,
          contentLength: contentLength
        });
        _this7._hasWarned.perf = true;
      }

      _this7._scrollMetrics = {
        contentLength: contentLength,
        dt: dt,
        dOffset: dOffset,
        offset: offset,
        timestamp: timestamp,
        velocity: velocity,
        visibleLength: visibleLength
      };

      _this7._updateViewableItems(_this7.props.data);

      if (!_this7.props) {
        return;
      }

      _this7._maybeCallOnEndReached();

      if (velocity !== 0) {
        _this7._fillRateHelper.activate();
      }

      _this7._computeBlankness();

      _this7._scheduleCellsToRenderUpdate();
    };

    this._onScrollBeginDrag = function (e) {
      _this7._nestedChildLists.forEach(function (childList) {
        childList.ref && childList.ref._onScrollBeginDrag(e);
      });

      _this7._viewabilityTuples.forEach(function (tuple) {
        tuple.viewabilityHelper.recordInteraction();
      });

      _this7._hasInteracted = true;
      _this7.props.onScrollBeginDrag && _this7.props.onScrollBeginDrag(e);
    };

    this._onScrollEndDrag = function (e) {
      var velocity = e.nativeEvent.velocity;

      if (velocity) {
        _this7._scrollMetrics.velocity = _this7._selectOffset(velocity);
      }

      _this7._computeBlankness();

      _this7.props.onScrollEndDrag && _this7.props.onScrollEndDrag(e);
    };

    this._onMomentumScrollEnd = function (e) {
      _this7._scrollMetrics.velocity = 0;

      _this7._computeBlankness();

      _this7.props.onMomentumScrollEnd && _this7.props.onMomentumScrollEnd(e);
    };

    this._updateCellsToRender = function () {
      var _props9 = _this7.props,
          data = _props9.data,
          getItemCount = _props9.getItemCount,
          onEndReachedThreshold = _props9.onEndReachedThreshold;

      var isVirtualizationDisabled = _this7._isVirtualizationDisabled();

      _this7._updateViewableItems(data);

      if (!data) {
        return;
      }

      _this7.setState(function (state) {
        var newState = void 0;

        if (!isVirtualizationDisabled) {
          if (_this7._scrollMetrics.visibleLength) {
            if (!_this7.props.initialScrollIndex || _this7._scrollMetrics.offset) {
              newState = computeWindowedRenderLimits(_this7.props, state, _this7._getFrameMetricsApprox, _this7._scrollMetrics);
            }
          }
        } else {
          var _scrollMetrics3 = _this7._scrollMetrics,
              contentLength = _scrollMetrics3.contentLength,
              _offset = _scrollMetrics3.offset,
              visibleLength = _scrollMetrics3.visibleLength;

          var _distanceFromEnd = contentLength - visibleLength - _offset;

          var renderAhead = _distanceFromEnd < onEndReachedThreshold * visibleLength ? _this7.props.maxToRenderPerBatch : 0;
          newState = {
            first: 0,
            last: Math.min(state.last + renderAhead, getItemCount(data) - 1)
          };
        }

        if (newState && _this7._nestedChildLists.size > 0) {
          var newFirst = newState.first;
          var newLast = newState.last;

          for (var ii = newFirst; ii <= newLast; ii++) {
            var cellKeyForIndex = _this7._indicesToKeys.get(ii);

            var childListKeys = cellKeyForIndex && _this7._cellKeysToChildListKeys.get(cellKeyForIndex);

            if (!childListKeys) {
              continue;
            }

            var someChildHasMore = false;

            for (var _iterator = childListKeys, _isArray = Array.isArray(_iterator), _i = 0, _iterator = _isArray ? _iterator : _iterator[typeof Symbol === "function" ? Symbol.iterator : "@@iterator"]();;) {
              var _ref7;

              if (_isArray) {
                if (_i >= _iterator.length) break;
                _ref7 = _iterator[_i++];
              } else {
                _i = _iterator.next();
                if (_i.done) break;
                _ref7 = _i.value;
              }

              var childKey = _ref7;

              var childList = _this7._nestedChildLists.get(childKey);

              if (childList && childList.ref && childList.ref.hasMore()) {
                someChildHasMore = true;
                break;
              }
            }

            if (someChildHasMore) {
              newState.last = ii;
              break;
            }
          }
        }

        return newState;
      });
    };

    this._createViewToken = function (index, isViewable) {
      var _props10 = _this7.props,
          data = _props10.data,
          getItem = _props10.getItem,
          keyExtractor = _props10.keyExtractor;
      var item = getItem(data, index);
      return {
        index: index,
        item: item,
        key: keyExtractor(item, index),
        isViewable: isViewable
      };
    };

    this._getFrameMetricsApprox = function (index) {
      var frame = _this7._getFrameMetrics(index);

      if (frame && frame.index === index) {
        return frame;
      } else {
        var _getItemLayout = _this7.props.getItemLayout;
        invariant(!_getItemLayout, 'Should not have to estimate frames when a measurement metrics function is provided');
        return {
          length: _this7._averageCellLength,
          offset: _this7._averageCellLength * index
        };
      }
    };

    this._getFrameMetrics = function (index) {
      var _props11 = _this7.props,
          data = _props11.data,
          getItem = _props11.getItem,
          getItemCount = _props11.getItemCount,
          getItemLayout = _props11.getItemLayout,
          keyExtractor = _props11.keyExtractor;
      invariant(getItemCount(data) > index, 'Tried to get frame for out of range index ' + index);
      var item = getItem(data, index);

      var frame = item && _this7._frames[keyExtractor(item, index)];

      if (!frame || frame.index !== index) {
        if (getItemLayout) {
          frame = getItemLayout(data, index);

          if (__DEV__) {
            var frameType = PropTypes.shape({
              length: PropTypes.number.isRequired,
              offset: PropTypes.number.isRequired,
              index: PropTypes.number.isRequired
            }).isRequired;
            PropTypes.checkPropTypes({
              frame: frameType
            }, {
              frame: frame
            }, 'frame', 'VirtualizedList.getItemLayout');
          }
        }
      }

      return frame;
    };
  };

  var CellRenderer = function (_React$Component) {
    babelHelpers.inherits(CellRenderer, _React$Component);

    function CellRenderer() {
      var _ref6;

      var _temp, _this5, _ret2;

      babelHelpers.classCallCheck(this, CellRenderer);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret2 = (_temp = (_this5 = babelHelpers.possibleConstructorReturn(this, (_ref6 = CellRenderer.__proto__ || Object.getPrototypeOf(CellRenderer)).call.apply(_ref6, [this].concat(args))), _this5), _this5.state = {
        separatorProps: {
          highlighted: false,
          leadingItem: _this5.props.item
        }
      }, _this5._separators = {
        highlight: function highlight() {
          var _this5$props = _this5.props,
              cellKey = _this5$props.cellKey,
              prevCellKey = _this5$props.prevCellKey;

          _this5.props.onUpdateSeparators([cellKey, prevCellKey], {
            highlighted: true
          });
        },
        unhighlight: function unhighlight() {
          var _this5$props2 = _this5.props,
              cellKey = _this5$props2.cellKey,
              prevCellKey = _this5$props2.prevCellKey;

          _this5.props.onUpdateSeparators([cellKey, prevCellKey], {
            highlighted: false
          });
        },
        updateProps: function updateProps(select, newProps) {
          var _this5$props3 = _this5.props,
              cellKey = _this5$props3.cellKey,
              prevCellKey = _this5$props3.prevCellKey;

          _this5.props.onUpdateSeparators([select === 'leading' ? prevCellKey : cellKey], newProps);
        }
      }, _temp), babelHelpers.possibleConstructorReturn(_this5, _ret2);
    }

    babelHelpers.createClass(CellRenderer, [{
      key: "getChildContext",
      value: function getChildContext() {
        return {
          virtualizedCell: {
            cellKey: this.props.cellKey
          }
        };
      }
    }, {
      key: "updateSeparatorProps",
      value: function updateSeparatorProps(newProps) {
        this.setState(function (state) {
          return {
            separatorProps: babelHelpers.extends({}, state.separatorProps, newProps)
          };
        });
      }
    }, {
      key: "componentWillUnmount",
      value: function componentWillUnmount() {
        this.props.onUnmount(this.props.cellKey);
      }
    }, {
      key: "render",
      value: function render() {
        var _props8 = this.props,
            CellRendererComponent = _props8.CellRendererComponent,
            ItemSeparatorComponent = _props8.ItemSeparatorComponent,
            fillRateHelper = _props8.fillRateHelper,
            horizontal = _props8.horizontal,
            item = _props8.item,
            index = _props8.index,
            inversionStyle = _props8.inversionStyle,
            parentProps = _props8.parentProps;
        var renderItem = parentProps.renderItem,
            getItemLayout = parentProps.getItemLayout;
        invariant(renderItem, 'no renderItem!');
        var element = renderItem({
          item: item,
          index: index,
          separators: this._separators
        });
        var onLayout = getItemLayout && !parentProps.debug && !fillRateHelper.enabled() ? undefined : this.props.onLayout;
        var itemSeparator = ItemSeparatorComponent && React.createElement(ItemSeparatorComponent, babelHelpers.extends({}, this.state.separatorProps, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 1655
          }
        }));
        var cellStyle = inversionStyle ? horizontal ? [{
          flexDirection: 'row-reverse'
        }, inversionStyle] : [{
          flexDirection: 'column-reverse'
        }, inversionStyle] : horizontal ? [{
          flexDirection: 'row'
        }, inversionStyle] : inversionStyle;

        if (!CellRendererComponent) {
          return React.createElement(
            View,
            {
              style: cellStyle,
              onLayout: onLayout,
              __source: {
                fileName: _jsxFileName,
                lineNumber: 1664
              }
            },
            element,
            itemSeparator
          );
        }

        return React.createElement(
          CellRendererComponent,
          babelHelpers.extends({}, this.props, {
            style: cellStyle,
            onLayout: onLayout,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 1671
            }
          }),
          element,
          itemSeparator
        );
      }
    }]);
    return CellRenderer;
  }(React.Component);

  CellRenderer.childContextTypes = {
    virtualizedCell: PropTypes.shape({
      cellKey: PropTypes.string
    })
  };

  var VirtualizedCellWrapper = function (_React$Component2) {
    babelHelpers.inherits(VirtualizedCellWrapper, _React$Component2);

    function VirtualizedCellWrapper() {
      babelHelpers.classCallCheck(this, VirtualizedCellWrapper);
      return babelHelpers.possibleConstructorReturn(this, (VirtualizedCellWrapper.__proto__ || Object.getPrototypeOf(VirtualizedCellWrapper)).apply(this, arguments));
    }

    babelHelpers.createClass(VirtualizedCellWrapper, [{
      key: "getChildContext",
      value: function getChildContext() {
        return {
          virtualizedCell: {
            cellKey: this.props.cellKey
          }
        };
      }
    }, {
      key: "render",
      value: function render() {
        return this.props.children;
      }
    }]);
    return VirtualizedCellWrapper;
  }(React.Component);

  VirtualizedCellWrapper.childContextTypes = {
    virtualizedCell: PropTypes.shape({
      cellKey: PropTypes.string
    })
  };
  var styles = StyleSheet.create({
    verticallyInverted: {
      transform: [{
        scaleY: -1
      }]
    },
    horizontallyInverted: {
      transform: [{
        scaleX: -1
      }]
    }
  });
  module.exports = VirtualizedList;
},"acd0db067821acba68e31f8e1998a3c5",["35fcc2415adbe7c9d61c43ec7632939d","02933ae517f504391cd5df131490a413","18eeaf4e01377a466daaccc6ba8ce6f5","e6db4f0efed6b72f641ef0ffed29569f","1102b68d89d7a6aede9677567aa01362","9f3ff99e5e0f1e9644e8a1222733210c","aa8514022050149acc8c46c0b18dc75a","d31e8c1a3f9844becc88973ecddac872","467cd3365342d9aaa2e941fe7ace641c","30a3b04291b6e1f01b778ff31271ccc5","a8ea75c38a911a17cdc7cc33ba49ca4e","869f0bd4eed428d95df80a8c03d71093","ee890a01982619b17df5249a8a8fb462","8940a4ad43b101ffc23e725363c70f8d","09babf511a081d9520406a63f452d2ef","880bb3ad6cffca7c7958f9770e6baf87"],"VirtualizedList");