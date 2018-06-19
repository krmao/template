import React from 'react';
import { Text,AppRegistry, StyleSheet, ScrollView, TouchableHighlight, ToastAndroid } from 'react-native';

export default class ApiComponent extends React.PureComponent {

  render() {
    return (
      <ScrollView style={styles.box} showsVerticalScrollIndicator={ true }>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testInitialProperties}>
          <Text style={styles.centerText}>显示initialProperties</Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testToastApi}>
          <Text style={styles.centerText}>brn.showToast 显示消息</Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testGetRequestApi}>
          <Text style={styles.centerText}>brn.request GET执行请求</Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testPostRequestApi}>
          <Text style={styles.centerText}>brn.request POST执行请求</Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testGetLocaleApi}>
          <Text style={styles.centerText}>brn.getLocale 获取当前Locale</Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testLoginInfo}>
          <Text style={styles.centerText}>brn.getLoginInfo 获取登录用户信息</Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testGetVersionInfo}>
          <Text style={styles.centerText}>brn.getVersionInfo 获取版本信息</Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testSelectDate}>
          <Text style={styles.centerText}>brn.selectDate 选择日期</Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testGetNetworkParams}>
          <Text style={styles.centerText}>brn.getNetworkParams 获取请求参数</Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testGetRequestDomain}>
          <Text style={styles.centerText}>brn.getRequestDomain 根据url获取最优链路</Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testOpenScene}>
          <Text style={styles.centerText}>brn.openScene 根据componentName跳转另一个RN页面
          </Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testOpenH5Page}>
          <Text style={styles.centerText}>brn.openH5Page 根据componentName跳转另一个H5页面
          </Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testOpenSchema}>
          <Text style={styles.centerText}>brn.openSchema 呼叫抖音的schema,当然别的也可以
          </Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testPreference}>
          <Text style={styles.centerText}>brn.savePreference brn.getPreference 测试存储能力
          </Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testFinishSelf}>
          <Text style={styles.centerText}>brn.close 关闭当前页面
          </Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testNativeCommunication}>
          <Text style={styles.centerText}>Native层通信方案
          </Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testEventBus}>
          <Text style={styles.centerText}>Js层EventBus通信方案
          </Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testLongList}>
          <Text style={styles.centerText}>大列表测试
          </Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testIconFont}>
          <Text style={styles.centerText}>支持IconFont
          </Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testGradient}>
          <Text style={styles.centerText}>gradient指北
          </Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testPollution}>
          <Text style={styles.centerText}>数据污染测试
          </Text>
        </TouchableHighlight>
        <TouchableHighlight style={styles.textWrapper} onPress={this._testFakeApp}>
          <Text style={styles.centerText}>Fake App加载实验
          </Text>
        </TouchableHighlight>
      </ScrollView>
    );
  }

  _testInitialProperties = () => {
    
  }

  _testToastApi = () => {
    
  }

  _testPostRequestApi = () => {

  }

  _testGetRequestApi = () => {
    
  }

  _testGetVersionInfo = () => {
    
  }

  _testLoginInfo = () => {
    
  }

  _testGetLocaleApi = () => {
    
  }

  _testSelectDate = () => {
    
  }

  _testGetNetworkParams = () => {
    
  }

  _testGetRequestDomain = () => {
    
  }


  // brn.openScene(reactId: String, sceneName: String, initProps: Map, callback)
  _testOpenScene = () => {
    
  }

  // brn.openH5Page(reactId: String, url: String, controlFlags: Map, callback)
  _testOpenH5Page = () => {
    
  }

  // brn.openSchema(reactId: String, schema: String, callback)
  _testOpenSchema = () => {
    
  }

  _testFinishSelf = () => {
    
  }

  _testNativeCommunication = () => {
  
  }

  _testEventBus = () => {
    
  }

  _testLongList = () => {
    
  }

  _testIconFont = () => {
    
  }

  _testGradient = () => {
    
  }

  _testPollution = () => {
    if(!ToastAndroid.clickCount) {
      ToastAndroid.clickCount = 0; 
    } else {
      ToastAndroid.clickCount += 1;
    }
    var str = `ToastAndroid.clickCount -> ` + ToastAndroid.clickCount.toString();
    console.log(str);
  }

  // brn.savePreference(key: String, value: String, callback
  _testPreference = () => {
    
  }

  _testFakeApp = () => {
    
  }
}

const styles = StyleSheet.create({
  box: {
    marginBottom: 30,
    marginTop: 30,
    width: '100%',
    height: '100%'
  },
  textWrapper: {
    width: '100%',
    height: 50,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: "#666666",
    marginBottom: 10
  },
  centerText: {
    color: 'white',
    fontSize: 12
  }
});

AppRegistry.registerComponent('test_land', () => ApiComponent);