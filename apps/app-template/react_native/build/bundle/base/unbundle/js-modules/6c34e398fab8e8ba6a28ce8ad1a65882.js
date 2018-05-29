__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  exports._TESTING_ONLY_normalize_keys = _TESTING_ONLY_normalize_keys;
  exports.generateKey = generateKey;
  var uniqueBaseId = "id-" + Date.now();
  var uuidCount = 0;

  function _TESTING_ONLY_normalize_keys() {
    uniqueBaseId = 'id';
    uuidCount = 0;
  }

  function generateKey() {
    return uniqueBaseId + "-" + uuidCount++;
  }
},"6c34e398fab8e8ba6a28ce8ad1a65882",[],"node_modules/react-navigation/src/routers/KeyGenerator.js");