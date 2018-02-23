import React, {Component} from "react";
import {Dimensions, Platform, StyleSheet, View} from "react-native";
import {SceneMap, TabBar, TabViewAnimated} from "react-native-tab-view";


const initialLayout = {
    height: 0,
    width: Dimensions.get('window').width,
};

const HomePage = () => <View style={[styles.container, {backgroundColor: '#ff4081'}]}/>;
const OtherPage = () => <View style={[styles.container, {backgroundColor: '#673ab7'}]}/>;
const MinePage = () => <View style={[styles.container, {backgroundColor: '#EFEFEF'}]}/>;

export default class App extends Component {
    state = {
        index: 0,
        routes: [
            {key: 'a', title: '首页'},
            {key: 'b', title: '其它'},
            {key: 'c', title: '我'},
        ],
    };

    _handleIndexChange = index => this.setState({index});

    _renderFooter = props => <TabBar {...props} />;

    _renderScene = SceneMap({
        a: HomePage,
        b: OtherPage,
        c: MinePage,
    });

    render() {
        return (
            <TabViewAnimated
                style={styles.container}
                navigationState={this.state}
                renderScene={this._renderScene}
                renderFooter={this._renderFooter}
                onIndexChange={this._handleIndexChange}
                initialLayout={initialLayout}
            />
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
        flex: 1
    }
});
