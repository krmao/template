__d(function (global, _require, module, exports, _dependencyMap) {
  module.exports = {
    get createNavigationContainer() {
      return _require(_dependencyMap[0], './createNavigationContainer').default;
    },

    get StateUtils() {
      return _require(_dependencyMap[1], './StateUtils').default;
    },

    get createNavigator() {
      return _require(_dependencyMap[2], './navigators/createNavigator').default;
    },

    get createStackNavigator() {
      return _require(_dependencyMap[3], './navigators/createStackNavigator').default;
    },

    get StackNavigator() {
      console.warn('The StackNavigator function name is deprecated, please use createStackNavigator instead');
      return _require(_dependencyMap[3], './navigators/createStackNavigator').default;
    },

    get createSwitchNavigator() {
      return _require(_dependencyMap[4], './navigators/createSwitchNavigator').default;
    },

    get SwitchNavigator() {
      console.warn('The SwitchNavigator function name is deprecated, please use createSwitchNavigator instead');
      return _require(_dependencyMap[4], './navigators/createSwitchNavigator').default;
    },

    get createDrawerNavigator() {
      return _require(_dependencyMap[5], './navigators/createDrawerNavigator').default;
    },

    get DrawerNavigator() {
      console.warn('The DrawerNavigator function name is deprecated, please use createDrawerNavigator instead');
      return _require(_dependencyMap[5], './navigators/createDrawerNavigator').default;
    },

    get createTabNavigator() {
      console.warn('createTabNavigator is deprecated. Please use the createBottomTabNavigator or createMaterialTopTabNavigator instead.');
      return _require(_dependencyMap[6], 'react-navigation-deprecated-tab-navigator').createTabNavigator;
    },

    get TabNavigator() {
      console.warn('TabNavigator is deprecated. Please use the createBottomTabNavigator or createMaterialTopTabNavigator instead.');
      return _require(_dependencyMap[6], 'react-navigation-deprecated-tab-navigator').createTabNavigator;
    },

    get createBottomTabNavigator() {
      return _require(_dependencyMap[7], 'react-navigation-tabs').createBottomTabNavigator;
    },

    get createMaterialTopTabNavigator() {
      return _require(_dependencyMap[7], 'react-navigation-tabs').createMaterialTopTabNavigator;
    },

    get NavigationActions() {
      return _require(_dependencyMap[8], './NavigationActions').default;
    },

    get StackActions() {
      return _require(_dependencyMap[9], './routers/StackActions').default;
    },

    get DrawerActions() {
      return _require(_dependencyMap[10], './routers/DrawerActions').default;
    },

    get StackRouter() {
      return _require(_dependencyMap[11], './routers/StackRouter').default;
    },

    get TabRouter() {
      return _require(_dependencyMap[12], './routers/TabRouter').default;
    },

    get DrawerRouter() {
      return _require(_dependencyMap[13], './routers/DrawerRouter').default;
    },

    get SwitchRouter() {
      return _require(_dependencyMap[14], './routers/SwitchRouter').default;
    },

    get Transitioner() {
      return _require(_dependencyMap[15], './views/Transitioner').default;
    },

    get StackView() {
      return _require(_dependencyMap[16], './views/StackView/StackView').default;
    },

    get StackViewCard() {
      return _require(_dependencyMap[17], './views/StackView/StackViewCard').default;
    },

    get SafeAreaView() {
      return _require(_dependencyMap[18], 'react-native-safe-area-view').default;
    },

    get SceneView() {
      return _require(_dependencyMap[19], './views/SceneView').default;
    },

    get ResourceSavingSceneView() {
      return _require(_dependencyMap[20], './views/ResourceSavingSceneView').default;
    },

    get Header() {
      return _require(_dependencyMap[21], './views/Header/Header').default;
    },

    get HeaderTitle() {
      return _require(_dependencyMap[22], './views/Header/HeaderTitle').default;
    },

    get HeaderBackButton() {
      return _require(_dependencyMap[23], './views/Header/HeaderBackButton').default;
    },

    get DrawerView() {
      return _require(_dependencyMap[24], './views/Drawer/DrawerView').default;
    },

    get DrawerItems() {
      return _require(_dependencyMap[25], './views/Drawer/DrawerNavigatorItems').default;
    },

    get DrawerSidebar() {
      return _require(_dependencyMap[26], './views/Drawer/DrawerSidebar').default;
    },

    get TabView() {
      console.warn('TabView is deprecated. Please use the react-navigation-tabs package instead: https://github.com/react-navigation/react-navigation-tabs');
      return _require(_dependencyMap[6], 'react-navigation-deprecated-tab-navigator').TabView;
    },

    get TabBarTop() {
      console.warn('TabBarTop is deprecated. Please use the react-navigation-tabs package instead: https://github.com/react-navigation/react-navigation-tabs');
      return _require(_dependencyMap[6], 'react-navigation-deprecated-tab-navigator').TabBarTop;
    },

    get TabBarBottom() {
      console.warn('TabBarBottom is deprecated. Please use the react-navigation-tabs package instead: https://github.com/react-navigation/react-navigation-tabs');
      return _require(_dependencyMap[6], 'react-navigation-deprecated-tab-navigator').TabBarBottom;
    },

    get SwitchView() {
      return _require(_dependencyMap[27], './views/SwitchView/SwitchView').default;
    },

    get withNavigation() {
      return _require(_dependencyMap[28], './views/withNavigation').default;
    },

    get withNavigationFocus() {
      return _require(_dependencyMap[29], './views/withNavigationFocus').default;
    },

    get withOrientation() {
      return _require(_dependencyMap[30], './views/withOrientation').default;
    }

  };
},337,[338,345,346,348,389,392,400,416,341,369,395,381,438,393,390,378,350,353,357,370,439,356,360,361,396,399,398,391,440,441,367],"node_modules/react-navigation/src/react-navigation.js");