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
},"3cedea3227936aea9cc98d4a11eaf70c",["2682705ec52ed4b42777ccfb240eb792","8bf70c0b333358752c448c44a6a8f9ab","80bc0b30071534809c7bc11f7396fe43","53633159adb3fa4b6e34bb38c107c293","1ed5f0fc7766d995af463fa28301e999","b5cdce2e70756f7cd58b738ed07cf530","6bd0d432e058233d5647b8c4e8003cd6","06fb5f04009e0f6892e8fec935f1d499","e91a423170a2d063c973900187b02b24","6bd004bef2f53ff343e677f2e121d275","0585470f8233b750e0e3e018473efb42","b0a0a8203dafaf12fbd104b343b6497a","679113a46e8db0d210be300fb377ea4f","8002f3a49b3e2f1b4db38c0522a53ac6","9f5d5448f569850fd5db4a2e7fe03ee2","07f0214b33d7d02821e9126871ddee24","e4b2ccf4e724c26e8054886e2cde54c1","77563c833b96b601648a52bbb84413e8","3a4dc8c54e97a0cf03c38b0fb8563a28","642023f04391babe6960216152f851fe","c3be15fe301c34ea6c6abea8e37ffde4","1ee12e34263a5c37106b178984ec4d9b","9b2d8d757c32a5aade25f6f151e5a821","d507133210f4c812c4a882e06f689725","e97bf41bf3f59b2bfe30b0c2eda9c31f","45dc52dd040d415ff6823373cc17144c","4325b3aa6189efc5ba91d75239de749c","90b6bc7b10f4e683231c018cad5699c0","c22ef7269d48d3e10a1207c748eab698","82debb848906769fd4f1cbb5c9a9cc7a","36c636e765f427a2f89641e81f1ebba9"],"node_modules/react-navigation/src/react-navigation.js");