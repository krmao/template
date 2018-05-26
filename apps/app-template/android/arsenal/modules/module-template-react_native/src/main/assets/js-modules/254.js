__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var performanceNow = _require(_dependencyMap[0], 'fbjs/lib/performanceNow');

  var warning = _require(_dependencyMap[1], 'fbjs/lib/warning');

  var Info = function Info() {
    babelHelpers.classCallCheck(this, Info);
    this.any_blank_count = 0;
    this.any_blank_ms = 0;
    this.any_blank_speed_sum = 0;
    this.mostly_blank_count = 0;
    this.mostly_blank_ms = 0;
    this.pixels_blank = 0;
    this.pixels_sampled = 0;
    this.pixels_scrolled = 0;
    this.total_time_spent = 0;
    this.sample_count = 0;
  };

  var DEBUG = false;
  var _listeners = [];
  var _minSampleCount = 10;

  var _sampleRate = DEBUG ? 1 : null;

  var FillRateHelper = function () {
    babelHelpers.createClass(FillRateHelper, null, [{
      key: "addListener",
      value: function addListener(callback) {
        warning(_sampleRate !== null, 'Call `FillRateHelper.setSampleRate` before `addListener`.');

        _listeners.push(callback);

        return {
          remove: function remove() {
            _listeners = _listeners.filter(function (listener) {
              return callback !== listener;
            });
          }
        };
      }
    }, {
      key: "setSampleRate",
      value: function setSampleRate(sampleRate) {
        _sampleRate = sampleRate;
      }
    }, {
      key: "setMinSampleCount",
      value: function setMinSampleCount(minSampleCount) {
        _minSampleCount = minSampleCount;
      }
    }]);

    function FillRateHelper(getFrameMetrics) {
      babelHelpers.classCallCheck(this, FillRateHelper);
      this._anyBlankStartTime = null;
      this._enabled = false;
      this._info = new Info();
      this._mostlyBlankStartTime = null;
      this._samplesStartTime = null;
      this._getFrameMetrics = getFrameMetrics;
      this._enabled = (_sampleRate || 0) > Math.random();

      this._resetData();
    }

    babelHelpers.createClass(FillRateHelper, [{
      key: "activate",
      value: function activate() {
        if (this._enabled && this._samplesStartTime == null) {
          DEBUG && console.debug('FillRateHelper: activate');
          this._samplesStartTime = performanceNow();
        }
      }
    }, {
      key: "deactivateAndFlush",
      value: function deactivateAndFlush() {
        if (!this._enabled) {
          return;
        }

        var start = this._samplesStartTime;

        if (start == null) {
          DEBUG && console.debug('FillRateHelper: bail on deactivate with no start time');
          return;
        }

        if (this._info.sample_count < _minSampleCount) {
          this._resetData();

          return;
        }

        var total_time_spent = performanceNow() - start;
        var info = babelHelpers.extends({}, this._info, {
          total_time_spent: total_time_spent
        });

        if (DEBUG) {
          var derived = {
            avg_blankness: this._info.pixels_blank / this._info.pixels_sampled,
            avg_speed: this._info.pixels_scrolled / (total_time_spent / 1000),
            avg_speed_when_any_blank: this._info.any_blank_speed_sum / this._info.any_blank_count,
            any_blank_per_min: this._info.any_blank_count / (total_time_spent / 1000 / 60),
            any_blank_time_frac: this._info.any_blank_ms / total_time_spent,
            mostly_blank_per_min: this._info.mostly_blank_count / (total_time_spent / 1000 / 60),
            mostly_blank_time_frac: this._info.mostly_blank_ms / total_time_spent
          };

          for (var key in derived) {
            derived[key] = Math.round(1000 * derived[key]) / 1000;
          }

          console.debug('FillRateHelper deactivateAndFlush: ', {
            derived: derived,
            info: info
          });
        }

        _listeners.forEach(function (listener) {
          return listener(info);
        });

        this._resetData();
      }
    }, {
      key: "computeBlankness",
      value: function computeBlankness(props, state, scrollMetrics) {
        if (!this._enabled || props.getItemCount(props.data) === 0 || this._samplesStartTime == null) {
          return 0;
        }

        var dOffset = scrollMetrics.dOffset,
            offset = scrollMetrics.offset,
            velocity = scrollMetrics.velocity,
            visibleLength = scrollMetrics.visibleLength;
        this._info.sample_count++;
        this._info.pixels_sampled += Math.round(visibleLength);
        this._info.pixels_scrolled += Math.round(Math.abs(dOffset));
        var scrollSpeed = Math.round(Math.abs(velocity) * 1000);
        var now = performanceNow();

        if (this._anyBlankStartTime != null) {
          this._info.any_blank_ms += now - this._anyBlankStartTime;
        }

        this._anyBlankStartTime = null;

        if (this._mostlyBlankStartTime != null) {
          this._info.mostly_blank_ms += now - this._mostlyBlankStartTime;
        }

        this._mostlyBlankStartTime = null;
        var blankTop = 0;
        var first = state.first;

        var firstFrame = this._getFrameMetrics(first);

        while (first <= state.last && (!firstFrame || !firstFrame.inLayout)) {
          firstFrame = this._getFrameMetrics(first);
          first++;
        }

        if (firstFrame && first > 0) {
          blankTop = Math.min(visibleLength, Math.max(0, firstFrame.offset - offset));
        }

        var blankBottom = 0;
        var last = state.last;

        var lastFrame = this._getFrameMetrics(last);

        while (last >= state.first && (!lastFrame || !lastFrame.inLayout)) {
          lastFrame = this._getFrameMetrics(last);
          last--;
        }

        if (lastFrame && last < props.getItemCount(props.data) - 1) {
          var bottomEdge = lastFrame.offset + lastFrame.length;
          blankBottom = Math.min(visibleLength, Math.max(0, offset + visibleLength - bottomEdge));
        }

        var pixels_blank = Math.round(blankTop + blankBottom);
        var blankness = pixels_blank / visibleLength;

        if (blankness > 0) {
          this._anyBlankStartTime = now;
          this._info.any_blank_speed_sum += scrollSpeed;
          this._info.any_blank_count++;
          this._info.pixels_blank += pixels_blank;

          if (blankness > 0.5) {
            this._mostlyBlankStartTime = now;
            this._info.mostly_blank_count++;
          }
        } else if (scrollSpeed < 0.01 || Math.abs(dOffset) < 1) {
          this.deactivateAndFlush();
        }

        return blankness;
      }
    }, {
      key: "enabled",
      value: function enabled() {
        return this._enabled;
      }
    }, {
      key: "_resetData",
      value: function _resetData() {
        this._anyBlankStartTime = null;
        this._info = new Info();
        this._mostlyBlankStartTime = null;
        this._samplesStartTime = null;
      }
    }]);
    return FillRateHelper;
  }();

  module.exports = FillRateHelper;
},254,[33,19],"FillRateHelper");