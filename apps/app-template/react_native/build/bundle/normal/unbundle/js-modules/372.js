__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  exports.__esModule = true;

  var _react = _require(_dependencyMap[0], 'react');

  var _react2 = _interopRequireDefault(_react);

  var _implementation = _require(_dependencyMap[1], './implementation');

  var _implementation2 = _interopRequireDefault(_implementation);

  function _interopRequireDefault(obj) {
    return obj && obj.__esModule ? obj : {
      default: obj
    };
  }

  exports.default = _react2.default.createContext || _implementation2.default;
  module.exports = exports['default'];
},372,[12,373],"node_modules/create-react-context/lib/index.js");