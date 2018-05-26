__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var Blob = _require2(_dependencyMap[0], 'Blob');

  var BlobRegistry = _require2(_dependencyMap[1], 'BlobRegistry');

  var _require = _require2(_dependencyMap[2], 'NativeModules'),
      BlobModule = _require.BlobModule;

  function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      var r = Math.random() * 16 | 0,
          v = c == 'x' ? r : r & 0x3 | 0x8;
      return v.toString(16);
    });
  }

  var BlobManager = function () {
    function BlobManager() {
      babelHelpers.classCallCheck(this, BlobManager);
    }

    babelHelpers.createClass(BlobManager, null, [{
      key: "createFromParts",
      value: function createFromParts(parts, options) {
        var blobId = uuidv4();
        var items = parts.map(function (part) {
          if (part instanceof ArrayBuffer || global.ArrayBufferView && part instanceof global.ArrayBufferView) {
            throw new Error("Creating blobs from 'ArrayBuffer' and 'ArrayBufferView' are not supported");
          }

          if (part instanceof Blob) {
            return {
              data: part.data,
              type: 'blob'
            };
          } else {
            return {
              data: String(part),
              type: 'string'
            };
          }
        });
        var size = items.reduce(function (acc, curr) {
          if (curr.type === 'string') {
            return acc + global.unescape(encodeURI(curr.data)).length;
          } else {
            return acc + curr.data.size;
          }
        }, 0);
        BlobModule.createFromParts(items, blobId);
        return BlobManager.createFromOptions({
          blobId: blobId,
          offset: 0,
          size: size,
          type: options ? options.type : '',
          lastModified: options ? options.lastModified : Date.now()
        });
      }
    }, {
      key: "createFromOptions",
      value: function createFromOptions(options) {
        BlobRegistry.register(options.blobId);
        return babelHelpers.extends(Object.create(Blob.prototype), {
          data: options
        });
      }
    }, {
      key: "release",
      value: function release(blobId) {
        BlobRegistry.unregister(blobId);

        if (BlobRegistry.has(blobId)) {
          return;
        }

        BlobModule.release(blobId);
      }
    }, {
      key: "addNetworkingHandler",
      value: function addNetworkingHandler() {
        BlobModule.addNetworkingHandler();
      }
    }, {
      key: "addWebSocketHandler",
      value: function addWebSocketHandler(socketId) {
        BlobModule.addWebSocketHandler(socketId);
      }
    }, {
      key: "removeWebSocketHandler",
      value: function removeWebSocketHandler(socketId) {
        BlobModule.removeWebSocketHandler(socketId);
      }
    }, {
      key: "sendOverSocket",
      value: function sendOverSocket(blob, socketId) {
        BlobModule.sendOverSocket(blob.data, socketId);
      }
    }]);
    return BlobManager;
  }();

  BlobManager.isAvailable = !!BlobModule;
  module.exports = BlobManager;
},87,[86,88,24],"BlobManager");