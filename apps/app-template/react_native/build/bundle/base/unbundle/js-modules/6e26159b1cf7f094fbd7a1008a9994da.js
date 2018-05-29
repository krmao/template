__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var binaryToBase64 = _require(_dependencyMap[0], 'binaryToBase64');

  var Blob = _require(_dependencyMap[1], 'Blob');

  var FormData = _require(_dependencyMap[2], 'FormData');

  function convertRequestBody(body) {
    if (typeof body === 'string') {
      return {
        string: body
      };
    }

    if (body instanceof Blob) {
      return {
        blob: body.data
      };
    }

    if (body instanceof FormData) {
      return {
        formData: body.getParts()
      };
    }

    if (body instanceof ArrayBuffer || ArrayBuffer.isView(body)) {
      return {
        base64: binaryToBase64(body)
      };
    }

    return body;
  }

  module.exports = convertRequestBody;
},"6e26159b1cf7f094fbd7a1008a9994da",["2c5c0b8cc147ed9fc3c48f037910a196","096007123c886a641b5b7e629326a442","b090ac9e0b10c45b80ebd8c19c20504a"],"convertRequestBody");