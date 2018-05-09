import React from "react";
import {AppRegistry, DeviceEventEmitter, Dimensions, Image, NativeModules, ScrollView, StyleSheet, Text, TextInput, TouchableHighlight, View} from "react-native";


class HomeModule extends React.Component {

    constructor() {
        super();

        this.state = {
            "dataToNative": 0,
            "resultFromNative": "null"
        }
    }

    componentWillMount() {
        console.debug("componentWillMount -> " + this.props.native_params);
    }

    componentDidMount() {
        console.debug("componentDidMount -> " + this.props.native_params);
        console.debug(this.props);

        this.reactBridge = NativeModules.ReactBridge;


        var CXToastUtil = NativeModules.CXToastUtil;
        this.native_listener = DeviceEventEmitter.addListener('native_event', (event) => {
            console.warn("监听到有数据从native传递过来(这里不会重新渲染界面) -> " + event);
            //CXToastUtil.showWithDurationAndGravity(event, CXToastUtil.BOTTOM, CXToastUtil.LENGTH_SHORT);
        });
    }

    componentWillReceiveProps() {
        console.debug("componentWillReceiveProps -> " + this.props.native_params);
    }

    /**
     * 参数更新流程
     * componentWillReceiveProps -> shouldComponentUpdate(false)
     * componentWillReceiveProps -> shouldComponentUpdate(true) -> render -> componentDidUpdate
     * @returns {boolean}
     */
    shouldComponentUpdate() {
        let shouldComponentUpdate = this.props.native_params % 2 !== 0;
        console.debug("shouldComponentUpdate(" + shouldComponentUpdate + ") -> " + this.props.native_params);
        return shouldComponentUpdate
    }

    componentDidUpdate() {
        console.debug("componentDidUpdate -> " + this.props.native_params);
    }

    componentWillUnmount() {
        console.debug("componentWillUnmount -> " + this.props.native_params);
        this.native_listener.remove();
    }

    render() {
        console.debug("render -> " + this.props.native_params);

        return (
            <View style={styles.root}>

                <View style={styles.content}>

                    <View style={styles.titleBar}>
                        <Image
                            style={{width: 38, height: 38}}
                            source={require('./images/back.png')}
                        />
                        <Text style={styles.title}>轮毂清洁</Text>
                        <Image
                            style={{width: 38, height: 38}}
                            source={require('./images/share.png')}
                        />

                    </View>

                    <ScrollView>


                        <Image
                            style={styles.banner}
                            resizeMode={"contain"}
                            source={require('./images/banner.png')}
                        />
                        <View style={styles.description}>

                            <Text style={styles.text}>轮毂清洁</Text>
                            <Text style={styles.text}>轮毂去除铁粉, 去油去污</Text>
                            <Text style={styles.text}>¥80.00</Text>
                            <View style={{backgroundColor: "#eeeeee", height: 1, marginLeft: 15}}/>
                            <Text style={styles.text}>可直接到店服务, 消费高峰时段需等候, 敬请谅解.</Text>
                            <View style={{backgroundColor: "#eeeeee", height: 10, marginLeft: 0}}/>

                            <View style={{flexDirection: "row", backgroundColor: "#ffffff"}}>
                                <Text style={{flex: 1}}>客户体验(8)</Text>

                                <Text>查看更多</Text>
                            </View>

                            <Image style={styles.banner} resizeMode={"contain"} source={require('./images/banner.png')}/>
                            <Image style={styles.banner} resizeMode={"contain"} source={require('./images/banner.png')}/>
                            <Image style={styles.banner} resizeMode={"contain"} source={require('./images/banner.png')}/>
                            <Image style={styles.banner} resizeMode={"contain"} source={require('./images/banner.png')}/>
                            <Image style={styles.banner} resizeMode={"contain"} source={require('./images/banner.png')}/>
                            <Image style={styles.banner} resizeMode={"contain"} source={require('./images/banner.png')}/>
                            <Image style={styles.banner} resizeMode={"contain"} source={require('./images/banner.png')}/>
                            <Image style={styles.banner} resizeMode={"contain"} source={require('./images/banner.png')}/>
                            <Image style={styles.banner} resizeMode={"contain"} source={require('./images/banner.png')}/>
                            <Image style={styles.banner} resizeMode={"contain"} source={require('./images/banner.png')}/>
                            <Image style={styles.banner} resizeMode={"contain"} source={require('./images/banner.png')}/>

                        </View>

                    </ScrollView>
                </View>


                <View>
                    <View style={{flexDirection: "row", backgroundColor: "#fff"}}>
                        <TextInput style={{flex: 1}}>xxxx</TextInput>
                        <Text>切换></Text>
                    </View>
                    <View style={{flexDirection: "row", backgroundColor: "#fff"}}>
                        <View style={{flexDirection: "column", backgroundColor: "#fff"}}>
                            <Image
                                style={{width: 38, height: 38}}
                                resizeMode={"contain"}
                                source={require('./images/kefu.png')}
                            />
                            <Text>客服</Text>
                        </View>
                        <TouchableHighlight
                            style={{
                                flex: 1,
                                justifyContent: "center",
                                alignContent: "center",
                                margin: 5,
                                alignItems: "center",
                                backgroundColor: "red",
                            }}
                            onPress={() => {
                            }}>
                            <Text style={{textAlign: "center"}}>立即预约</Text>
                        </TouchableHighlight>
                    </View>
                </View>

            </View>
        );
    }

}
var styles = StyleSheet.create({

    root: {
        flex: 1,
        flexDirection: "column",
        backgroundColor: '#efefef'
    },

    content: {
        flex: 1,
    },

    titleBar: {
        //沿着方向居中
        justifyContent: "center",
        //垂直方向居中
        alignItems: "center",
        flexDirection: "row",
        marginTop: 22,
        height: 48,
        backgroundColor: "#ff00ff"
    },

    title: {
        fontSize: 20,
        fontWeight: 'bold',
        color: 'red',
        textAlign: 'center',
        margin: 10,
    },

    banner: {
        width: Dimensions.get('window').width,
        height: Dimensions.get('window').width * 2.0 / 3.0,
    },

    description: {
        flexDirection: "column",
        width: Dimensions.get('window').width
    },

    text: {
        fontSize: 20,
        color: 'black',
        textAlign: 'left',
    }

});

AppRegistry.registerComponent('react-module-home', () => HomeModule);
