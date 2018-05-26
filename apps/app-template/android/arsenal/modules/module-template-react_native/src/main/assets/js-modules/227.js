__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Image/Image.android.js";

  var ImageResizeMode = _require2(_dependencyMap[0], 'ImageResizeMode');

  var ImageStylePropTypes = _require2(_dependencyMap[1], 'ImageStylePropTypes');

  var NativeMethodsMixin = _require2(_dependencyMap[2], 'NativeMethodsMixin');

  var NativeModules = _require2(_dependencyMap[3], 'NativeModules');

  var React = _require2(_dependencyMap[4], 'React');

  var PropTypes = _require2(_dependencyMap[5], 'prop-types');

  var ReactNativeViewAttributes = _require2(_dependencyMap[6], 'ReactNativeViewAttributes');

  var StyleSheet = _require2(_dependencyMap[7], 'StyleSheet');

  var StyleSheetPropType = _require2(_dependencyMap[8], 'StyleSheetPropType');

  var ViewPropTypes = _require2(_dependencyMap[9], 'ViewPropTypes');

  var createReactClass = _require2(_dependencyMap[10], 'create-react-class');

  var flattenStyle = _require2(_dependencyMap[11], 'flattenStyle');

  var merge = _require2(_dependencyMap[12], 'merge');

  var requireNativeComponent = _require2(_dependencyMap[13], 'requireNativeComponent');

  var resolveAssetSource = _require2(_dependencyMap[14], 'resolveAssetSource');

  var _require = _require2(_dependencyMap[15], 'ViewContext'),
      ViewContextTypes = _require.ViewContextTypes;

  var ImageLoader = NativeModules.ImageLoader;
  var _requestId = 1;

  function generateRequestId() {
    return _requestId++;
  }

  var Image = createReactClass({
    displayName: 'Image',
    propTypes: babelHelpers.extends({}, ViewPropTypes, {
      style: StyleSheetPropType(ImageStylePropTypes),
      source: PropTypes.oneOfType([PropTypes.shape({
        uri: PropTypes.string,
        headers: PropTypes.objectOf(PropTypes.string)
      }), PropTypes.number, PropTypes.arrayOf(PropTypes.shape({
        uri: PropTypes.string,
        width: PropTypes.number,
        height: PropTypes.number,
        headers: PropTypes.objectOf(PropTypes.string)
      }))]),
      blurRadius: PropTypes.number,
      loadingIndicatorSource: PropTypes.oneOfType([PropTypes.shape({
        uri: PropTypes.string
      }), PropTypes.number]),
      progressiveRenderingEnabled: PropTypes.bool,
      fadeDuration: PropTypes.number,
      onLoadStart: PropTypes.func,
      onError: PropTypes.func,
      onLoad: PropTypes.func,
      onLoadEnd: PropTypes.func,
      testID: PropTypes.string,
      resizeMethod: PropTypes.oneOf(['auto', 'resize', 'scale']),
      resizeMode: PropTypes.oneOf(['cover', 'contain', 'stretch', 'center'])
    }),
    statics: {
      resizeMode: ImageResizeMode,
      getSize: function getSize(url, success, failure) {
        return ImageLoader.getSize(url).then(function (sizes) {
          success(sizes.width, sizes.height);
        }).catch(failure || function () {
          console.warn('Failed to get size for image: ' + url);
        });
      },
      prefetch: function prefetch(url, callback) {
        var requestId = generateRequestId();
        callback && callback(requestId);
        return ImageLoader.prefetchImage(url, requestId);
      },
      abortPrefetch: function abortPrefetch(requestId) {
        ImageLoader.abortRequest(requestId);
      },
      queryCache: function queryCache(urls) {
        return regeneratorRuntime.async(function queryCache$(_context) {
          while (1) {
            switch (_context.prev = _context.next) {
              case 0:
                _context.next = 2;
                return regeneratorRuntime.awrap(ImageLoader.queryCache(urls));

              case 2:
                return _context.abrupt("return", _context.sent);

              case 3:
              case "end":
                return _context.stop();
            }
          }
        }, null, this);
      },
      resolveAssetSource: resolveAssetSource
    },
    mixins: [NativeMethodsMixin],
    viewConfig: {
      uiViewClassName: 'RCTView',
      validAttributes: ReactNativeViewAttributes.RCTView
    },
    contextTypes: ViewContextTypes,
    render: function render() {
      var source = resolveAssetSource(this.props.source);
      var loadingIndicatorSource = resolveAssetSource(this.props.loadingIndicatorSource);

      if (source && source.uri === '') {
        console.warn('source.uri should not be an empty string');
      }

      if (this.props.src) {
        console.warn('The <Image> component requires a `source` property rather than `src`.');
      }

      if (this.props.children) {
        throw new Error('The <Image> component cannot contain children. If you want to render content on top of the image, consider using the <ImageBackground> component or absolute positioning.');
      }

      if (source && (source.uri || Array.isArray(source))) {
        var style = void 0;
        var sources = void 0;

        if (source.uri) {
          var _width = source.width,
              _height = source.height;
          style = flattenStyle([{
            width: _width,
            height: _height
          }, styles.base, this.props.style]);
          sources = [{
            uri: source.uri
          }];
        } else {
          style = flattenStyle([styles.base, this.props.style]);
          sources = source;
        }

        var _props = this.props,
            onLoadStart = _props.onLoadStart,
            onLoad = _props.onLoad,
            onLoadEnd = _props.onLoadEnd,
            onError = _props.onError;
        var nativeProps = merge(this.props, {
          style: style,
          shouldNotifyLoadEvents: !!(onLoadStart || onLoad || onLoadEnd || onError),
          src: sources,
          headers: source.headers,
          loadingIndicatorSrc: loadingIndicatorSource ? loadingIndicatorSource.uri : null
        });

        if (this.context.isInAParentText) {
          return React.createElement(RCTTextInlineImage, babelHelpers.extends({}, nativeProps, {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 252
            }
          }));
        } else {
          return React.createElement(RKImage, babelHelpers.extends({}, nativeProps, {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 254
            }
          }));
        }
      }

      return null;
    }
  });
  var styles = StyleSheet.create({
    base: {
      overflow: 'hidden'
    }
  });
  var cfg = {
    nativeOnly: {
      src: true,
      headers: true,
      loadingIndicatorSrc: true,
      shouldNotifyLoadEvents: true
    }
  };
  var RKImage = requireNativeComponent('RCTImageView', Image, cfg);
  var RCTTextInlineImage = requireNativeComponent('RCTTextInlineImage', Image, cfg);
  module.exports = Image;
},227,[152,151,48,24,132,129,174,171,141,133,176,116,136,148,163,175],"Image");