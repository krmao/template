import React, {Component} from "react";
import {AppRegistry, BackHandler, Platform, YellowBox} from "react-native";
import {createStackNavigator} from "react-navigation";
import global from "./Global";
import HomeScreen from "./HomeScreen";
import BridgeScreen from "./BridgeScreen";

console.log("OS:" + Platform.OS);
console.log("STATUS_BAR_HEIGHT:" + global.STATUS_BAR_HEIGHT);

YellowBox.ignoreWarnings(['Warning: isMounted(...) is deprecated', 'Module RCTImageLoader']);

const RootStack = createStackNavigator(
    {
        home: {screen: HomeScreen, navigationOptions: (navigation) => global.defaultNavigationOptions(navigation)},
        bridge: {screen: BridgeScreen, navigationOptions: (navigation) => global.defaultNavigationOptions(navigation)}
    },
    {
        initialRouteName: "bridge",
    }
);

export default class App extends React.Component {

    componentWillMount() {
        BackHandler.addEventListener("hardwareBackPress", this.onBackPressed)
    }

    componentWillUnmount() {
        BackHandler.removeEventListener("hardwareBackPress", this.onBackPressed)
    }

    render() {
        return <RootStack />;
    }

    onBackPressed = () => {
        return false
    };
}

AppRegistry.registerComponent("react-module-home", () => App);