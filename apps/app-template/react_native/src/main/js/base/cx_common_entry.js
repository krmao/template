/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  View,
  DeviceEventEmitter
} from 'react-native';

//空依赖
require('@ctrip/crn');
require("lodash");
require('immutable');

var mainComponent = null;
DeviceEventEmitter.removeAllListeners(); //fixed ios multi-trigger
DeviceEventEmitter.addListener("CRNStartLoadEvent", function(event) {
  if (event) {
    if (event.startLoadTime) {
      console.log("CRN_LOAD_TIME_JS Receive startLoadTime: " + event.startLoadTime);
      global.__module_start_load_time = event.startLoadTime
    }
}});

DeviceEventEmitter.addListener("ToggleLoadModule", function(event) {
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
      mainComponent = require(event.moduleId);
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
    if (mainComponent) {
      //需将this.props传给component
      _content = React.createElement(mainComponent, this.props);
    }
    return _content || <View/>;
  }

}

AppRegistry.registerComponent('RNTemplateApp', () => StartComponent);
