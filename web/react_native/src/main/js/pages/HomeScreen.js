import React, {Component} from "react";
import {Dimensions, Image, ScrollView, StatusBar, StyleSheet, Text, TextInput, TouchableOpacity, View} from "react-native";

export default class HomeScreen extends React.Component {
    static navigationOptions = {
        title: "轮毂清洁",
        headerRight: (
            <TouchableOpacity onPress={() => {}}>
                <Image style={{width: 38, height: 38}} source={require("../../res/img/share.png")} />
            </TouchableOpacity>
        )
    };

    render() {
        return (
            <View style={styles.root}>
                <StatusBar translucent={false} />

                <View style={styles.content}>
                    <ScrollView>
                        <TouchableOpacity
                            onPress={() => {
                                this.props.navigation.push("bridge");
                            }}>
                            <Image style={styles.banner} resizeMode={"contain"} source={require("../../res/img/banner.png")} />
                        </TouchableOpacity>
                        <View style={styles.description}>
                            <Text style={{fontSize: 18, lineHeight: 20, color: "#27343a"}}>轮毂清洁</Text>
                            <Text style={{fontSize: 12, lineHeight: 14, color: "#646464", marginTop: 10}}>轮毂去除铁粉, 去油去污</Text>
                            <Text style={{fontSize: 19, lineHeight: 21, color: "#fc5430", marginTop: 18}}>
                                <Text style={{fontSize: 13}}>¥</Text>
                                80.00
                            </Text>

                            <View style={{backgroundColor: "#e8e8e8", height: 1, marginTop: 7}} />

                            <Text style={{fontSize: 11, lineHeight: 13, color: "#929292", marginTop: 15}}>可直接到店服务, 消费高峰时段需等候, 敬请谅解.</Text>
                        </View>

                        <View style={{backgroundColor: "#f5f5f5", height: 10}} />

                        <View style={{flexDirection: "row", backgroundColor: "#ffffff", paddingTop: 15, paddingBottom: 15, paddingLeft: 13, paddingRight: 10, alignItems: "center"}}>
                            <Text style={{fontSize: 15, lineHeight: 17, color: "#2c353c", flex: 1}}>客户体验(8)</Text>

                            <TouchableOpacity
                                style={{flexDirection: "row", alignItems: "center"}}
                                onPress={() => {
                                    ToastAndroid.show("查看更多", ToastAndroid.SHORT);
                                }}>
                                <Text style={{fontSize: 13, color: "#9a9a9a"}}>查看更多</Text>

                                <Image style={{width: 16, height: 16}} resizeMode={"contain"} source={require("../../res/img/right_arrow.png")} />
                            </TouchableOpacity>
                        </View>

                        <Image style={styles.banner} resizeMode={"contain"} source={require("../../res/img/banner.png")} />
                        <Image style={styles.banner} resizeMode={"contain"} source={require("../../res/img/banner.png")} />
                        <Image style={styles.banner} resizeMode={"contain"} source={require("../../res/img/banner.png")} />
                        <Image style={styles.banner} resizeMode={"contain"} source={require("../../res/img/banner.png")} />
                        <Image style={styles.banner} resizeMode={"contain"} source={require("../../res/img/banner.png")} />
                        <Image style={styles.banner} resizeMode={"contain"} source={require("../../res/img/banner.png")} />
                        <Image style={styles.banner} resizeMode={"contain"} source={require("../../res/img/banner.png")} />
                        <Image style={styles.banner} resizeMode={"contain"} source={require("../../res/img/banner.png")} />
                        <Image style={styles.banner} resizeMode={"contain"} source={require("../../res/img/banner.png")} />
                        <Image style={styles.banner} resizeMode={"contain"} source={require("../../res/img/banner.png")} />
                        <Image style={styles.banner} resizeMode={"contain"} source={require("../../res/img/banner.png")} />
                    </ScrollView>
                </View>

                <View>
                    <View style={{backgroundColor: "#e8e8e8", height: 1}} />

                    <View style={{flexDirection: "row", backgroundColor: "#fff", padding: 8, alignItems: "center"}}>
                        <TextInput
                            style={{
                                height: 30,
                                lineHeight: 15,
                                fontSize: 15,
                                color: "#28373e",
                                textAlignVertical: "center",
                                backgroundColor: "#f5f5f5",
                                paddingTop: 0,
                                paddingBottom: 0,
                                paddingRight: 0,
                                paddingLeft: 15,
                                margin: 0,
                                flex: 1,
                                borderRadius: 5,
                                borderWidth: 0
                            }}
                            editable={true}
                            multiline={false}
                            autoFocus={false}
                            underlineColorAndroid="transparent"
                            placeholderText="#f5f5f5"
                            placeholder="已选门店: 上海光明路店"
                            onChangeText={(text) => {}}></TextInput>

                        <TouchableOpacity
                            style={{marginLeft: 25, flexDirection: "row", alignItems: "center"}}
                            onPress={() => {
                                ToastAndroid.show("切换", ToastAndroid.SHORT);
                            }}>
                            <Text style={{fontSize: 13, color: "#2d2e30", marginRight: 3}}>切换</Text>

                            <Image style={{width: 16, height: 16, marginRight: 10}} resizeMode={"contain"} source={require("../../res/img/right_arrow.png")} />
                        </TouchableOpacity>
                    </View>
                    <View style={{backgroundColor: "#e8e8e8", height: 1}} />

                    <View style={{flexDirection: "row", backgroundColor: "#fff", alignItems: "center", justifyContent: "center"}}>
                        <TouchableOpacity
                            onPress={() => {
                                ToastAndroid.show("客服", ToastAndroid.SHORT);
                            }}>
                            <View style={{flexDirection: "column", backgroundColor: "#fff", alignItems: "center", justifyContent: "center", marginLeft: 23}}>
                                <Image style={{width: 22, height: 22}} resizeMode={"contain"} source={require("../../res/img/kefu.png")} />
                                <Text style={{fontSize: 12, color: "#666666"}}>客服</Text>
                            </View>
                        </TouchableOpacity>

                        <View style={{backgroundColor: "#e8e8e8", width: 1, height: 40, marginLeft: 15, marginRight: 7, marginTop: 9, marginBottom: 9}} />

                        <TouchableOpacity
                            style={{
                                flex: 1,
                                height: 40,
                                justifyContent: "center",
                                alignContent: "center",
                                margin: 8,
                                alignItems: "center",
                                backgroundColor: "#f94246",
                                borderRadius: 5,
                                borderWidth: 0
                            }}
                            onPress={() => {
                                //this.props.navigation.navigate('bridge')
                                this.props.navigation.navigate("order_commit");
                            }}>
                            <Text style={{textAlign: "center", fontSize: 16, color: "white"}}>立即预约</Text>
                        </TouchableOpacity>
                    </View>
                </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    banner: {
        height: (Dimensions.get("window").width * 2.0) / 3.0,
        width: Dimensions.get("window").width
    },

    content: {
        flex: 1
    },

    description: {
        backgroundColor: "#fff",
        flexDirection: "column",
        paddingBottom: 18,
        paddingLeft: 18,
        paddingTop: 18,
        width: Dimensions.get("window").width
    },

    root: {
        flex: 1,
        flexDirection: "column",
        backgroundColor: "#efefef"
    }
});
