import React from "react";
import {Button, Text} from "react-native";
import {SafeAreaView} from "react-native-safe-area-context";

export default class TabMoneyPage extends React.Component {
    render() {
        return (
            <SafeAreaView style={{flex: 1, justifyContent: "flex-start", alignItems: "flex-start", backgroundColor: "#e65100"}}>
                <Text>TabMoneyPage</Text>
                <Button title="跳转到订单" color="#bf360c" onPress={() => this.props.navigation.navigate("Order")} />
                <Button title="跳转到用户" color="#bf360c" onPress={() => this.props.navigation.navigate("User")} />
                <Button title="跳转到商品" color="#bf360c" onPress={() => this.props.navigation.navigate("Goods")} />
                <Button title="跳转到佣金" color="#bf360c" onPress={() => this.props.navigation.navigate("Money")} />
            </SafeAreaView>
        );
    }
}
