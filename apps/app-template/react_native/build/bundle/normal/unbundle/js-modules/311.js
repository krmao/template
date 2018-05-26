__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/ToolbarAndroid/ToolbarAndroid.android.js";

  var Image = _require(_dependencyMap[0], 'Image');

  var NativeMethodsMixin = _require(_dependencyMap[1], 'NativeMethodsMixin');

  var React = _require(_dependencyMap[2], 'React');

  var PropTypes = _require(_dependencyMap[3], 'prop-types');

  var ReactNativeViewAttributes = _require(_dependencyMap[4], 'ReactNativeViewAttributes');

  var UIManager = _require(_dependencyMap[5], 'UIManager');

  var ViewPropTypes = _require(_dependencyMap[6], 'ViewPropTypes');

  var ColorPropType = _require(_dependencyMap[7], 'ColorPropType');

  var createReactClass = _require(_dependencyMap[8], 'create-react-class');

  var requireNativeComponent = _require(_dependencyMap[9], 'requireNativeComponent');

  var resolveAssetSource = _require(_dependencyMap[10], 'resolveAssetSource');

  var optionalImageSource = PropTypes.oneOfType([Image.propTypes.source, PropTypes.oneOf([])]);
  var ToolbarAndroid = createReactClass({
    displayName: 'ToolbarAndroid',
    mixins: [NativeMethodsMixin],
    propTypes: babelHelpers.extends({}, ViewPropTypes, {
      actions: PropTypes.arrayOf(PropTypes.shape({
        title: PropTypes.string.isRequired,
        icon: optionalImageSource,
        show: PropTypes.oneOf(['always', 'ifRoom', 'never']),
        showWithText: PropTypes.bool
      })),
      logo: optionalImageSource,
      navIcon: optionalImageSource,
      onActionSelected: PropTypes.func,
      onIconClicked: PropTypes.func,
      overflowIcon: optionalImageSource,
      subtitle: PropTypes.string,
      subtitleColor: ColorPropType,
      title: PropTypes.string,
      titleColor: ColorPropType,
      contentInsetStart: PropTypes.number,
      contentInsetEnd: PropTypes.number,
      rtl: PropTypes.bool,
      testID: PropTypes.string
    }),
    render: function render() {
      var nativeProps = babelHelpers.extends({}, this.props);

      if (this.props.logo) {
        nativeProps.logo = resolveAssetSource(this.props.logo);
      }

      if (this.props.navIcon) {
        nativeProps.navIcon = resolveAssetSource(this.props.navIcon);
      }

      if (this.props.overflowIcon) {
        nativeProps.overflowIcon = resolveAssetSource(this.props.overflowIcon);
      }

      if (this.props.actions) {
        var nativeActions = [];

        for (var i = 0; i < this.props.actions.length; i++) {
          var action = babelHelpers.extends({}, this.props.actions[i]);

          if (action.icon) {
            action.icon = resolveAssetSource(action.icon);
          }

          if (action.show) {
            action.show = UIManager.ToolbarAndroid.Constants.ShowAsAction[action.show];
          }

          nativeActions.push(action);
        }

        nativeProps.nativeActions = nativeActions;
      }

      return React.createElement(NativeToolbar, babelHelpers.extends({
        onSelect: this._onSelect
      }, nativeProps, {
        __source: {
          fileName: _jsxFileName,
          lineNumber: 194
        }
      }));
    },
    _onSelect: function _onSelect(event) {
      var position = event.nativeEvent.position;

      if (position === -1) {
        this.props.onIconClicked && this.props.onIconClicked();
      } else {
        this.props.onActionSelected && this.props.onActionSelected(position);
      }
    }
  });
  var NativeToolbar = requireNativeComponent('ToolbarAndroid', ToolbarAndroid, {
    nativeOnly: {
      nativeActions: true
    }
  });
  module.exports = ToolbarAndroid;
},311,[227,48,132,129,174,121,133,46,176,148,163],"ToolbarAndroid");