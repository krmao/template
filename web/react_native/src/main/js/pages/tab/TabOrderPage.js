import React from "react";
import {Button, Text} from "react-native";
import {SafeAreaView} from "react-native-safe-area-context";

export default class TabOrderPage extends React.Component {
    render() {
        return (
            <SafeAreaView style={{flex: 1, justifyContent: "flex-start", alignItems: "flex-start", backgroundColor: "#fac446"}}>
                <Text>TabOrderPage</Text>
                <Button title="跳转到订单" color="#ff9800" onPress={() => this.props.navigation.navigate("Order")} />
                <Button title="跳转到用户" color="#ff9800" onPress={() => this.props.navigation.navigate("User")} />
                <Button title="跳转到商品" color="#ff9800" onPress={() => this.props.navigation.navigate("Goods")} />
                <Button title="跳转到佣金" color="#ff9800" onPress={() => this.props.navigation.navigate("Money")} />
            </SafeAreaView>
        );
    }
}
