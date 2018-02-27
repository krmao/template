import React from "react";
import {AppRegistry, DeviceEventEmitter, StyleSheet, Text, View} from "react-native";

class HomeModule extends React.Component {

    render() {

        var key = this.props;

        console.debug(key);
        console.log(key);
        console.warn(key);
        console.trace(key);
        console.info(key);


        DeviceEventEmitter.addListener('native_event', (event) => {
            console.warn(event);
        });

        return (
            <View style={styles.container}>
                <Text style={styles.hello}>Hello, World</Text>
                <Text style={styles.hello}>{this.props.a}</Text>
                <Text style={styles.hello}>{this.props.b}</Text>
                <Text style={styles.hello}>{this.props.c}</Text>
                <Text style={styles.hello}>{this.props.d}</Text>
            </View>
        );
    }

}
var styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
    },
    hello: {
        fontSize: 20,
        textAlign: 'center',
        margin: 10,
    },
});

AppRegistry.registerComponent('react-module-home', () => HomeModule);
