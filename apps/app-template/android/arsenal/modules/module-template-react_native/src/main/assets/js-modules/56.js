__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  function isNode(object) {
    var doc = object ? object.ownerDocument || object : document;
    var defaultView = doc.defaultView || window;
    return !!(object && (typeof defaultView.Node === 'function' ? object instanceof defaultView.Node : typeof object === 'object' && typeof object.nodeType === 'number' && typeof object.nodeName === 'string'));
  }

  module.exports = isNode;
},56,[],"node_modules/fbjs/lib/isNode.js");