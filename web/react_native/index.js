import React from "react";
import {Animated, AppRegistry, BackHandler, Easing, Platform, Dimensions, YellowBox} from "react-native";
import {createStackNavigator} from "react-navigation";

import NavigationUtil from "./src/main/js/base/NavigationUtil";
import "./src/main/js/base/Bridge";
import TestScreen from "./src/main/js/pages/TestPage";

console.log("OS:" + Platform.OS);
console.log("STATUS_BAR_HEIGHT:" + NavigationUtil.STATUS_BAR_HEIGHT);

YellowBox.ignoreWarnings(["Warning: isMounted(...) is deprecated", "Module RCTImageLoader"]);

let global = {};
let RootStack = {};

export default class App extends React.Component {
    componentWillMount() {
        BackHandler.addEventListener("hardwareBackPress", this.onBackPressed);
        global.isIPhoneX = false;
        if (Platform.OS === "ios") {
            const {height: D_HEIGHT, width: D_WIDTH} = Dimensions.get("window");
            if (D_WIDTH === 375 && D_HEIGHT === 812) {
                global.isIPhoneX = true;
            }
        }
        global.ENV = null;
        global.HEADER = null;
        global.CITY = null;
        global.navDisable = false;
        global.webViewUserInfo = null;
        global.STATUSBAR_HEIGHT = Platform.OS === "ios" ? (global.isIPhoneX ? 44 : 20) : 0;
        global.APPBAR_HEIGHT = Platform.OS === "ios" ? 44 : 56;
        global.NETWORK_ERROR = false;
        // DeviceEventEmitter.addListener("globalHeight", (data) => {
        //     this.setState({
        //         globleHeight: data
        //     })
        // });
        // try {
        //     const nativeManagerEmitter = new NativeEventEmitter(NativeManager);
        //     this.listener = nativeManagerEmitter.addListener('NativeCallJSMethod', this.NativeCallJSMethod.bind(this));
        // } catch (error) {
        //
        // }
        let _initialRouteParams = this.props.param || {};
        if (Platform.OS !== "ios" && this.props.param) {
            _initialRouteParams = JSON.parse(this.props.param);
        }

        RootStack = createStackNavigator(
            {
                home: {
                    screen: TestScreen,
                    navigationOptions: (navigation) => NavigationUtil.defaultNavigationOptions(navigation)
                }
            },
            {
                initialRouteName: this.props.page,
                initialRouteParams: _initialRouteParams,

                navigationOptions: {
                    gesturesEnabled: true
                },
                mode: "card",
                headerMode: "screen",
                headerTransitionPreset: "uikit",
                transitionConfig: () => ({
                    transitionSpec: {
                        duration: 300,
                        easing: Easing.out(Easing.poly(4)),
                        timing: Animated.timing
                    },
                    screenInterpolator: (sceneProps) => {
                        const {layout, position, scene} = sceneProps;
                        const {index} = scene;

                        const width = layout.initWidth;
                        const translateX = position.interpolate({
                            inputRange: [index - 1, index, index + 1],
                            outputRange: [width, 0, 0]
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
                            outputRange: [0, 1, 1]
                        });
                        return {opacity, transform: [{translateX: translateX}]};
                    }
                })
            }
        );
    }

    componentWillUnmount() {
        BackHandler.removeEventListener("hardwareBackPress", this.onBackPressed);
    }

    componentDidMount() {
        global.pageName = this.props.page;
    }

    render() {
        return <RootStack />;
    }

    onBackPressed = () => {
        return false;
    };
}

AppRegistry.registerComponent("cc-rn", () => App);
