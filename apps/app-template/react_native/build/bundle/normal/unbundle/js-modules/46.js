__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var normalizeColor = _require(_dependencyMap[0], 'normalizeColor');

  var colorPropType = function colorPropType(isRequired, props, propName, componentName, location, propFullName) {
    var color = props[propName];

    if (color === undefined || color === null) {
      if (isRequired) {
        return new Error('Required ' + location + ' `' + (propFullName || propName) + '` was not specified in `' + componentName + '`.');
      }

      return;
    }

    if (typeof color === 'number') {
      return;
    }

    if (normalizeColor(color) === null) {
      return new Error('Invalid ' + location + ' `' + (propFullName || propName) + '` supplied to `' + componentName + '`: ' + color + '\n' + "Valid color formats are\n  - '#f0f' (#rgb)\n  - '#f0fc' (#rgba)\n  - '#ff00ff' (#rrggbb)\n  - '#ff00ff00' (#rrggbbaa)\n  - 'rgb(255, 255, 255)'\n  - 'rgba(255, 255, 255, 1.0)'\n  - 'hsl(360, 100%, 100%)'\n  - 'hsla(360, 100%, 100%, 1.0)'\n  - 'transparent'\n  - 'red'\n  - 0xff00ff00 (0xrrggbbaa)\n");
    }
  };

  var ColorPropType = colorPropType.bind(null, false);
  ColorPropType.isRequired = colorPropType.bind(null, true);
  module.exports = ColorPropType;
},46,[47],"ColorPropType");