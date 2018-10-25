import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';

import 'com/smart/base/DefaultApp.dart';
import 'com/smart/base/DefaultPage.dart';
import 'com/smart/business/HomePage.dart';
import 'com/smart/business/MinePage.dart';

const PATH_ROUTE_PREFIX = "flutter://";
const MIXED_WITH_NATIVE = false;

void main() {
    debugPaintSizeEnabled = false;
    runApp(DefaultApp(body: () => DefaultPage(state: MainTabWidgetState(),)));
}

class MainTabWidgetState extends State<StatefulWidget> {
    PageController controller;
    var pages = [HomePage(), MinePage()];
    var currentIndex = 0;

    @override
    Widget build(BuildContext context) {
        Size size = MediaQuery
            .of(context)
            .size;
        print("defaultRouteName:${window.defaultRouteName}, size:$size");
        controller = PageController(initialPage: 0);
        return Scaffold(
            backgroundColor: Colors.white,
            body: Column(children: <Widget>[
                Expanded(
                    flex: 1,
                    child: PageView.builder(itemBuilder: (BuildContext context, int index) => pages[index],
                        itemCount: pages.length,
                        onPageChanged: (index) {
                            print("onPageChanged:$index");
                            if (currentIndex != index) setState(() => currentIndex = index);
                        },
                        controller: controller,
                    ),
                ),
                bottomNavigationBar()
            ]));
    }

    Widget bottomNavigationBar() {
        return BottomNavigationBar(
            items: [
                BottomNavigationBarItem(icon: ImageIcon(AssetImage("images/home_menu_home.png"), size: 48.0, color: currentIndex == 0 ? Color(0xff0c0435) : Color(0xffb6b6b6),), title: Container()),
                BottomNavigationBarItem(icon: ImageIcon(AssetImage("images/home_menu_mine.png"), size: 48.0, color: currentIndex == 1 ? Color(0xff0c0435) : Color(0xffb6b6b6),), title: Container())
            ],
            onTap: (index) {
                print("onTap:$index");
                controller.jumpToPage(index); // https://github.com/flutter/flutter/issues/11895 fixed in v0.8.2-pre.26
            },
            currentIndex: 0,
            type: BottomNavigationBarType.fixed,
            fixedColor: Color(0xff0c0435));
    }
}
