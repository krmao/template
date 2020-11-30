import '../../settings/imports/flutter_imports_material.dart';

class MainTabWidgetState extends State<StatefulWidget> {
  PageController controller;
  var pages = [PlayerTabHomeWidget(), PlayerTabMineWidget()];
  var currentIndex = 0;
  var firstTime = true;

  @override
  Widget build(BuildContext context) {
    if (firstTime) {
      Size size = MediaQuery
          .of(context)
          .size;
      print("screen:$size");
      firstTime = false;
    }

    // init at widget which parent state is MaterialApp
    STBaseNativeManager.initialize(context);

    controller = PageController(initialPage: 0);
    return Scaffold(
        backgroundColor: Colors.white,
        body: Column(children: <Widget>[
          Expanded(
              flex: 1,
              child: PageView.builder(
                  pageSnapping: true,
                  itemBuilder: (BuildContext context, int index) => pages[index],
                  itemCount: pages.length,
                  onPageChanged: (index) {
                    print("onPageChanged:$index");
                    if (currentIndex != index) setState(() => currentIndex = index);
                  },
                  controller: controller
              )
          ),
          bottomNavigationBar()
        ])
    );
  }

  Widget bottomNavigationBar() {
    return BottomNavigationBar(items: [
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