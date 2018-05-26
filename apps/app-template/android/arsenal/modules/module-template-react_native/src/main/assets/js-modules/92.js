__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Blob = _require(_dependencyMap[0], 'Blob');

  var invariant = _require(_dependencyMap[1], 'fbjs/lib/invariant');

  var File = function (_Blob) {
    babelHelpers.inherits(File, _Blob);

    function File(parts, name, options) {
      babelHelpers.classCallCheck(this, File);
      invariant(parts != null && name != null, 'Failed to construct `File`: Must pass both `parts` and `name` arguments.');

      var _this = babelHelpers.possibleConstructorReturn(this, (File.__proto__ || Object.getPrototypeOf(File)).call(this, parts, options));

      _this.data.name = name;
      return _this;
    }

    babelHelpers.createClass(File, [{
      key: "name",
      get: function get() {
        invariant(this.data.name != null, 'Files must have a name set.');
        return this.data.name;
      }
    }, {
      key: "lastModified",
      get: function get() {
        return this.data.lastModified || 0;
      }
    }]);
    return File;
  }(Blob);

  module.exports = File;
},92,[86,18],"File");