__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/ART/ReactNativeART.js";

  var Color = _require(_dependencyMap[0], 'art/core/color');

  var Path = _require(_dependencyMap[1], 'ARTSerializablePath');

  var Transform = _require(_dependencyMap[2], 'art/core/transform');

  var React = _require(_dependencyMap[3], 'React');

  var PropTypes = _require(_dependencyMap[4], 'prop-types');

  var ReactNativeViewAttributes = _require(_dependencyMap[5], 'ReactNativeViewAttributes');

  var createReactNativeComponentClass = _require(_dependencyMap[6], 'createReactNativeComponentClass');

  var merge = _require(_dependencyMap[7], 'merge');

  var invariant = _require(_dependencyMap[8], 'fbjs/lib/invariant');

  function arrayDiffer(a, b) {
    if (a == null || b == null) {
      return true;
    }

    if (a.length !== b.length) {
      return true;
    }

    for (var i = 0; i < a.length; i++) {
      if (a[i] !== b[i]) {
        return true;
      }
    }

    return false;
  }

  function fontAndLinesDiffer(a, b) {
    if (a === b) {
      return false;
    }

    if (a.font !== b.font) {
      if (a.font === null) {
        return true;
      }

      if (b.font === null) {
        return true;
      }

      if (a.font.fontFamily !== b.font.fontFamily || a.font.fontSize !== b.font.fontSize || a.font.fontWeight !== b.font.fontWeight || a.font.fontStyle !== b.font.fontStyle) {
        return true;
      }
    }

    return arrayDiffer(a.lines, b.lines);
  }

  var SurfaceViewAttributes = merge(ReactNativeViewAttributes.UIView, {});
  var NodeAttributes = {
    transform: {
      diff: arrayDiffer
    },
    opacity: true
  };
  var GroupAttributes = merge(NodeAttributes, {
    clipping: {
      diff: arrayDiffer
    }
  });
  var RenderableAttributes = merge(NodeAttributes, {
    fill: {
      diff: arrayDiffer
    },
    stroke: {
      diff: arrayDiffer
    },
    strokeWidth: true,
    strokeCap: true,
    strokeJoin: true,
    strokeDash: {
      diff: arrayDiffer
    }
  });
  var ShapeAttributes = merge(RenderableAttributes, {
    d: {
      diff: arrayDiffer
    }
  });
  var TextAttributes = merge(RenderableAttributes, {
    alignment: true,
    frame: {
      diff: fontAndLinesDiffer
    },
    path: {
      diff: arrayDiffer
    }
  });
  var NativeSurfaceView = createReactNativeComponentClass('ARTSurfaceView', function () {
    return {
      validAttributes: SurfaceViewAttributes,
      uiViewClassName: 'ARTSurfaceView'
    };
  });
  var NativeGroup = createReactNativeComponentClass('ARTGroup', function () {
    return {
      validAttributes: GroupAttributes,
      uiViewClassName: 'ARTGroup'
    };
  });
  var NativeShape = createReactNativeComponentClass('ARTShape', function () {
    return {
      validAttributes: ShapeAttributes,
      uiViewClassName: 'ARTShape'
    };
  });
  var NativeText = createReactNativeComponentClass('ARTText', function () {
    return {
      validAttributes: TextAttributes,
      uiViewClassName: 'ARTText'
    };
  });

  function childrenAsString(children) {
    if (!children) {
      return '';
    }

    if (typeof children === 'string') {
      return children;
    }

    if (children.length) {
      return children.join('\n');
    }

    return '';
  }

  var Surface = function (_React$Component) {
    babelHelpers.inherits(Surface, _React$Component);

    function Surface() {
      babelHelpers.classCallCheck(this, Surface);
      return babelHelpers.possibleConstructorReturn(this, (Surface.__proto__ || Object.getPrototypeOf(Surface)).apply(this, arguments));
    }

    babelHelpers.createClass(Surface, [{
      key: "getChildContext",
      value: function getChildContext() {
        return {
          isInSurface: true
        };
      }
    }, {
      key: "render",
      value: function render() {
        var props = this.props;
        var w = extractNumber(props.width, 0);
        var h = extractNumber(props.height, 0);
        return React.createElement(
          NativeSurfaceView,
          {
            style: [props.style, {
              width: w,
              height: h
            }],
            __source: {
              fileName: _jsxFileName,
              lineNumber: 157
            }
          },
          this.props.children
        );
      }
    }]);
    return Surface;
  }(React.Component);

  Surface.childContextTypes = {
    isInSurface: PropTypes.bool
  };

  function extractNumber(value, defaultValue) {
    if (value == null) {
      return defaultValue;
    }

    return +value;
  }

  var pooledTransform = new Transform();

  function extractTransform(props) {
    var scaleX = props.scaleX != null ? props.scaleX : props.scale != null ? props.scale : 1;
    var scaleY = props.scaleY != null ? props.scaleY : props.scale != null ? props.scale : 1;
    pooledTransform.transformTo(1, 0, 0, 1, 0, 0).move(props.x || 0, props.y || 0).rotate(props.rotation || 0, props.originX, props.originY).scale(scaleX, scaleY, props.originX, props.originY);

    if (props.transform != null) {
      pooledTransform.transform(props.transform);
    }

    return [pooledTransform.xx, pooledTransform.yx, pooledTransform.xy, pooledTransform.yy, pooledTransform.x, pooledTransform.y];
  }

  function extractOpacity(props) {
    if (props.visible === false) {
      return 0;
    }

    if (props.opacity == null) {
      return 1;
    }

    return +props.opacity;
  }

  var Group = function (_React$Component2) {
    babelHelpers.inherits(Group, _React$Component2);

    function Group() {
      babelHelpers.classCallCheck(this, Group);
      return babelHelpers.possibleConstructorReturn(this, (Group.__proto__ || Object.getPrototypeOf(Group)).apply(this, arguments));
    }

    babelHelpers.createClass(Group, [{
      key: "render",
      value: function render() {
        var props = this.props;
        invariant(this.context.isInSurface, 'ART: <Group /> must be a child of a <Surface />');
        return React.createElement(
          NativeGroup,
          {
            opacity: extractOpacity(props),
            transform: extractTransform(props),
            __source: {
              fileName: _jsxFileName,
              lineNumber: 229
            }
          },
          this.props.children
        );
      }
    }]);
    return Group;
  }(React.Component);

  Group.contextTypes = {
    isInSurface: PropTypes.bool.isRequired
  };

  var ClippingRectangle = function (_React$Component3) {
    babelHelpers.inherits(ClippingRectangle, _React$Component3);

    function ClippingRectangle() {
      babelHelpers.classCallCheck(this, ClippingRectangle);
      return babelHelpers.possibleConstructorReturn(this, (ClippingRectangle.__proto__ || Object.getPrototypeOf(ClippingRectangle)).apply(this, arguments));
    }

    babelHelpers.createClass(ClippingRectangle, [{
      key: "render",
      value: function render() {
        var props = this.props;
        var x = extractNumber(props.x, 0);
        var y = extractNumber(props.y, 0);
        var w = extractNumber(props.width, 0);
        var h = extractNumber(props.height, 0);
        var clipping = [x, y, w, h];
        var propsExcludingXAndY = merge(props);
        delete propsExcludingXAndY.x;
        delete propsExcludingXAndY.y;
        return React.createElement(
          NativeGroup,
          {
            clipping: clipping,
            opacity: extractOpacity(props),
            transform: extractTransform(propsExcludingXAndY),
            __source: {
              fileName: _jsxFileName,
              lineNumber: 251
            }
          },
          this.props.children
        );
      }
    }]);
    return ClippingRectangle;
  }(React.Component);

  var SOLID_COLOR = 0;
  var LINEAR_GRADIENT = 1;
  var RADIAL_GRADIENT = 2;
  var PATTERN = 3;

  function insertColorIntoArray(color, targetArray, atIndex) {
    var c = new Color(color);
    targetArray[atIndex + 0] = c.red / 255;
    targetArray[atIndex + 1] = c.green / 255;
    targetArray[atIndex + 2] = c.blue / 255;
    targetArray[atIndex + 3] = c.alpha;
  }

  function insertColorsIntoArray(stops, targetArray, atIndex) {
    var i = 0;

    if ('length' in stops) {
      while (i < stops.length) {
        insertColorIntoArray(stops[i], targetArray, atIndex + i * 4);
        i++;
      }
    } else {
      for (var offset in stops) {
        insertColorIntoArray(stops[offset], targetArray, atIndex + i * 4);
        i++;
      }
    }

    return atIndex + i * 4;
  }

  function insertOffsetsIntoArray(stops, targetArray, atIndex, multi, reverse) {
    var offsetNumber;
    var i = 0;

    if ('length' in stops) {
      while (i < stops.length) {
        offsetNumber = i / (stops.length - 1) * multi;
        targetArray[atIndex + i] = reverse ? 1 - offsetNumber : offsetNumber;
        i++;
      }
    } else {
      for (var offsetString in stops) {
        offsetNumber = +offsetString * multi;
        targetArray[atIndex + i] = reverse ? 1 - offsetNumber : offsetNumber;
        i++;
      }
    }

    return atIndex + i;
  }

  function insertColorStopsIntoArray(stops, targetArray, atIndex) {
    var lastIndex = insertColorsIntoArray(stops, targetArray, atIndex);
    insertOffsetsIntoArray(stops, targetArray, lastIndex, 1, false);
  }

  function insertDoubleColorStopsIntoArray(stops, targetArray, atIndex) {
    var lastIndex = insertColorsIntoArray(stops, targetArray, atIndex);
    lastIndex = insertColorsIntoArray(stops, targetArray, lastIndex);
    lastIndex = insertOffsetsIntoArray(stops, targetArray, lastIndex, 0.5, false);
    insertOffsetsIntoArray(stops, targetArray, lastIndex, 0.5, true);
  }

  function applyBoundingBoxToBrushData(brushData, props) {
    var type = brushData[0];
    var width = +props.width;
    var height = +props.height;

    if (type === LINEAR_GRADIENT) {
      brushData[1] *= width;
      brushData[2] *= height;
      brushData[3] *= width;
      brushData[4] *= height;
    } else if (type === RADIAL_GRADIENT) {
      brushData[1] *= width;
      brushData[2] *= height;
      brushData[3] *= width;
      brushData[4] *= height;
      brushData[5] *= width;
      brushData[6] *= height;
    } else if (type === PATTERN) {}
  }

  function extractBrush(colorOrBrush, props) {
    if (colorOrBrush == null) {
      return null;
    }

    if (colorOrBrush._brush) {
      if (colorOrBrush._bb) {
        applyBoundingBoxToBrushData(colorOrBrush._brush, props);
        colorOrBrush._bb = false;
      }

      return colorOrBrush._brush;
    }

    var c = new Color(colorOrBrush);
    return [SOLID_COLOR, c.red / 255, c.green / 255, c.blue / 255, c.alpha];
  }

  function extractColor(color) {
    if (color == null) {
      return null;
    }

    var c = new Color(color);
    return [c.red / 255, c.green / 255, c.blue / 255, c.alpha];
  }

  function extractStrokeCap(strokeCap) {
    switch (strokeCap) {
      case 'butt':
        return 0;

      case 'square':
        return 2;

      default:
        return 1;
    }
  }

  function extractStrokeJoin(strokeJoin) {
    switch (strokeJoin) {
      case 'miter':
        return 0;

      case 'bevel':
        return 2;

      default:
        return 1;
    }
  }

  var Shape = function (_React$Component4) {
    babelHelpers.inherits(Shape, _React$Component4);

    function Shape() {
      babelHelpers.classCallCheck(this, Shape);
      return babelHelpers.possibleConstructorReturn(this, (Shape.__proto__ || Object.getPrototypeOf(Shape)).apply(this, arguments));
    }

    babelHelpers.createClass(Shape, [{
      key: "render",
      value: function render() {
        var props = this.props;
        var path = props.d || childrenAsString(props.children);
        var d = (path instanceof Path ? path : new Path(path)).toJSON();
        return React.createElement(NativeShape, {
          fill: extractBrush(props.fill, props),
          opacity: extractOpacity(props),
          stroke: extractColor(props.stroke),
          strokeCap: extractStrokeCap(props.strokeCap),
          strokeDash: props.strokeDash || null,
          strokeJoin: extractStrokeJoin(props.strokeJoin),
          strokeWidth: extractNumber(props.strokeWidth, 1),
          transform: extractTransform(props),
          d: d,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 399
          }
        });
      }
    }]);
    return Shape;
  }(React.Component);

  var cachedFontObjectsFromString = {};
  var fontFamilyPrefix = /^[\s"']*/;
  var fontFamilySuffix = /[\s"']*$/;

  function extractSingleFontFamily(fontFamilyString) {
    return fontFamilyString.split(',')[0].replace(fontFamilyPrefix, '').replace(fontFamilySuffix, '');
  }

  function parseFontString(font) {
    if (cachedFontObjectsFromString.hasOwnProperty(font)) {
      return cachedFontObjectsFromString[font];
    }

    var regexp = /^\s*((?:(?:normal|bold|italic)\s+)*)(?:(\d+(?:\.\d+)?)[ptexm\%]*(?:\s*\/.*?)?\s+)?\s*\"?([^\"]*)/i;
    var match = regexp.exec(font);

    if (!match) {
      return null;
    }

    var fontFamily = extractSingleFontFamily(match[3]);
    var fontSize = +match[2] || 12;
    var isBold = /bold/.exec(match[1]);
    var isItalic = /italic/.exec(match[1]);
    cachedFontObjectsFromString[font] = {
      fontFamily: fontFamily,
      fontSize: fontSize,
      fontWeight: isBold ? 'bold' : 'normal',
      fontStyle: isItalic ? 'italic' : 'normal'
    };
    return cachedFontObjectsFromString[font];
  }

  function extractFont(font) {
    if (font == null) {
      return null;
    }

    if (typeof font === 'string') {
      return parseFontString(font);
    }

    var fontFamily = extractSingleFontFamily(font.fontFamily);
    var fontSize = +font.fontSize || 12;
    var fontWeight = font.fontWeight != null ? font.fontWeight.toString() : '400';
    return {
      fontFamily: fontFamily,
      fontSize: fontSize,
      fontWeight: fontWeight,
      fontStyle: font.fontStyle
    };
  }

  var newLine = /\n/g;

  function extractFontAndLines(font, text) {
    return {
      font: extractFont(font),
      lines: text.split(newLine)
    };
  }

  function extractAlignment(alignment) {
    switch (alignment) {
      case 'right':
        return 1;

      case 'center':
        return 2;

      default:
        return 0;
    }
  }

  var Text = function (_React$Component5) {
    babelHelpers.inherits(Text, _React$Component5);

    function Text() {
      babelHelpers.classCallCheck(this, Text);
      return babelHelpers.possibleConstructorReturn(this, (Text.__proto__ || Object.getPrototypeOf(Text)).apply(this, arguments));
    }

    babelHelpers.createClass(Text, [{
      key: "render",
      value: function render() {
        var props = this.props;
        var path = props.path;
        var textPath = path ? (path instanceof Path ? path : new Path(path)).toJSON() : null;
        var textFrame = extractFontAndLines(props.font, childrenAsString(props.children));
        return React.createElement(NativeText, {
          fill: extractBrush(props.fill, props),
          opacity: extractOpacity(props),
          stroke: extractColor(props.stroke),
          strokeCap: extractStrokeCap(props.strokeCap),
          strokeDash: props.strokeDash || null,
          strokeJoin: extractStrokeJoin(props.strokeJoin),
          strokeWidth: extractNumber(props.strokeWidth, 1),
          transform: extractTransform(props),
          alignment: extractAlignment(props.alignment),
          frame: textFrame,
          path: textPath,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 498
          }
        });
      }
    }]);
    return Text;
  }(React.Component);

  function LinearGradient(stops, x1, y1, x2, y2) {
    var type = LINEAR_GRADIENT;

    if (arguments.length < 5) {
      var angle = (x1 == null ? 270 : x1) * Math.PI / 180;
      var x = Math.cos(angle);
      var y = -Math.sin(angle);
      var l = (Math.abs(x) + Math.abs(y)) / 2;
      x *= l;
      y *= l;
      x1 = 0.5 - x;
      x2 = 0.5 + x;
      y1 = 0.5 - y;
      y2 = 0.5 + y;
      this._bb = true;
    } else {
      this._bb = false;
    }

    var brushData = [type, +x1, +y1, +x2, +y2];
    insertColorStopsIntoArray(stops, brushData, 5);
    this._brush = brushData;
  }

  function RadialGradient(stops, fx, fy, rx, ry, cx, cy) {
    if (ry == null) {
      ry = rx;
    }

    if (cx == null) {
      cx = fx;
    }

    if (cy == null) {
      cy = fy;
    }

    if (fx == null) {
      fx = fy = rx = ry = cx = cy = 0.5;
      this._bb = true;
    } else {
      this._bb = false;
    }

    var brushData = [RADIAL_GRADIENT, +fx, +fy, +rx * 2, +ry * 2, +cx, +cy];
    insertDoubleColorStopsIntoArray(stops, brushData, 7);
    this._brush = brushData;
  }

  function Pattern(url, width, height, left, top) {
    this._brush = [PATTERN, url, +left || 0, +top || 0, +width, +height];
  }

  var ReactART = {
    LinearGradient: LinearGradient,
    RadialGradient: RadialGradient,
    Pattern: Pattern,
    Transform: Transform,
    Path: Path,
    Surface: Surface,
    Group: Group,
    ClippingRectangle: ClippingRectangle,
    Shape: Shape,
    Text: Text
  };
  module.exports = ReactART;
},"8d14c6b03bba86ef5985c909d17fc83e",["9a9afbe459d3e9fcdc7cd8f0952639a6","d09f9eb90729add943e86b1e930fbe11","ae7cd8236c7c54db7d84f0777ed82e73","e6db4f0efed6b72f641ef0ffed29569f","18eeaf4e01377a466daaccc6ba8ce6f5","6477887be0d285a967d42967386335cd","c24d6856e671b47ee028a906101ebc28","a02d3c2e8a09d16754c1a3b806847366","8940a4ad43b101ffc23e725363c70f8d"],"ReactNativeART");