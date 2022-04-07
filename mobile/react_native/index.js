import React from "react";
import {Animated, AppRegistry, BackHandler, NativeModules, Platform, Dimensions, YellowBox} from "react-native";
import {createAppContainer} from "react-navigation";
import {createStackNavigator} from "react-navigation-stack";

import NavigationUtil from "./src/main/js/base/NavigationUtil";
import "./src/main/js/base/Bridge";
import BridgePage from "./src/main/js/pages/BridgePage";
import TabPage from "./src/main/js/pages/TabPage";

YellowBox.ignoreWarnings(["Warning: isMounted(...) is deprecated", "Module RCTImageLoader"]);

let global = {};
let RootStack = {};

export default class App extends React.Component {
    constructor(props) {
        super(props);
        this.tag = "[App]";
        console.log(this.tag, "constructor props:", props);
        console.log(this.tag, "constructor react bridge constants:", {
            OS: Platform.OS,
            SDK_INT: NativeModules.ReactBridge.SDK_INT,
            versionCode: NativeModules.ReactBridge.versionCode,
            versionName: NativeModules.ReactBridge.versionName,
            appName: NativeModules.ReactBridge.appName,
            screenWidth: NativeModules.ReactBridge.screenWidth,
            screenHeight: NativeModules.ReactBridge.screenHeight,
            isSdCardExist: NativeModules.ReactBridge.isSdCardExist,
            statusBarHeight: NativeModules.ReactBridge.statusBarHeight,
            statusBarHeightByDensity: NativeModules.ReactBridge.statusBarHeightByDensity,
            density: NativeModules.ReactBridge.density
        });

        BackHandler.addEventListener("hardwareBackPress", this.onBackPressed);
        global.isIPhoneX = false;
        global.pageName = this.props.page;
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

        RootStack = createAppContainer(
            createStackNavigator(
                {
                    home: {
                        screen: TabPage
                    },
                    bridge: {
                        screen: BridgePage
                    }
                },
                {
                    initialRouteName: this.props.page,
                    initialRouteParams: _initialRouteParams,

                    defaultNavigationOptions: {
                        gestureEnabled: true,
                        headerShown: false
                    },
                    mode: "card",
                    headerMode: "screen"
                }
            )
        );
    }

    componentWillUnmount() {
        console.log(this.tag, "componentWillUnmount");
        BackHandler.removeEventListener("hardwareBackPress", this.onBackPressed);
    }

    render() {
        console.log(this.tag, "render");
        return <RootStack />;
    }

    onBackPressed = () => {
        console.log(this.tag, "onBackPressed");
        return false;
    };
}

AppRegistry.registerComponent("cc-rn", () => App);
