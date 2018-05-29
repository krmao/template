__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var Platform = _require(_dependencyMap[0], 'Platform');

  var UIManager = _require(_dependencyMap[1], 'UIManager');

  var TextInputState = {
    _currentlyFocusedID: null,
    currentlyFocusedField: function currentlyFocusedField() {
      return this._currentlyFocusedID;
    },
    focusTextInput: function focusTextInput(textFieldID) {
      if (this._currentlyFocusedID !== textFieldID && textFieldID !== null) {
        this._currentlyFocusedID = textFieldID;

        if (Platform.OS === 'ios') {
          UIManager.focus(textFieldID);
        } else if (Platform.OS === 'android') {
          UIManager.dispatchViewManagerCommand(textFieldID, UIManager.AndroidTextInput.Commands.focusTextInput, null);
        }
      }
    },
    blurTextInput: function blurTextInput(textFieldID) {
      if (this._currentlyFocusedID === textFieldID && textFieldID !== null) {
        this._currentlyFocusedID = null;

        if (Platform.OS === 'ios') {
          UIManager.blur(textFieldID);
        } else if (Platform.OS === 'android') {
          UIManager.dispatchViewManagerCommand(textFieldID, UIManager.AndroidTextInput.Commands.blurTextInput, null);
        }
      }
    }
  };
  module.exports = TextInputState;
},"a54fbb2d9dfc450c68d1d302d42f00e1",["9493a89f5d95c3a8a47c65cfed9b5542","467cd3365342d9aaa2e941fe7ace641c"],"TextInputState");