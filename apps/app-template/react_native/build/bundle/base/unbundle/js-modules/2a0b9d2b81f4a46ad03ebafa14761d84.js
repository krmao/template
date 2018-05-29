__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Experimental/SwipeableRow/SwipeableFlatList.js";

  var PropTypes = _require(_dependencyMap[0], 'prop-types');

  var React = _require(_dependencyMap[1], 'React');

  var SwipeableRow = _require(_dependencyMap[2], 'SwipeableRow');

  var FlatList = _require(_dependencyMap[3], 'FlatList');

  var SwipeableFlatList = function (_React$Component) {
    babelHelpers.inherits(SwipeableFlatList, _React$Component);

    function SwipeableFlatList(props, context) {
      babelHelpers.classCallCheck(this, SwipeableFlatList);

      var _this = babelHelpers.possibleConstructorReturn(this, (SwipeableFlatList.__proto__ || Object.getPrototypeOf(SwipeableFlatList)).call(this, props, context));

      _this._flatListRef = null;
      _this._shouldBounceFirstRowOnMount = false;

      _this._onScroll = function (e) {
        if (_this.state.openRowKey) {
          _this.setState({
            openRowKey: null
          });
        }

        _this.props.onScroll && _this.props.onScroll(e);
      };

      _this._renderItem = function (info) {
        var slideoutView = _this.props.renderQuickActions(info);

        var key = _this.props.keyExtractor(info.item, info.index);

        if (!slideoutView) {
          return _this.props.renderItem(info);
        }

        var shouldBounceOnMount = false;

        if (_this._shouldBounceFirstRowOnMount) {
          _this._shouldBounceFirstRowOnMount = false;
          shouldBounceOnMount = true;
        }

        return React.createElement(
          SwipeableRow,
          {
            slideoutView: slideoutView,
            isOpen: key === _this.state.openRowKey,
            maxSwipeDistance: _this._getMaxSwipeDistance(info),
            onOpen: function onOpen() {
              return _this._onOpen(key);
            },
            onClose: function onClose() {
              return _this._onClose(key);
            },
            shouldBounceOnMount: shouldBounceOnMount,
            onSwipeEnd: _this._setListViewScrollable,
            onSwipeStart: _this._setListViewNotScrollable,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 135
            }
          },
          _this.props.renderItem(info)
        );
      };

      _this._setListViewScrollable = function () {
        _this._setListViewScrollableTo(true);
      };

      _this._setListViewNotScrollable = function () {
        _this._setListViewScrollableTo(false);
      };

      _this.state = {
        openRowKey: null
      };
      _this._shouldBounceFirstRowOnMount = _this.props.bounceFirstRowOnMount;
      return _this;
    }

    babelHelpers.createClass(SwipeableFlatList, [{
      key: "render",
      value: function render() {
        var _this2 = this;

        return React.createElement(FlatList, babelHelpers.extends({}, this.props, {
          ref: function ref(_ref) {
            _this2._flatListRef = _ref;
          },
          onScroll: this._onScroll,
          renderItem: this._renderItem,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 97
          }
        }));
      }
    }, {
      key: "_getMaxSwipeDistance",
      value: function _getMaxSwipeDistance(info) {
        if (typeof this.props.maxSwipeDistance === 'function') {
          return this.props.maxSwipeDistance(info);
        }

        return this.props.maxSwipeDistance;
      }
    }, {
      key: "_setListViewScrollableTo",
      value: function _setListViewScrollableTo(value) {
        if (this._flatListRef) {
          this._flatListRef.setNativeProps({
            scrollEnabled: value
          });
        }
      }
    }, {
      key: "_onOpen",
      value: function _onOpen(key) {
        this.setState({
          openRowKey: key
        });
      }
    }, {
      key: "_onClose",
      value: function _onClose(key) {
        this.setState({
          openRowKey: null
        });
      }
    }]);
    return SwipeableFlatList;
  }(React.Component);

  SwipeableFlatList.propTypes = babelHelpers.extends({}, FlatList.propTypes, {
    bounceFirstRowOnMount: PropTypes.bool.isRequired,
    maxSwipeDistance: PropTypes.oneOfType([PropTypes.number, PropTypes.func]).isRequired,
    renderQuickActions: PropTypes.func.isRequired
  });
  SwipeableFlatList.defaultProps = babelHelpers.extends({}, FlatList.defaultProps, {
    bounceFirstRowOnMount: true,
    renderQuickActions: function renderQuickActions() {
      return null;
    }
  });
  module.exports = SwipeableFlatList;
},"2a0b9d2b81f4a46ad03ebafa14761d84",["18eeaf4e01377a466daaccc6ba8ce6f5","e6db4f0efed6b72f641ef0ffed29569f","c5007eec13dd84314183ba16a055e41c","4e300ae15b8ba38c3d72e292b852c3f1"],"SwipeableFlatList");