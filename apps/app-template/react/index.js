import React from "react";
import {AppRegistry, DeviceEventEmitter, NativeModules, StyleSheet, Text, View} from "react-native";


class HomeModule extends React.Component {

    componentWillMount() {
        console.debug("componentWillMount -> " + this.props.native_params);
    }

    componentDidMount() {
        console.debug("componentDidMount -> " + this.props.native_params);
        console.debug(this.props);

        var CXToastUtil = NativeModules.CXToastUtil;
        this.native_listener = DeviceEventEmitter.addListener('native_event', (event) => {
            console.warn("监听到有数据从native传递过来(这里不会重新渲染界面) -> " + event);
            CXToastUtil.showWithDurationAndGravity(event, CXToastUtil.BOTTOM, CXToastUtil.LENGTH_SHORT);
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
});

AppRegistry.registerComponent('react-module-home', () => HomeModule);
