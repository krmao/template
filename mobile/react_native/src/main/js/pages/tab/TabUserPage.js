import React from "react";
import {Button, Text} from "react-native";
import {SafeAreaView} from "react-native-safe-area-context";

export default class TabUserPage extends React.Component {
    render() {
        return (
            <SafeAreaView style={{flex: 1, justifyContent: "flex-start", alignItems: "flex-start", backgroundColor: "#f9a825"}}>
                <Text>TabUserPage</Text>
                <Button title="跳转到订单" color="#f57c00" onPress={() => this.props.navigation.navigate("Order")} />
                <Button title="跳转到用户" color="#f57c00" onPress={() => this.props.navigation.navigate("User")} />
                <Button title="跳转到商品" color="#f57c00" onPress={() => this.props.navigation.navigate("Goods")} />
                <Button title="跳转到佣金" color="#f57c00" onPress={() => this.props.navigation.navigate("Money")} />
            </SafeAreaView>
        );
    }
}
