import React from "react";
import {ScrollView, StatusBar, StyleSheet, Text, Alert, TextInput, View, TouchableHighlight} from "react-native";
import {SafeAreaView} from "react-native-safe-area-context";

/**
 * 生命周期更新
 * componentWillMount 被弃用, 请求/添加事件监听 放到 componentDidMount, 里面的 setState 直接作为成员变量初始化
 *
 *
 * https://www.javascriptcn.com/read-28923.html
 * https://reactjs.org/blog/2018/03/27/update-on-async-rendering.html
 */

export default class BridgePage extends React.Component {
    static navigationOptions = ({navigation}) => ({
        tabBarVisible: false,
        headerShown: false
    });

    /**
     * 新的静态getDerivedStateFromProps生命周期在组件实例化以及接收新props后调用。
     * 它可以返回一个对象来更新state，或者返回null来表示新的props不需要任何state更新。
     * 与componentDidUpdate一起，这个新的生命周期应该覆盖传统componentWillReceiveProps的所有用例。
     *
     * @param {*} props
     * @param {*} state
     */
    static getDerivedStateFromProps(nextProps, prevState) {
        console.log(this.tag, "getDerivedStateFromProps -> nextProps", nextProps, " , prevState=", prevState);
        // Store prevId in state so we can compare when props change.
        // Clear out previously-loaded data (so we don't render stale stuff).
        /* if (nextProps.id !== prevState.prevId) {
            return {
                isScrollingDown: props.currentRow > state.lastRow,
                lastRow: props.currentRow,
                externalData: null,
                prevId: nextProps.id
            };
        } */

        // No state update necessary
        return null;
    }

    constructor(props) {
        super(props);
        this.tag = "[BridgePage]";
        console.log(this.tag, "constructor");
    }

    state = {
        currentColor: "red",
        palette: "rgb",
        subscribedValue: "hahaha",
        isScrollingDown: false,
        lastRow: null,
        externalData: null
    };

    componentDidMount() {
        console.log(this.tag, "componentDidMount");
        // Event listeners are only safe to add after mount,
        // So they won't leak if mount is interrupted or errors.
        // this.props.value.subscribe(this.handleSubscriptionChange);

        // External values could change between render and mount,
        // In some cases it may be important to handle this case.
        /* if (this.state.subscribedValue !== this.props.value) {
            this.setState({
                subscribedValue: this.props.value
            });
        }
        this._loadAsyncData(this.props.id); */
    }

    /**
     * 新的getSnapshotBeforeUpdate生命周期在更新之前被调用（例如，在DOM被更新之前）。
     * 此生命周期的返回值将作为第三个参数传递给componentDidUpdate。 （这个生命周期不是经常需要的，但可以用于在恢复期间手动保存滚动位置的情况。）
     * 与componentDidUpdate一起，这个新的生命周期将覆盖旧版componentWillUpdate的所有用例。
     *
     * @param {*} prevProps
     * @param {*} prevState
     */
    getSnapshotBeforeUpdate(prevProps, prevState) {
        console.log(this.tag, "getSnapshotBeforeUpdate prevProps", prevProps + " , prevState=", prevState);
    }

    /**
     * 参数更新流程
     * getDerivedStateFromProps -> shouldComponentUpdate(false)
     * getDerivedStateFromProps -> shouldComponentUpdate(true) -> render -> getSnapshotBeforeUpdate -> componentDidUpdate
     * @returns {boolean}
     */
    shouldComponentUpdate() {
        return false;
    }

    componentDidUpdate(prevProps, prevState) {
        console.log(this.tag, "componentDidUpdate prevProps=", prevProps + " , prevState=", prevState);
        /* if (this.state.someStatefulValue !== prevState.someStatefulValue) {
            this.props.onChange(this.state.someStatefulValue);
        }
        if (this.state.externalData === null) {
            this._loadAsyncData(this.props.id);
        } */
    }

    componentWillUnmount() {
        console.log(this.tag, "componentWillUnmount");
        // this.props.value.unsubscribe(this.handleSubscriptionChange);
        /* if (this._asyncRequest) {
            this._asyncRequest.cancel();
        } */
    }

    handleSubscriptionChange = (value) => {
        console.log(this.tag, "handleSubscriptionChange");
        /* this.setState({
            subscribedValue: value
        }); */
    };

