__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Modal/Modal.js",
      _container;

  var AppContainer = _require(_dependencyMap[0], 'AppContainer');

  var I18nManager = _require(_dependencyMap[1], 'I18nManager');

  var NativeEventEmitter = _require(_dependencyMap[2], 'NativeEventEmitter');

  var NativeModules = _require(_dependencyMap[3], 'NativeModules');

  var Platform = _require(_dependencyMap[4], 'Platform');

  var React = _require(_dependencyMap[5], 'React');

  var PropTypes = _require(_dependencyMap[6], 'prop-types');

  var StyleSheet = _require(_dependencyMap[7], 'StyleSheet');

  var View = _require(_dependencyMap[8], 'View');

  var deprecatedPropType = _require(_dependencyMap[9], 'deprecatedPropType');

  var requireNativeComponent = _require(_dependencyMap[10], 'requireNativeComponent');

  var RCTModalHostView = requireNativeComponent('RCTModalHostView', null);
  var ModalEventEmitter = Platform.OS === 'ios' && NativeModules.ModalManager ? new NativeEventEmitter(NativeModules.ModalManager) : null;
  var uniqueModalIdentifier = 0;

  var Modal = function (_React$Component) {
    babelHelpers.inherits(Modal, _React$Component);

    function Modal(props) {
      babelHelpers.classCallCheck(this, Modal);

      var _this = babelHelpers.possibleConstructorReturn(this, (Modal.__proto__ || Object.getPrototypeOf(Modal)).call(this, props));

      Modal._confirmProps(props);

      _this._identifier = uniqueModalIdentifier++;
      return _this;
    }

    babelHelpers.createClass(Modal, [{
      key: "componentDidMount",
      value: function componentDidMount() {
        var _this2 = this;

        if (ModalEventEmitter) {
          this._eventSubscription = ModalEventEmitter.addListener('modalDismissed', function (event) {
            if (event.modalID === _this2._identifier && _this2.props.onDismiss) {
              _this2.props.onDismiss();
            }
          });
        }
      }
    }, {
      key: "componentWillUnmount",
      value: function componentWillUnmount() {
        if (this._eventSubscription) {
          this._eventSubscription.remove();
        }
      }
    }, {
      key: "UNSAFE_componentWillReceiveProps",
      value: function UNSAFE_componentWillReceiveProps(nextProps) {
        Modal._confirmProps(nextProps);
      }
    }, {
      key: "render",
      value: function render() {
        if (this.props.visible === false) {
          return null;
        }

        var containerStyles = {
          backgroundColor: this.props.transparent ? 'transparent' : 'white'
        };
        var animationType = this.props.animationType;

        if (!animationType) {
          animationType = 'none';

          if (this.props.animated) {
            animationType = 'slide';
          }
        }

        var presentationStyle = this.props.presentationStyle;

        if (!presentationStyle) {
          presentationStyle = 'fullScreen';

          if (this.props.transparent) {
            presentationStyle = 'overFullScreen';
          }
        }

        var innerChildren = __DEV__ ? React.createElement(
          AppContainer,
          {
            rootTag: this.context.rootTag,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 189
            }
          },
          this.props.children
        ) : this.props.children;
        return React.createElement(
          RCTModalHostView,
          {
            animationType: animationType,
            presentationStyle: presentationStyle,
            transparent: this.props.transparent,
            hardwareAccelerated: this.props.hardwareAccelerated,
            onRequestClose: this.props.onRequestClose,
            onShow: this.props.onShow,
            identifier: this._identifier,
            style: styles.modal,
            onStartShouldSetResponder: this._shouldSetResponder,
            supportedOrientations: this.props.supportedOrientations,
            onOrientationChange: this.props.onOrientationChange,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 195
            }
          },
          React.createElement(
            View,
            {
              style: [styles.container, containerStyles],
              __source: {
                fileName: _jsxFileName,
                lineNumber: 208
              }
            },
            innerChildren
          )
        );
      }
    }, {
      key: "_shouldSetResponder",
      value: function _shouldSetResponder() {
        return true;
      }
    }], [{
      key: "_confirmProps",
      value: function _confirmProps(props) {
        if (props.presentationStyle && props.presentationStyle !== 'overFullScreen' && props.transparent) {
          console.warn("Modal with '" + props.presentationStyle + "' presentation style and 'transparent' value is not supported.");
        }
      }
    }]);
    return Modal;
  }(React.Component);

  Modal.propTypes = {
    animationType: PropTypes.oneOf(['none', 'slide', 'fade']),
    presentationStyle: PropTypes.oneOf(['fullScreen', 'pageSheet', 'formSheet', 'overFullScreen']),
    transparent: PropTypes.bool,
    hardwareAccelerated: PropTypes.bool,
    visible: PropTypes.bool,
    onRequestClose: Platform.isTVOS || Platform.OS === 'android' ? PropTypes.func.isRequired : PropTypes.func,
    onShow: PropTypes.func,
    onDismiss: PropTypes.func,
    animated: deprecatedPropType(PropTypes.bool, 'Use the `animationType` prop instead.'),
    supportedOrientations: PropTypes.arrayOf(PropTypes.oneOf(['portrait', 'portrait-upside-down', 'landscape', 'landscape-left', 'landscape-right'])),
    onOrientationChange: PropTypes.func
  };
  Modal.defaultProps = {
    visible: true,
    hardwareAccelerated: false
  };
  Modal.contextTypes = {
    rootTag: PropTypes.number
  };
  var side = I18nManager.isRTL ? 'right' : 'left';
  var styles = StyleSheet.create({
    modal: {
      position: 'absolute'
    },
    container: (_container = {
      position: 'absolute'
    }, babelHelpers.defineProperty(_container, side, 0), babelHelpers.defineProperty(_container, "top", 0), _container)
  });
  module.exports = Modal;
},264,[265,284,82,24,32,132,129,171,173,146,148],"Modal");