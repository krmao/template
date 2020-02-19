import React from "react";
import {Button, Dimensions, NativeModules, Text, View} from "react-native";
import RecyclerComponent from "../../base/component/RecyclerComponent";
import Toast from 'react-native-root-toast';
// Store width in variable
const screenWidth = Dimensions.get('window').width;
const screenHeight = Dimensions.get('window').height;
const statusBarHeightByDensity = NativeModules.ReactBridge.statusBarHeightByDensity

export default class TabOrderPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentPageIndex: 0,
            pageSize: 10,
            initData: [],
            loadMoreData: [],
            showErrorAt5: true,
            showNoMoreAt10: true
        }
    }

    getDataList() {
        let dataList = [];
        let toPageIndex = this.state.currentPageIndex + 1;
        let index = 0;
        for (let i = (this.state.currentPageIndex * this.state.pageSize); i < (toPageIndex * this.state.pageSize); i++) {
            dataList[index++] = i;
        }
        this.setState({
            currentPageIndex: this.state.currentPageIndex + 1
        });
        return dataList;
    }

    componentDidMount() {
        this.setState({
            initData: this.getDataList()
        })
    }

    render() {
        let that = this;
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
                    style={{flex: 1, width: "100%", overflow: "hidden", backgroundColor: "white"}}
                    zIndex={-1}
                    orientation={1}
                    spanCount={2}
                    initData={this.state.initData}
                    loadMoreData={this.state.loadMoreData}
                    onItemClicked={(event) => {
                        Toast.show('onItemClicked:' + event.nativeEvent.position, {
                            duration: Toast.durations.SHORT,
                            position: Toast.positions.BOTTOM,
                            shadow: true,
                            animation: true,
                            hideOnPress: true
                        });
                    }}
                    onRequestLoadMore={(event) => {
                        console.log("order page", "nativeEvent:", event.nativeEvent);

                        setTimeout(() => {

                            if (this.state.currentPageIndex === 5 && this.state.showErrorAt5 === true) {
                                console.log("order page", "加载出错了");
                                that.setState({
                                    loadMoreData: null,
                                    showErrorAt5: false
                                })
                            } else if (this.state.currentPageIndex === 10 && this.state.showNoMoreAt10 === true) {
                                console.log("order page", "没有更多数据了");
                                that.setState({
                                    loadMoreData: [],
                                    showNoMoreAt10: false
                                })
                            } else {
                                let loadMoreData = that.getDataList();
                                console.log("order page", "成功获取下一页数据", loadMoreData);
                                that.setState({
                                    loadMoreData: loadMoreData
                                })
                            }

                        }, 2000);
                    }
                    }
                />
            </View>
        );
    }
}