    _loadAsyncData(id) {
        console.log(this.tag, "_loadAsyncData");
        /* this._asyncRequest = asyncLoadData(id).then((externalData) => {
            this._asyncRequest = null;
            this.setState({externalData});
        }); */
    }

    render() {
        console.log(this.tag, "render");
        /* if (this.state.externalData === null) {
            // Render loading state ...
        } else {
            // Render real UI ...
        } */

        return (
            <SafeAreaView style={styles.container}>
                {/* <StatusBar translucent={true} /> */}

                <ScrollView style={styles.root} showsVerticalScrollIndicator={true}>
                    <TouchableHighlight style={styles.item} underlayColor="midnightblue" onPress={() => this.onItemClick("open")}>
                        <Text style={styles.text}>1. open new page</Text>
                    </TouchableHighlight>

                    <TouchableHighlight style={styles.item} onPress={() => this.onItemClick("close")}>
                        <Text style={styles.text}>2. close current page with result</Text>
                    </TouchableHighlight>

                    <TouchableHighlight style={styles.item} onPress={() => this.onItemClick("showToast")}>
                        <Text style={styles.text}>3. show toast</Text>
                    </TouchableHighlight>

                    <View style={styles.itemMulti}>
                        <TouchableHighlight style={styles.item} onPress={() => this.onItemClick("put")}>
                            <Text style={styles.text}>4. put value</Text>
                        </TouchableHighlight>

                        <TextInput
                            style={styles.itemEdit}
                            placeholder={"请输入"}
                            underlineColorAndroid="transparent"
                            placeholderTextColor="black"
                            onChangeText={(text) => {
                                this.setState({
                                    valueName: text
                                });
                            }}
                        />
                    </View>

                    <TouchableHighlight style={styles.item} onPress={() => this.onItemClick("get")}>
                        <Text style={styles.text}>5. get value</Text>
                    </TouchableHighlight>

                    <TouchableHighlight style={styles.item} onPress={() => this.onItemClick("getUserInfo")}>
                        <Text style={styles.text}>6. get user info</Text>
                    </TouchableHighlight>

                    <TouchableHighlight style={styles.item} onPress={() => this.onItemClick("getLocationInfo")}>
                        <Text style={styles.text}>7. get location info</Text>
                    </TouchableHighlight>

                    <TouchableHighlight style={styles.item} onPress={() => this.onItemClick("getDeviceInfo")}>
                        <Text style={styles.text}>8. get device info</Text>
                    </TouchableHighlight>
                </ScrollView>
            </SafeAreaView>
        );
    }

    onItemClick = (type) => {
        switch (type) {
            case "open": {
                window.bridge.open("smart://template/rn?component=cc-rn&page=home&params=");
                break;
            }
            case "close": {
                window.bridge.close("OK");
                break;
            }
            case "showToast": {
                window.bridge.showToast("Hello I am from Html5");
                break;
            }
            case "put": {
                console.log("userName:" + this.state.valueName);
                window.bridge.put("userName", this.state.valueName);
                break;
            }
            case "get": {
                window.bridge.get("userName", function(value) {
                    Alert.alert(JSON.stringify(value.result));
                });
                break;
            }
            case "getUserInfo": {
                window.bridge.getUserInfo(function(value) {
                    Alert.alert(JSON.stringify(value.result));
                });
                break;
            }
            case "getLocationInfo": {
                window.bridge.getLocationInfo(function(value) {
                    Alert.alert(JSON.stringify(value.result));
                });
                break;
            }
            case "getDeviceInfo": {
                window.bridge.getDeviceInfo(function(value) {
                    Alert.alert(JSON.stringify(value.result));
                });
                break;
            }
        }
    };
}

const styles = StyleSheet.create({
    container: {
        backgroundColor: "darkblue",
        flex: 1
    },
    item: {
        backgroundColor: "royalblue",
        flex: 5,
        marginBottom: 2.5,
        padding: 15
    },
    itemEdit: {
        backgroundColor: "white",
        color: "black",
        flex: 2,
        fontSize: 15,
        fontStyle: "italic",
        fontWeight: "bold"
    },
    itemMulti: {
        flexDirection: "row"
    },
    root: {
        flex: 1,
        paddingTop: 0
    },
    text: {
        color: "white",
        fontSize: 15,
        fontStyle: "italic",
        fontWeight: "bold",
        textAlign: "left"
    }
});
