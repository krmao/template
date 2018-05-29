__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Components/DrawerAndroid/DrawerLayoutAndroid.android.js";

  var ColorPropType = _require(_dependencyMap[0], 'ColorPropType');

  var NativeMethodsMixin = _require(_dependencyMap[1], 'NativeMethodsMixin');

  var Platform = _require(_dependencyMap[2], 'Platform');

  var React = _require(_dependencyMap[3], 'React');

  var PropTypes = _require(_dependencyMap[4], 'prop-types');

  var ReactNative = _require(_dependencyMap[5], 'ReactNative');

  var StatusBar = _require(_dependencyMap[6], 'StatusBar');

  var StyleSheet = _require(_dependencyMap[7], 'StyleSheet');

  var UIManager = _require(_dependencyMap[8], 'UIManager');

  var View = _require(_dependencyMap[9], 'View');

  var ViewPropTypes = _require(_dependencyMap[10], 'ViewPropTypes');

  var DrawerConsts = UIManager.AndroidDrawerLayout.Constants;

  var createReactClass = _require(_dependencyMap[11], 'create-react-class');

  var dismissKeyboard = _require(_dependencyMap[12], 'dismissKeyboard');

  var requireNativeComponent = _require(_dependencyMap[13], 'requireNativeComponent');

  var RK_DRAWER_REF = 'drawerlayout';
  var INNERVIEW_REF = 'innerView';
  var DRAWER_STATES = ['Idle', 'Dragging', 'Settling'];
  var DrawerLayoutAndroid = createReactClass({
    displayName: 'DrawerLayoutAndroid',
    statics: {
      positions: DrawerConsts.DrawerPosition
    },
    propTypes: babelHelpers.extends({}, ViewPropTypes, {
      keyboardDismissMode: PropTypes.oneOf(['none', 'on-drag']),
      drawerBackgroundColor: ColorPropType,
      drawerPosition: PropTypes.oneOf([DrawerConsts.DrawerPosition.Left, DrawerConsts.DrawerPosition.Right]),
      drawerWidth: PropTypes.number,
      drawerLockMode: PropTypes.oneOf(['unlocked', 'locked-closed', 'locked-open']),
      onDrawerSlide: PropTypes.func,
      onDrawerStateChanged: PropTypes.func,
      onDrawerOpen: PropTypes.func,
      onDrawerClose: PropTypes.func,
      renderNavigationView: PropTypes.func.isRequired,
      statusBarBackgroundColor: ColorPropType
    }),
    mixins: [NativeMethodsMixin],
    getDefaultProps: function getDefaultProps() {
      return {
        drawerBackgroundColor: 'white'
      };
    },
    getInitialState: function getInitialState() {
      return {
        statusBarBackgroundColor: undefined
      };
    },
    getInnerViewNode: function getInnerViewNode() {
      return this.refs[INNERVIEW_REF].getInnerViewNode();
    },
    render: function render() {
      var drawStatusBar = Platform.Version >= 21 && this.props.statusBarBackgroundColor;
      var drawerViewWrapper = React.createElement(
        View,
        {
          style: [styles.drawerSubview, {
            width: this.props.drawerWidth,
            backgroundColor: this.props.drawerBackgroundColor
          }],
          collapsable: false,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 174
          }
        },
        this.props.renderNavigationView(),
        drawStatusBar && React.createElement(View, {
          style: styles.drawerStatusBar,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 181
          }
        })
      );
      var childrenWrapper = React.createElement(
        View,
        {
          ref: INNERVIEW_REF,
          style: styles.mainSubview,
          collapsable: false,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 184
          }
        },
        drawStatusBar && React.createElement(StatusBar, {
          translucent: true,
          backgroundColor: this.props.statusBarBackgroundColor,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 186
          }
        }),
        drawStatusBar && React.createElement(View, {
          style: [styles.statusBar, {
            backgroundColor: this.props.statusBarBackgroundColor
          }],
          __source: {
            fileName: _jsxFileName,
            lineNumber: 191
          }
        }),
        this.props.children
      );
      return React.createElement(
        AndroidDrawerLayout,
        babelHelpers.extends({}, this.props, {
          ref: RK_DRAWER_REF,
          drawerWidth: this.props.drawerWidth,
          drawerPosition: this.props.drawerPosition,
          drawerLockMode: this.props.drawerLockMode,
          style: [styles.base, this.props.style],
          onDrawerSlide: this._onDrawerSlide,
          onDrawerOpen: this._onDrawerOpen,
          onDrawerClose: this._onDrawerClose,
          onDrawerStateChanged: this._onDrawerStateChanged,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 198
          }
        }),
        childrenWrapper,
        drawerViewWrapper
      );
    },
    _onDrawerSlide: function _onDrawerSlide(event) {
      if (this.props.onDrawerSlide) {
        this.props.onDrawerSlide(event);
      }

      if (this.props.keyboardDismissMode === 'on-drag') {
        dismissKeyboard();
      }
    },
    _onDrawerOpen: function _onDrawerOpen() {
      if (this.props.onDrawerOpen) {
        this.props.onDrawerOpen();
      }
    },
    _onDrawerClose: function _onDrawerClose() {
      if (this.props.onDrawerClose) {
        this.props.onDrawerClose();
      }
    },
    _onDrawerStateChanged: function _onDrawerStateChanged(event) {
      if (this.props.onDrawerStateChanged) {
        this.props.onDrawerStateChanged(DRAWER_STATES[event.nativeEvent.drawerState]);
      }
    },
    openDrawer: function openDrawer() {
      UIManager.dispatchViewManagerCommand(this._getDrawerLayoutHandle(), UIManager.AndroidDrawerLayout.Commands.openDrawer, null);
    },
    closeDrawer: function closeDrawer() {
      UIManager.dispatchViewManagerCommand(this._getDrawerLayoutHandle(), UIManager.AndroidDrawerLayout.Commands.closeDrawer, null);
    },
    _getDrawerLayoutHandle: function _getDrawerLayoutHandle() {
      return ReactNative.findNodeHandle(this.refs[RK_DRAWER_REF]);
    }
  });
  var styles = StyleSheet.create({
    base: {
      flex: 1,
      elevation: 16
    },
    mainSubview: {
      position: 'absolute',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0
    },
    drawerSubview: {
      position: 'absolute',
      top: 0,
      bottom: 0
    },
    statusBar: {
      height: StatusBar.currentHeight
    },
    drawerStatusBar: {
      position: 'absolute',
      top: 0,
      left: 0,
      right: 0,
      height: StatusBar.currentHeight,
      backgroundColor: 'rgba(0, 0, 0, 0.251)'
    }
  });
  var AndroidDrawerLayout = requireNativeComponent('AndroidDrawerLayout', DrawerLayoutAndroid);
  module.exports = DrawerLayoutAndroid;
},"a28f8a2db2bc87a3ceedb498ea3689f4",["63c61c7eda525c10d0670d2ef8475012","e2817b4a53aaef19afef34f031e1b9c9","9493a89f5d95c3a8a47c65cfed9b5542","e6db4f0efed6b72f641ef0ffed29569f","18eeaf4e01377a466daaccc6ba8ce6f5","1102b68d89d7a6aede9677567aa01362","3b9e8669111967f90ad28e9ee872e85e","d31e8c1a3f9844becc88973ecddac872","467cd3365342d9aaa2e941fe7ace641c","30a3b04291b6e1f01b778ff31271ccc5","9ff7e107ed674a99182e71b796d889aa","29cb0e104e5fce198008f3e789631772","9bc80013596b455d6a897518595d41ba","98c1697e1928b0d4ea4ae3837ea09d48"],"DrawerLayoutAndroid");