__d(function (global, _require, module, exports, _dependencyMap) {
  var Platform = _require(_dependencyMap[0], 'Platform');

  var TVViewPropTypes = {};

  if (Platform.isTV || Platform.OS === 'android') {
    TVViewPropTypes = _require(_dependencyMap[1], 'TVViewPropTypes');
  }

  module.exports = TVViewPropTypes;
},139,[32,140],"PlatformViewPropTypes");