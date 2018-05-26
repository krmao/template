__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var JSDevSupportModule = {
    getJSHierarchy: function getJSHierarchy(tag) {
      var hook = window.__REACT_DEVTOOLS_GLOBAL_HOOK__;
      var renderers = hook._renderers;
      var keys = Object.keys(renderers);
      var renderer = renderers[keys[0]];
      var result = renderer.getInspectorDataForViewTag(tag);
      var path = result.hierarchy.map(function (item) {
        return item.name;
      }).join(' -> ');

      _require(_dependencyMap[0], 'NativeModules').JSDevSupport.setResult(path, null);
    }
  };
  module.exports = JSDevSupportModule;
},106,[24],"JSDevSupportModule");