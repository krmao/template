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
},186,[46,134,129,141,154],"TextPropTypes");