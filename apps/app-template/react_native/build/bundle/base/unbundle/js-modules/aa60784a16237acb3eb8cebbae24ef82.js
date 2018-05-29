__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var AnimatedImplementation = _require(_dependencyMap[0], 'AnimatedImplementation');

  var Image = _require(_dependencyMap[1], 'Image');

  var ScrollView = _require(_dependencyMap[2], 'ScrollView');

  var Text = _require(_dependencyMap[3], 'Text');

  var View = _require(_dependencyMap[4], 'View');

  var Animated = {
    View: AnimatedImplementation.createAnimatedComponent(View),
    Text: AnimatedImplementation.createAnimatedComponent(Text),
    Image: AnimatedImplementation.createAnimatedComponent(Image),
    ScrollView: AnimatedImplementation.createAnimatedComponent(ScrollView)
  };
  babelHelpers.extends(Animated, AnimatedImplementation);
  module.exports = Animated;
},"aa60784a16237acb3eb8cebbae24ef82",["a5a3455ec2263330f8d878126eaa4345","717234c0b5cb768e5677b97d7b48fff8","aa8514022050149acc8c46c0b18dc75a","c03ca8878a60b3cdaf32e10931ff258d","30a3b04291b6e1f01b778ff31271ccc5"],"Animated");