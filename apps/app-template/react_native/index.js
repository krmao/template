import React, {Component} from "react";
import {Animated, AppRegistry, BackHandler, Easing, Platform, YellowBox} from "react-native";
import {createStackNavigator} from "react-navigation";
import global from "./src/main/js/base/Global";
import HomeScreen from "./src/main/js/pages/HomeScreen";
import BridgeScreen from "./src/main/js/pages/BridgeScreen";
import OrderCommitScreen from "./src/main/js/pages/OrderCommitScreen";

console.log("OS:" + Platform.OS);
console.log("STATUS_BAR_HEIGHT:" + global.STATUS_BAR_HEIGHT);

YellowBox.ignoreWarnings(['Warning: isMounted(...) is deprecated', 'Module RCTImageLoader']);

const RootStack = createStackNavigator(
    {
        home: {screen: HomeScreen, navigationOptions: (navigation) => global.defaultNavigationOptions(navigation)},
        bridge: {screen: BridgeScreen, navigationOptions: (navigation) => global.defaultNavigationOptions(navigation)},
        order_commit: {screen: OrderCommitScreen, navigationOptions: (navigation) => global.defaultNavigationOptions(navigation)}
    },
    {
        initialRouteName: "home",

        navigationOptions: {
            gesturesEnabled: true,
        },
        mode: "card",
        headerMode: "screen",
        headerTransitionPreset: "uikit",
        transitionConfig: () => ({
            transitionSpec: {
                duration: 300,
                easing: Easing.out(Easing.poly(4)),
                timing: Animated.timing,
            },
            screenInterpolator: sceneProps => {
                const {layout, position, scene} = sceneProps;
                const {index} = scene;

                const width = layout.initWidth;
                const translateX = position.interpolate({
                    inputRange: [index - 1, index, index + 1],
                    outputRange: [width, 0, 0],
                });

                /*
                 const height = layout.initHeight;
                 const translateY = position.interpolate({
                 inputRange: [index - 1, index, index + 1],
                 outputRange: [height, 0, 0],
                 });
                 */

                const opacity = position.interpolate({
                    inputRange: [index - 1, index - 0.99, index],
                    outputRange: [0, 1, 1],
                });

                return {opacity, transform: [{translateX: translateX}]};
            },
        }),
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