__d(function (global, _require, module, exports, _dependencyMap) {
  var Platform = _require(_dependencyMap[0], 'Platform');

  var TVViewPropTypes = {};

  if (Platform.isTV || Platform.OS === 'android') {
    TVViewPropTypes = _require(_dependencyMap[1], 'TVViewPropTypes');
  }

  module.exports = TVViewPropTypes;
},"790dec3eba5569b328f412e9e7a6101d",["9493a89f5d95c3a8a47c65cfed9b5542","54816097bcd607377ccff2567a7d892a"],"PlatformViewPropTypes");