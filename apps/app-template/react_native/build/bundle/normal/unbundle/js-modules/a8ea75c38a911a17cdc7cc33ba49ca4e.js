__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var invariant = _require(_dependencyMap[0], 'fbjs/lib/invariant');

  var ViewabilityHelper = function () {
    function ViewabilityHelper() {
      var config = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {
        viewAreaCoveragePercentThreshold: 0
      };
      babelHelpers.classCallCheck(this, ViewabilityHelper);
      this._hasInteracted = false;
      this._timers = new Set();
      this._viewableIndices = [];
      this._viewableItems = new Map();
      this._config = config;
    }

    babelHelpers.createClass(ViewabilityHelper, [{
      key: "dispose",
      value: function dispose() {
        this._timers.forEach(clearTimeout);
      }
    }, {
      key: "computeViewableItems",
      value: function computeViewableItems(itemCount, scrollOffset, viewportHeight, getFrameMetrics, renderRange) {
        var _config = this._config,
            itemVisiblePercentThreshold = _config.itemVisiblePercentThreshold,
            viewAreaCoveragePercentThreshold = _config.viewAreaCoveragePercentThreshold;
        var viewAreaMode = viewAreaCoveragePercentThreshold != null;
        var viewablePercentThreshold = viewAreaMode ? viewAreaCoveragePercentThreshold : itemVisiblePercentThreshold;
        invariant(viewablePercentThreshold != null && itemVisiblePercentThreshold != null !== (viewAreaCoveragePercentThreshold != null), 'Must set exactly one of itemVisiblePercentThreshold or viewAreaCoveragePercentThreshold');
        var viewableIndices = [];

        if (itemCount === 0) {
          return viewableIndices;
        }

        var firstVisible = -1;

        var _ref = renderRange || {
          first: 0,
          last: itemCount - 1
        },
            first = _ref.first,
            last = _ref.last;

        invariant(last < itemCount, 'Invalid render range ' + JSON.stringify({
          renderRange: renderRange,
          itemCount: itemCount
        }));

        for (var idx = first; idx <= last; idx++) {
          var metrics = getFrameMetrics(idx);

          if (!metrics) {
            continue;
          }

          var top = metrics.offset - scrollOffset;
          var bottom = top + metrics.length;

          if (top < viewportHeight && bottom > 0) {
            firstVisible = idx;

            if (_isViewable(viewAreaMode, viewablePercentThreshold, top, bottom, viewportHeight, metrics.length)) {
              viewableIndices.push(idx);
            }
          } else if (firstVisible >= 0) {
            break;
          }
        }

        return viewableIndices;
      }
    }, {
      key: "onUpdate",
      value: function onUpdate(itemCount, scrollOffset, viewportHeight, getFrameMetrics, createViewToken, onViewableItemsChanged, renderRange) {
        var _this = this;

        if (this._config.waitForInteraction && !this._hasInteracted || itemCount === 0 || !getFrameMetrics(0)) {
          return;
        }

        var viewableIndices = [];

        if (itemCount) {
          viewableIndices = this.computeViewableItems(itemCount, scrollOffset, viewportHeight, getFrameMetrics, renderRange);
        }

        if (this._viewableIndices.length === viewableIndices.length && this._viewableIndices.every(function (v, ii) {
          return v === viewableIndices[ii];
        })) {
          return;
        }

        this._viewableIndices = viewableIndices;

        if (this._config.minimumViewTime) {
          var handle = setTimeout(function () {
            _this._timers.delete(handle);

            _this._onUpdateSync(viewableIndices, onViewableItemsChanged, createViewToken);
          }, this._config.minimumViewTime);

          this._timers.add(handle);
        } else {
          this._onUpdateSync(viewableIndices, onViewableItemsChanged, createViewToken);
        }
      }
    }, {
      key: "resetViewableIndices",
      value: function resetViewableIndices() {
        this._viewableIndices = [];
      }
    }, {
      key: "recordInteraction",
      value: function recordInteraction() {
        this._hasInteracted = true;
      }
    }, {
      key: "_onUpdateSync",
      value: function _onUpdateSync(viewableIndicesToCheck, onViewableItemsChanged, createViewToken) {
        var _this2 = this;

        viewableIndicesToCheck = viewableIndicesToCheck.filter(function (ii) {
          return _this2._viewableIndices.includes(ii);
        });
        var prevItems = this._viewableItems;
        var nextItems = new Map(viewableIndicesToCheck.map(function (ii) {
          var viewable = createViewToken(ii, true);
          return [viewable.key, viewable];
        }));
        var changed = [];

        for (var _iterator = nextItems, _isArray = Array.isArray(_iterator), _i = 0, _iterator = _isArray ? _iterator : _iterator[typeof Symbol === "function" ? Symbol.iterator : "@@iterator"]();;) {
          var _ref4;

          if (_isArray) {
            if (_i >= _iterator.length) break;
            _ref4 = _iterator[_i++];
          } else {
            _i = _iterator.next();
            if (_i.done) break;
            _ref4 = _i.value;
          }

          var _ref2 = _ref4;

          var _ref3 = babelHelpers.slicedToArray(_ref2, 2);

          var _key = _ref3[0];
          var viewable = _ref3[1];

          if (!prevItems.has(_key)) {
            changed.push(viewable);
          }
        }

        for (var _iterator2 = prevItems, _isArray2 = Array.isArray(_iterator2), _i2 = 0, _iterator2 = _isArray2 ? _iterator2 : _iterator2[typeof Symbol === "function" ? Symbol.iterator : "@@iterator"]();;) {
          var _ref7;

          if (_isArray2) {
            if (_i2 >= _iterator2.length) break;
            _ref7 = _iterator2[_i2++];
          } else {
            _i2 = _iterator2.next();
            if (_i2.done) break;
            _ref7 = _i2.value;
          }

          var _ref5 = _ref7;

          var _ref6 = babelHelpers.slicedToArray(_ref5, 2);

          var _key2 = _ref6[0];
          var _viewable = _ref6[1];

          if (!nextItems.has(_key2)) {
            changed.push(babelHelpers.extends({}, _viewable, {
              isViewable: false
            }));
          }
        }

        if (changed.length > 0) {
          this._viewableItems = nextItems;
          onViewableItemsChanged({
            viewableItems: Array.from(nextItems.values()),
            changed: changed,
            viewabilityConfig: this._config
          });
        }
      }
    }]);
    return ViewabilityHelper;
  }();

  function _isViewable(viewAreaMode, viewablePercentThreshold, top, bottom, viewportHeight, itemLength) {
    if (_isEntirelyVisible(top, bottom, viewportHeight)) {
      return true;
    } else {
      var pixels = _getPixelsVisible(top, bottom, viewportHeight);

      var percent = 100 * (viewAreaMode ? pixels / viewportHeight : pixels / itemLength);
      return percent >= viewablePercentThreshold;
    }
  }

  function _getPixelsVisible(top, bottom, viewportHeight) {
    var visibleHeight = Math.min(bottom, viewportHeight) - Math.max(top, 0);
    return Math.max(0, visibleHeight);
  }

  function _isEntirelyVisible(top, bottom, viewportHeight) {
    return top >= 0 && bottom <= viewportHeight && bottom > top;
  }

  module.exports = ViewabilityHelper;
},"a8ea75c38a911a17cdc7cc33ba49ca4e",["8940a4ad43b101ffc23e725363c70f8d"],"ViewabilityHelper");