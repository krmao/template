import React, {PureComponent} from "react";
import {FlatList, RefreshControl, Text, TouchableOpacity, View} from "react-native";

var Dimensions = require('Dimensions');

var SCREEN_WIDTH = Dimensions.get('window').width;
var SCREEN_HEIGHT = Dimensions.get('window').height;
var SCREEN_SCALE = Dimensions.get('window').scale;

function getDataList(count: int) {
    let dataList = [];
    for (var i = 0; i < count; i++) {
        dataList.push({title: "hello michael mao nice to meet you now, hello michael mao nice to meet you now !!! --> "})
    }
    return dataList;
}

export default class HomeComponent extends React.PureComponent {

    constructor(props) {
        super(props);

        this.state = {
            currentIndex: -1,
            isRefreshingNow: false,
            isLoadingNow: false,
            loadMoreState: 'loading',
            dataList: getDataList(3),
        };

        this.onRefresh = () => {
            this.setState({isRefreshingNow: true});

            setTimeout(() => {
                this.setState({isRefreshingNow: false});
                this.setState({isLoadingNow: false});
                this.setState({dataList: getDataList(3)});

                // 一共只有 20 条数据
                this.setState({loadMoreState: this.state.dataList.length >= 20 ? 'empty' : 'loading'});

            }, 2000);
        };

        this.onLoadMore = () => {
            this.setState({isRefreshingNow: false});

            if (this.state.loadMoreState !== "empty" && !this.state.isLoadingNow) {
                this.setState({loadMoreState: 'loading'});
                this.setState({isLoadingNow: true});

                setTimeout(() => {
                    this.setState({dataList: this.state.dataList.concat(getDataList(3))}); // 追加 1 条数据

                    this.setState({isLoadingNow: false});

                    if (this.state.dataList.length >= 20) {
                        this.setState({loadMoreState: 'empty'});
                    }
                }, 2000);
            }
        };
    }

    render() {
        return (
            <FlatList
                data={this.state.dataList}
                numColumns={1}
                onEndReached={this.onLoadMore}

                ItemSeparatorComponent={() =>
                    <View style={{height: 1, backgroundColor: '#EEEEEE'}}/>
                }

                renderItem={({item, index}) => (
                    <ItemView
                        item={item}
                        index={index}
                        currentIndex={this.state.currentIndex}
                        onPressItem={(index: string) => {
                            this.setState({currentIndex: index});
                        }}
                    />
                )}

                refreshControl={
                    <RefreshControl
                        refreshing={this.state.isRefreshingNow}
                        onRefresh={this.onRefresh}
                        title="Loading..."
                        titleColor="#00ff00"
                        tintColor="#ff0000"
                        colors={['#ff0000', '#00ff00', '#0000ff']}
                        progressBackgroundColor="#ffffff"
                    />
                }

                ListHeaderComponent={ <View
                    style={{
                        height: 48,
                        justifyContent: 'center',
                        backgroundColor: 'red',
                        alignItems: 'center'
                    }}
                    activeOpacity={1}
                >
                    <Text
                        style={{
                            fontSize: 24,
                            fontWeight: 'bold',
                            fontStyle: 'italic',
                            textAlign: 'center',
                        }}
                    >
                        {'HEADER VIEW'}
                    </Text>
                </View>
                }
                ListFooterComponent={() => {
                    if (this.state.dataList.length !== 0 && this.state.loadMoreState === 'loading') {
                        return (
                            <View
                                style={{
                                    height: 100,
                                    justifyContent: 'center',
                                    backgroundColor: 'red',
                                    alignItems: 'center'
                                }}
                                activeOpacity={1}
                            >
                                <Text
                                    style={{
                                        fontSize: 24,
                                        fontWeight: 'bold',
                                        fontStyle: 'italic',
                                        textAlign: 'center',
                                    }}
                                >
                                    {'努力加载中...'}
                                </Text>
                            </View>
                        )
                    } else if (this.state.loadMoreState === 'empty') {
                        return (
                            <View
                                style={{
                                    height: 100,
                                    justifyContent: 'center',
                                    backgroundColor: 'red',
                                    alignItems: 'center'
                                }}
                                activeOpacity={1}
                            >
                                <Text
                                    style={{
                                        fontSize: 24,
                                        fontWeight: 'bold',
                                        fontStyle: 'italic',
                                        textAlign: 'center',
                                    }}
                                >
                                    {'没有更多数据了...'}
                                </Text>
                            </View>
                        )
                    } else {
                        return null
                    }
                }}

                extraData={this.state}                        // 当指定的值变化时,会触发 FlatList 更新
                keyExtractor={(item, index) => index}         // 使用 index 作为区分不同元素的 key
                enableEmptySections={true}
                onEndReachedThreshold={0.1}                   // 执行上啦的时候10%执行
                showsVerticalScrollIndicator={true}           // 是否显示垂直滚动条
                showsHorizontalScrollIndicator={false}        // 是否显示水平滚动条
            />
        );
    }
}

class ItemView extends React.PureComponent {
    render() {
        return (
            <TouchableOpacity onPress={() => this.props.onPressItem(this.props.index)}>
                <View style={{height: SCREEN_HEIGHT / 3}}>
                    <Text
                        style={{
                            flex: 1,
                            color: this.props.currentIndex === this.props.index ? "black" : "white",
                            paddingTop: 80,
                            paddingBottom: 80,
                            paddingLeft: 15,
                            paddingRight: 15,
                            fontSize: 24,
                            fontWeight: 'bold',
                            fontStyle: 'italic',
                            textAlign: 'center',
                            backgroundColor: this.props.currentIndex === this.props.index ? "white" : (this.props.index % 2 === 0 ? "orange" : "green")
                        }}
                        numberOfLines={1}
                        ellipsizeMode='tail'
                    >
                        {this.props.index} --> {this.props.item.title}
                    </Text>
                </View>
            </TouchableOpacity>
        );
    }
}
