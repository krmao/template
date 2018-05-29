__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var invariant = _require(_dependencyMap[0], 'fbjs/lib/invariant');

  var ensurePositiveDelayProps = function ensurePositiveDelayProps(props) {
    invariant(!(props.delayPressIn < 0 || props.delayPressOut < 0 || props.delayLongPress < 0), 'Touchable components cannot have negative delay properties');
  };

  module.exports = ensurePositiveDelayProps;
},"6ba49cd3c30b475f38fef604b303c1ac",["8940a4ad43b101ffc23e725363c70f8d"],"ensurePositiveDelayProps");