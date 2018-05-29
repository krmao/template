__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var base64 = _require(_dependencyMap[0], 'base64-js');

  function binaryToBase64(data) {
    if (data instanceof ArrayBuffer) {
      data = new Uint8Array(data);
    }

    if (data instanceof Uint8Array) {
      return base64.fromByteArray(data);
    }

    if (!ArrayBuffer.isView(data)) {
      throw new Error('data must be ArrayBuffer or typed array');
    }

    var _data = data,
        buffer = _data.buffer,
        byteOffset = _data.byteOffset,
        byteLength = _data.byteLength;
    return base64.fromByteArray(new Uint8Array(buffer, byteOffset, byteLength));
  }

  module.exports = binaryToBase64;
},"2c5c0b8cc147ed9fc3c48f037910a196",["350961b4c78178ca4358ee1e601c5e2f"],"binaryToBase64");