__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var TextInputState = _require(_dependencyMap[0], 'TextInputState');

  function dismissKeyboard() {
    TextInputState.blurTextInput(TextInputState.currentlyFocusedField());
  }

  module.exports = dismissKeyboard;
},234,[123],"dismissKeyboard");