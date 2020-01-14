import React from "react";
import {Platform, View} from "react-native";

const STATUS_BAR_HEIGHT = Platform.OS === "android" ? 22 : 0;

/**
 * 沉浸式状态栏
 */
const defaultHeaderTranslucentStyle = {
    backgroundColor: "#20aa80",
    height: 48 + STATUS_BAR_HEIGHT,
    paddingTop: STATUS_BAR_HEIGHT,
    elevation: 2
};

/**
 * 普通状态栏
 */
const defaultHeaderStyle = {
    backgroundColor: "#fff",
    height: 48,
    elevation: 2
};

const defaultHeaderTitleStyle = {
    textAlign: "center",
    fontWeight: "normal",
    color: "#000",
    flex: 1,
    fontSize: 17
};

const defaultNavigationOptions = ({navigation}) => ({
    headerTitleStyle: global.defaultHeaderTitleStyle,
    headerStyle: global.defaultHeaderStyle,
    // headerLeft: (
    //     <TouchableOpacity onPress={() => navigation.goBack()}>
    //         <Image style={{width: 38, height: 38}} source={require("../../res/img/back.png")} />
    //     </TouchableOpacity>
    // ),
    headerRight: <View />
});

// eslint-disable-next-line camelcase
const cx_navigation_util = {
    defaultHeaderTranslucentStyle,
    defaultHeaderStyle,
    defaultHeaderTitleStyle,
    defaultNavigationOptions
};

// eslint-disable-next-line camelcase
export default cx_navigation_util;
