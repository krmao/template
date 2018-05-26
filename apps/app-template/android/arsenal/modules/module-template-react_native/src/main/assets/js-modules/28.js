__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var invariant = _require(_dependencyMap[0], 'fbjs/lib/invariant');

  var TRACE_TAG_REACT_APPS = 1 << 17;
  var TRACE_TAG_JS_VM_CALLS = 1 << 27;
  var _enabled = false;
  var _asyncCookie = 0;
  var _markStack = [];

  var _markStackIndex = -1;

  var _canInstallReactHook = false;
  var REACT_MARKER = "\u269B";
  var userTimingPolyfill = __DEV__ ? {
    mark: function mark(markName) {
      if (_enabled) {
        _markStackIndex++;
        _markStack[_markStackIndex] = markName;
        var systraceLabel = markName;

        if (markName[0] === REACT_MARKER) {
          var indexOfId = markName.lastIndexOf(' (#');
          var cutoffIndex = indexOfId !== -1 ? indexOfId : markName.length;
          systraceLabel = markName.slice(2, cutoffIndex);
        }

        Systrace.beginEvent(systraceLabel);
      }
    },
    measure: function measure(measureName, startMark, endMark) {
      if (_enabled) {
        invariant(typeof measureName === 'string' && typeof startMark === 'string' && typeof endMark === 'undefined', 'Only performance.measure(string, string) overload is supported.');
        var topMark = _markStack[_markStackIndex];
        invariant(startMark === topMark, 'There was a mismatching performance.measure() call. ' + 'Expected "%s" but got "%s."', topMark, startMark);
        _markStackIndex--;
        Systrace.endEvent();
      }
    },
    clearMarks: function clearMarks(markName) {
      if (_enabled) {
        if (_markStackIndex === -1) {
          return;
        }

        if (markName === _markStack[_markStackIndex]) {
          if (userTimingPolyfill != null) {
            userTimingPolyfill.measure(markName, markName);
          }
        }
      }
    },
    clearMeasures: function clearMeasures() {}
  } : null;
  var Systrace = {
    installReactHook: function installReactHook() {
      if (_enabled) {
        if (__DEV__) {
          global.performance = userTimingPolyfill;
        }
      }

      _canInstallReactHook = true;
    },
    setEnabled: function setEnabled(enabled) {
      if (_enabled !== enabled) {
        if (__DEV__) {
          if (enabled) {
            global.nativeTraceBeginLegacy && global.nativeTraceBeginLegacy(TRACE_TAG_JS_VM_CALLS);
          } else {
            global.nativeTraceEndLegacy && global.nativeTraceEndLegacy(TRACE_TAG_JS_VM_CALLS);
          }

          if (_canInstallReactHook) {
            if (enabled && global.performance === undefined) {
              global.performance = userTimingPolyfill;
            }
          }
        }

        _enabled = enabled;
      }
    },
    isEnabled: function isEnabled() {
      return _enabled;
    },
    beginEvent: function beginEvent(profileName, args) {
      if (_enabled) {
        profileName = typeof profileName === 'function' ? profileName() : profileName;
        global.nativeTraceBeginSection(TRACE_TAG_REACT_APPS, profileName, args);
      }
    },
    endEvent: function endEvent() {
      if (_enabled) {
        global.nativeTraceEndSection(TRACE_TAG_REACT_APPS);
      }
    },
    beginAsyncEvent: function beginAsyncEvent(profileName) {
      var cookie = _asyncCookie;

      if (_enabled) {
        _asyncCookie++;
        profileName = typeof profileName === 'function' ? profileName() : profileName;
        global.nativeTraceBeginAsyncSection(TRACE_TAG_REACT_APPS, profileName, cookie);
      }

      return cookie;
    },
    endAsyncEvent: function endAsyncEvent(profileName, cookie) {
      if (_enabled) {
        profileName = typeof profileName === 'function' ? profileName() : profileName;
        global.nativeTraceEndAsyncSection(TRACE_TAG_REACT_APPS, profileName, cookie);
      }
    },
    counterEvent: function counterEvent(profileName, value) {
      if (_enabled) {
        profileName = typeof profileName === 'function' ? profileName() : profileName;
        global.nativeTraceCounter && global.nativeTraceCounter(TRACE_TAG_REACT_APPS, profileName, value);
      }
    }
  };

  if (__DEV__) {
    _require.Systrace = Systrace;
  }

  module.exports = Systrace;
},28,[18],"Systrace");