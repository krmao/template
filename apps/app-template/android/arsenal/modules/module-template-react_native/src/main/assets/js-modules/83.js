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
},83,[84,86,89],"convertRequestBody");