__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Lists/ListView/ListView.js";

  var ListViewDataSource = _require(_dependencyMap[0], 'ListViewDataSource');

  var Platform = _require(_dependencyMap[1], 'Platform');

  var React = _require(_dependencyMap[2], 'React');

  var PropTypes = _require(_dependencyMap[3], 'prop-types');

  var ReactNative = _require(_dependencyMap[4], 'ReactNative');

  var RCTScrollViewManager = _require(_dependencyMap[5], 'NativeModules').ScrollViewManager;

  var ScrollView = _require(_dependencyMap[6], 'ScrollView');

  var ScrollResponder = _require(_dependencyMap[7], 'ScrollResponder');

  var StaticRenderer = _require(_dependencyMap[8], 'StaticRenderer');

  var TimerMixin = _require(_dependencyMap[9], 'react-timer-mixin');

  var View = _require(_dependencyMap[10], 'View');

  var cloneReferencedElement = _require(_dependencyMap[11], 'react-clone-referenced-element');

  var createReactClass = _require(_dependencyMap[12], 'create-react-class');

  var isEmpty = _require(_dependencyMap[13], 'isEmpty');

  var merge = _require(_dependencyMap[14], 'merge');

  var DEFAULT_PAGE_SIZE = 1;
  var DEFAULT_INITIAL_ROWS = 10;
  var DEFAULT_SCROLL_RENDER_AHEAD = 1000;
  var DEFAULT_END_REACHED_THRESHOLD = 1000;
  var DEFAULT_SCROLL_CALLBACK_THROTTLE = 50;
  var ListView = createReactClass({
    displayName: 'ListView',
    _childFrames: [],
    _sentEndForContentLength: null,
    _scrollComponent: null,
    _prevRenderedRowsCount: 0,
    _visibleRows: {},
    scrollProperties: {},
    mixins: [ScrollResponder.Mixin, TimerMixin],
    statics: {
      DataSource: ListViewDataSource
    },
    propTypes: babelHelpers.extends({}, ScrollView.propTypes, {
      dataSource: PropTypes.instanceOf(ListViewDataSource).isRequired,
      renderSeparator: PropTypes.func,
      renderRow: PropTypes.func.isRequired,
      initialListSize: PropTypes.number.isRequired,
      onEndReached: PropTypes.func,
      onEndReachedThreshold: PropTypes.number.isRequired,
      pageSize: PropTypes.number.isRequired,
      renderFooter: PropTypes.func,
      renderHeader: PropTypes.func,
      renderSectionHeader: PropTypes.func,
      renderScrollComponent: PropTypes.func.isRequired,
      scrollRenderAheadDistance: PropTypes.number.isRequired,
      onChangeVisibleRows: PropTypes.func,
      removeClippedSubviews: PropTypes.bool,
      stickySectionHeadersEnabled: PropTypes.bool,
      stickyHeaderIndices: PropTypes.arrayOf(PropTypes.number).isRequired,
      enableEmptySections: PropTypes.bool
    }),
    getMetrics: function getMetrics() {
      return {
        contentLength: this.scrollProperties.contentLength,
        totalRows: this.props.enableEmptySections ? this.props.dataSource.getRowAndSectionCount() : this.props.dataSource.getRowCount(),
        renderedRows: this.state.curRenderedRowsCount,
        visibleRows: Object.keys(this._visibleRows).length
      };
    },
    getScrollResponder: function getScrollResponder() {
      if (this._scrollComponent && this._scrollComponent.getScrollResponder) {
        return this._scrollComponent.getScrollResponder();
      }
    },
    getScrollableNode: function getScrollableNode() {
      if (this._scrollComponent && this._scrollComponent.getScrollableNode) {
        return this._scrollComponent.getScrollableNode();
      } else {
        return ReactNative.findNodeHandle(this._scrollComponent);
      }
    },
    scrollTo: function scrollTo() {
      if (this._scrollComponent && this._scrollComponent.scrollTo) {
        var _scrollComponent;

        (_scrollComponent = this._scrollComponent).scrollTo.apply(_scrollComponent, arguments);
      }
    },
    scrollToEnd: function scrollToEnd(options) {
      if (this._scrollComponent) {
        if (this._scrollComponent.scrollToEnd) {
          this._scrollComponent.scrollToEnd(options);
        } else {
          console.warn('The scroll component used by the ListView does not support ' + 'scrollToEnd. Check the renderScrollComponent prop of your ListView.');
        }
      }
    },
    flashScrollIndicators: function flashScrollIndicators() {
      if (this._scrollComponent && this._scrollComponent.flashScrollIndicators) {
        this._scrollComponent.flashScrollIndicators();
      }
    },
    setNativeProps: function setNativeProps(props) {
      if (this._scrollComponent) {
        this._scrollComponent.setNativeProps(props);
      }
    },
    getDefaultProps: function getDefaultProps() {
      return {
        initialListSize: DEFAULT_INITIAL_ROWS,
        pageSize: DEFAULT_PAGE_SIZE,
        renderScrollComponent: function renderScrollComponent(props) {
          return React.createElement(ScrollView, babelHelpers.extends({}, props, {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 336
            }
          }));
        },
        scrollRenderAheadDistance: DEFAULT_SCROLL_RENDER_AHEAD,
        onEndReachedThreshold: DEFAULT_END_REACHED_THRESHOLD,
        stickySectionHeadersEnabled: Platform.OS === 'ios',
        stickyHeaderIndices: []
      };
    },
    getInitialState: function getInitialState() {
      return {
        curRenderedRowsCount: this.props.initialListSize,
        highlightedRow: {}
      };
    },
    getInnerViewNode: function getInnerViewNode() {
      return this._scrollComponent.getInnerViewNode();
    },
    UNSAFE_componentWillMount: function UNSAFE_componentWillMount() {
      this.scrollProperties = {
        visibleLength: null,
        contentLength: null,
        offset: 0
      };
      this._childFrames = [];
      this._visibleRows = {};
      this._prevRenderedRowsCount = 0;
      this._sentEndForContentLength = null;
    },
    componentDidMount: function componentDidMount() {
      var _this = this;

      this.requestAnimationFrame(function () {
        _this._measureAndUpdateScrollProps();
      });
    },
    UNSAFE_componentWillReceiveProps: function UNSAFE_componentWillReceiveProps(nextProps) {
      var _this2 = this;

      if (this.props.dataSource !== nextProps.dataSource || this.props.initialListSize !== nextProps.initialListSize) {
        this.setState(function (state, props) {
          _this2._prevRenderedRowsCount = 0;
          return {
            curRenderedRowsCount: Math.min(Math.max(state.curRenderedRowsCount, props.initialListSize), props.enableEmptySections ? props.dataSource.getRowAndSectionCount() : props.dataSource.getRowCount())
          };
        }, function () {
          return _this2._renderMoreRowsIfNeeded();
        });
      }
    },
    componentDidUpdate: function componentDidUpdate() {
      var _this3 = this;

      this.requestAnimationFrame(function () {
        _this3._measureAndUpdateScrollProps();
      });
    },
    _onRowHighlighted: function _onRowHighlighted(sectionID, rowID) {
      this.setState({
        highlightedRow: {
          sectionID: sectionID,
          rowID: rowID
        }
      });
    },
    render: function render() {
      var bodyComponents = [];
      var dataSource = this.props.dataSource;
      var allRowIDs = dataSource.rowIdentities;
      var rowCount = 0;
      var stickySectionHeaderIndices = [];
      var renderSectionHeader = this.props.renderSectionHeader;
      var header = this.props.renderHeader && this.props.renderHeader();
      var footer = this.props.renderFooter && this.props.renderFooter();
      var totalIndex = header ? 1 : 0;

      for (var sectionIdx = 0; sectionIdx < allRowIDs.length; sectionIdx++) {
        var sectionID = dataSource.sectionIdentities[sectionIdx];
        var rowIDs = allRowIDs[sectionIdx];

        if (rowIDs.length === 0) {
          if (this.props.enableEmptySections === undefined) {
            var warning = _require(_dependencyMap[15], 'fbjs/lib/warning');

            warning(false, 'In next release empty section headers will be rendered.' + " In this release you can use 'enableEmptySections' flag to render empty section headers.");
            continue;
          } else {
            var invariant = _require(_dependencyMap[16], 'fbjs/lib/invariant');

            invariant(this.props.enableEmptySections, "In next release 'enableEmptySections' flag will be deprecated, empty section headers will always be rendered." + ' If empty section headers are not desirable their indices should be excluded from sectionIDs object.' + " In this release 'enableEmptySections' may only have value 'true' to allow empty section headers rendering.");
          }
        }

        if (renderSectionHeader) {
          var element = renderSectionHeader(dataSource.getSectionHeaderData(sectionIdx), sectionID);

          if (element) {
            bodyComponents.push(React.cloneElement(element, {
              key: 's_' + sectionID
            }));

            if (this.props.stickySectionHeadersEnabled) {
              stickySectionHeaderIndices.push(totalIndex);
            }

            totalIndex++;
          }
        }

        for (var rowIdx = 0; rowIdx < rowIDs.length; rowIdx++) {
          var rowID = rowIDs[rowIdx];
          var comboID = sectionID + '_' + rowID;
          var shouldUpdateRow = rowCount >= this._prevRenderedRowsCount && dataSource.rowShouldUpdate(sectionIdx, rowIdx);
          var row = React.createElement(StaticRenderer, {
            key: 'r_' + comboID,
            shouldUpdate: !!shouldUpdateRow,
            render: this.props.renderRow.bind(null, dataSource.getRowData(sectionIdx, rowIdx), sectionID, rowID, this._onRowHighlighted),
            __source: {
              fileName: _jsxFileName,
              lineNumber: 471
            }
          });
          bodyComponents.push(row);
          totalIndex++;

          if (this.props.renderSeparator && (rowIdx !== rowIDs.length - 1 || sectionIdx === allRowIDs.length - 1)) {
            var adjacentRowHighlighted = this.state.highlightedRow.sectionID === sectionID && (this.state.highlightedRow.rowID === rowID || this.state.highlightedRow.rowID === rowIDs[rowIdx + 1]);
            var separator = this.props.renderSeparator(sectionID, rowID, adjacentRowHighlighted);

            if (separator) {
              bodyComponents.push(React.createElement(
                View,
                {
                  key: 's_' + comboID,
                  __source: {
                    fileName: _jsxFileName,
                    lineNumber: 500
                  }
                },
                separator
              ));
              totalIndex++;
            }
          }

          if (++rowCount === this.state.curRenderedRowsCount) {
            break;
          }
        }

        if (rowCount >= this.state.curRenderedRowsCount) {
          break;
        }
      }

      var _props = this.props,
          renderScrollComponent = _props.renderScrollComponent,
          props = babelHelpers.objectWithoutProperties(_props, ["renderScrollComponent"]);

      if (!props.scrollEventThrottle) {
        props.scrollEventThrottle = DEFAULT_SCROLL_CALLBACK_THROTTLE;
      }

      if (props.removeClippedSubviews === undefined) {
        props.removeClippedSubviews = true;
      }

      babelHelpers.extends(props, {
        onScroll: this._onScroll,
        stickyHeaderIndices: this.props.stickyHeaderIndices.concat(stickySectionHeaderIndices),
        onKeyboardWillShow: undefined,
        onKeyboardWillHide: undefined,
        onKeyboardDidShow: undefined,
        onKeyboardDidHide: undefined
      });
      return cloneReferencedElement(renderScrollComponent(props), {
        ref: this._setScrollComponentRef,
        onContentSizeChange: this._onContentSizeChange,
        onLayout: this._onLayout,
        DEPRECATED_sendUpdatedChildFrames: typeof props.onChangeVisibleRows !== undefined
      }, header, bodyComponents, footer);
    },
    _measureAndUpdateScrollProps: function _measureAndUpdateScrollProps() {
      var scrollComponent = this.getScrollResponder();

      if (!scrollComponent || !scrollComponent.getInnerViewNode) {
        return;
      }

      RCTScrollViewManager && RCTScrollViewManager.calculateChildFrames && RCTScrollViewManager.calculateChildFrames(ReactNative.findNodeHandle(scrollComponent), this._updateVisibleRows);
    },
    _setScrollComponentRef: function _setScrollComponentRef(scrollComponent) {
      this._scrollComponent = scrollComponent;
    },
    _onContentSizeChange: function _onContentSizeChange(width, height) {
      var contentLength = !this.props.horizontal ? height : width;

      if (contentLength !== this.scrollProperties.contentLength) {
        this.scrollProperties.contentLength = contentLength;

        this._updateVisibleRows();

        this._renderMoreRowsIfNeeded();
      }

      this.props.onContentSizeChange && this.props.onContentSizeChange(width, height);
    },
    _onLayout: function _onLayout(event) {
      var _event$nativeEvent$la = event.nativeEvent.layout,
          width = _event$nativeEvent$la.width,
          height = _event$nativeEvent$la.height;
      var visibleLength = !this.props.horizontal ? height : width;

      if (visibleLength !== this.scrollProperties.visibleLength) {
        this.scrollProperties.visibleLength = visibleLength;

        this._updateVisibleRows();

        this._renderMoreRowsIfNeeded();
      }

      this.props.onLayout && this.props.onLayout(event);
    },
    _maybeCallOnEndReached: function _maybeCallOnEndReached(event) {
      if (this.props.onEndReached && this.scrollProperties.contentLength !== this._sentEndForContentLength && this._getDistanceFromEnd(this.scrollProperties) < this.props.onEndReachedThreshold && this.state.curRenderedRowsCount === (this.props.enableEmptySections ? this.props.dataSource.getRowAndSectionCount() : this.props.dataSource.getRowCount())) {
        this._sentEndForContentLength = this.scrollProperties.contentLength;
        this.props.onEndReached(event);
        return true;
      }

      return false;
    },
    _renderMoreRowsIfNeeded: function _renderMoreRowsIfNeeded() {
      if (this.scrollProperties.contentLength === null || this.scrollProperties.visibleLength === null || this.state.curRenderedRowsCount === (this.props.enableEmptySections ? this.props.dataSource.getRowAndSectionCount() : this.props.dataSource.getRowCount())) {
        this._maybeCallOnEndReached();

        return;
      }

      var distanceFromEnd = this._getDistanceFromEnd(this.scrollProperties);

      if (distanceFromEnd < this.props.scrollRenderAheadDistance) {
        this._pageInNewRows();
      }
    },
    _pageInNewRows: function _pageInNewRows() {
      var _this4 = this;

      this.setState(function (state, props) {
        var rowsToRender = Math.min(state.curRenderedRowsCount + props.pageSize, props.enableEmptySections ? props.dataSource.getRowAndSectionCount() : props.dataSource.getRowCount());
        _this4._prevRenderedRowsCount = state.curRenderedRowsCount;
        return {
          curRenderedRowsCount: rowsToRender
        };
      }, function () {
        _this4._measureAndUpdateScrollProps();

        _this4._prevRenderedRowsCount = _this4.state.curRenderedRowsCount;
      });
    },
    _getDistanceFromEnd: function _getDistanceFromEnd(scrollProperties) {
      return scrollProperties.contentLength - scrollProperties.visibleLength - scrollProperties.offset;
    },
    _updateVisibleRows: function _updateVisibleRows(updatedFrames) {
      var _this5 = this;

      if (!this.props.onChangeVisibleRows) {
        return;
      }

      if (updatedFrames) {
        updatedFrames.forEach(function (newFrame) {
          _this5._childFrames[newFrame.index] = merge(newFrame);
        });
      }

      var isVertical = !this.props.horizontal;
      var dataSource = this.props.dataSource;
      var visibleMin = this.scrollProperties.offset;
      var visibleMax = visibleMin + this.scrollProperties.visibleLength;
      var allRowIDs = dataSource.rowIdentities;
      var header = this.props.renderHeader && this.props.renderHeader();
      var totalIndex = header ? 1 : 0;
      var visibilityChanged = false;
      var changedRows = {};

      for (var sectionIdx = 0; sectionIdx < allRowIDs.length; sectionIdx++) {
        var rowIDs = allRowIDs[sectionIdx];

        if (rowIDs.length === 0) {
          continue;
        }

        var sectionID = dataSource.sectionIdentities[sectionIdx];

        if (this.props.renderSectionHeader) {
          totalIndex++;
        }

        var visibleSection = this._visibleRows[sectionID];

        if (!visibleSection) {
          visibleSection = {};
        }

        for (var rowIdx = 0; rowIdx < rowIDs.length; rowIdx++) {
          var rowID = rowIDs[rowIdx];
          var frame = this._childFrames[totalIndex];
          totalIndex++;

          if (this.props.renderSeparator && (rowIdx !== rowIDs.length - 1 || sectionIdx === allRowIDs.length - 1)) {
            totalIndex++;
          }

          if (!frame) {
            break;
          }

          var rowVisible = visibleSection[rowID];
          var min = isVertical ? frame.y : frame.x;
          var max = min + (isVertical ? frame.height : frame.width);

          if (!min && !max || min === max) {
            break;
          }

          if (min > visibleMax || max < visibleMin) {
            if (rowVisible) {
              visibilityChanged = true;
              delete visibleSection[rowID];

              if (!changedRows[sectionID]) {
                changedRows[sectionID] = {};
              }

              changedRows[sectionID][rowID] = false;
            }
          } else if (!rowVisible) {
            visibilityChanged = true;
            visibleSection[rowID] = true;

            if (!changedRows[sectionID]) {
              changedRows[sectionID] = {};
            }

            changedRows[sectionID][rowID] = true;
          }
        }

        if (!isEmpty(visibleSection)) {
          this._visibleRows[sectionID] = visibleSection;
        } else if (this._visibleRows[sectionID]) {
          delete this._visibleRows[sectionID];
        }
      }

      visibilityChanged && this.props.onChangeVisibleRows(this._visibleRows, changedRows);
    },
    _onScroll: function _onScroll(e) {
      var isVertical = !this.props.horizontal;
      this.scrollProperties.visibleLength = e.nativeEvent.layoutMeasurement[isVertical ? 'height' : 'width'];
      this.scrollProperties.contentLength = e.nativeEvent.contentSize[isVertical ? 'height' : 'width'];
      this.scrollProperties.offset = e.nativeEvent.contentOffset[isVertical ? 'y' : 'x'];

      this._updateVisibleRows(e.nativeEvent.updatedChildFrames);

      if (!this._maybeCallOnEndReached(e)) {
        this._renderMoreRowsIfNeeded();
      }

      if (this.props.onEndReached && this._getDistanceFromEnd(this.scrollProperties) > this.props.onEndReachedThreshold) {
        this._sentEndForContentLength = null;
      }

      this.props.onScroll && this.props.onScroll(e);
    }
  });
  module.exports = ListView;
},"f7953c54cdefedbfed49ad61cce46031",["5af28862bae859ebbe0413bc29e556a2","9493a89f5d95c3a8a47c65cfed9b5542","e6db4f0efed6b72f641ef0ffed29569f","18eeaf4e01377a466daaccc6ba8ce6f5","1102b68d89d7a6aede9677567aa01362","ce21807d4d291be64fa852393519f6c8","aa8514022050149acc8c46c0b18dc75a","8e58ceb0d552a877aab98690121b12d7","2a3b918a2dbde47333fd60d9eb86ecb6","5c0ec7f0bbb23ef3f86807b9f50421dc","30a3b04291b6e1f01b778ff31271ccc5","b5bf80ec634bf7384bb40ddaeec9c818","29cb0e104e5fce198008f3e789631772","22f4957a8dfdf9b2393880addce36ff6","a02d3c2e8a09d16754c1a3b806847366","09babf511a081d9520406a63f452d2ef","8940a4ad43b101ffc23e725363c70f8d"],"ListView");