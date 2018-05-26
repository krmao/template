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
},416,[417,426,420,437,419],"node_modules/react-navigation/node_modules/react-navigation-tabs/dist/index.js");