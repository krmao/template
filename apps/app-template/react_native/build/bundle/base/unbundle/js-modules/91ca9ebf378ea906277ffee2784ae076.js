__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  function resolveBoxStyle(prefix, style) {
    var res = {};
    var subs = ['top', 'left', 'bottom', 'right'];
    var set = false;
    subs.forEach(function (sub) {
      res[sub] = style[prefix] || 0;
    });

    if (style[prefix]) {
      set = true;
    }

    if (style[prefix + 'Vertical']) {
      res.top = res.bottom = style[prefix + 'Vertical'];
      set = true;
    }

    if (style[prefix + 'Horizontal']) {
      res.left = res.right = style[prefix + 'Horizontal'];
      set = true;
    }

    subs.forEach(function (sub) {
      var val = style[prefix + capFirst(sub)];

      if (val) {
        res[sub] = val;
        set = true;
      }
    });

    if (!set) {
      return;
    }

    return res;
  }

  function capFirst(text) {
    return text[0].toUpperCase() + text.slice(1);
  }

  module.exports = resolveBoxStyle;
},"91ca9ebf378ea906277ffee2784ae076",[],"resolveBoxStyle");