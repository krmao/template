import React from "react";
import {AppRegistry, DeviceEventEmitter, NativeModules, StyleSheet, Text, TouchableHighlight, View} from "react-native";


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
            <View style={styles.container}>
                <TouchableHighlight onPress={() => {
                    this.reactBridge.callNative((this.state.dataToNative).toString())
                        .then(
                            (successResult) => {
                                console.debug("successResult -> " + successResult);
                                this.setState({
                                    "dataToNative": Number(successResult) + 1,
                                    "resultFromNative": successResult
                                })
                            },
                            (errorCode, errorMsg, error) => {
                                console.debug("errorCode -> " + errorCode);
                                console.debug("errorMsg -> " + errorMsg);
                                console.debug("error -> ");
                                console.warn(error);
                            }
                        )
                }}>
                    <Text style={styles.button}>{"点击调用 native 方法 并传值:" + (this.state.dataToNative)}</Text>
                </TouchableHighlight>


                <Text style={styles.content}>调用 native 后的返回结果: {this.state.resultFromNative}</Text>
                <Text style={styles.content}>当前 REACT-NATIVE 启动参数: {this.props.native_params}</Text>
                <Text style={styles.desc}>(只有双数才会重新渲染界面)</Text>
                <Text style={styles.desc}>通过在 native 重新设置 react_root_view?.appProperties 修改 REACT-NATIVE 启动参数</Text>
                <Text style={styles.desc}>通过 shouldComponentUpdate 判断是否需要重新渲染界面</Text>
            </View>
        );
    }

}
var styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        backgroundColor: '#D3D3D3'
    },
    content: {
        fontSize: 20,
        fontWeight: 'bold',
        color: 'red',
        textAlign: 'center',
        margin: 10,
    },
    desc: {
        fontSize: 12,
        textAlign: 'center'
    },
    button: {
        fontSize: 20,
        fontWeight: 'bold',
        color: 'white',
        textAlign: 'center',
        padding: 10,
        backgroundColor: 'blue',
    },
});

AppRegistry.registerComponent('react-module-home', () => HomeModule);
