import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:lib_flutter_base/lib_flutter_base.dart';

import '../../modules/player/player_tab_home_widget.dart';
import 'player_tab_mine_widget.dart';

class MainTabWidgetState extends BaseState<StatefulWidget> {
  PageController controller;
  var pages = [PlayerTabHomeWidget(), PlayerTabMineWidget()];
  var currentIndex = 0;
  var firstTime = true;

  MainTabWidgetState({String argumentsJsonString})
      : super(argumentsJsonString: argumentsJsonString){
      print(
          "[page] ---- MainTabWidgetState constructor argumentsJsonString=$argumentsJsonString");
  }

  @override
  Widget build(BuildContext context) {
    if (firstTime) {
      Size size = MediaQuery.of(context).size;
      print("screen:$size");
      firstTime = false;
    }

    // init at widget which parent state is MaterialApp
    controller = PageController(initialPage: 0);
    return Scaffold(
        backgroundColor: Colors.white,
        body: Column(children: <Widget>[
          Expanded(
              flex: 1,
              child: PageView.builder(
                  pageSnapping: true,
                  itemBuilder: (BuildContext context, int index) =>
                      pages[index],
                  itemCount: pages.length,
                  onPageChanged: (index) {
                    print("onPageChanged:$index");
                    if (currentIndex != index)
                      setState(() => currentIndex = index);
                  },
                  controller: controller)),
          bottomNavigationBar()
        ]));
  }

  Widget bottomNavigationBar() {
    return BottomNavigationBar(
        items: [
          BottomNavigationBarItem(
              icon: ImageIcon(
                AssetImage("images/home_menu_home.png"),
                size: 48.0,
                color:
                    currentIndex == 0 ? Color(0xff0c0435) : Color(0xffb6b6b6),
              ),
              title: Container()),
          BottomNavigationBarItem(
              icon: ImageIcon(
                AssetImage("images/home_menu_mine.png"),
                size: 48.0,
                color:
                    currentIndex == 1 ? Color(0xff0c0435) : Color(0xffb6b6b6),
              ),
              title: Container())
        ],
        onTap: (index) {
          print("onTap:$index");
          controller.jumpToPage(
              index); // https://github.com/flutter/flutter/issues/11895 fixed in v0.8.2-pre.26
        },
        currentIndex: 0,
        type: BottomNavigationBarType.fixed,
        fixedColor: Color(0xff0c0435));
  }
}
