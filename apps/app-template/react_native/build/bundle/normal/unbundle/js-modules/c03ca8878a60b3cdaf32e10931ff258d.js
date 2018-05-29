__d(function (global, _require2, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Text/Text.js";

  var NativeMethodsMixin = _require2(_dependencyMap[0], 'NativeMethodsMixin');

  var React = _require2(_dependencyMap[1], 'React');

  var ReactNativeViewAttributes = _require2(_dependencyMap[2], 'ReactNativeViewAttributes');

  var TextPropTypes = _require2(_dependencyMap[3], 'TextPropTypes');

  var Touchable = _require2(_dependencyMap[4], 'Touchable');

  var UIManager = _require2(_dependencyMap[5], 'UIManager');

  var createReactClass = _require2(_dependencyMap[6], 'create-react-class');

  var createReactNativeComponentClass = _require2(_dependencyMap[7], 'createReactNativeComponentClass');

  var mergeFast = _require2(_dependencyMap[8], 'mergeFast');

  var processColor = _require2(_dependencyMap[9], 'processColor');

  var _require = _require2(_dependencyMap[10], 'ViewContext'),
      ViewContextTypes = _require.ViewContextTypes;

  var viewConfig = {
    validAttributes: mergeFast(ReactNativeViewAttributes.UIView, {
      isHighlighted: true,
      numberOfLines: true,
      ellipsizeMode: true,
      allowFontScaling: true,
      disabled: true,
      selectable: true,
      selectionColor: true,
      adjustsFontSizeToFit: true,
      minimumFontScale: true,
      textBreakStrategy: true
    }),
    uiViewClassName: 'RCTText'
  };
  var Text = createReactClass({
    displayName: 'Text',
    propTypes: TextPropTypes,
    getDefaultProps: function getDefaultProps() {
      return {
        accessible: true,
        allowFontScaling: true,
        ellipsizeMode: 'tail'
      };
    },
    getInitialState: function getInitialState() {
      return mergeFast(Touchable.Mixin.touchableGetInitialState(), {
        isHighlighted: false
      });
    },
    mixins: [NativeMethodsMixin],
    viewConfig: viewConfig,
    getChildContext: function getChildContext() {
      return {
        isInAParentText: true
      };
    },
    childContextTypes: ViewContextTypes,
    contextTypes: ViewContextTypes,
    _handlers: null,
    _hasPressHandler: function _hasPressHandler() {
      return !!this.props.onPress || !!this.props.onLongPress;
    },
    touchableHandleActivePressIn: null,
    touchableHandleActivePressOut: null,
    touchableHandlePress: null,
    touchableHandleLongPress: null,
    touchableGetPressRectOffset: null,
    render: function render() {
      var _this = this;

      var newProps = this.props;

      if (this.props.onStartShouldSetResponder || this._hasPressHandler()) {
        if (!this._handlers) {
          this._handlers = {
            onStartShouldSetResponder: function onStartShouldSetResponder() {
              var shouldSetFromProps = _this.props.onStartShouldSetResponder && _this.props.onStartShouldSetResponder();

              var setResponder = shouldSetFromProps || _this._hasPressHandler();

              if (setResponder && !_this.touchableHandleActivePressIn) {
                for (var key in Touchable.Mixin) {
                  if (typeof Touchable.Mixin[key] === 'function') {
                    _this[key] = Touchable.Mixin[key].bind(_this);
                  }
                }

                _this.touchableHandleActivePressIn = function () {
                  if (_this.props.suppressHighlighting || !_this._hasPressHandler()) {
                    return;
                  }

                  _this.setState({
                    isHighlighted: true
                  });
                };

                _this.touchableHandleActivePressOut = function () {
                  if (_this.props.suppressHighlighting || !_this._hasPressHandler()) {
                    return;
                  }

                  _this.setState({
                    isHighlighted: false
                  });
                };

                _this.touchableHandlePress = function (e) {
                  _this.props.onPress && _this.props.onPress(e);
                };

                _this.touchableHandleLongPress = function (e) {
                  _this.props.onLongPress && _this.props.onLongPress(e);
                };

                _this.touchableGetPressRectOffset = function () {
                  return this.props.pressRetentionOffset || PRESS_RECT_OFFSET;
                };
              }

              return setResponder;
            },
            onResponderGrant: function (e, dispatchID) {
              this.touchableHandleResponderGrant(e, dispatchID);
              this.props.onResponderGrant && this.props.onResponderGrant.apply(this, arguments);
            }.bind(this),
            onResponderMove: function (e) {
              this.touchableHandleResponderMove(e);
              this.props.onResponderMove && this.props.onResponderMove.apply(this, arguments);
            }.bind(this),
            onResponderRelease: function (e) {
              this.touchableHandleResponderRelease(e);
              this.props.onResponderRelease && this.props.onResponderRelease.apply(this, arguments);
            }.bind(this),
            onResponderTerminate: function (e) {
              this.touchableHandleResponderTerminate(e);
              this.props.onResponderTerminate && this.props.onResponderTerminate.apply(this, arguments);
            }.bind(this),
            onResponderTerminationRequest: function () {
              var allowTermination = this.touchableHandleResponderTerminationRequest();

              if (allowTermination && this.props.onResponderTerminationRequest) {
                allowTermination = this.props.onResponderTerminationRequest.apply(this, arguments);
              }

              return allowTermination;
            }.bind(this)
          };
        }

        newProps = babelHelpers.extends({}, this.props, this._handlers, {
          isHighlighted: this.state.isHighlighted
        });
      }

      if (newProps.selectionColor != null) {
        newProps = babelHelpers.extends({}, newProps, {
          selectionColor: processColor(newProps.selectionColor)
        });
      }

      if (Touchable.TOUCH_TARGET_DEBUG && newProps.onPress) {
        newProps = babelHelpers.extends({}, newProps, {
          style: [this.props.style, {
            color: 'magenta'
          }]
        });
      }

      if (this.context.isInAParentText) {
        return React.createElement(RCTVirtualText, babelHelpers.extends({}, newProps, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 200
          }
        }));
      } else {
        return React.createElement(RCTText, babelHelpers.extends({}, newProps, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 202
          }
        }));
      }
    }
  });
  var PRESS_RECT_OFFSET = {
    top: 20,
    left: 20,
    right: 20,
    bottom: 30
  };
  var RCTText = createReactNativeComponentClass(viewConfig.uiViewClassName, function () {
    return viewConfig;
  });
  var RCTVirtualText = RCTText;

  if (UIManager.RCTVirtualText) {
    RCTVirtualText = createReactNativeComponentClass('RCTVirtualText', function () {
      return {
        validAttributes: mergeFast(ReactNativeViewAttributes.UIView, {
          isHighlighted: true
        }),
        uiViewClassName: 'RCTVirtualText'
      };
    });
  }

  module.exports = Text;
},"c03ca8878a60b3cdaf32e10931ff258d",["e2817b4a53aaef19afef34f031e1b9c9","e6db4f0efed6b72f641ef0ffed29569f","6477887be0d285a967d42967386335cd","98dc3d7a81acedddf558bd7f3ed6cba7","9be7bff2ec732c7f9f96a83cea3bc22f","467cd3365342d9aaa2e941fe7ace641c","29cb0e104e5fce198008f3e789631772","c24d6856e671b47ee028a906101ebc28","cd2f3074586691ca138857438f8ba498","1b69977972a3b6ad650756d07de7954c","a0a67b647dff8a7e11698d04fd60772b"],"Text");