__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ReactPropTypes = _require(_dependencyMap[0], 'prop-types');

  var LayoutPropTypes = {
    display: ReactPropTypes.oneOf(['none', 'flex']),
    width: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    height: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    start: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    end: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    top: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    left: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    right: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    bottom: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    minWidth: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    maxWidth: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    minHeight: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    maxHeight: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    margin: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    marginVertical: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    marginHorizontal: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    marginTop: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    marginBottom: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    marginLeft: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    marginRight: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    marginStart: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    marginEnd: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    padding: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    paddingVertical: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    paddingHorizontal: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    paddingTop: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    paddingBottom: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    paddingLeft: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    paddingRight: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    paddingStart: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    paddingEnd: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    borderWidth: ReactPropTypes.number,
    borderTopWidth: ReactPropTypes.number,
    borderStartWidth: ReactPropTypes.number,
    borderEndWidth: ReactPropTypes.number,
    borderRightWidth: ReactPropTypes.number,
    borderBottomWidth: ReactPropTypes.number,
    borderLeftWidth: ReactPropTypes.number,
    position: ReactPropTypes.oneOf(['absolute', 'relative']),
    flexDirection: ReactPropTypes.oneOf(['row', 'row-reverse', 'column', 'column-reverse']),
    flexWrap: ReactPropTypes.oneOf(['wrap', 'nowrap']),
    justifyContent: ReactPropTypes.oneOf(['flex-start', 'flex-end', 'center', 'space-between', 'space-around', 'space-evenly']),
    alignItems: ReactPropTypes.oneOf(['flex-start', 'flex-end', 'center', 'stretch', 'baseline']),
    alignSelf: ReactPropTypes.oneOf(['auto', 'flex-start', 'flex-end', 'center', 'stretch', 'baseline']),
    alignContent: ReactPropTypes.oneOf(['flex-start', 'flex-end', 'center', 'stretch', 'space-between', 'space-around']),
    overflow: ReactPropTypes.oneOf(['visible', 'hidden', 'scroll']),
    flex: ReactPropTypes.number,
    flexGrow: ReactPropTypes.number,
    flexShrink: ReactPropTypes.number,
    flexBasis: ReactPropTypes.oneOfType([ReactPropTypes.number, ReactPropTypes.string]),
    aspectRatio: ReactPropTypes.number,
    zIndex: ReactPropTypes.number,
    direction: ReactPropTypes.oneOf(['inherit', 'ltr', 'rtl'])
  };
  module.exports = LayoutPropTypes;
},"8ea8f5ebe2044017e275a1899793cb51",["18eeaf4e01377a466daaccc6ba8ce6f5"],"LayoutPropTypes");