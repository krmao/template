__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var getDevServer = _require2(_dependencyMap[0], 'getDevServer');

  var _require = _require2(_dependencyMap[1], 'NativeModules'),
      SourceCode = _require.SourceCode;

  var fetch = void 0;

  function isSourcedFromDisk(sourcePath) {
    return !/^http/.test(sourcePath) && /[\\/]/.test(sourcePath);
  }

  function symbolicateStackTrace(stack) {
    var devServer, stackCopy, foundInternalSource, response, json;
    return regeneratorRuntime.async(function symbolicateStackTrace$(_context) {
      while (1) {
        switch (_context.prev = _context.next) {
          case 0:
            if (!fetch) {
              fetch = global.fetch || _require2(_dependencyMap[2], 'fetch').fetch;
            }

            devServer = getDevServer();

            if (devServer.bundleLoadedFromServer) {
              _context.next = 4;
              break;
            }

            throw new Error('Bundle was not loaded from the packager');

          case 4:
            stackCopy = stack;

            if (SourceCode.scriptURL) {
              foundInternalSource = false;
              stackCopy = stack.map(function (frame) {
                if (!foundInternalSource && isSourcedFromDisk(frame.file)) {
                  return babelHelpers.extends({}, frame, {
                    file: SourceCode.scriptURL
                  });
                }

                foundInternalSource = true;
                return frame;
              });
            }

            _context.next = 8;
            return regeneratorRuntime.awrap(fetch(devServer.url + 'symbolicate', {
              method: 'POST',
              body: JSON.stringify({
                stack: stackCopy
              })
            }));

          case 8:
            response = _context.sent;
            _context.next = 11;
            return regeneratorRuntime.awrap(response.json());

          case 11:
            json = _context.sent;
            return _context.abrupt("return", json.stack);

          case 13:
          case "end":
            return _context.stop();
        }
      }
    }, null, this);
  }

  module.exports = symbolicateStackTrace;
},"989261d5f68bc4ada2cf64b6bdcc3362",["760b6f2c9fa353738d851f7c59cb5365","ce21807d4d291be64fa852393519f6c8","bd82c605a16f8c2f5185d959b02a54dc"],"symbolicateStackTrace");