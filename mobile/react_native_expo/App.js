import React, {Component} from "react";
import {Dimensions, Platform, StyleSheet} from "react-native";
import {SceneMap, TabBar, TabViewAnimated} from "react-native-tab-view";
import HomeComponent from "./home/HomeComponent";
import MineComponent from "./mine/MineComponent";
import OtherComponent from "./other/OtherComponent";

// 携程优化策略
// https://zhuanlan.zhihu.com/p/23715716

const initialLayout = {
    height: 0,
    width: Dimensions.get('window').width,
};

const HomePage = () => <HomeComponent/>;
const OtherPage = () => <OtherComponent/>;
const MinePage = () => <MineComponent/>;

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

    _renderFooter = props => <TabBar {...props} onTabPress={(scene) => {
        console.log(scene.index)
    }}/>;

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
                backgroundColor: '#EEEEEE',
            },
            android: {
                backgroundColor: '#EFEFEF',
            },
        }),
        flex: 1
    }
});
