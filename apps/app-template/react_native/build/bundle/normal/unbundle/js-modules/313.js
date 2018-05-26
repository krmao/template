__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/WebView/WebView.android.js";

  var EdgeInsetsPropType = _require(_dependencyMap[0], 'EdgeInsetsPropType');

  var ActivityIndicator = _require(_dependencyMap[1], 'ActivityIndicator');

  var React = _require(_dependencyMap[2], 'React');

  var PropTypes = _require(_dependencyMap[3], 'prop-types');

  var ReactNative = _require(_dependencyMap[4], 'ReactNative');

  var StyleSheet = _require(_dependencyMap[5], 'StyleSheet');

  var UIManager = _require(_dependencyMap[6], 'UIManager');

  var View = _require(_dependencyMap[7], 'View');

  var ViewPropTypes = _require(_dependencyMap[8], 'ViewPropTypes');

  var deprecatedPropType = _require(_dependencyMap[9], 'deprecatedPropType');

  var keyMirror = _require(_dependencyMap[10], 'fbjs/lib/keyMirror');

  var requireNativeComponent = _require(_dependencyMap[11], 'requireNativeComponent');

  var resolveAssetSource = _require(_dependencyMap[12], 'resolveAssetSource');

  var RCT_WEBVIEW_REF = 'webview';
  var WebViewState = keyMirror({
    IDLE: null,
    LOADING: null,
    ERROR: null
  });

  var defaultRenderLoading = function defaultRenderLoading() {
    return React.createElement(
      View,
      {
        style: styles.loadingView,
        __source: {
          fileName: _jsxFileName,
          lineNumber: 35
        }
      },
      React.createElement(ActivityIndicator, {
        style: styles.loadingProgressBar,
        __source: {
          fileName: _jsxFileName,
          lineNumber: 36
        }
      })
    );
  };

  var WebView = function (_React$Component) {
    babelHelpers.inherits(WebView, _React$Component);

    function WebView() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, WebView);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = WebView.__proto__ || Object.getPrototypeOf(WebView)).call.apply(_ref, [this].concat(args))), _this), _this.state = {
        viewState: WebViewState.IDLE,
        lastErrorEvent: null,
        startInLoadingState: true
      }, _this.goForward = function () {
        UIManager.dispatchViewManagerCommand(_this.getWebViewHandle(), UIManager.RCTWebView.Commands.goForward, null);
      }, _this.goBack = function () {
        UIManager.dispatchViewManagerCommand(_this.getWebViewHandle(), UIManager.RCTWebView.Commands.goBack, null);
      }, _this.reload = function () {
        _this.setState({
          viewState: WebViewState.LOADING
        });

        UIManager.dispatchViewManagerCommand(_this.getWebViewHandle(), UIManager.RCTWebView.Commands.reload, null);
      }, _this.stopLoading = function () {
        UIManager.dispatchViewManagerCommand(_this.getWebViewHandle(), UIManager.RCTWebView.Commands.stopLoading, null);
      }, _this.postMessage = function (data) {
        UIManager.dispatchViewManagerCommand(_this.getWebViewHandle(), UIManager.RCTWebView.Commands.postMessage, [String(data)]);
      }, _this.injectJavaScript = function (data) {
        UIManager.dispatchViewManagerCommand(_this.getWebViewHandle(), UIManager.RCTWebView.Commands.injectJavaScript, [data]);
      }, _this.updateNavigationState = function (event) {
        if (_this.props.onNavigationStateChange) {
          _this.props.onNavigationStateChange(event.nativeEvent);
        }
      }, _this.getWebViewHandle = function () {
        return ReactNative.findNodeHandle(_this.refs[RCT_WEBVIEW_REF]);
      }, _this.onLoadingStart = function (event) {
        var onLoadStart = _this.props.onLoadStart;
        onLoadStart && onLoadStart(event);

        _this.updateNavigationState(event);
      }, _this.onLoadingError = function (event) {
        event.persist();
        var _this$props = _this.props,
            onError = _this$props.onError,
            onLoadEnd = _this$props.onLoadEnd;
        onError && onError(event);
        onLoadEnd && onLoadEnd(event);
        console.warn('Encountered an error loading page', event.nativeEvent);

        _this.setState({
          lastErrorEvent: event.nativeEvent,
          viewState: WebViewState.ERROR
        });
      }, _this.onLoadingFinish = function (event) {
        var _this$props2 = _this.props,
            onLoad = _this$props2.onLoad,
            onLoadEnd = _this$props2.onLoadEnd;
        onLoad && onLoad(event);
        onLoadEnd && onLoadEnd(event);

        _this.setState({
          viewState: WebViewState.IDLE
        });

        _this.updateNavigationState(event);
      }, _this.onMessage = function (event) {
        var onMessage = _this.props.onMessage;
        onMessage && onMessage(event);
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(WebView, [{
      key: "UNSAFE_componentWillMount",
      value: function UNSAFE_componentWillMount() {
        if (this.props.startInLoadingState) {
          this.setState({
            viewState: WebViewState.LOADING
          });
        }
      }
    }, {
      key: "render",
      value: function render() {
        var otherView = null;

        if (this.state.viewState === WebViewState.LOADING) {
          otherView = (this.props.renderLoading || defaultRenderLoading)();
        } else if (this.state.viewState === WebViewState.ERROR) {
          var errorEvent = this.state.lastErrorEvent;
          otherView = this.props.renderError && this.props.renderError(errorEvent.domain, errorEvent.code, errorEvent.description);
        } else if (this.state.viewState !== WebViewState.IDLE) {
          console.error('RCTWebView invalid state encountered: ' + this.state.loading);
        }

        var webViewStyles = [styles.container, this.props.style];

        if (this.state.viewState === WebViewState.LOADING || this.state.viewState === WebViewState.ERROR) {
          webViewStyles.push(styles.hidden);
        }

        var source = this.props.source || {};

        if (this.props.html) {
          source.html = this.props.html;
        } else if (this.props.url) {
          source.uri = this.props.url;
        }

        if (source.method === 'POST' && source.headers) {
          console.warn('WebView: `source.headers` is not supported when using POST.');
        } else if (source.method === 'GET' && source.body) {
          console.warn('WebView: `source.body` is not supported when using GET.');
        }

        var nativeConfig = this.props.nativeConfig || {};
        var NativeWebView = nativeConfig.component || RCTWebView;
        var webView = React.createElement(NativeWebView, babelHelpers.extends({
          ref: RCT_WEBVIEW_REF,
          key: "webViewKey",
          style: webViewStyles,
          source: resolveAssetSource(source),
          scalesPageToFit: this.props.scalesPageToFit,
          injectedJavaScript: this.props.injectedJavaScript,
          userAgent: this.props.userAgent,
          javaScriptEnabled: this.props.javaScriptEnabled,
          thirdPartyCookiesEnabled: this.props.thirdPartyCookiesEnabled,
          domStorageEnabled: this.props.domStorageEnabled,
          messagingEnabled: typeof this.props.onMessage === 'function',
          onMessage: this.onMessage,
          contentInset: this.props.contentInset,
          automaticallyAdjustContentInsets: this.props.automaticallyAdjustContentInsets,
          onContentSizeChange: this.props.onContentSizeChange,
          onLoadingStart: this.onLoadingStart,
          onLoadingFinish: this.onLoadingFinish,
          onLoadingError: this.onLoadingError,
          testID: this.props.testID,
          mediaPlaybackRequiresUserAction: this.props.mediaPlaybackRequiresUserAction,
          allowUniversalAccessFromFileURLs: this.props.allowUniversalAccessFromFileURLs,
          mixedContentMode: this.props.mixedContentMode,
          saveFormDataDisabled: this.props.saveFormDataDisabled,
          urlPrefixesForDefaultIntent: this.props.urlPrefixesForDefaultIntent
        }, nativeConfig.props, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 294
          }
        }));
        return React.createElement(
          View,
          {
            style: styles.container,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 323
            }
          },
          webView,
          otherView
        );
      }
    }], [{
      key: "extraNativeComponentConfig",
      get: function get() {
        return {
          nativeOnly: {
            messagingEnabled: PropTypes.bool
          }
        };
      }
    }]);
    return WebView;
  }(React.Component);

  WebView.propTypes = babelHelpers.extends({}, ViewPropTypes, {
    renderError: PropTypes.func,
    renderLoading: PropTypes.func,
    onLoad: PropTypes.func,
    onLoadEnd: PropTypes.func,
    onLoadStart: PropTypes.func,
    onError: PropTypes.func,
    automaticallyAdjustContentInsets: PropTypes.bool,
    contentInset: EdgeInsetsPropType,
    onNavigationStateChange: PropTypes.func,
    onMessage: PropTypes.func,
    onContentSizeChange: PropTypes.func,
    startInLoadingState: PropTypes.bool,
    style: ViewPropTypes.style,
    html: deprecatedPropType(PropTypes.string, 'Use the `source` prop instead.'),
    url: deprecatedPropType(PropTypes.string, 'Use the `source` prop instead.'),
    source: PropTypes.oneOfType([PropTypes.shape({
      uri: PropTypes.string,
      method: PropTypes.oneOf(['GET', 'POST']),
      headers: PropTypes.object,
      body: PropTypes.string
    }), PropTypes.shape({
      html: PropTypes.string,
      baseUrl: PropTypes.string
    }), PropTypes.number]),
    javaScriptEnabled: PropTypes.bool,
    thirdPartyCookiesEnabled: PropTypes.bool,
    domStorageEnabled: PropTypes.bool,
    injectedJavaScript: PropTypes.string,
    scalesPageToFit: PropTypes.bool,
    userAgent: PropTypes.string,
    testID: PropTypes.string,
    mediaPlaybackRequiresUserAction: PropTypes.bool,
    allowUniversalAccessFromFileURLs: PropTypes.bool,
    injectJavaScript: PropTypes.func,
    mixedContentMode: PropTypes.oneOf(['never', 'always', 'compatibility']),
    saveFormDataDisabled: PropTypes.bool,
    nativeConfig: PropTypes.shape({
      component: PropTypes.any,
      props: PropTypes.object,
      viewManager: PropTypes.object
    }),
    urlPrefixesForDefaultIntent: PropTypes.arrayOf(PropTypes.string)
  });
  WebView.defaultProps = {
    javaScriptEnabled: true,
    thirdPartyCookiesEnabled: true,
    scalesPageToFit: true,
    saveFormDataDisabled: false
  };
  var RCTWebView = requireNativeComponent('RCTWebView', WebView, WebView.extraNativeComponentConfig);
  var styles = StyleSheet.create({
    container: {
      flex: 1
    },
    hidden: {
      height: 0,
      flex: 0
    },
    loadingView: {
      flex: 1,
      justifyContent: 'center',
      alignItems: 'center'
    },
    loadingProgressBar: {
      height: 20
    }
  });
  module.exports = WebView;
},313,[134,45,132,129,49,171,121,173,133,146,153,148,163],"WebView");