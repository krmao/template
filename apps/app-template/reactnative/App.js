import React, {Component} from "react";
import {Button, Image, Platform, ScrollView, StyleSheet, Text, TextInput, View} from "react-native";

class MText extends Component {
    render() {
        return (
            <Text>{this.props.text}</Text>
        );
    }
}

export default class App extends Component {
    render() {
        let pic = {
            uri: 'https://upload.wikimedia.org/wikipedia/commons/d/de/Bananavarieties.jpg'
        };
        var inputText = '';

        return (
            <View style={styles.container}>

                <TextInput
                    style={{color: 'red', fontWeight: 'bold', fontSize: 30, marginBottom: 15, height: 40}}
                    placeholder="Type here to translate!"
                    onChangeText={(text) => inputText = {text}}
                />

                <Text style={{color: 'red', fontWeight: 'bold', fontSize: 30, marginBottom: 15}}>Hello World!</Text>

                <MText text='Michael Mao'/>

                <Image source={pic} style={{width: 193, height: 110, backgroundColor: 'powderblue'}}/>
                <Button
                    onPress={() => {
                        alert('You tapped the button!');
                    }}
                    title="Press Me"
                />
                <ScrollView style={{backgroundColor: '#ccc'}}>
                    <Text style={{fontSize: 96}}>Scroll me plz</Text>
                    <Image source={require('./image.png')}/>
                </ScrollView>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        ...Platform.select({
            ios: {
                backgroundColor: '#eeeeee',
            },
            android: {
                backgroundColor: 'blue',
            },
        }),
        padding: 15,
        flexDirection: 'column',
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    }
});
