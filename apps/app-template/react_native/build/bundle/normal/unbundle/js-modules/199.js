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
},199,[200,227,228,185,173],"Animated");