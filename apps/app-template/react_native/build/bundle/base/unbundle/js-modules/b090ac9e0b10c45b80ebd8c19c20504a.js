__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var FormData = function () {
    function FormData() {
      babelHelpers.classCallCheck(this, FormData);
      this._parts = [];
    }

    babelHelpers.createClass(FormData, [{
      key: "append",
      value: function append(key, value) {
        this._parts.push([key, value]);
      }
    }, {
      key: "getParts",
      value: function getParts() {
        return this._parts.map(function (_ref) {
          var _ref2 = babelHelpers.slicedToArray(_ref, 2),
              name = _ref2[0],
              value = _ref2[1];

          var contentDisposition = 'form-data; name="' + name + '"';
          var headers = {
            'content-disposition': contentDisposition
          };

          if (typeof value === 'object' && value) {
            if (typeof value.name === 'string') {
              headers['content-disposition'] += '; filename="' + value.name + '"';
            }

            if (typeof value.type === 'string') {
              headers['content-type'] = value.type;
            }

            return babelHelpers.extends({}, value, {
              headers: headers,
              fieldName: name
            });
          }

          return {
            string: String(value),
            headers: headers,
            fieldName: name
          };
        });
      }
    }]);
    return FormData;
  }();

  module.exports = FormData;
},"b090ac9e0b10c45b80ebd8c19c20504a",[],"FormData");