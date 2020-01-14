import React from "react";
import {ScrollView, StatusBar, StyleSheet, Text, Alert, TextInput, View, TouchableHighlight} from "react-native";
import {SafeAreaView} from "react-native-safe-area-context";

export default class BridgeScreen extends React.Component {
    static navigationOptions = ({navigation}) => ({
        tabBarVisible: false,
        header: null
    });

    constructor(props) {
        super(props);

        this.state = {
            name: null,
            valueName: null
        };
    }

    componentWillMount() {
        console.debug("componentWillMount -> " + this.props.native_params);
    }

    componentDidMount() {
        console.debug("componentDidMount -> " + this.props.native_params);
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
        return false;
    }

    componentDidUpdate() {
        console.debug("componentDidUpdate -> " + this.props.native_params);
    }

    componentWillUnmount() {
        console.debug("componentWillUnmount -> " + this.props.native_params);
    }

    render() {
        console.debug("render -> " + this.props.native_params);

        return (
            <SafeAreaView style={styles.container}>
                <StatusBar translucent={true} />

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
        backgroundColor: "lightgray",
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
        paddingTop: 15
    },
    text: {
        color: "white",
        fontSize: 15,
        fontStyle: "italic",
        fontWeight: "bold",
        textAlign: "left"
    }
});
