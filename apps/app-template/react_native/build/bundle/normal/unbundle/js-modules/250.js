__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var React = _require(_dependencyMap[0], 'react');

  function cloneReferencedElement(element, config) {
    var cloneRef = config.ref;
    var originalRef = element.ref;

    for (var _len = arguments.length, children = Array(_len > 2 ? _len - 2 : 0), _key = 2; _key < _len; _key++) {
      children[_key - 2] = arguments[_key];
    }

    if (originalRef == null || cloneRef == null) {
      return React.cloneElement.apply(React, [element, config].concat(children));
    }

    if (typeof originalRef !== 'function') {
      if (__DEV__) {
        console.warn('Cloning an element with a ref that will be overwritten because it ' + 'is not a function. Use a composable callback-style ref instead. ' + 'Ignoring ref: ' + originalRef);
      }

      return React.cloneElement.apply(React, [element, config].concat(children));
    }

    return React.cloneElement.apply(React, [element, babelHelpers.extends({}, config, {
      ref: function ref(component) {
        cloneRef(component);
        originalRef(component);
      }
    })].concat(children));
  }

  module.exports = cloneReferencedElement;
},250,[12],"node_modules/react-clone-referenced-element/cloneReferencedElement.js");