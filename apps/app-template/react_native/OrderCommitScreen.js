import React, {Component} from "react";
import {Alert, Image, ScrollView, StyleSheet, Text, TextInput, TouchableHighlight, View} from "react-native";

export default class OrderCommitScreen extends React.Component {

    static navigationOptions = {
        title: '提交订单',
    };

    render() {
        return (
            <View style={styles.container}>
                <ScrollView style={{flex: 1}}>

                    <View style={{flexDirection: 'row', alignItems: 'center', paddingLeft: 10, height: 60, backgroundColor: '#ffffff', marginTop: 0}}>
                        <Text style={{fontSize: 24, fontWeight: 'bold'}}>上海光明路店</Text>
                    </View>

                    <View style={{flexDirection: 'row', alignItems: 'center', paddingLeft: 10, paddingRight: 10, backgroundColor: '#ffffff', height: 45, borderBottomWidth: 1, borderBottomColor: '#e7e7e7'}}>
                        <Text style={{flex: 1}}>服务时间</Text>
                        <Text style={{paddingHorizontal: 10}}>2018 -06-11 上午 10：00</Text>
                        <Image style={{width: 16, height: 16, marginRight: 10}} resizeMode={"contain"} source={require('./img/right_arrow.png')}/>
                    </View>

                    <View style={{flexDirection: 'row', alignItems: 'center', paddingLeft: 10, paddingRight: 10, backgroundColor: '#ffffff', height: 45, borderBottomWidth: 1, borderBottomColor: '#e7e7e7'}}>
                        <Text style={{width: 80}}>联系电话</Text>
                        <Text style={{color: '#cccccc'}}>18980471122</Text>
                    </View>

                    <View style={{flexDirection: 'row', alignItems: 'center', paddingLeft: 10, backgroundColor: '#ffffff', height: 45, borderBottomWidth: 1, borderBottomColor: '#e7e7e7'}}>
                        <Text style={{width: 80}}>联系人</Text>
                        <TextInput style={{color: '#000000', fontWeight: 'bold'}}>胡冬瓜</TextInput>
                    </View>

                    <View style={{height: 45, flexDirection: 'row', alignItems: 'center', paddingLeft: 10, backgroundColor: '#f5f5f5', borderBottomWidth: 1, borderBottomColor: '#e7e7e7'}}>
                        <Text style={{color: '#666666'}}>服务项目</Text>
                    </View>

                    <View style={{flexDirection: 'row', alignItems: 'center', paddingLeft: 10, paddingRight: 10, backgroundColor: '#ffffff', height: 45, borderBottomWidth: 1, borderBottomColor: '#e7e7e7'}}>
                        <Text style={{flex: 1}}>轮毂清洗</Text>
                        <Text>￥80.00</Text>
                    </View>

                    <View style={{flexDirection: 'row', alignItems: 'center', paddingLeft: 10, paddingRight: 10, backgroundColor: '#ffffff', height: 45, borderBottomWidth: 1, borderBottomColor: '#e7e7e7'}}>
                        <Text style={{flex: 1}}/>
                        <Text style={{textAlign: 'right'}}>合计</Text>
                        <Text style={{textAlign: 'right', color: 'red'}}>￥80.00</Text>
                    </View>

                    <View style={{flexDirection: 'row', alignItems: 'center', marginTop: 10, paddingLeft: 10, paddingRight: 10, backgroundColor: '#ffffff', height: 45, borderBottomWidth: 1, borderBottomColor: '#e7e7e7'}}>
                        <Text style={{flex: 1}}>优惠劵</Text>
                        <Text style={{color: '#cccccc', paddingHorizontal: 10}}>无可用券</Text>
                        <Image style={{width: 16, height: 16, marginRight: 10}} resizeMode={"contain"} source={require('./img/right_arrow.png')}/>
                    </View>

                    <View style={{flexDirection: 'row', alignItems: 'center', paddingLeft: 10, paddingRight: 10, backgroundColor: '#ffffff', height: 45, borderBottomWidth: 1, borderBottomColor: '#e7e7e7'}}>
                        <Text style={{flex: 1}}>套餐卡</Text>
                        <Text style={{color: '#cccccc', paddingHorizontal: 10}}>无可用卡</Text>
                        <Image style={{width: 16, height: 16, marginRight: 10}} resizeMode={"contain"} source={require('./img/right_arrow.png')}/>
                    </View>

                </ScrollView>
                <View style={{flexDirection: 'row', alignItems: 'center', paddingLeft: 10, backgroundColor: '#f9fbfd', height: 75, borderTopWidth: 1, borderTopColor: '#e7e7e7'}}>
                    <Text >在线支付</Text>
                    <Text style={{flex: 1, color: 'red'}}>￥80.00</Text>
                    <TouchableHighlight style={{alignItems: 'center', justifyContent: 'center', borderRadius: 5, marginRight: 5, backgroundColor: '#dd3f48', width: 120, height: 48}} onPress={
                        () => {
                            Alert.alert(
                                `提示`,
                                '跳转到 bridge 测试 ?',
                                [
                                    {text: '以后再说', onPress: () => console.log('Ask me later pressed')},
                                    {text: '取消', onPress: () => console.log('Cancel Pressed'), style: 'cancel'},
                                    {text: '确定', onPress: () => this.props.navigation.push('bridge')},
                                ]
                            )
                        }
                    }>
                        <Text style={{color: 'white', fontWeight: 'bold', fontSize: 20}}>确认提交</Text>
                    </TouchableHighlight>
                </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F5F5F5',
    },
    welcome: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
});
