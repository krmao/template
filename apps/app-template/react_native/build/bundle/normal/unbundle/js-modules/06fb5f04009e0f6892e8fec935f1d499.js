__d(function (global, _require, module, exports, _dependencyMap) {
  module.exports = {
    get createBottomTabNavigator() {
      return _require(_dependencyMap[0], './navigators/createBottomTabNavigator').default;
    },

    get createMaterialTopTabNavigator() {
      return _require(_dependencyMap[1], './navigators/createMaterialTopTabNavigator').default;
    },

    get BottomTabBar() {
      return _require(_dependencyMap[2], './views/BottomTabBar').default;
    },

    get MaterialTopTabBar() {
      return _require(_dependencyMap[3], './views/MaterialTopTabBar').default;
    },

    get createTabNavigator() {
      return _require(_dependencyMap[4], './utils/createTabNavigator').default;
    }

  };
},"06fb5f04009e0f6892e8fec935f1d499",["b198b03b0c2fc5513c0571e39ff0c2a8","920f873b2c6dfac1e53d27d987410d16","3428988f13d25a50f5c1799805d11334","084bb08bba1c65dfbdd218e16c5e104f","c237d25f932c60ab5f6da545341f2bb9"],"node_modules/react-navigation/node_modules/react-navigation-tabs/dist/index.js");