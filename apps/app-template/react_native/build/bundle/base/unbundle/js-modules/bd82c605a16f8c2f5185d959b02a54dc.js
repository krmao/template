__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _whatwgFetch = _require(_dependencyMap[0], "whatwg-fetch");

  var _whatwgFetch2 = babelHelpers.interopRequireDefault(_whatwgFetch);

  if (_whatwgFetch2.default && _whatwgFetch2.default.fetch) {
    module.exports = _whatwgFetch2.default;
  } else {
    module.exports = {
      fetch: fetch,
      Headers: Headers,
      Request: Request,
      Response: Response
    };
  }
},"bd82c605a16f8c2f5185d959b02a54dc",["c5ed53acccc9bf89ed9697d3686274c9"],"fetch");