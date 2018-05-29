__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  if (process.env.NODE_ENV === 'production') {
    module.exports = _require(_dependencyMap[0], './cjs/react.production.min.js');
  } else {
    module.exports = _require(_dependencyMap[1], './cjs/react.development.js');
  }
},"c42a5e17831e80ed1e1c8cf91f5ddb40",["f633b4df9bb24abbced2514340d9ed79","a0336f28b5d8fe3a1450da5e32b7a631"],"node_modules/react/index.js");