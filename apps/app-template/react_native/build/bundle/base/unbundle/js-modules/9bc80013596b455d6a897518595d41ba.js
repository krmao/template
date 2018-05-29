__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var TextInputState = _require(_dependencyMap[0], 'TextInputState');

  function dismissKeyboard() {
    TextInputState.blurTextInput(TextInputState.currentlyFocusedField());
  }

  module.exports = dismissKeyboard;
},"9bc80013596b455d6a897518595d41ba",["a54fbb2d9dfc450c68d1d302d42f00e1"],"dismissKeyboard");