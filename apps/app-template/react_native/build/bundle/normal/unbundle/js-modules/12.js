__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  if (process.env.NODE_ENV === 'production') {
    module.exports = _require(_dependencyMap[0], './cjs/react.production.min.js');
  } else {
    module.exports = _require(_dependencyMap[1], './cjs/react.development.js');
  }
},12,[13,17],"node_modules/react/index.js");