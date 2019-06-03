/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */
//noinspection ES6UnusedImports
import React, {Component} from "react";
//noinspection ES6UnusedImports
import {AccessibilityInfo, ActivityIndicator, Animated, AppRegistry, BackHandler, Button, CheckBox, DeviceEventEmitter, Easing, FlatList, Image, ImageBackground, ImageEditor, ImageStore, KeyboardAvoidingView, Modal, Picker, Platform, RefreshControl, SafeAreaView, ScrollView, SectionList, Slider, StatusBar, StyleSheet, SwipeableFlatList, Switch, Text, TextInput, Touchable, TouchableHighlight, TouchableNativeFeedback, TouchableOpacity, TouchableWithoutFeedback, View, VirtualizedList, WebView, YellowBox} from "react-native";
//noinspection ES6UnusedImports
import {createStackNavigator} from "react-navigation";

/*
//==========================================================================================
import cx_navigation_util from "./src/main/js/base/cx_navigation_util";
import HomeScreen from "./src/main/js/pages/HomeScreen";
import BridgeScreen from "./src/main/js/pages/BridgeScreen";
import OrderCommitScreen from "./src/main/js/pages/OrderCommitScreen";
console.log("OS:" + Platform.OS);
console.log("STATUS_BAR_HEIGHT:" + cx_navigation_util.STATUS_BAR_HEIGHT);
YellowBox.ignoreWarnings(['Warning: isMounted(...) is deprecated', 'Module RCTImageLoader']);
//==========================================================================================

var mainComponent = null;
DeviceEventEmitter.removeAllListeners(); //fixed ios multi-trigger
DeviceEventEmitter.addListener("CRNStartLoadEvent", function (event) {
	if (event) {
		if (event.startLoadTime) {
			console.log("CRN_LOAD_TIME_JS Receive startLoadTime: " + event.startLoadTime);
			global.__module_start_load_time = event.startLoadTime
		}
	}
});

DeviceEventEmitter.addListener("ToggleLoadModule", function (event) {
	if (event) {
		if (event.crnDev) {
			global.__CRN_DEV__ = parseInt(event.crnDev) > 0;
			console.log("event crnDev: " + event.crnDev + " && __CRN_DEV__: " + global.__CRN_DEV__);
		} else {
			global.__CRN_DEV__ = false;
		}
		if (event.modulePath) {
			console.log("event modulePath: " + event.modulePath);
			global.__module_path = event.modulePath;
		}
		if (event.inUsePkgId) {
			console.log("event inUsePkgId: " + event.inUsePkgId);
			global._crn_constant_pkgid = event.inUsePkgId;
		}
		if (event.inUseCommonPkgId) {
			console.log("event inUseCommonPkgId: " + event.inUseCommonPkgId);
			global._crn_constant_compkgid = event.inUseCommonPkgId;
		}
		if (event.inAppPkgId) {
			console.log("event inAppPkgId: " + event.inAppPkgId);
			global._crn_constant_inapppkgid = event.inAppPkgId;
		}
		if (event.inAppCommonPkgId) {
			console.log("event inAppCommonPkgId: " + event.inAppCommonPkgId);
			global._crn_constant_inappcompkgid = event.inAppCommonPkgId;
		}
		if (event.moduleId && (typeof(mainComponent) === "undefined" || mainComponent === null)) {
			console.log("event moduleId: " + event.moduleId);
			// mainComponent = global.requireMap["fad32daa29eacd62cf9b88ef2e370da5"]();
			// global.__module_start_load_time = event.startLoadTime
			if (_component) {
				_component.setState({trigger: true});
				_component = null;
			}
		}
	}
});

var _component;
class StartComponent extends Component {

	getInitialState() {
		return {trigger: false};
	}

	render() {
		_component = this;
		var _content = null;

		// mainComponent = global.requireMap["fad32daa29eacd62cf9b88ef2e370da5"]();
		mainComponent = HomeScreen;

		if (mainComponent) {
			//需将this.props传给component
			_content = React.createElement(mainComponent, this.props);
		}
		return _content || <View/>;
	}

}

AppRegistry.registerComponent('react-module-home', () => StartComponent);
*/
