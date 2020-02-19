import React from "react";
import {Button, Dimensions, NativeModules, Text, View} from "react-native";
import RecyclerComponent from "../../base/component/RecyclerComponent";
import Toast from 'react-native-root-toast';
// Store width in variable
const screenWidth = Dimensions.get('window').width;
const screenHeight = Dimensions.get('window').height;
const statusBarHeightByDensity = NativeModules.ReactBridge.statusBarHeightByDensity

export default class TabOrderPage extends React.Component {
    render() {
        const data = [];
        for (let i = 0; i < 50; i++) {
            data[i] = i + 1;
        }

        return (
            <View style={{flex: 1, flexDirection: "column", flexWrap: "nowrap", justifyContent: "flex-start", alignItems: "flex-start", backgroundColor: "#fac446"}}>
                <View zIndex={0} style={{width: "100%", height: statusBarHeightByDensity, backgroundColor: "#fac446"}}/>
                <View zIndex={0} style={{width: "100%", flexDirection: "column", flexWrap: "nowrap", justifyContent: "flex-start", alignItems: "flex-start", backgroundColor: "#fac446"}}>
                    <Text>TabOrderPage</Text>
                    <Button title="跳转到订单" color="#ff9800" onPress={() => this.props.navigation.navigate("Order")}/>
                    <Button title="跳转到用户" color="#ff9800" onPress={() => this.props.navigation.navigate("User")}/>
                    <Button title="跳转到商品" color="#ff9800" onPress={() => this.props.navigation.navigate("Goods")}/>
                    <Button title="跳转到佣金" color="#ff9800" onPress={() => this.props.navigation.navigate("Money")}/>
                    <Text style={{marginTop: 5, marginBottom: 5, fontSize: 15, fontWeight: "bold", borderWidth: 2, borderColor: "black", borderRadius: 5, padding: 5}}>invoke native recyclerView</Text>
                </View>
                <RecyclerComponent
                    style={{flex: 1, width: "100%", overflow: "hidden"}}
                    zIndex={-1}
                    orientation={1}
                    spanCount={4}
                    data={data}
                    onItemClicked={(event) => {
                        Toast.show('onItemClicked:' + event.nativeEvent.position, {
                            duration: Toast.durations.SHORT,
                            position: Toast.positions.BOTTOM,
                            shadow: true,
                            animation: true,
                            hideOnPress: true
                        });
                    }}
                />
            </View>
        );
    }
}
