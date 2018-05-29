__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ColorPropType = _require(_dependencyMap[0], 'ColorPropType');

  var EdgeInsetsPropType = _require(_dependencyMap[1], 'EdgeInsetsPropType');

  var PropTypes = _require(_dependencyMap[2], 'prop-types');

  var StyleSheetPropType = _require(_dependencyMap[3], 'StyleSheetPropType');

  var TextStylePropTypes = _require(_dependencyMap[4], 'TextStylePropTypes');

  var stylePropType = StyleSheetPropType(TextStylePropTypes);
  module.exports = {
    ellipsizeMode: PropTypes.oneOf(['head', 'middle', 'tail', 'clip']),
    numberOfLines: PropTypes.number,
    textBreakStrategy: PropTypes.oneOf(['simple', 'highQuality', 'balanced']),
    onLayout: PropTypes.func,
    onPress: PropTypes.func,
    onLongPress: PropTypes.func,
    pressRetentionOffset: EdgeInsetsPropType,
    selectable: PropTypes.bool,
    selectionColor: ColorPropType,
    suppressHighlighting: PropTypes.bool,
    style: stylePropType,
    testID: PropTypes.string,
    nativeID: PropTypes.string,
    allowFontScaling: PropTypes.bool,
    accessible: PropTypes.bool,
    adjustsFontSizeToFit: PropTypes.bool,
    minimumFontScale: PropTypes.number,
    disabled: PropTypes.bool
  };
},"98dc3d7a81acedddf558bd7f3ed6cba7",["63c61c7eda525c10d0670d2ef8475012","20099b775ac7bd546d3c34ceb85c88e4","18eeaf4e01377a466daaccc6ba8ce6f5","60dc775dcc40daa6b8d0b23f322ce91f","45e1de962d0658d8cb6946a3c4531cbf"],"TextPropTypes");