import React from "react";
import {createBottomTabNavigator} from "react-navigation-tabs";

import Ionicons from "react-native-vector-icons/Ionicons";
import {createAppContainer} from "react-navigation";

import TabGoodsPage from "./tab/TabGoodsPage";
import TabMoneyPage from "./tab/TabMoneyPage";
import TabOrderPage from "./tab/TabOrderPage";
import TabUserPage from "./tab/TabUserPage";

import IconWithBadgeComponent from "../base/component/IconWithBadgeComponent"; // https://oblador.github.io/react-native-vector-icons/

export default createAppContainer(
    createBottomTabNavigator(
        {
            Order: {
                screen: TabOrderPage,
                navigationOptions: ({navigation}) => ({
                    title: "订单"
                })
            },
            User: {
                screen: TabUserPage,
                navigationOptions: ({navigation}) => ({
                    title: "用户"
                })
            },
            Goods: {
                screen: TabGoodsPage,
                navigationOptions: ({navigation}) => ({
                    title: "商品"
                })
            },
            Money: {
                screen: TabMoneyPage,
                navigationOptions: ({navigation}) => ({
                    title: "佣金"
                })
            }
        },
        {
            initialRouteName: "Order",
            defaultNavigationOptions: ({navigation}) => ({
                tabBarIcon: ({focused, horizontal, tintColor}) => {
                    const {routeName} = navigation.state;
                    let IconComponent = Ionicons;
                    let iconName;
                    if (routeName === "Order") {
                        iconName = focused ? "md-today" : "md-today";
                        // IconComponent = (props) => <IconWithBadgeComponent {...props} badgeCount={3} />;
                    } else if (routeName === "User") {
                        iconName = focused ? "md-person" : "md-person";
                    } else if (routeName === "Goods") {
                        iconName = focused ? "md-appstore" : "md-appstore";
                    } else if (routeName === "Money") {
                        iconName = focused ? "logo-bitcoin" : "logo-bitcoin";
                    }
                    return <IconComponent name={iconName} size={25} color={tintColor} />;
                }
            }),
            tabBarOptions: {
                activeTintColor: "#f9a825", // 文字和图片选中颜色
                inactiveTintColor: "#bdbdbd", // 文字和图片默认颜色
                style: {
                    backgroundColor: "whitesmoke" // TabBar 背景色
                },
                labelStyle: {
                    fontSize: 10 // 文字大小
                }
            }
        }
    )
);
